package dto

import (
	"github.com/polka/backend/internal/domain"
)

type SearchGameRequest struct {
	Name string `json:"name"`

	MinPlayTimeMinutes *int32 `json:"min_play_time_minutes,omitempty"`
	MaxPlayTimeMinutes *int32 `json:"max_play_time_minutes,omitempty"`

	MinAge *int32 `json:"min_age,omitempty"`

	AvailableCountPlayers []int32 `json:"available_count_players,omitempty"`

	MinWeight *float64 `json:"min_weight,omitempty"`
	MaxWeight *float64 `json:"max_weight,omitempty"`

	MinPolkaRating *float64 `json:"min_polka_rating,omitempty"`
	MinBggRating   *float64 `json:"min_bgg_rating,omitempty"`

	Mechanics  []string `json:"mechanics,omitempty"`
	Categories []string `json:"categories,omitempty"`
	Publishers []string `json:"publishers,omitempty"`
	Designers  []string `json:"designers,omitempty"`

	Offset int32 `json:"offset"`
	Limit  int32 `json:"limit"`
}

type SearchGameResponse struct {
	Games []domain.GameSearchInfo `json:"games"`

	NextOffset int32 `json:"next_offset"`

	HasNext bool `json:"has_next"`
}

type CreateGameRequest struct {
	Name string `json:"name" binding:"required"`
}

type CreateGameResponse struct {
	GameID int64  `json:"game_id"`
	Name   string `json:"name"`
}
