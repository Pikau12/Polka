package repository

import "errors"

var (
	ErrLoginAlreadyExists  = errors.New("user already exists")
	ErrEmailAlreadyExists  = errors.New("user already exists")
	ErrUserNotFound        = errors.New("user not found")
	ErrAuthSessionNotFound = errors.New("auth session not found")
)
