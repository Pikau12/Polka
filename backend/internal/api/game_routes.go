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
		games.GET("/:id/images", gameHandler.GetImages)
		games.GET("/:id/images/:image_id", gameHandler.GetImage)
		games.POST("/create", gameHandler.CreateGame)
		games.POST("/:id/images", gameHandler.UploadImage)
		games.DELETE("/:id/images/:image_id", gameHandler.DeleteImage)
	}
}
