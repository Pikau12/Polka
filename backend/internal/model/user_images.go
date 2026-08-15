package model

type UserImage struct {
	UserID  int64 `gorm:"primaryKey;autoIncrement:false"`
	ImageID int64 `gorm:"primaryKey;autoIncrement:false"`

	User  User
	Image Image
}
