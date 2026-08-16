package model

type GameCategory struct {
	GameID     int64 `gorm:"primaryKey;autoIncrement:false"`
	CategoryID int64 `gorm:"primaryKey;autoIncrement:false"`

	Game     Game
	Category Category
}
