package main

import (
	"context"
	"log/slog"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/polka/backend/internal/api"
	"github.com/polka/backend/internal/api/handler"
	"github.com/polka/backend/internal/auth/hasher"
	"github.com/polka/backend/internal/auth/jwt"
	"github.com/polka/backend/internal/bgg"
	"github.com/polka/backend/internal/config"
	"github.com/polka/backend/internal/logger"
	"github.com/polka/backend/internal/postgres"
	"github.com/polka/backend/internal/repository"
	"github.com/polka/backend/internal/service"
)

func main() {
	ctx, cancel := context.WithTimeout(context.Background(), time.Second*10)
	defer cancel()

	log := logger.New()

	postgresConfig := config.NewPostgresConfig()
	authConfig, err := config.NewAuthConfig()
	if err != nil {
		log.ErrorContext(
			ctx,
			"could not create auth config",
			slog.Any("error", err),
		)

		os.Exit(1)
	}

	bggConfig, err := config.NewBggConfig()
	if err != nil {
		log.ErrorContext(
			ctx,
			"could not create BGG config",
			slog.Any("error", err),
		)

		os.Exit(1)
	}

	db, err := postgres.New(ctx, postgresConfig)
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
	collectionRepo := repository.NewCollectionRepository(db)

	appCtx, stop := signal.NotifyContext(context.Background(), os.Interrupt, syscall.SIGTERM)
	defer stop()

	tokenManager := jwt.NewTokenManager(authConfig)
	bggClient := bgg.NewClient(bggConfig)
	cachedClient := bgg.NewCachedClient(bggClient, bggConfig.GameCacheTTL)
	cachedClient.Start(appCtx)

	authService := service.NewAuthService(userRepo, hasher.NewBcryptHasher(), tokenManager)
	gameService := service.NewGameService(gameRepo, cachedClient)
	collectionService := service.NewCollectionService(collectionRepo, userRepo, gameRepo)

	authHandler := handler.NewAuthHandler(authService, log)
	gameHandler := handler.NewGameHandler(gameService, log)
	collectionHandler := handler.NewCollectionHandler(collectionService, log)

	r := api.New(api.Dependencies{
		AuthHandler:       authHandler,
		GameHandler:       gameHandler,
		CollectionHandler: collectionHandler,
		TokenParser:       tokenManager,
		Log:               log,
	})

	if err := r.Run(":8080"); err != nil {
		log.ErrorContext(
			ctx,
			"unable to start server",
			slog.Any("error", err),
		)

		os.Exit(1)
	}
}
