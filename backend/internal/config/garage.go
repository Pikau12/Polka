package config

import (
	"fmt"
	"os"
)

type GarageConfig struct {
	Endpoint  string
	Region    string
	Bucket    string
	AccessKey string
	SecretKey string
}

func NewGarageConfig() (*GarageConfig, error) {
	cfg := &GarageConfig{
		Endpoint:  os.Getenv("GARAGE_ENDPOINT"),
		Region:    os.Getenv("GARAGE_REGION"),
		Bucket:    os.Getenv("GARAGE_BUCKET"),
		AccessKey: os.Getenv("GARAGE_ACCESS_KEY"),
		SecretKey: os.Getenv("GARAGE_SECRET_KEY"),
	}

	if cfg.AccessKey == "" || cfg.SecretKey == "" || cfg.Endpoint == "" || cfg.Bucket == "" || cfg.Region == "" {
		return nil, fmt.Errorf("error read garage config")
	}

	return cfg, nil
}
