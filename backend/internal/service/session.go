package service

import (
	"context"
	"errors"
	"fmt"
	"strings"
	"time"
	"unicode/utf8"

	"github.com/polka/backend/internal/domain"
	"github.com/polka/backend/internal/model"
	"github.com/polka/backend/internal/repository"
)

var (
	ErrInvalidSession      = errors.New("invalid session")
	ErrSessionNotFound     = errors.New("session not found")
	ErrSessionGameNotFound = errors.New("game not found")
	ErrSessionForbidden    = errors.New("session access forbidden")
	ErrSessionConflict     = errors.New("session data conflict")
)

type SessionGameRepository interface {
	GetGameByID(ctx context.Context, gameID int64) (*model.Game, error)
}

type SessionRepository interface {
	Create(ctx context.Context, session *domain.Session) (*domain.Session, error)
	GetAll(ctx context.Context, userID int64) ([]domain.Session, error)
	GetByID(ctx context.Context, id int64) (*domain.Session, error)
	IsParticipant(ctx context.Context, sessionID, userID int64) (bool, error)
	Update(ctx context.Context, session *domain.Session) (*domain.Session, error)
	Delete(ctx context.Context, id, creatorID int64) error
}

type SessionService struct {
	sessions SessionRepository
	games    SessionGameRepository
}

func NewSessionService(sessions SessionRepository, games SessionGameRepository) *SessionService {
	return &SessionService{sessions: sessions, games: games}
}

func (s *SessionService) Create(ctx context.Context, session *domain.Session) (*domain.Session, error) {
	normalizeSessionTimes(session)

	if err := validateSession(session); err != nil {
		return nil, err
	}

	if err := s.checkGame(ctx, session.GameID); err != nil {
		return nil, err
	}

	created, err := s.sessions.Create(ctx, session)
	if err != nil {
		return nil, fmt.Errorf("create session: %w", sessionError(err))
	}

	return created, nil
}

func (s *SessionService) GetAll(ctx context.Context, userID int64) ([]domain.Session, error) {
	sessions, err := s.sessions.GetAll(ctx, userID)
	if err != nil {
		return nil, fmt.Errorf("list sessions: %w", err)
	}

	if sessions == nil {
		sessions = make([]domain.Session, 0)
	}

	return sessions, nil
}

func (s *SessionService) GetByID(ctx context.Context, userID, id int64) (*domain.Session, error) {
	session, err := s.getSession(ctx, id)
	if err != nil {
		return nil, err
	}

	if session.CreatorID == userID {
		return session, nil
	}

	participant, err := s.sessions.IsParticipant(ctx, id, userID)
	if err != nil {
		return nil, fmt.Errorf("check session participant: %w", err)
	}

	if !participant {
		return nil, ErrSessionForbidden
	}

	return session, nil
}

func (s *SessionService) Update(ctx context.Context, session *domain.Session) (*domain.Session, error) {
	if session == nil {
		return nil, fmt.Errorf("%w: session is nil", ErrInvalidSession)
	}

	serverSession, err := s.getSession(ctx, session.ID)
	if err != nil {
		return nil, err
	}

	if serverSession.CreatorID != session.CreatorID {
		return nil, ErrSessionForbidden
	}

	updated := *serverSession
	if session.Note != nil {
		updated.Note = session.Note
	}
	if session.Place != nil {
		updated.Place = session.Place
	}
	if session.Status != "" {
		updated.Status = session.Status
	}
	if session.StartedAt != nil {
		updated.StartedAt = session.StartedAt
	}
	if session.FinishedAt != nil {
		updated.FinishedAt = session.FinishedAt
	}
	updated.Participants = session.Participants

	normalizeSessionTimes(&updated)

	if err := validateSession(&updated); err != nil {
		return nil, err
	}

	if err := s.checkGame(ctx, updated.GameID); err != nil {
		return nil, err
	}

	res, err := s.sessions.Update(ctx, &updated)
	if err != nil {
		return nil, fmt.Errorf("update session: %w", sessionError(err))
	}

	return res, nil
}

func (s *SessionService) Delete(ctx context.Context, userID, id int64) error {
	session, err := s.getSession(ctx, id)
	if err != nil {
		return err
	}

	if session.CreatorID != userID {
		return ErrSessionForbidden
	}

	if err := s.sessions.Delete(ctx, id, userID); err != nil {
		return fmt.Errorf("delete session: %w", sessionError(err))
	}

	return nil
}

func (s *SessionService) getSession(ctx context.Context, id int64) (*domain.Session, error) {
	session, err := s.sessions.GetByID(ctx, id)
	if err != nil {
		return nil, fmt.Errorf("get session: %w", sessionError(err))
	}

	return session, nil
}

func (s *SessionService) checkGame(ctx context.Context, id int64) error {
	if _, err := s.games.GetGameByID(ctx, id); err != nil {
		if errors.Is(err, repository.ErrGameNotFound) {
			return ErrSessionGameNotFound
		}
		return fmt.Errorf("get session game: %w", err)
	}
	return nil
}

func sessionError(err error) error {
	switch {
	case errors.Is(err, repository.ErrSessionNotFound):
		return ErrSessionNotFound
	case errors.Is(err, repository.ErrSessionConflict):
		return ErrSessionConflict
	default:
		return err
	}
}

func normalizeSessionTimes(session *domain.Session) {
	if session.StartedAt != nil {
		value := session.StartedAt.UTC().Truncate(time.Microsecond)
		session.StartedAt = &value
	}
	if session.FinishedAt != nil {
		value := session.FinishedAt.UTC().Truncate(time.Microsecond)
		session.FinishedAt = &value
	}
}

func validateSession(session *domain.Session) error {
	if session.GameID <= 0 {
		return fmt.Errorf("%w: game_id must be positive", ErrInvalidSession)
	}
	switch model.SessionStatus(session.Status) {
	case model.SessionStatusScheduled, model.SessionStatusStarted, model.SessionStatusFinished, model.SessionStatusCancelled:
	default:
		return fmt.Errorf("%w: unknown status", ErrInvalidSession)
	}
	if session.Note != nil && utf8.RuneCountInString(*session.Note) > 1024 {
		return fmt.Errorf("%w: note exceeds 1024 characters", ErrInvalidSession)
	}
	if session.Place != nil && (strings.TrimSpace(*session.Place) == "" || utf8.RuneCountInString(*session.Place) > 255) {
		return fmt.Errorf("%w: place must contain 1 to 255 characters", ErrInvalidSession)
	}
	if session.StartedAt != nil && session.FinishedAt != nil && !session.StartedAt.Before(*session.FinishedAt) {
		return fmt.Errorf("%w: started_at must precede finished_at", ErrInvalidSession)
	}
	return nil
}
