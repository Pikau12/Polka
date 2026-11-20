package domain

import "time"

type Session struct {
	ID         int64
	GameID     int64
	CreatorID  int64
	Note       *string
	Place      *string
	Status     string
	StartedAt  *time.Time
	FinishedAt *time.Time
	CreatedAt  time.Time
	UpdatedAt  time.Time

	Participants []SessionParticipant
}

type SessionParticipant struct {
	ID        *int64
	UserID    *int64
	GuestName *string
	Score     int32
	IsWinner  bool
}
