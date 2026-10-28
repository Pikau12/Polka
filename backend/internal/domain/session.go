package domain

import "time"

type Session struct {
	ID         int64   `json:"session_id"`
	GameID     int64   `json:"game_id"`
	Place      *string `json:"place"`
	Status     string  `json:"status"`
	StartedAt  *time.Time
	FinishedAt *time.Time
}
