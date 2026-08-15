package model

type GameImage struct {
	GameID  int64 `gorm:"primaryKey;autoIncrement:false"`
	ImageID int64 `gorm:"primaryKey;autoIncrement:false"`

	Game  Game
	Image Image
}
