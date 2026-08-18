package api

import (
	"log/slog"

	"github.com/gin-gonic/gin"
	"github.com/polka/backend/internal/api/handler"
	"github.com/polka/backend/internal/api/middleware"
)

func New(log *slog.Logger, authHandler *handler.AuthHandler, parser middleware.AccessTokenParser) *gin.Engine {
	r := gin.New()
	r.Use(middleware.LoggerMiddleware(log), gin.Recovery())

	registerRoutes(r, authHandler, parser)

	return r
}

func registerRoutes(r *gin.Engine, authHandler *handler.AuthHandler, parser middleware.AccessTokenParser) {
	api := r.Group("api")

	auth := api.Group("/auth")
	{
		auth.POST("/register", authHandler.Register)
		auth.GET("/login", authHandler.Login)
		auth.POST("/refresh", authHandler.Refresh)
		auth.POST("/logout", authHandler.Logout)
	}

	protected := api.Group("/protected")
	protected.Use(middleware.AuthMiddleware(parser))
}
