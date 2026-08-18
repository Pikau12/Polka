package config

import (
	"fmt"
	"os"
)

type Config struct {
	Host     string
	Port     string
	User     string
	Password string
	DBName   string
}

func New() Config {
	/*
		Uncomment if u want start project without docker
		if err := godotenv.Load(".env", "../.env"); err != nil {
			log.Fatal("Error loading .env file")
		}
	*/
	config := Config{
		Host:     os.Getenv("DB_HOST"),
		Port:     os.Getenv("DB_PORT"),
		User:     os.Getenv("DB_USER"),
		Password: os.Getenv("DB_PASSWORD"),
		DBName:   os.Getenv("DB_NAME"),
	}

	if config.Host == "" {
		config.Host = "host"
	}

	if config.Port == "" {
		config.Port = "5432"
	}

	if config.User == "" {
		config.User = "postgres"
	}

	if config.Password == "" {
		config.Password = "postgres"
	}

	if config.DBName == "" {
		config.DBName = "polka_db"
	}

	return config
}

func (c *Config) DSN() string {
	return fmt.Sprintf("host=%s port=%s user=%s password=%s dbname=%s sslmode=disable", c.Host, c.Port, c.User, c.Password, c.DBName)
}
