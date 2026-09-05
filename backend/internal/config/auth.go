package config

import (
	"fmt"
	"os"
	"time"
)

type AuthConfig struct {
	JWTSecret   []byte
	AccessTTL   time.Duration
	RefreshTTL  time.Duration
	JWTIssuer   string
	JWTAudience string
}

func NewAuthConfig() (*AuthConfig, error) {
	cfg := &AuthConfig{}

	cfg.JWTSecret = []byte(os.Getenv("JWT_SECRET"))
	if len(cfg.JWTSecret) == 0 {
		return nil, fmt.Errorf("failed to get JWT_SECRET env")
	}

	accessTTL, err := time.ParseDuration(os.Getenv("JWT_ACCESS_TTL"))
	if err != nil || accessTTL <= 0 {
		return nil, fmt.Errorf("failed to get JWT_ACCESS_TTL env: %w", err)
	}

	cfg.AccessTTL = accessTTL

	refreshTTL, err := time.ParseDuration(os.Getenv("JWT_REFRESH_TTL"))
	if err != nil || refreshTTL <= 0 {
		return nil, fmt.Errorf("failed to get JWT_REFRESH_TTL env: %w", err)
	}

	cfg.RefreshTTL = refreshTTL

	cfg.JWTIssuer = os.Getenv("JWT_ISSUER")
	if cfg.JWTIssuer == "" {
		return nil, fmt.Errorf("failed to get JWT_ISSUER env")
	}

	cfg.JWTAudience = os.Getenv("JWT_AUDIENCE")
	if cfg.JWTAudience == "" {
		return nil, fmt.Errorf("failed to get JWT_AUDIENCE env")
	}

	return cfg, nil
}
