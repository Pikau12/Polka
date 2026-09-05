package config

import (
	"errors"
	"fmt"
	"net/url"
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

	baseURL := os.Getenv("BGG_BASE_URL")

	_, err = url.ParseRequestURI(baseURL)
	if err != nil {
		return nil, fmt.Errorf("error parsing BGG_BASE_URL: %w", err)
	}

	config.BaseURL = baseURL

	if config.Token == "" {
		return nil, errors.New("BGG_TOKEN environment variable not set")
	}

	return config, nil
}
