package model

import "time"

type SessionStatus string

const (
	SessionStatusScheduled SessionStatus = "scheduled"
	SessionStatusStarted   SessionStatus = "started"
	SessionStatusFinished  SessionStatus = "finished"
	SessionStatusCancelled SessionStatus = "cancelled"
)

type Session struct {
	ID int64

	GameID    int64
	CreatorID int64

	Note   *string
	Place  *string
	Status SessionStatus

	StartedAt  *time.Time
	FinishedAt *time.Time

	CreatedAt time.Time
	UpdatedAt time.Time

	Game    Game
	Creator User
}
