package model

type UserRole struct {
	UserID int64 `gorm:"primaryKey;autoIncrement:false"`
	RoleID int64 `gorm:"primaryKey;autoIncrement:false"`

	User User
	Role Role
}
