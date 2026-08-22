package service

import (
	"context"
	"fmt"

	"github.com/polka/backend/internal/api/dto"
	"github.com/polka/backend/internal/domain"
)

type GameService struct {
	gameRepository GameRepository
}

type GameRepository interface {
	SearchGameByFilter(c context.Context, gameFilter *domain.GameFilter) ([]domain.GameSearchInfo, error)
}

func NewGameService(gameRepository GameRepository) *GameService {
	return &GameService{gameRepository: gameRepository}
}

func (s *GameService) SearchGameByFilter(ctx context.Context, request dto.GameRequest) ([]domain.GameSearchInfo, error) {
	filter := gameRequestToGameFilter(request)

	if err := validateFilter(filter); err != nil {
		return nil, fmt.Errorf("invalid filter: %w", err)
	}

	normalizeFilter(filter)

	games, err := s.gameRepository.SearchGameByFilter(ctx, filter)
	if err != nil {
		return nil, err
	}

	return games, nil
}

func gameRequestToGameFilter(request dto.GameRequest) *domain.GameFilter {
	return &domain.GameFilter{
		Name: request.Name,

		MinPlayTimeMinutes: request.MinPlayTimeMinutes,
		MaxPlayTimeMinutes: request.MaxPlayTimeMinutes,

		MinAge: request.MinAge,

		AvailableCountPlayers: request.AvailableCountPlayers,

		MinWeight: request.MinWeight,
		MaxWeight: request.MaxWeight,

		MinPolkaRating: request.MinPolkaRating,
		MinBggRating:   request.MinBggRating,

		Mechanics:  request.Mechanics,
		Categories: request.Categories,
		Publishers: request.Publishers,
		Designers:  request.Designers,

		Page:     request.Page,
		PageSize: request.PageSize,
	}
}

func validateFilter(filter *domain.GameFilter) error {
	if filter.MinPlayTimeMinutes != nil && *filter.MinPlayTimeMinutes < 0 {
		return fmt.Errorf("min_play_time_minutes must be greater than or equal to 0")
	}

	if filter.MaxPlayTimeMinutes != nil && *filter.MaxPlayTimeMinutes < 0 {
		return fmt.Errorf("max_play_time_minutes must be greater than or equal to 0")
	}

	if filter.MinAge != nil && *filter.MinAge < 0 {
		return fmt.Errorf("min_age must be greater than or equal to 0")
	}

	if filter.AvailableCountPlayers != nil && len(filter.AvailableCountPlayers) > 0 {
		for _, count := range filter.AvailableCountPlayers {
			if count <= 0 {
				return fmt.Errorf("available_count_players must be greater than or equal to 0")
			}
		}
	}

	if filter.MinWeight != nil && *filter.MinWeight < 0 {
		return fmt.Errorf("min_weight must be greater than or equal to 0")
	}

	if filter.MaxWeight != nil && *filter.MaxWeight < 0 {
		return fmt.Errorf("max_weight must be greater than or equal to 0")
	}

	if filter.MinPolkaRating != nil && *filter.MinPolkaRating < 0 {
		return fmt.Errorf("min_polka_rating must be greater than or equal to 0")
	}

	if filter.MinBggRating != nil && *filter.MinBggRating < 0 {
		return fmt.Errorf("min_bgg_rating must be greater than or equal to 0")
	}

	if filter.Page <= 0 {
		return fmt.Errorf("invalid page")
	}

	if filter.PageSize <= 0 && filter.PageSize > 100 {
		return fmt.Errorf("invalid page_size")
	}

	return nil
}

func normalizeFilter(filter *domain.GameFilter) *domain.GameFilter {
	if filter.Mechanics != nil && len(filter.Mechanics) > 0 {
		filter.Mechanics = deleteDuplicates(filter.Mechanics)
	}

	if filter.Categories != nil && len(filter.Categories) > 0 {
		filter.Categories = deleteDuplicates(filter.Categories)
	}

	if filter.Designers != nil && len(filter.Designers) > 0 {
		filter.Designers = deleteDuplicates(filter.Designers)
	}

	if filter.Publishers != nil && len(filter.Publishers) > 0 {
		filter.Publishers = deleteDuplicates(filter.Publishers)
	}

	return filter
}

func deleteDuplicates(slice []string) []string {
	set := make(map[string]bool, len(slice))
	res := make([]string, 0, len(slice))

	for _, v := range slice {
		if set[v] {
			continue
		}

		set[v] = true
		res = append(res, v)
	}

	return res
}
