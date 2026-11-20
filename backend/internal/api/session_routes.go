package api

import (
	"github.com/gin-gonic/gin"
	"github.com/polka/backend/internal/api/handler"
)

func registerSessionRoutes(sessionHandler *handler.SessionHandler, parent *gin.RouterGroup) {
	sessions := parent.Group("/sessions")
	{
		sessions.POST("", sessionHandler.Create)
		sessions.GET("", sessionHandler.GetAll)
		sessions.GET("/:id", sessionHandler.GetByID)
		sessions.PATCH("/:id", sessionHandler.Update)
		sessions.DELETE("/:id", sessionHandler.Delete)
	}
}
