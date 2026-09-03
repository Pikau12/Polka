package api

import (
	"github.com/gin-gonic/gin"
	"github.com/polka/backend/internal/api/handler"
)

func registerAuthRoutes(authHandler *handler.AuthHandler, parentRouter *gin.RouterGroup) {
	auth := parentRouter.Group("/auth")
	{
		auth.POST("/register", authHandler.Register)
		auth.POST("/login", authHandler.Login)
		auth.POST("/refresh", authHandler.Refresh)
		auth.POST("/logout", authHandler.Logout)
	}
}
