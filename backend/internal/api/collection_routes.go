package api

import (
	"github.com/gin-gonic/gin"
	"github.com/polka/backend/internal/api/handler"
)

func registerCollectionRoutes(collectionHandler *handler.CollectionHandler, parentRoute *gin.RouterGroup) {
	collections := parentRoute.Group("/collections/")
	{
		collections.POST("/add", collectionHandler.AddGameToCollection)
		collections.DELETE("/delete", collectionHandler.DeleteGameFromCollection)
		collections.GET("", collectionHandler.GetCollection)
	}
}
