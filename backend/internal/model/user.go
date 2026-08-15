package model

type User struct {
	ID           int64
	BggID        *int64
	Login        string
	Username     string
	PasswordHash string
	Email        string
}
