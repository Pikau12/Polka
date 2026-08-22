package jwt

import (
	"crypto/rand"
	"crypto/sha256"
	"encoding/base64"
	"encoding/hex"
	"fmt"
	"os"
	"strconv"
	"time"

	"github.com/golang-jwt/jwt/v5"
)

type TokenManager struct {
	secret     []byte
	accessTTL  time.Duration
	refreshTTL time.Duration
	issuer     string
	audience   string
}

type AccessToken struct {
	Value     string    `json:"value"`
	ExpiresAt time.Time `json:"expires_at"`
}

type RefreshToken struct {
	Value     string    `json:"value"`
	ExpiresAt time.Time `json:"expires_at"`
}

type AccessTokenClaims struct {
	UserID int64
}

func NewTokenManager() (*TokenManager, error) {
	manager := &TokenManager{
		secret:     []byte(os.Getenv("JWT_SECRET")),
		accessTTL:  time.Duration(15) * time.Minute,
		refreshTTL: time.Duration(30) * time.Hour * 24,
		issuer:     os.Getenv("JWT_ISSUER"),
		audience:   os.Getenv("JWT_AUDIENCE"),
	}

	if len(manager.secret) == 0 {
		return nil, fmt.Errorf("failed to get JWT_SECRET env")
	}

	if manager.issuer == "" {
		return nil, fmt.Errorf("failed to get JWT_ISSUER env")
	}

	if manager.audience == "" {
		return nil, fmt.Errorf("failed to get JWT_AUDIENCE env")
	}

	return manager, nil
}

func (t *TokenManager) CreateAccessToken(userID int64) (*AccessToken, error) {
	issuedAt := time.Now()

	claims := &jwt.RegisteredClaims{
		Subject:   strconv.FormatInt(userID, 10),
		Issuer:    t.issuer,
		Audience:  []string{t.audience},
		ExpiresAt: jwt.NewNumericDate(issuedAt.Add(t.accessTTL)),
		IssuedAt:  jwt.NewNumericDate(issuedAt),
	}

	newToken := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)

	tokenStr, err := newToken.SignedString(t.secret)
	if err != nil {
		return nil, fmt.Errorf("create access token: %w", err)
	}

	return &AccessToken{
		Value:     tokenStr,
		ExpiresAt: claims.ExpiresAt.Time,
	}, nil
}

func (t *TokenManager) ParseAccessToken(tokenString string) (*AccessTokenClaims, error) {
	token, err := jwt.ParseWithClaims(tokenString, &jwt.RegisteredClaims{}, func(token *jwt.Token) (interface{}, error) {
		return t.secret, nil
	},
		jwt.WithValidMethods([]string{jwt.SigningMethodHS256.Alg()}),
		jwt.WithIssuer(t.issuer),
		jwt.WithAudience(t.audience),
		jwt.WithExpirationRequired(),
	)

	if err != nil {
		return nil, fmt.Errorf("parse access token: %w", err)
	}

	if claims, ok := token.Claims.(*jwt.RegisteredClaims); ok && token.Valid {
		id, err := strconv.ParseInt(claims.Subject, 10, 64)
		if err != nil {
			return nil, fmt.Errorf("parse access token: %w", err)
		}

		return &AccessTokenClaims{UserID: id}, nil
	}

	return nil, fmt.Errorf("invalid token claims")
}

func (t *TokenManager) CreateRefreshToken() (*RefreshToken, error) {
	randomBytes := make([]byte, 32)
	_, err := rand.Read(randomBytes)
	if err != nil {
		return nil, fmt.Errorf("create refresh token: %w", err)
	}

	token := base64.RawURLEncoding.EncodeToString(randomBytes)

	return &RefreshToken{
		Value:     token,
		ExpiresAt: time.Now().Add(t.refreshTTL),
	}, nil
}

func (t *TokenManager) HashRefreshToken(token string) string {
	hash := sha256.Sum256([]byte(token))
	hashString := hex.EncodeToString(hash[:])

	return hashString
}
