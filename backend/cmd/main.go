package main

import (
	"context"

	"github.com/polka/backend/internal/postgres"
	"github.com/polka/backend/internal/postgres/config"
)

func main() {
	mainCtx := context.Background()
	ctx, cancel := context.WithCancel(mainCtx)
	defer cancel()

	_, err := postgres.New(ctx, config.New())

	if err != nil {
		panic(err)
	}
}
