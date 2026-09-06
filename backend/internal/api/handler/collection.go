package handler

import (
	"context"
	"errors"
	"fmt"
	"log/slog"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/polka/backend/internal/api/dto"
	"github.com/polka/backend/internal/model"
	"github.com/polka/backend/internal/service"
)

type CollectionService interface {
	AddGameToCollection(ctx context.Context, gameID int64, userID int64) error
	DeleteGameFromCollection(ctx context.Context, gameID int64, userID int64) error
	GetCollection(ctx context.Context, userID int64) ([]model.Game, error)
}
type CollectionHandler struct {
	collectionService CollectionService
	log               *slog.Logger
}

func NewCollectionHandler(s CollectionService, log *slog.Logger) *CollectionHandler {
	return &CollectionHandler{
		collectionService: s,
		log:               log,
	}
}

func (h *CollectionHandler) AddGameToCollection(c *gin.Context) {
	request := dto.CreateCollectionItemRequest{}

	if err := c.ShouldBindJSON(&request); err != nil {
		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	id, err := getUserIDFromContext(c)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	err = h.collectionService.AddGameToCollection(c.Request.Context(), request.GameID, *id)
	if err != nil {
		if errors.Is(err, service.ErrCollectionItemAlreadyExists) {
			c.JSON(http.StatusConflict, gin.H{"error": err.Error()})
			return
		}

		c.JSON(http.StatusInternalServerError, gin.H{"error": "internal server error"})
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "success"})
}

func (h *CollectionHandler) DeleteGameFromCollection(c *gin.Context) {
	request := dto.DeleteFromCollectionRequest{}

	if err := c.ShouldBindJSON(&request); err != nil {
		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	id, err := getUserIDFromContext(c)
	if err != nil {
		c.JSON(http.StatusUnauthorized, gin.H{"error": err.Error()})
		return
	}

	if err := h.collectionService.DeleteGameFromCollection(c.Request.Context(), request.GameID, *id); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "internal server error"})
		return
	}
}

func (h *CollectionHandler) GetCollection(c *gin.Context) {
	id, err := getUserIDFromContext(c)
	if err != nil {
		c.JSON(http.StatusUnauthorized, gin.H{"error": err.Error()})
		return
	}

	games, err := h.collectionService.GetCollection(c.Request.Context(), *id)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "internal server error"})
		return
	}

	response := dto.GetCollectionResponse{
		Games: games,
	}

	c.JSON(http.StatusOK, response)
}

func getUserIDFromContext(c *gin.Context) (*int64, error) {
	value, exists := c.Get("user_id")
	if !exists {
		return nil, fmt.Errorf("unauthorized")
	}

	id, ok := value.(int64)
	if !ok {
		return nil, fmt.Errorf("invalid type user id")
	}

	return &id, nil
}
