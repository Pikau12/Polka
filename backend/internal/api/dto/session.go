package dto

import "time"

type CreateSessionRequest struct {
	GameID int64   `json:"game_id"`
	Place  *string `json:"place"`

	StartedAt  *time.Time `json:"started_at"`
	FinishedAt *time.Time `json:"finished_at"`
}

type CreateSessionResponse struct {
	ID         int64   `json:"id"`
	GameID     int64   `json:"game_id"`
	Place      *string `json:"place"`
	Status     string  `json:"status"`
	StartedAt  *time.Time
	FinishedAt *time.Time
}
