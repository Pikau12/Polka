package service

import (
	"context"
	"errors"
	"fmt"

	"github.com/polka/backend/internal/model"
	"github.com/polka/backend/internal/repository"
)

type CollectionService struct {
	collectionRepository collectionRepository
	userRepository       userRepository
	gameRepository       gameRepository
}

type CreateCollectionItemResult struct {
}

type collectionRepository interface {
	CreateCollectionItem(ctx context.Context, item *model.CollectionItem) error
	DeleteCollectionItem(ctx context.Context, gameID int64, userID int64) error
	GetCollection(ctx context.Context, userID int64) ([]model.Game, error)
}

type userRepository interface {
	GetByID(ctx context.Context, id int64) (*model.User, error)
}

type gameRepository interface {
	GetGameByID(ctx context.Context, gameID int64) (*model.Game, error)
}

type AddGameToCollectionResult struct {
}

func NewCollectionService(collectionRepository collectionRepository, userRepo userRepository, gameRepository gameRepository) *CollectionService {
	return &CollectionService{collectionRepository: collectionRepository, userRepository: userRepo, gameRepository: gameRepository}
}

func (s *CollectionService) AddGameToCollection(ctx context.Context, gameID int64, userID int64) error {
	if _, err := s.userRepository.GetByID(ctx, userID); err != nil {
		return fmt.Errorf("get user by id %w", err)
	}

	if _, err := s.gameRepository.GetGameByID(ctx, gameID); err != nil {
		return fmt.Errorf("get game by id %w", err)
	}

	err := s.collectionRepository.CreateCollectionItem(ctx, &model.CollectionItem{
		GameID:  gameID,
		OwnerID: userID,
		Status:  make(model.CollectionItemStatuses, 0),
	})

	if err != nil {
		if errors.Is(err, repository.ErrCollectionItemAlreadyExists) {
			return ErrCollectionItemAlreadyExists
		}

		return fmt.Errorf("create collection item %w", err)
	}

	return nil
}

func (s *CollectionService) DeleteGameFromCollection(ctx context.Context, gameID int64, userID int64) error {
	if _, err := s.userRepository.GetByID(ctx, userID); err != nil {
		return fmt.Errorf("get user by id %w", err)
	}

	if _, err := s.gameRepository.GetGameByID(ctx, gameID); err != nil {
		return fmt.Errorf("get game by id %w", err)
	}

	if err := s.collectionRepository.DeleteCollectionItem(ctx, gameID, userID); err != nil {
		return fmt.Errorf("delete game from collection %w", err)
	}

	return nil
}

func (s *CollectionService) GetCollection(ctx context.Context, userID int64) ([]model.Game, error) {
	if _, err := s.userRepository.GetByID(ctx, userID); err != nil {
		return nil, fmt.Errorf("get user by id %w", err)
	}

	games, err := s.collectionRepository.GetCollection(ctx, userID)
	if err != nil {
		return nil, fmt.Errorf("get collection %w", err)
	}

	return games, nil
}
