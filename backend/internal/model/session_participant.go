package model

type SessionParticipant struct {
	ID        int64
	UserID    *int64
	SessionID int64
	Score     int32
	IsWinner  bool
	GuestName *string

	User    *User
	Session Session
}
