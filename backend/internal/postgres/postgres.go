package postgres

import (
	"context"

	"github.com/polka/backend/internal/config"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

func New(ctx context.Context, c config.PostgresConfig) (*gorm.DB, error) {
	db, err := gorm.Open(postgres.Open(c.DSN()), &gorm.Config{TranslateError: true})
	if err != nil {
		return nil, err
	}

	sqlDB, err := db.DB()
	if err != nil {
		sqlDB.Close()

		return nil, err
	}

	if err := sqlDB.PingContext(ctx); err != nil {
		return nil, err
	}

	return db, nil
}
