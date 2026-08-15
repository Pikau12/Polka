package model

import "time"

type Game struct {
	ID    int64
	BggID *int64

	Name        string
	Description *string

	BggRating   *float64
	PolkaRating *float64

	BestCountPlayers      []int32
	AvailableCountPlayers []int32

	MinPlayTimeMinutes *int32
	MaxPlayTimeMinutes *int32
	MinAge             *int32

	Weight *float64

	CreatedAt time.Time
	UpdatedAt time.Time
}
