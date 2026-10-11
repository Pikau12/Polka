package api

import (
	"github.com/gin-gonic/gin"
	"github.com/polka/backend/internal/api/handler"
)

func registerUserRoutes(userHandler *handler.UserHandler, parentRoute *gin.RouterGroup) {
	profile := parentRoute.Group("/profile")
	{
		profile.POST(":id/images", userHandler.UploadImage)
		profile.DELETE(":id/images/:image_id", userHandler.DeleteImage)
	}
}
