package api

import (
	"log/slog"

	"github.com/gin-gonic/gin"
	"github.com/polka/backend/internal/api/handler"
	"github.com/polka/backend/internal/api/middleware"
)

type Dependencies struct {
	AuthHandler       *handler.AuthHandler
	GameHandler       *handler.GameHandler
	CollectionHandler *handler.CollectionHandler
	TokenParser       middleware.AccessTokenParser
	Log               *slog.Logger
}

func New(deps Dependencies) *gin.Engine {
	r := gin.New()
	r.Use(middleware.LoggerMiddleware(deps.Log), gin.Recovery())

	registerRoutes(r, deps)
	return r
}

func registerRoutes(r *gin.Engine, deps Dependencies) {
	api := r.Group("api")

	registerAuthRoutes(deps.AuthHandler, api)

	protected := api.Group("/protected")
	protected.Use(middleware.AuthMiddleware(deps.TokenParser))
	{
		registerGameRoutes(deps.GameHandler, protected)
		registerCollectionRoutes(deps.CollectionHandler, protected)
	}
}
