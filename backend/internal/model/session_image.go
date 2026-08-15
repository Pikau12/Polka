package model

type SessionImage struct {
	SessionID int64 `gorm:"primaryKey;autoIncrement:false"`
	ImageID   int64 `gorm:"primaryKey;autoIncrement:false"`

	Session Session
	Image   Image
}
