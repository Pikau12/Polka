package bgg

import (
	"context"
	"encoding/xml"
	"fmt"
	"net/http"
	"net/url"
	"strconv"

	"github.com/polka/backend/internal/domain"
)

func (c *Client) Search(ctx context.Context, name string) ([]domain.BggGameSearch, error) {
	u, err := url.Parse(c.baseURL + "/search")
	if err != nil {
		return nil, fmt.Errorf("parse bgg search: %w", err)
	}

	params := u.Query()
	params.Set("query", name)
	params.Set("type", "boardgame")

	u.RawQuery = params.Encode()

	req, err := http.NewRequestWithContext(ctx, http.MethodGet, u.String(), nil)
	if err != nil {
		return nil, fmt.Errorf("create request: %w", err)
	}

	req.Header.Set("Authorization", fmt.Sprintf("Bearer %s", c.token))

	resp, err := c.httpClient.Do(req)
	if err != nil {
		return nil, fmt.Errorf("do request: %w", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("search result: %s", resp.Status)
	}

	searchResponse := searchResponseXML{}

	if err := xml.NewDecoder(resp.Body).Decode(&searchResponse); err != nil {
		return nil, fmt.Errorf("decode bgg response: %w", err)
	}

	games := make([]domain.BggGameSearch, 0, len(searchResponse.Items))

	for _, item := range searchResponse.Items {
		game := domain.BggGameSearch{}

		if item.ID <= 0 {
			continue
		}

		if item.Name.Value == ""{
			continue
		}

		if item.YearPublished.Value != "" {
			yearPublished, err := strconv.ParseInt(item.YearPublished.Value, 10, 32)
			if err != nil {
				return nil, fmt.Errorf("parse year published: %w", err)
			}
			game.YearPublished = int32(yearPublished)
		}

		games = append(games, game)
	}

	return games, nil
}
