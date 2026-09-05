package repository

import (
	"context"
	"errors"
	"fmt"

	"github.com/polka/backend/internal/model"
	"gorm.io/gorm"
)

type CollectionRepository struct {
	db *gorm.DB
}

func NewCollectionRepository(db *gorm.DB) *CollectionRepository {
	return &CollectionRepository{db: db}
}

func (r *CollectionRepository) CreateCollectionItem(ctx context.Context, item *model.CollectionItem) error {
	if err := r.db.
		WithContext(ctx).
		Table("collection_items").
		Create(item).
		Error; err != nil {
		if errors.Is(err, gorm.ErrDuplicatedKey) {
			return ErrCollectionItemAlreadyExists
		}

		return fmt.Errorf("add game to collection %w", err)
	}

	return nil
}

func (r *CollectionRepository) DeleteCollectionItem(ctx context.Context, gameID int64, userID int64) error {
	tx := r.db.
		WithContext(ctx).
		Table("collection_items").
		Where("game_id = ?", gameID).
		Where("owner_id = ?", userID).
		Delete(&model.CollectionItem{})

	if tx.Error != nil {
		return fmt.Errorf("remove game from collection %w", tx.Error)
	}

	if tx.RowsAffected == 0 {
		return ErrGameNotFound
	}

	return nil
}

func (r *CollectionRepository) GetCollection(ctx context.Context, userID int64) ([]model.Game, error) {
	games := make([]model.Game, 0)

	if err := r.db.
		WithContext(ctx).
		Table("collection_items").
		Joins("JOIN games ON games.id = collection_items.game_id").
		Select("games.*").
		Omit("created_at", "updated_at").
		Where("owner_id = ?", userID).
		Find(&games).
		Error; err != nil {
		return nil, fmt.Errorf("get collection %w", err)
	}

	return games, nil
}
