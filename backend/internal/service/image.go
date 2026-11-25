package service

import (
	"context"
	"fmt"
	"io"
	"mime/multipart"
	"uuid"

	"github.com/polka/backend/internal/domain"
	"github.com/polka/backend/internal/model"
	"github.com/polka/backend/internal/storage"
)

type ImageService struct {
	imageRepository GameImageRepository
	userRepository  UserImageRepository
	gameRepository  gameRepository
	storage         Garage
}

type UploadGameImage struct {
	Body        multipart.File
	Size        int64
	ContentType string
}

type ImageContent struct {
	Body          io.ReadCloser
	ContentType   string
	ContentLength int64
}

type GameImageRepository interface {
	CreateForGame(ctx context.Context, gameID int64, image *domain.Image) (*domain.Image, error)
	RemoveFromGame(ctx context.Context, gameID, imageID int64) error
	CreateForUser(ctx context.Context, userID int64, image *domain.Image) (*domain.Image, error)
	RemoveFromUser(ctx context.Context, userID, imageID int64) error
	CreateForCollectionItem(ctx context.Context, gameID, ownerID int64, image *domain.Image) (*domain.Image, error)
	RemoveFromCollectionItem(ctx context.Context, userID, gameID, imageID int64) error
	GetGameImage(ctx context.Context, gameID, imageID int64) (*domain.Image, error)
	GetUserImage(ctx context.Context, userID, imageID int64) (*domain.Image, error)
	GetCollectionItemImage(ctx context.Context, userID, gameID, imageID int64) (*domain.Image, error)
	GetGameImages(ctx context.Context, gameID int64) ([]domain.Image, error)
}

type Garage interface {
	Put(ctx context.Context, key string, body io.Reader, contentType string, size int64) (*domain.StoredObject, error)
	Get(ctx context.Context, key string) (*storage.Object, error)
	Delete(ctx context.Context, key string) error
}

type UserImageRepository interface {
	GetByID(ctx context.Context, id int64) (*model.User, error)
}

func NewImageService(imageRepo GameImageRepository, userRepo userRepository, gameRepo GameRepository, storage Garage) *ImageService {
	return &ImageService{imageRepository: imageRepo, userRepository: userRepo, gameRepository: gameRepo, storage: storage}
}

func (s *ImageService) UploadGameImage(ctx context.Context, gameID int64, image *UploadGameImage) (*domain.Image, error) {
	_, err := s.gameRepository.GetGameByID(ctx, gameID)
	if err != nil {
		return nil, fmt.Errorf("get game by id when upload game image: %w", err)
	}

	key, err := generateObjectKey(image, gameID)
	if err != nil {
		return nil, fmt.Errorf("generate object key when upload game image: %w", err)
	}

	object, err := s.storage.Put(ctx, key, image.Body, image.ContentType, image.Size)
	if err != nil {
		return nil, fmt.Errorf("upload gameImage to garage: %w", err)
	}

	domainImage := &domain.Image{
		Bucket:    object.Bucket,
		ObjectKey: object.ObjectKey,
	}

	savedImage, err := s.imageRepository.CreateForGame(ctx, gameID, domainImage)
	if err != nil {
		return nil, fmt.Errorf("upload gameImage to garage: %w", err)
	}

	return savedImage, nil
}

func (s *ImageService) DeleteGameImage(ctx context.Context, gameID int64, imageID int64) error {
	if _, err := s.gameRepository.GetGameByID(ctx, gameID); err != nil {
		return fmt.Errorf("get game by id when delete game image: %w", err)
	}

	image, err := s.imageRepository.GetGameImage(ctx, gameID, imageID)
	if err != nil {
		return fmt.Errorf("get game by id when delete game image: %w", err)
	}

	if err = s.imageRepository.RemoveFromGame(ctx, gameID, imageID); err != nil {
		return fmt.Errorf("delete game image from db: %w", err)
	}

	if err = s.storage.Delete(ctx, image.ObjectKey); err != nil {
		return fmt.Errorf("delete game image from garage: %w", err)
	}

	return nil
}

