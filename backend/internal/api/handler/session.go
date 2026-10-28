package handler

import (
	"log/slog"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/polka/backend/internal/api/dto"
)

type SessionHandler struct {
	sessionService SessionService
	log            *slog.Logger
}

type SessionService interface {
}

func NewSessionHandler(sessionService SessionService, log *slog.Logger) *SessionHandler {
	return &SessionHandler{sessionService: sessionService, log: log}
}

func (h *SessionHandler) CreateSession(c *gin.Context) {
	request := &dto.CreateSessionRequest{}

	if err := c.ShouldBind(request); err != nil {
		h.log.ErrorContext(c.Request.Context(), "error binding request", slog.Any("err", err))
		c.JSON(http.StatusBadRequest, gin.H{})
		return
	}

	session, err := h.sessionService.
}
