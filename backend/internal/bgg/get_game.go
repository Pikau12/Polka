package bgg

import (
	"context"
	"fmt"
	"net/url"
	"strconv"

	"github.com/polka/backend/internal/model"
)

func (c *Client) GetGame(ctx context.Context, gameID int64) (*model.Game, error) {
	u, err := url.Parse(c.baseURL + "/thing")
	if err != nil {
		return nil, fmt.Errorf("parse bgg thing: %w", err)
	}

	params := u.Query()
	params.Set()
	params.Set("game_id", strconv.FormatInt(gameID, 10))
}
