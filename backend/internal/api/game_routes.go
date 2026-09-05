package api

import (
	"github.com/gin-gonic/gin"
	"github.com/polka/backend/internal/api/handler"
)

func registerGameRoutes(gameHandler *handler.GameHandler, parentRoute *gin.RouterGroup) {
	games := parentRoute.Group("/games")
	{
		games.GET("/search", gameHandler.SearchGames)
		games.GET("", gameHandler.GetGame)
		games.POST("/create", gameHandler.CreateGame)
	}
}
