package dto

import "github.com/polka/backend/internal/domain"

type GameRequest struct {
	Name *string `json:"name,omitempty"`

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

	Page     int32 `json:"page"`
	PageSize int32 `json:"page_size"`
}

type GameResponse struct {
	Games []domain.GameSearchInfo `json:"games"`

	Page     int32 `json:"page"`
	PageSize int32 `json:"page_size"`

	HasNext bool `json:"has_next"`
}
