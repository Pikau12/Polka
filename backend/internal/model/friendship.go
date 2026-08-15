package model

type FriendshipStatus string

const (
	FriendshipStatusFriend   FriendshipStatus = "friend"
	FriendshipStatusRequest  FriendshipStatus = "request"
	FriendshipStatusRejected FriendshipStatus = "rejected"
)

type Friendship struct {
	SenderID   int64 `gorm:"primaryKey;autoIncrement:false"`
	AcceptorID int64 `gorm:"primaryKey;autoIncrement:false"`
	Status     FriendshipStatus

	Sender   User
	Acceptor User
}
