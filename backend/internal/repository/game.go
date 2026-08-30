package repository

import (
	"context"
	"fmt"

	"github.com/polka/backend/internal/domain"
	"gorm.io/gorm"
)

type GameRepository struct {
	db *gorm.DB
}

func NewGameRepository(db *gorm.DB) *GameRepository {
	return &GameRepository{db: db}
}

func (r *GameRepository) SearchGameByFilter(ctx context.Context, filter *domain.GameFilter) ([]domain.GameSearchInfo, error) {
	if filter == nil {
		return nil, fmt.Errorf("game filter is nil")
	}

	games := make([]domain.GameSearchInfo, 0, filter.Limit)

	query := r.db.
		WithContext(ctx).
		Table("games").
		Select("games.id, games.name, games.description, games.polka_rating, games.bgg_rating")

	if filter.Name != "" {
		query = query.Where("games.name ILIKE ?", "%"+filter.Name+"%")
	}

	if filter.MinPlayTimeMinutes != nil {
		query = query.Where("games.min_play_time_minutes >= ?", *filter.MinPlayTimeMinutes)
	}

	if filter.MaxPlayTimeMinutes != nil {
		query = query.Where("games.max_play_time_minutes <= ?", *filter.MaxPlayTimeMinutes)
	}

	if filter.MinAge != nil {
		query = query.Where("games.min_age >= ?", *filter.MinAge)
	}

	if len(filter.AvailableCountPlayers) > 0 {
		playersQuery := r.db.Where("? = ANY(games.available_count_players)", filter.AvailableCountPlayers[0])

		for _, count := range filter.AvailableCountPlayers[1:] {
			playersQuery = playersQuery.Or("? = ANY(games.available_count_players)", count)
		}

		query = query.Where(playersQuery)
	}

	if filter.MinWeight != nil {
		query = query.Where("games.weight >= ?", *filter.MinWeight)
	}

	if filter.MaxWeight != nil {
		query = query.Where("games.weight <= ?", *filter.MaxWeight)
	}

	if filter.MinBggRating != nil {
		query = query.Where("games.bgg_rating >= ?", *filter.MinBggRating)
	}

	if filter.MinPolkaRating != nil {
		query = query.Where("games.polka_rating >= ?", *filter.MinPolkaRating)
	}

	if len(filter.Mechanics) > 0 {
		mechanicsQuery := r.db.
			WithContext(ctx).
			Table("game_mechanics").
			Select("game_mechanics.game_id").
			Joins("JOIN mechanics ON mechanics.id = game_mechanics.mechanic_id").
			Where("mechanics.name IN ?", filter.Mechanics).
			Group("game_mechanics.game_id").
			Having("COUNT(DISTINCT mechanics.id) = ?", len(filter.Mechanics))

		query = query.Where("games.id IN (?)", mechanicsQuery)
	}

	if len(filter.Designers) > 0 {
		designersQuery := r.db.
			WithContext(ctx).
			Table("game_designers").
			Select("game_designers.game_id").
			Joins("JOIN designers ON designers.id = game_designers.designer_id").
			Where("designers.name IN ?", filter.Designers).
			Group("game_designers.game_id").
			Having("COUNT(DISTINCT designers.id) = ?", len(filter.Designers))

		query = query.Where("games.id IN (?)", designersQuery)
	}

	if len(filter.Categories) > 0 {
		categoriesQuery := r.db.
			WithContext(ctx).
			Table("game_categories").
			Select("game_categories.game_id").
			Joins("JOIN categories ON categories.id = game_categories.category_id").
			Where("categories.name IN ?", filter.Categories).
			Group("game_categories.game_id").
			Having("COUNT(DISTINCT categories.id) = ?", len(filter.Categories))

		query = query.Where("games.id IN (?)", categoriesQuery)
	}

	if len(filter.Publishers) > 0 {
		publishersQuery := r.db.
			WithContext(ctx).
			Table("game_publishers").
			Select("game_publishers.game_id").
			Joins("JOIN publishers ON publishers.id = game_publishers.publisher_id").
			Where("publishers.name IN ?", filter.Publishers).
			Group("game_publishers.game_id").
			Having("COUNT(DISTINCT publishers.id) = ?", len(filter.Publishers))

		query = query.Where("games.id IN (?)", publishersQuery)
	}

	query = query.
		Order("games.id ASC").
		Limit(int(filter.Limit)).
		Offset(int(filter.Offset))

	if err := query.Scan(&games).Error; err != nil {
		return nil, fmt.Errorf("search game by filter: %w", err)
	}

	return games, nil
}

func (r *GameRepository) FindGamesByBggID(ctx context.Context, ids []int64) ([]int64, error) {
	if len(ids) == 0 {
		return []int64{}, nil
	}

	matched := make([]int64, 0)

	if err := r.db.
		WithContext(ctx).
		Table("games").
		Where("games.bgg_id IN (?)", ids).
		Pluck("games.bgg_id", &matched).
		Error; err != nil {
		return nil, fmt.Errorf("find games by bgg_id: %w", err)
	}

	return matched, nil
}

func (r *GameRepository) GetCountGamesByName(ctx context.Context, name string) (int64, error) {
	var count int64

	r.db.
		WithContext(ctx).
		Table("games").
		Where("games.name ILIKE ?", "%"+name+"%").
		Count(&count)

	return count, nil
}
