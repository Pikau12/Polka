package model

type GameArtist struct {
	GameID   int64 `gorm:"primaryKey;autoIncrement:false"`
	ArtistID int64 `gorm:"primaryKey;autoIncrement:false"`

	Game   Game
	Artist Artist
}
