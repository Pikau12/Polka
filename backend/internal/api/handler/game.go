package handler

import (
	"context"
	"log/slog"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/polka/backend/internal/api/dto"
	"github.com/polka/backend/internal/service"
)

type GameHandler struct {
	gameService GameService
	log         *slog.Logger
}

type GameService interface {
	SearchGames(ctx context.Context, request dto.SearchGameRequest) (*service.SearchGameResult, error)
	CreateGame(ctx context.Context, name string) (*service.CreateGameResult, error)
}

func NewGameHandler(service GameService, log *slog.Logger) *GameHandler {
	return &GameHandler{gameService: service, log: log}
}

func (h *GameHandler) SearchGames(c *gin.Context) {
	request := dto.SearchGameRequest{}

	if err := c.ShouldBindJSON(&request); err != nil {
		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	searchGamesRes, err := h.gameService.SearchGames(c, request)
	if err != nil {
		h.log.ErrorContext(c.Request.Context(), "search game by filter", slog.Any("error", err))

		c.JSON(http.StatusInternalServerError, gin.H{"error": err})
		return
	}

	response := dto.SearchGameResponse{
		Games:      searchGamesRes.Games,
		NextOffset: searchGamesRes.NextOffset,
		HasNext:    searchGamesRes.HasNext,
	}

	c.JSON(http.StatusOK, response)
}

func (h *GameHandler) CreateGame(c *gin.Context) {
	request := dto.CreateGameRequest{}
	if err := c.ShouldBindJSON(&request); err != nil {
		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	game, err := h.gameService.CreateGame(c.Request.Context(), request.Name)
	if err != nil {
		h.log.ErrorContext(c.Request.Context(), "create game", slog.Any("error", err))
		c.JSON(http.StatusInternalServerError, gin.H{"error": err})
		return
	}

	response := dto.CreateGameResponse{
		GameID: game.GameID,
		Name:   game.Name,
	}

	c.JSON(http.StatusOK, response)
}
