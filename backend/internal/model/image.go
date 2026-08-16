package model

import "time"

type Image struct {
	ID        int64
	Bucket    string
	ObjectKey string

	CreatedAt time.Time
	UpdatedAt time.Time
}
