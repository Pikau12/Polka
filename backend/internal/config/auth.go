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

	cfg.AccessTTL, _ = time.ParseDuration(os.Getenv("JWT_ACCESS_TTL"))
	if cfg.AccessTTL == 0 {
		return nil, fmt.Errorf("failed to get JWT_ACCESS_TTL env")
	}

	cfg.RefreshTTL, _ = time.ParseDuration(os.Getenv("JWT_REFRESH_TTL"))
	if cfg.RefreshTTL == 0 {
		return nil, fmt.Errorf("failed to get JWT_REFRESH_TTL env")
	}

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
