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
	GetGame(ctx context.Context, request dto.GetGamesRequest) ([]service.GetGameResult, error)
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

func (h *GameHandler) GetGame(c *gin.Context) {
	request := dto.GetGamesRequest{}

	if err := c.ShouldBindJSON(&request); err != nil {
		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	games, err := h.gameService.GetGame(c.Request.Context(), request)
	if err != nil {
		h.log.ErrorContext(c.Request.Context(), "get game", slog.Any("error", err))
		c.JSON(http.StatusInternalServerError, gin.H{"error": err})
		return
	}

	response := dto.GetGamesResponse{Games: make([]dto.GetGameResponse, 0, len(games))}

	for _, game := range games {
		response.Games = append(response.Games, dto.GetGameResponse{
			ServerID:              game.ServerID,
			BggID:                 game.BggID,
			Name:                  game.Name,
			Description:           game.Description,
			YearPublished:         game.YearPublished,
			BggRating:             game.BggRating,
			PolkaRating:           game.PolkaRating,
			BestCountPlayers:      game.BestCountPlayers,
			AvailableCountPlayers: game.AvailableCountPlayers,
			MinPlayTimeMinutes:    game.MinPlayTimeMinutes,
			MaxPlayTimeMinutes:    game.MaxPlayTimeMinutes,
			MinAge:                game.MinAge,
			Weight:                game.Weight,
		})
	}

	c.JSON(http.StatusOK, response)
}
