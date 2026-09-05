package model

import "time"

type AuthSession struct {
	ID               int64
	UserID           int64
	RefreshTokenHash string
	CreatedAt        time.Time
	ExpiresAt        time.Time
	RevokedAt        *time.Time

	User User
}
