package handler

import (
	"context"
	"fmt"
	"io"
	"log/slog"
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
	"github.com/polka/backend/internal/api/dto"
	"github.com/polka/backend/internal/domain"
	"github.com/polka/backend/internal/service"
)

type GameHandler struct {
	gameService  GameService
	imageService ImageService
	log          *slog.Logger
}

type GameService interface {
	SearchGames(ctx context.Context, request dto.SearchGameRequest) (*service.SearchGameResult, error)
	CreateGame(ctx context.Context, name string) (*service.CreateGameResult, error)
	GetGame(ctx context.Context, request dto.GetGamesRequest) ([]service.GetGameResult, error)
}

type ImageService interface {
	UploadGameImage(ctx context.Context, gameID int64, image *service.UploadGameImage) (*domain.Image, error)
	DeleteGameImage(ctx context.Context, gameID int64, imageID int64) error
	GetGameImage(ctx context.Context, gameID, imageID int64) (*service.ImageContent, error)
	GetGameImageIDs(ctx context.Context, gameID int64) ([]int64, error)
}

func NewGameHandler(service GameService, imageService ImageService, log *slog.Logger) *GameHandler {
	return &GameHandler{gameService: service, imageService: imageService, log: log}
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

func (h *GameHandler) UploadImage(c *gin.Context) {
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

	image, err := h.imageService.UploadGameImage(c.Request.Context(), gameID, &service.UploadGameImage{Body: file, Size: fileHeader.Size, ContentType: contentType})
	if err != nil {
		h.log.ErrorContext(c.Request.Context(), "upload image", slog.Any("error", err))
		c.JSON(http.StatusInternalServerError, gin.H{"error": err})
		return
	}

	response := &dto.ImageUploadResponse{ID: image.ID}

	c.JSON(http.StatusOK, response)
}

func (h *GameHandler) DeleteImage(c *gin.Context) {
	gameID, err := parsePositiveInt64Param(c, "id")
	if err != nil {
		h.log.ErrorContext(c.Request.Context(), "delete game image", slog.Any("error", err))
		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{"error": err})
		return
	}

	imageID, err := parsePositiveInt64Param(c, "image_id")
	if err != nil {
		h.log.ErrorContext(c.Request.Context(), "delete game image", slog.Any("error", err))
		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{"error": err})
		return
	}

	err = h.imageService.DeleteGameImage(c.Request.Context(), gameID, imageID)
	if err != nil {
		h.log.ErrorContext(c.Request.Context(), "delete game image", slog.Any("error", err))
		c.JSON(http.StatusInternalServerError, gin.H{"error": err})
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "game image deleted"})
}

func (h *GameHandler) GetImage(c *gin.Context) {
	gameID, err := parsePositiveInt64Param(c, "id")
	if err != nil {
		h.log.ErrorContext(c.Request.Context(), "get game image", slog.Any("error", err))
		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{"error": err})
		return
	}

	imageID, err := parsePositiveInt64Param(c, "image_id")
	if err != nil {
		h.log.ErrorContext(c.Request.Context(), "get game image", slog.Any("error", err))
		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{"error": err})
		return
	}

	image, err := h.imageService.GetGameImage(c.Request.Context(), gameID, imageID)
	if err != nil {
		h.log.ErrorContext(c.Request.Context(), "get game image", slog.Any("error", err))
		c.JSON(http.StatusInternalServerError, gin.H{"error": err})
		return
	}

	defer image.Body.Close()

	c.DataFromReader(http.StatusOK, image.ContentLength, image.ContentType, image.Body, nil)
}

func (h *GameHandler) GetImages(c *gin.Context) {
	gameID, err := parsePositiveInt64Param(c, "id")
	if err != nil {
		h.log.ErrorContext(c.Request.Context(), "get game image", slog.Any("error", err))
		c.AbortWithStatusJSON(http.StatusBadRequest, gin.H{"error": err})
		return
	}

	imageIDs, err := h.imageService.GetGameImageIDs(c.Request.Context(), gameID)
	if err != nil {
		h.log.ErrorContext(c.Request.Context(), "get game image", slog.Any("error", err))
		c.JSON(http.StatusInternalServerError, gin.H{"error": err})
		return
	}

	response := dto.GetImagesResponse{IDs: make([]dto.GetImageResponse, 0, len(imageIDs))}

	for _, id := range imageIDs {
		response.IDs = append(response.IDs, dto.GetImageResponse{ID: id})
	}

	c.JSON(http.StatusOK, response)
}

func parsePositiveInt64Param(c *gin.Context, name string) (int64, error) {
	value := c.Param(name)

	id, err := strconv.ParseInt(value, 10, 64)
	if err != nil || id <= 0 {
		return 0, fmt.Errorf("invalid %s", name)
	}

	return id, nil
}
