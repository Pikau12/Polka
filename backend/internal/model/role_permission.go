package model

type RolePermission struct {
	PermissionID int64 `gorm:"primaryKey;autoIncrement:false"`
	RoleID       int64 `gorm:"primaryKey;autoIncrement:false"`

	Permission Permission
	Role       Role
}
