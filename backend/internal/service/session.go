package service

import (
	"context"
	"fmt"
	"time"

	"github.com/polka/backend/internal/domain"
	"github.com/polka/backend/internal/model"
)

type SessionService struct {
	sessionRepository SessionRepository
	gameRepository    SessionGameRepository
	userRepository SessionUserRepository
}

type SessionGameRepository interface {
	GetGameByID(ctx context.Context, gameID int64) (*model.Game, error)
}

type SessionUserRepository interface {

}

type SessionRepository interface {
	Create(ctx context.Context, session *model.) error
}

func NewSessionService(sessionRepository SessionRepository, gameRepository SessionGameRepository, userRepository SessionUserRepository) *SessionService {
	return &SessionService{sessionRepository: sessionRepository, gameRepository: gameRepository, userRepository: userRepository}
}

func (s *SessionService)CreateSession(gameID int64, place *string, startedAt, finishedAt *time.Time) (*domain.Session, error) {
	_, err := s.gameRepository.GetGameByID(context.Background(), gameID)
	if err != nil {
		return nil, fmt.Errorf("get game by id: %w", err)
	}

	session := &domain.Session{GameID: gameID, Place: place, StartedAt: startedAt, FinishedAt: finishedAt}

}
