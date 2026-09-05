package service

import (
	"context"
	"errors"
	"fmt"
	"time"

	"github.com/polka/backend/internal/api/dto"
	"github.com/polka/backend/internal/auth/jwt"
	"github.com/polka/backend/internal/model"
	"github.com/polka/backend/internal/repository"
)

type AuthRepository interface {
	CreateUser(ctx context.Context, user *model.User) error
	FindByLogin(ctx context.Context, login string) (*model.User, error)
	CreateAuthSession(ctx context.Context, authSession *model.AuthSession) error
	FindAuthSessionByHash(ctx context.Context, hash string) (*model.AuthSession, error)
	RevokeAuthSession(ctx context.Context, id int64, revokedAt time.Time) error
}

type PasswordHasher interface {
	Hash(password string) (string, error)
	Compare(password string, hash string) error
}

type TokenManager interface {
	CreateAccessToken(userID int64) (*jwt.AccessToken, error)

	CreateRefreshToken() (*jwt.RefreshToken, error)
	HashRefreshToken(token string) string
}

type AuthService struct {
	authRepository AuthRepository
	passwordHasher PasswordHasher
	tokenManager   TokenManager
}

func NewAuthService(authRepository AuthRepository, passwordHasher PasswordHasher, tokenManager TokenManager) *AuthService {
	return &AuthService{
		authRepository: authRepository,
		passwordHasher: passwordHasher,
		tokenManager:   tokenManager,
	}
}

func (s *AuthService) Register(ctx context.Context, request dto.RegisterRequest) error {
	passwordHash, err := s.passwordHasher.Hash(request.Password)
	if err != nil {
		return fmt.Errorf("could not hash password: %w", err)
	}

	user := model.User{
		Username:     request.Username,
		Login:        request.Login,
		Email:        request.Email,
		PasswordHash: passwordHash,
	}

	if err = s.authRepository.CreateUser(ctx, &user); err != nil {
		if errors.Is(err, repository.ErrLoginAlreadyExists) {
			return ErrUserAlreadyExists
		}

		if errors.Is(err, repository.ErrEmailAlreadyExists) {
			return ErrUserAlreadyExists
		}

		return fmt.Errorf("create user: %w", err)
	}

	return nil
}

func (s *AuthService) Login(ctx context.Context, request dto.LoginRequest) (*jwt.AccessToken, *jwt.RefreshToken, error) {
	user, err := s.authRepository.FindByLogin(ctx, request.Login)
	if err != nil {
		if errors.Is(err, repository.ErrUserNotFound) {
			return nil, nil, ErrInvalidCredentials
		}

		return nil, nil, fmt.Errorf("find user: %w", err)
	}

	if err := s.passwordHasher.Compare(request.Password, user.PasswordHash); err != nil {
		return nil, nil, ErrInvalidCredentials
	}

	accessToken, err := s.tokenManager.CreateAccessToken(user.ID)
	if err != nil {
		return nil, nil, fmt.Errorf("create access token: %w", err)
	}

	refreshToken, err := s.tokenManager.CreateRefreshToken()
	if err != nil {
		return nil, nil, fmt.Errorf("create refresh token: %w", err)
	}

	authSession := &model.AuthSession{
		UserID:           user.ID,
		RefreshTokenHash: s.tokenManager.HashRefreshToken(refreshToken.Value),
		ExpiresAt:        refreshToken.ExpiresAt,
	}

	if err := s.authRepository.CreateAuthSession(ctx, authSession); err != nil {
		return nil, nil, fmt.Errorf("create auth session: %w", err)
	}

	return accessToken, refreshToken, nil
}

func (s *AuthService) Refresh(ctx context.Context, request dto.RefreshRequest) (*jwt.AccessToken, *jwt.RefreshToken, error) {
	hash := s.tokenManager.HashRefreshToken(request.RefreshToken)

	authSession, err := s.authRepository.FindAuthSessionByHash(ctx, hash)
	if err != nil {
		if errors.Is(err, repository.ErrAuthSessionNotFound) {
			return nil, nil, ErrInvalidRefreshToken
		}

		return nil, nil, fmt.Errorf("find auth session: %w", err)
	}

	if authSession.ExpiresAt.Before(time.Now()) {
		return nil, nil, ErrInvalidRefreshToken
	}

	if authSession.RevokedAt != nil {
		return nil, nil, ErrInvalidRefreshToken
	}

	accessToken, err := s.tokenManager.CreateAccessToken(authSession.UserID)
	if err != nil {
		return nil, nil, fmt.Errorf("create access token: %w", err)
	}

	refreshToken, err := s.tokenManager.CreateRefreshToken()
	if err != nil {
		return nil, nil, fmt.Errorf("create refresh token: %w", err)
	}

	if err := s.authRepository.RevokeAuthSession(ctx, authSession.ID, time.Now()); err != nil {
		return nil, nil, fmt.Errorf("revoke auth session: %w", err)
	}

	if err := s.authRepository.CreateAuthSession(ctx, &model.AuthSession{
		UserID:           authSession.UserID,
		RefreshTokenHash: s.tokenManager.HashRefreshToken(refreshToken.Value),
		ExpiresAt:        refreshToken.ExpiresAt,
	}); err != nil {
		return nil, nil, fmt.Errorf("create auth session: %w", err)
	}

	return accessToken, refreshToken, nil
}

func (s *AuthService) Logout(ctx context.Context, request dto.LogoutRequest) error {
	hash := s.tokenManager.HashRefreshToken(request.RefreshToken)

	authSession, err := s.authRepository.FindAuthSessionByHash(ctx, hash)
	if err != nil {
		if errors.Is(err, repository.ErrAuthSessionNotFound) {
			return ErrInvalidRefreshToken
		}

		return fmt.Errorf("find auth session: %w", err)
	}

	if authSession.RevokedAt != nil {
		return ErrInvalidRefreshToken
	}

	if err := s.authRepository.RevokeAuthSession(ctx, authSession.ID, time.Now()); err != nil {
		return fmt.Errorf("update auth session: %w", err)
	}

	return nil
}
