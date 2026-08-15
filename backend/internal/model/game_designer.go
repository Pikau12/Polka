package model

type GameDesigner struct {
	GameID     int64 `gorm:"primaryKey;autoIncrement:false"`
	DesignerID int64 `gorm:"primaryKey;autoIncrement:false"`

	Game     Game
	Designer Designer
}
