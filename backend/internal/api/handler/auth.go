package handler

import (
	"errors"
	"log/slog"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/polka/backend/internal/api/dto"
	"github.com/polka/backend/internal/service"
)

type AuthHandler struct {
	service *service.AuthService
	log     *slog.Logger
}

func NewAuthHandler(authService *service.AuthService, log *slog.Logger) *AuthHandler {
	return &AuthHandler{service: authService, log: log}
}

func (h *AuthHandler) Register(c *gin.Context) {
	request := dto.RegisterRequest{}

	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "bad request"})
		return
	}

	if err := h.service.Register(c.Request.Context(), request); err != nil {
		if errors.Is(err, service.ErrUserAlreadyExists) {
			c.JSON(http.StatusConflict, gin.H{"error": "user already exists"})
			return
		}

		h.log.ErrorContext(
			c.Request.Context(),
			"failed to register user",
			slog.Any("error", err),
		)
		c.JSON(http.StatusInternalServerError, gin.H{"error": "internal server error"})

		return
	}

	c.Status(http.StatusCreated)
}

func (h *AuthHandler) Login(c *gin.Context) {
	request := dto.LoginRequest{}

	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "bad request"})
		return
	}

	accessToken, refreshToken, err := h.service.Login(c.Request.Context(), request)

	if err != nil {
		if errors.Is(err, service.ErrInvalidCredentials) {
			c.JSON(http.StatusUnauthorized, gin.H{"error": "invalid credentials"})
			return
		}

		h.log.ErrorContext(
			c.Request.Context(),
			"failed to login",
			slog.Any("error", err),
		)
		c.JSON(http.StatusInternalServerError, gin.H{"error": "internal server error"})

		return
	}

	response := dto.LoginResponse{
		AccessToken:      accessToken.Value,
		TokenType:        "Bearer",
		RefreshToken:     refreshToken.Value,
		AccessExpiresAt:  accessToken.ExpiresAt.Unix(),
		RefreshExpiresAt: refreshToken.ExpiresAt.Unix(),
	}

	c.JSON(http.StatusOK, response)
}

func (h *AuthHandler) Refresh(c *gin.Context) {
	request := dto.RefreshRequest{}

	if err := c.ShouldBindJSON(&request); err != nil {
		h.log.ErrorContext(
			c.Request.Context(),
			"failed to bind refresh request",
			slog.Any("error", err),
		)

		c.JSON(http.StatusBadRequest, gin.H{"error": "bad request"})
		return
	}

	accessToken, err := h.service.Refresh(c.Request.Context(), request)
	if err != nil {
		if errors.Is(err, service.ErrInvalidRefreshToken) {
			c.JSON(http.StatusUnauthorized, gin.H{"error": "unauthorized"})
			return
		}

		h.log.ErrorContext(
			c.Request.Context(),
			"failed to refresh access token",
			slog.Any("error", err),
		)
		c.JSON(http.StatusInternalServerError, gin.H{"error": "internal server error"})

		return
	}

	response := dto.RefreshResponse{
		AccessToken: accessToken.Value,
		TokenType:   "Bearer",
		ExpiresAt:   accessToken.ExpiresAt.Unix(),
	}

	c.JSON(http.StatusOK, response)
}

func (h *AuthHandler) Logout(c *gin.Context) {
	request := dto.LogoutRequest{}

	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "bad request"})
		return
	}

	if err := h.service.Logout(c.Request.Context(), request); err != nil {
		if errors.Is(err, service.ErrInvalidRefreshToken) {
			c.JSON(http.StatusUnauthorized, gin.H{"error": "unauthorized"})
			return
		}

		h.log.ErrorContext(c.Request.Context(), "failed to logout",
			slog.Any("error", err),
		)

		c.JSON(http.StatusInternalServerError, gin.H{"error": "internal server error"})
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "logged out successfully"})
}
