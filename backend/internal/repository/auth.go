package repository

import (
	"context"
	"errors"
	"fmt"
	"time"

	"github.com/jackc/pgx/v5/pgconn"
	"github.com/polka/backend/internal/model"
	"gorm.io/gorm"
)

type UserRepository struct {
	db *gorm.DB
}

func NewUserRepository(db *gorm.DB) *UserRepository {
	return &UserRepository{db: db}
}

func (r *UserRepository) CreateUser(ctx context.Context, user *model.User) error {
	err := r.db.
		WithContext(ctx).
		Create(user).
		Error

	if err == nil {
		return nil
	}

	var pgErr *pgconn.PgError

	if errors.As(err, &pgErr) {
		switch pgErr.ConstraintName {
		case "uq_users_login":
			return ErrLoginAlreadyExists

		case "uq_users_email":
			return ErrEmailAlreadyExists
		}
	}

	return fmt.Errorf("create user: %w", err)
}

func (r *UserRepository) FindByLogin(ctx context.Context, login string) (*model.User, error) {
	user := model.User{}

	if err := r.db.
		WithContext(ctx).
		Table("users").
		Where("users.login = ?", login).
		Take(&user).
		Error; err != nil {

		if errors.Is(err, gorm.ErrRecordNotFound) {
			return nil, ErrUserNotFound
		}

		return nil, fmt.Errorf("find user by login: %w", err)
	}

	return &user, nil
}

func (r *UserRepository) CreateAuthSession(ctx context.Context, authSession *model.AuthSession) error {
	err := r.db.
		WithContext(ctx).
		Create(authSession).
		Error

	if err != nil {
		return fmt.Errorf("create auth session: %w", err)
	}

	return nil
}

func (r *UserRepository) FindAuthSessionByHash(ctx context.Context, hash string) (*model.AuthSession, error) {
	session := &model.AuthSession{}

	if err := r.db.
		WithContext(ctx).
		Table("auth_sessions").
		Where("refresh_token_hash = ?", hash).
		Take(session).
		Error; err != nil {

		if errors.Is(err, gorm.ErrRecordNotFound) {
			return nil, ErrAuthSessionNotFound
		}

		return nil, fmt.Errorf("get auth session: %w", err)
	}

	return session, nil
}

func (r *UserRepository) RevokeAuthSession(ctx context.Context, id int64, revokedAt time.Time) error {
	if err := r.db.
		WithContext(ctx).
		Table("auth_sessions").
		Where("id = ?", id).
		Update("revoked_at", revokedAt).
		Error; err != nil {
		return fmt.Errorf("revoke auth session: %w", err)
	}

	return nil
}
