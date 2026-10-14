package handler

import (
	"log/slog"

	"github.com/gin-gonic/gin"
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

}
