package repository

import (
	"context"
	"fmt"

	"github.com/polka/backend/internal/domain"
	"github.com/polka/backend/internal/model"
	"gorm.io/gorm"
)

type ImageRepository struct {
	db *gorm.DB
}

func NewImageRepository(db *gorm.DB) *ImageRepository {
	return &ImageRepository{db: db}
}

func (r *ImageRepository) CreateForGame(ctx context.Context, gameID int64, image *domain.Image) (*domain.Image, error) {
	dbImage := domainToImage(image)

	if err := r.db.
		WithContext(ctx).
		Transaction(func(tx *gorm.DB) error {
			if err := tx.
				Table("images").
				Create(dbImage).
				Error; err != nil {
				return fmt.Errorf("create image: %w", err)
			}

			if err := tx.
				Table("game_images").
				Create(&model.GameImage{GameID: gameID, ImageID: dbImage.ID}).
				Error; err != nil {
				return fmt.Errorf("create relation game image: %w", err)
			}

			return nil
		}); err != nil {
		return nil, err
	}

	image.ID = dbImage.ID

	return image, nil
}

func (r *ImageRepository) RemoveFromGame(ctx context.Context, gameID, imageID int64) error {
	return r.db.Transaction(func(tx *gorm.DB) error {
		if err := tx.
			WithContext(ctx).
			Table("game_images").
			Where("game_id = ? AND image_id = ?", gameID, imageID).
			Delete(&model.GameImage{}).
			Error; err != nil {
			return fmt.Errorf("remove game image relation: %w", err)
		}

		if err := tx.
			WithContext(ctx).
			Table("images").
			Where("id = ?", imageID).
			Delete(&model.Image{}).
			Error; err != nil {
			return fmt.Errorf("remove image: %w", err)
		}

		return nil
	})
}

func (r *ImageRepository) CreateForUser(ctx context.Context, userID int64, image *domain.Image) (*domain.Image, error) {
	dbImage := domainToImage(image)

	if err := r.db.
		WithContext(ctx).
		Transaction(func(tx *gorm.DB) error {
			if err := tx.
				Table("images").
				Create(dbImage).
				Error; err != nil {
				return fmt.Errorf("create image: %w", err)
			}

			if err := tx.
				Table("user_images").
				Create(&model.UserImage{UserID: userID, ImageID: dbImage.ID}).
				Error; err != nil {
				return fmt.Errorf("create relation user image: %w", err)
			}

			return nil
		}); err != nil {
		return nil, err
	}

	image.ID = dbImage.ID

	return image, nil
}

func (r *ImageRepository) RemoveFromUser(ctx context.Context, userID, imageID int64) error {
	return r.db.Transaction(func(tx *gorm.DB) error {
		if err := tx.
			WithContext(ctx).
			Table("user_images").
			Where("user_id = ? AND image_id = ?", userID, imageID).
			Delete(&model.UserImage{}).
			Error; err != nil {
			return fmt.Errorf("remove user image relation: %w", err)
		}

		if err := tx.
			WithContext(ctx).
			Table("images").
			Where("id = ?", imageID).
			Delete(&model.Image{}).
			Error; err != nil {
			return fmt.Errorf("remove image: %w", err)
		}

		return nil
	})
}

func (r *ImageRepository) CreateForCollectionItem(ctx context.Context, gameID, ownerID int64, image *domain.Image) (*domain.Image, error) {
	dbImage := domainToImage(image)

	if err := r.db.
		WithContext(ctx).
		Transaction(func(tx *gorm.DB) error {
			if err := tx.
				Table("images").
				Create(dbImage).
				Error; err != nil {
				return fmt.Errorf("create image: %w", err)
			}

			if err := tx.
				Table("collection_item_images").
				Create(&model.CollectionItemImage{GameID: gameID, OwnerID: ownerID, ImageID: dbImage.ID}).
				Error; err != nil {
				return fmt.Errorf("create relation game image: %w", err)
			}

			return nil
		}); err != nil {
		return nil, err
	}

	image.ID = dbImage.ID

	return image, nil
}

func (r *ImageRepository) RemoveFromCollectionItem(ctx context.Context, userID, gameID, imageID int64) error {
	return r.db.Transaction(func(tx *gorm.DB) error {
		if err := tx.
			WithContext(ctx).
			Table("collection_item_images").
			Where("owner_id = ? AND image_id = ? AND game_id = ?", userID, imageID, gameID).
			Delete(&model.CollectionItemImage{}).
			Error; err != nil {
			return fmt.Errorf("remove user image relation: %w", err)
		}

		if err := tx.
			WithContext(ctx).
			Table("images").
			Where("id = ?", imageID).
			Delete(&model.Image{}).
			Error; err != nil {
			return fmt.Errorf("remove image: %w", err)
		}

		return nil
	})
}

func (r *ImageRepository) GetGameImage(ctx context.Context, gameID, imageID int64) (*domain.Image, error) {
	image := model.Image{}

	err := r.db.
		WithContext(ctx).
		Table("images").
		Joins("JOIN game_images ON game_images.image_id = images.id").
		Where("game_images.game_id = ? AND game_images.image_id = ?", gameID, imageID).
		First(&image).
		Error

	if err != nil {
		return nil, fmt.Errorf("get images by game id: %w", err)
	}

	return imageToDomain(&image), nil
}

func (r *ImageRepository) GetGameImages(ctx context.Context, gameID int64) ([]domain.Image, error) {
	images := make([]model.Image, 0)

	err := r.db.
		WithContext(ctx).
		Table("images").
		Joins("JOIN game_images ON game_images.image_id = images.id").
		Where("game_images.game_id = ?", gameID).
		Find(&images).
		Error

	if err != nil {
		return nil, fmt.Errorf("get images by game id: %w", err)
	}

	res := make([]domain.Image, 0, len(images))

	for _, image := range images {
		res = append(res, *imageToDomain(&image))
	}

	return res, nil
}

func (r *ImageRepository) GetUserImage(ctx context.Context, userID, imageID int64) (*domain.Image, error) {
	image := model.Image{}

	err := r.db.
		WithContext(ctx).
		Table("images").
		Joins("JOIN user_images ON user_images.image_id = images.id").
		Where("user_images.user_id = ? AND user_images.image_id = ?", userID, imageID).
		First(&image).
		Error

	if err != nil {
		return nil, fmt.Errorf("get images by user id: %w", err)
	}

	return imageToDomain(&image), nil
}

func (r *ImageRepository) GetCollectionItemImage(ctx context.Context, userID, gameID, imageID int64) (*domain.Image, error) {
	image := model.Image{}

	err := r.db.
		WithContext(ctx).
		Table("images").
		Joins("JOIN collection_item_images ON collection_item_images.image_id = images.id").
		Where("collection_item_images.game_id = ? AND collection_item_images.owner_id = ? AND collection_item_images.image_id = ?", gameID, userID, imageID).
		First(&image).
		Error

	if err != nil {
		return nil, fmt.Errorf("get images by user id and game id: %w", err)
	}

	return imageToDomain(&image), nil
}

func imageToDomain(image *model.Image) *domain.Image {
	return &domain.Image{
		ID:        image.ID,
		Bucket:    image.Bucket,
		ObjectKey: image.ObjectKey,
	}
}

func domainToImage(image *domain.Image) *model.Image {
	return &model.Image{
		ID:        image.ID,
		Bucket:    image.Bucket,
		ObjectKey: image.ObjectKey,
	}
}