func (s *ImageService) GetGameImage(ctx context.Context, gameID, imageID int64) (*ImageContent, error) {
	if _, err := s.gameRepository.GetGameByID(ctx, gameID); err != nil {
		return nil, fmt.Errorf("get game by id when delete game image: %w", err)
	}

	image, err := s.imageRepository.GetGameImage(ctx, gameID, imageID)
	if err != nil {
		return nil, fmt.Errorf("get game by id when delete game image: %w", err)
	}

	object, err := s.storage.Get(ctx, image.ObjectKey)
	if err != nil {
		return nil, fmt.Errorf("get game by id when delete game image: %w", err)
	}

	return &ImageContent{Body: object.Body, ContentType: object.ContentType, ContentLength: object.ContentLength}, nil
}

func (s *ImageService) GetGameImageIDs(ctx context.Context, gameID int64) ([]int64, error) {
	if _, err := s.gameRepository.GetGameByID(ctx, gameID); err != nil {
		return nil, fmt.Errorf("get game by id when delete game image: %w", err)
	}

	images, err := s.imageRepository.GetGameImages(ctx, gameID)
	if err != nil {
		return nil, fmt.Errorf("get game by id when delete game image: %w", err)
	}

	ids := make([]int64, 0, len(images))

	for _, image := range images {
		ids = append(ids, image.ID)
	}

	return ids, nil
}

func (s *ImageService) UploadUserImage(ctx context.Context, userID int64, image *UploadGameImage) (*domain.Image, error) {
	_, err := s.userRepository.GetByID(ctx, userID)
	if err != nil {
		return nil, fmt.Errorf("get game by id when upload game image: %w", err)
	}

	key, err := generateObjectKey(image, userID)
	if err != nil {
		return nil, fmt.Errorf("generate object key when upload game image: %w", err)
	}

	object, err := s.storage.Put(ctx, key, image.Body, image.ContentType, image.Size)
	if err != nil {
		return nil, fmt.Errorf("upload gameImage to garage: %w", err)
	}

	domainImage := &domain.Image{
		Bucket:    object.Bucket,
		ObjectKey: object.ObjectKey,
	}

	savedImage, err := s.imageRepository.CreateForUser(ctx, userID, domainImage)
	if err != nil {
		return nil, fmt.Errorf("upload gameImage to garage: %w", err)
	}

	return savedImage, nil
}

func (s *ImageService) DeleteUserImage(ctx context.Context, gameID int64, imageID int64) error {
	if _, err := s.userRepository.GetByID(ctx, gameID); err != nil {
		return fmt.Errorf("get game by id when delete game image: %w", err)
	}

	image, err := s.imageRepository.GetUserImage(ctx, gameID, imageID)
	if err != nil {
		return fmt.Errorf("get game by id when delete game image: %w", err)
	}

	if err = s.imageRepository.RemoveFromUser(ctx, gameID, imageID); err != nil {
		return fmt.Errorf("delete game image from db: %w", err)
	}

	if err = s.storage.Delete(ctx, image.ObjectKey); err != nil {
		return fmt.Errorf("delete game image from garage: %w", err)
	}

	return nil
}

func generateObjectKey(image *UploadGameImage, gameID int64) (string, error) {
	ext, err := extensionByContentType(image.ContentType)
	if err != nil {
		return "", fmt.Errorf("get extension by content type: %w", err)
	}

	return fmt.Sprintf("games/%d/%s%s", gameID, uuid.New().String(), ext), nil
}

func extensionByContentType(contentType string) (string, error) {
	switch contentType {
	case "image/jpeg":
		return ".jpg", nil
	case "image/png":
		return ".png", nil
	case "image/webp":
		return ".webp", nil
	default:
		return "", fmt.Errorf("unsupported image content type: %s", contentType)
	}
}
