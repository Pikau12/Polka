package config

import (
	"fmt"
	"os"
)

type PostgresConfig struct {
	Host     string
	Port     string
	User     string
	Password string
	DBName   string
}

func NewPostgresConfig() (*PostgresConfig, error) {
	/*
		Uncomment if u want start project without docker
		if err := godotenv.Load(".env", "../.env"); err != nil {
			log.Fatal("Error loading .env file")
		}
	*/
	config := &PostgresConfig{
		Host:     os.Getenv("DB_HOST"),
		Port:     os.Getenv("DB_PORT"),
		User:     os.Getenv("DB_USER"),
		Password: os.Getenv("DB_PASSWORD"),
		DBName:   os.Getenv("DB_NAME"),
	}

	if config.Port == "" || config.User == "" || config.Password == "" || config.DBName == "" || config.Host == "" {
		return nil, fmt.Errorf("error read postgres config")
	}

	return config, nil
}

func (c *PostgresConfig) DSN() string {
	return fmt.Sprintf("host=%s port=%s user=%s password=%s dbname=%s sslmode=disable", c.Host, c.Port, c.User, c.Password, c.DBName)
}
