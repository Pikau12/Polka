package model

type GameMechanic struct {
	GameID     int64 `gorm:"primaryKey;autoIncrement:false"`
	MechanicID int64 `gorm:"primaryKey;autoIncrement:false"`

	Game     Game
	Mechanic Mechanic
}
