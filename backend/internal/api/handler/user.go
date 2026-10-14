package handler

import (
	"context"
	"io"
	"log/slog"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/polka/backend/internal/api/dto"
	"github.com/polka/backend/internal/domain"
	"github.com/polka/backend/internal/service"
)

type UserHandler struct {
	imageService UserImageService
	log          *slog.Logger
}

type UserImageService interface {
	UploadUserImage(ctx context.Context, userID int64, image *service.UploadGameImage) (*domain.Image, error)
	DeleteUserImage(ctx context.Context, userID int64, imageID int64) error
}

func NewUserHandler(imageService UserImageService, log *slog.Logger) *UserHandler {
	return &UserHandler{imageService: imageService, log: log}
}

func (h *UserHandler) UploadImage(c *gin.Context) {
	gameID, err := parsePositiveInt64Param(c, "id")
	if err != nil {
		c.AbortWithStatusJSON(http.StatusInternalServerError, gin.H{"error": err})
		return
	}

	fileHeader, err := c.FormFile("image")
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "image not found"})
		return
	}

	const maxImageSize int64 = 10 << 20

	if fileHeader.Size > maxImageSize || fileHeader.Size <= 0 {
		c.JSON(http.StatusBadRequest, gin.H{"error": "invalid image size"})
		return
	}

	file, err := fileHeader.Open()
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "failed to open file"})
		return
	}

	defer file.Close()

	buffer := make([]byte, 512)

	n, err := file.Read(buffer)
	if err != nil && err != io.EOF {
		c.JSON(http.StatusBadRequest, gin.H{"error": "failed to read file"})
		return
	}

	contentType := http.DetectContentType(buffer[:n])

	switch contentType {
	case "image/jpeg", "image/png", "image/webp":
	default:
		c.JSON(http.StatusBadRequest, gin.H{"error": "invalid content type"})
		return
	}

	_, err = file.Seek(0, io.SeekStart)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "failed to process file"})
		return
	}

	image, err := h.imageService.UploadUserImage(c.Request.Context(), gameID, &service.UploadGameImage{Body: file, Size: fileHeader.Size, ContentType: contentType})
	if err != nil {
		h.log.ErrorContext(c.Request.Context(), "upload image", slog.Any("error", err))
		c.JSON(http.StatusInternalServerError, gin.H{"error": err})
		return
	}

	response := &dto.ImageUploadResponse{ID: image.ID}

	c.JSON(http.StatusOK, response)
}

func (h *UserHandler) DeleteImage(c *gin.Context) {
	gameID, err := parsePositiveInt64Param(c, "id")
	if err != nil {
		h.log.ErrorContext(c.Request.Context(), "delete user image", slog.Any("error", err))
		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{"error": err})
		return
	}

	imageID, err := parsePositiveInt64Param(c, "image_id")
	if err != nil {
		h.log.ErrorContext(c.Request.Context(), "delete user image", slog.Any("error", err))
		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{"error": err})
		return
	}

	err = h.imageService.DeleteUserImage(c.Request.Context(), gameID, imageID)
	if err != nil {
		h.log.ErrorContext(c.Request.Context(), "delete user image", slog.Any("error", err))
		c.JSON(http.StatusInternalServerError, gin.H{"error": err})
		return
	}

	c.JSON(http.StatusOK, gin.H{})
}

//func (h *GameHandler) GetImage(c *gin.Context) {
//	gameID, err := parsePositiveInt64Param(c, "id")
//	if err != nil {
//		h.log.ErrorContext(c.Request.Context(), "get game image", slog.Any("error", err))
//		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{"error": err})
//		return
//	}
//
//	imageID, err := parsePositiveInt64Param(c, "image_id")
//	if err != nil {
//		h.log.ErrorContext(c.Request.Context(), "get game image", slog.Any("error", err))
//		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{"error": err})
//		return
//	}
//
//	image, err := h.imageService.GetGameImage(c.Request.Context(), gameID, imageID)
//	if err != nil {
//		h.log.ErrorContext(c.Request.Context(), "get game image", slog.Any("error", err))
//		c.JSON(http.StatusInternalServerError, gin.H{"error": err})
//		return
//	}
//
//	defer image.Body.Close()
//
//	c.DataFromReader(http.StatusOK, image.ContentLength, image.ContentType, image.Body, nil)
//}

//func (h *GameHandler) GetImages(c *gin.Context) {
//	gameID, err := parsePositiveInt64Param(c, "id")
//	if err != nil {
//		h.log.ErrorContext(c.Request.Context(), "get game image", slog.Any("error", err))
//		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{"error": err})
//		return
//	}
//
//	imageIDs, err := h.imageService.GetGameImageIDs(c.Request.Context(), gameID)
//	if err != nil {
//		h.log.ErrorContext(c.Request.Context(), "get game image", slog.Any("error", err))
//		c.JSON(http.StatusInternalServerError, gin.H{"error": err})
//		return
//	}
//
//	response := dto.GetImagesResponse{IDs: make([]dto.GetImageResponse, 0, len(imageIDs))}
//
//	for _, id := range imageIDs {
//		response.IDs = append(response.IDs, dto.GetImageResponse{ID: id})
//	}
//
//	c.JSON(http.StatusOK, response)
//}
