package handler

import (
	"context"
	"log/slog"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/polka/backend/internal/api/dto"
	"github.com/polka/backend/internal/domain"
)

type GameHandler struct {
	gameService GameService
	log         *slog.Logger
}

type GameService interface {
	SearchGameByFilter(c context.Context, request dto.GameRequest) ([]domain.GameSearchInfo, error)
}

func NewGameHandler(service GameService, log *slog.Logger) *GameHandler {
	return &GameHandler{gameService: service, log: log}
}

func (h *GameHandler) SearchGames(c *gin.Context) {
	request := dto.GameRequest{}

	if err := c.ShouldBindJSON(&request); err != nil {
		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	_, err := h.gameService.SearchGameByFilter(c, request)
	if err != nil {
		h.log.ErrorContext(c.Request.Context(), "search game by filter", slog.Any("error", err))

		c.JSON(http.StatusInternalServerError, gin.H{"error": err})
		return
	}
}
