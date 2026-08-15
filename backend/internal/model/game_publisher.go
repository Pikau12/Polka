package model

type GamePublisher struct {
	GameID      int64 `gorm:"primaryKey;autoIncrement:false"`
	PublisherID int64 `gorm:"primaryKey;autoIncrement:false"`

	Game      Game
	Publisher Publisher
}
