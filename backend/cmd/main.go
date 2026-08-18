package main

import (
	"context"
	"time"

	"github.com/polka/backend/internal/api"
	"github.com/polka/backend/internal/api/handler"
	"github.com/polka/backend/internal/logger"
	"github.com/polka/backend/internal/postgres"
	"github.com/polka/backend/internal/postgres/config"
	"github.com/polka/backend/internal/repository"
	"github.com/polka/backend/internal/service"
	"github.com/polka/backend/internal/service/hasher"
	"github.com/polka/backend/internal/service/jwt"
)

func main() {
	ctx, cancel := context.WithTimeout(context.Background(), time.Second*10)
	defer cancel()

	log := logger.New()

	db, err := postgres.New(ctx, config.New())
	if err != nil {
		panic(err)
	}

	userRepo := repository.NewUserRepository(db)

	tokenManager := jwt.NewTokenManager()

	authService := service.NewAuthService(userRepo, hasher.NewBcryptHasher(), tokenManager)

	authHandler := handler.NewAuthHandler(authService, log)

	r := api.New(log, authHandler, tokenManager)

	if err := r.Run(":8080"); err != nil {
		panic(err)
	}
}
