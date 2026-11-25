package dto

import (
	"time"
)

type CreateSessionRequest struct {
	GameID int64   `json:"game_id" binding:"required,gt=0"`
	Note   *string `json:"note"`
	Place  *string `json:"place"`
	Status string  `json:"status"`

	StartedAt  *time.Time `json:"started_at"`
	FinishedAt *time.Time `json:"finished_at"`

	Participants []SessionParticipant `json:"participants"`
}

type UpdateSessionRequest struct {
	Note       *string    `json:"note"`
	Place      *string    `json:"place"`
	Status     *string    `json:"status"`
	StartedAt  *time.Time `json:"started_at"`
	FinishedAt *time.Time `json:"finished_at"`

	Participants []SessionParticipant `json:"participants"`
}

type SessionResponse struct {
	ID         int64      `json:"id"`
	GameID     int64      `json:"game_id"`
	CreatorID  int64      `json:"creator_id"`
	Note       *string    `json:"note"`
	Place      *string    `json:"place"`
	Status     string     `json:"status"`
	StartedAt  *time.Time `json:"started_at"`
	FinishedAt *time.Time `json:"finished_at"`
	CreatedAt  time.Time  `json:"created_at"`
	UpdatedAt  time.Time  `json:"updated_at"`

	Participants []SessionParticipant `json:"participants"`
}

type SessionParticipant struct {
	ID        *int64  `json:"id"`
	UserID    *int64  `json:"user_id"`
	GuestName *string `json:"name"`
	Score     int32   `json:"score"`
	IsWinner  *bool   `json:"is_winner"`
}

type GetSessionsResponse struct {
	Sessions []SessionResponse `json:"sessions"`
}
