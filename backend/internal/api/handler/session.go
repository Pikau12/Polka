package handler

import (
	"context"
	"errors"
	"log/slog"
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/polka/backend/internal/api/dto"
	"github.com/polka/backend/internal/domain"
	"github.com/polka/backend/internal/service"
)

type SessionHandler struct {
	sessionService SessionService
	log            *slog.Logger
}

type SessionService interface {
	Create(ctx context.Context, session *domain.Session) (*domain.Session, error)
	GetAll(ctx context.Context, userID int64) ([]domain.Session, error)
	GetByID(ctx context.Context, userID, id int64) (*domain.Session, error)
	Update(ctx context.Context, session *domain.Session) (*domain.Session, error)
	Delete(ctx context.Context, userID, id int64) error
}

func NewSessionHandler(sessionService SessionService, log *slog.Logger) *SessionHandler {
	return &SessionHandler{sessionService: sessionService, log: log}
}

func (h *SessionHandler) Create(c *gin.Context) {
	userID, ok := sessionUserID(c)
	if !ok {
		return
	}

	var request dto.CreateSessionRequest
	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "invalid request body"})
		return
	}

	session, err := h.sessionService.Create(c.Request.Context(), &domain.Session{
		GameID:     request.GameID,
		CreatorID:  userID,
		Note:       request.Note,
		Place:      request.Place,
		Status:     request.Status,
		StartedAt:  request.StartedAt,
		FinishedAt: request.FinishedAt,

		Participants: toDomainSessionParticipants(request.Participants),
	})

	if err != nil {
		h.respondError(c, "create session", err)
		return
	}

	c.JSON(http.StatusCreated, toSessionResponse(session))
}

func (h *SessionHandler) GetAll(c *gin.Context) {
	userID, ok := sessionUserID(c)
	if !ok {
		return
	}

	sessions, err := h.sessionService.GetAll(c.Request.Context(), userID)
	if err != nil {
		h.respondError(c, "list sessions", err)
		return
	}

	response := dto.GetSessionsResponse{Sessions: make([]dto.SessionResponse, 0, len(sessions))}
	for i := range sessions {
		response.Sessions = append(response.Sessions, toSessionResponse(&sessions[i]))
	}
	c.JSON(http.StatusOK, response)
}

func (h *SessionHandler) GetByID(c *gin.Context) {
	userID, ok := sessionUserID(c)
	if !ok {
		return
	}

	id, err := parsePositiveInt64Param(c, "id")
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "invalid session id"})
		return
	}

	session, err := h.sessionService.GetByID(c.Request.Context(), userID, id)
	if err != nil {
		h.respondError(c, "get session", err)
		return
	}

	c.JSON(http.StatusOK, toSessionResponse(session))
}

func (h *SessionHandler) Update(c *gin.Context) {
	userID, ok := sessionUserID(c)
	if !ok {
		return
	}

	id, err := parsePositiveInt64Param(c, "id")
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "invalid session id"})
		return
	}

	var request dto.UpdateSessionRequest
	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "invalid request body"})
		return
	}

	session := domain.Session{
		ID:           id,
		CreatorID:    userID,
		Note:         request.Note,
		Place:        request.Place,
		StartedAt:    request.StartedAt,
		FinishedAt:   request.FinishedAt,
		Participants: toDomainSessionParticipants(request.Participants),
	}
	if request.Status != nil {
		session.Status = *request.Status
	}

	updatedSession, err := h.sessionService.Update(c.Request.Context(), &session)
	if err != nil {
		h.respondError(c, "update session", err)
		return
	}

	c.JSON(http.StatusOK, toSessionResponse(updatedSession))
}

func (h *SessionHandler) Delete(c *gin.Context) {
	userID, ok := sessionUserID(c)
	if !ok {
		return
	}

	id, err := parsePositiveInt64Param(c, "id")
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "invalid session id"})
		return
	}

	if err := h.sessionService.Delete(c.Request.Context(), userID, id); err != nil {
		h.respondError(c, "delete session", err)
		return
	}

	c.Status(http.StatusNoContent)
}

func sessionUserID(c *gin.Context) (int64, bool) {
	id, err := getUserIDFromContext(c)
	if err != nil || *id <= 0 {
		c.AbortWithStatusJSON(http.StatusUnauthorized, gin.H{"error": "unauthorized"})
		return 0, false
	}

	return *id, true
}

func (h *SessionHandler) respondError(c *gin.Context, operation string, err error) {
	status := http.StatusInternalServerError
	message := "internal server error"

	switch {
	case errors.Is(err, service.ErrInvalidSession):
		status, message = http.StatusBadRequest, service.ErrInvalidSession.Error()
	case errors.Is(err, service.ErrSessionNotFound):
		status, message = http.StatusNotFound, service.ErrSessionNotFound.Error()
	case errors.Is(err, service.ErrSessionGameNotFound):
		status, message = http.StatusNotFound, service.ErrSessionGameNotFound.Error()
	case errors.Is(err, service.ErrSessionForbidden):
		status, message = http.StatusForbidden, service.ErrSessionForbidden.Error()
	case errors.Is(err, service.ErrSessionConflict):
		status, message = http.StatusConflict, service.ErrSessionConflict.Error()
	default:
		h.log.ErrorContext(c.Request.Context(), operation, slog.Any("error", err))
	}

	c.JSON(status, gin.H{"error": message})
}

func toDomainSessionParticipants(participants []dto.SessionParticipant) []domain.SessionParticipant {
	if participants == nil {
		return nil
	}

	result := make([]domain.SessionParticipant, 0, len(participants))
	for _, participant := range participants {
		result = append(result, domain.SessionParticipant{
			ID:        participant.ID,
			UserID:    participant.UserID,
			GuestName: participant.GuestName,
			Score:     participant.Score,
			IsWinner:  *participant.IsWinner,
		})
	}

	return result
}

func toSessionResponse(session *domain.Session) dto.SessionResponse {
	participants := make([]dto.SessionParticipant, 0, len(session.Participants))
	for _, participant := range session.Participants {
		participants = append(participants, dto.SessionParticipant{
			ID:        participant.ID,
			UserID:    participant.UserID,
			GuestName: participant.GuestName,
			Score:     participant.Score,
			IsWinner:  &participant.IsWinner,
		})
	}

	return dto.SessionResponse{
		ID:           session.ID,
		GameID:       session.GameID,
		CreatorID:    session.CreatorID,
		Note:         session.Note,
		Place:        session.Place,
		Status:       session.Status,
		StartedAt:    session.StartedAt,
		FinishedAt:   session.FinishedAt,
		CreatedAt:    session.CreatedAt,
		UpdatedAt:    session.UpdatedAt,
		Participants: participants,
	}
}
