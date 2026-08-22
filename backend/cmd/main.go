package main

import (
	"context"
	"log/slog"
	"os"
	"time"

	"github.com/polka/backend/internal/api"
	"github.com/polka/backend/internal/api/handler"
	"github.com/polka/backend/internal/auth/hasher"
	"github.com/polka/backend/internal/auth/jwt"
	"github.com/polka/backend/internal/logger"
	"github.com/polka/backend/internal/postgres"
	"github.com/polka/backend/internal/postgres/config"
	"github.com/polka/backend/internal/repository"
	"github.com/polka/backend/internal/service"
)

func main() {
	ctx, cancel := context.WithTimeout(context.Background(), time.Second*10)
	defer cancel()

	log := logger.New()

	db, err := postgres.New(ctx, config.New())
	if err != nil {
		log.ErrorContext(
			ctx,
			"unable to connect to database",
			slog.Any("error", err),
		)

		os.Exit(1)
	}

	userRepo := repository.NewUserRepository(db)
	gameRepo := repository.NewGameRepository(db)

	tokenManager, err := jwt.NewTokenManager()
	if err != nil {
		log.ErrorContext(
			ctx,
			"unable to create token manager",
			slog.Any("error", err),
		)

		os.Exit(1)
	}

	authService := service.NewAuthService(userRepo, hasher.NewBcryptHasher(), tokenManager)
	gameService := service.NewGameService(gameRepo)

	authHandler := handler.NewAuthHandler(authService, log)
	gameHandler := handler.NewGameHandler(gameService, log)

	r := api.New(log, authHandler, gameHandler, tokenManager)

	if err := r.Run(":8080"); err != nil {
		log.ErrorContext(
			ctx,
			"unable to start server",
			slog.Any("error", err),
		)

		os.Exit(1)
	}
}
