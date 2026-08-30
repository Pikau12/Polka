package config

import (
	"errors"
	"os"
	"time"
)

type BggConfig struct {
	BaseURL      string
	Token        string
	GameCacheTTL time.Duration
}

func NewBggConfig() (*BggConfig, error) {
	config := &BggConfig{
		BaseURL: os.Getenv("BGG_BASE_URL"),
		Token:   os.Getenv("BGG_TOKEN"),
	}

	gameCacheTTL, err := time.ParseDuration(os.Getenv("BGG_GAME_CACHE_TTL"))
	if err != nil {
		config.GameCacheTTL = time.Second * 15
	} else {
		config.GameCacheTTL = gameCacheTTL
	}

	if config.BaseURL == "" {
		return nil, errors.New("BGG_BASE_URL environment variable not set")
	}

	if config.Token == "" {
		return nil, errors.New("BGG_TOKEN environment variable not set")
	}

	return config, nil
}
