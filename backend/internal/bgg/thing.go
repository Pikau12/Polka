package bgg

import (
	"context"
	"encoding/xml"
	"fmt"
	"net/http"
	"net/url"
	"strconv"
	"strings"

	"github.com/polka/backend/internal/domain"
)

func (c *Client) Thing(ctx context.Context, bggIDs []int64) ([]domain.BggGameThing, error) {
	u, err := url.Parse(c.baseURL + "/thing")
	if err != nil {
		return nil, fmt.Errorf("parse bgg thing: %w", err)
	}

	params := u.Query()
	ids := make([]string, 0, len(bggIDs))

	for _, id := range bggIDs {
		ids = append(ids, strconv.FormatInt(id, 10))
	}

	params.Set("id", strings.Join(ids, ","))

	u.RawQuery = params.Encode()

	req, err := http.NewRequestWithContext(ctx, "GET", u.String(), nil)
	if err != nil {
		return nil, fmt.Errorf("create request: %w", err)
	}

	req.Header.Set("Authorization", "Bearer "+c.token)

	resp, err := c.httpClient.Do(req)
	if err != nil {
		return nil, fmt.Errorf("do request thing: %w", err)
	}

	defer resp.Body.Close()
	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("bad status thing: %s", resp.Status)
	}

	thingResponse := &thingResponseXML{}

	if err := xml.NewDecoder(resp.Body).Decode(thingResponse); err != nil {
		return nil, fmt.Errorf("decode thing response: %w", err)
	}

	games := make([]domain.BggGameThing, 0, len(thingResponse.Items))

	for _, item := range thingResponse.Items {
		yearPublished, err := strconv.ParseInt(item.YearPublished.Value, 10, 32)
		if err != nil {
			return nil, fmt.Errorf("parse year published: %w", err)
		}

		minPlayers, err := strconv.ParseInt(item.MinPlayers.Value, 10, 32)
		if err != nil {
			return nil, fmt.Errorf("parse min players: %w", err)
		}

		maxPlayers, err := strconv.ParseInt(item.MaxPlayers.Value, 10, 32)
		if err != nil {
			return nil, fmt.Errorf("parse max players: %w", err)
		}

		playingTime, err := strconv.ParseInt(item.PlayingTime.Value, 10, 32)
		if err != nil {
			return nil, fmt.Errorf("parse playing time: %w", err)
		}

		minPlayTime, err := strconv.ParseInt(item.MinPlayTime.Value, 10, 32)
		if err != nil {
			return nil, fmt.Errorf("parse min play time: %w", err)
		}

		maxPlayTime, err := strconv.ParseInt(item.MaxPlayTime.Value, 10, 32)
		if err != nil {
			return nil, fmt.Errorf("parse max play time: %w", err)
		}

		minAge, err := strconv.ParseInt(item.MinAge.Value, 10, 32)
		if err != nil {
			return nil, fmt.Errorf("parse min age: %w", err)
		}

		var name string

		for _, n := range item.Names {
			if n.Type == "primary" {
				name = n.Value
				break
			}
		}

		games = append(games, domain.BggGameThing{
			BggID:         item.ID,
			Thumbnail:     item.Thumbnail,
			Image:         item.Image,
			Name:          name,
			Description:   item.Description,
			YearPublished: int32(yearPublished),
			MinPlayers:    int32(minPlayers),
			MaxPlayers:    int32(maxPlayers),
			PlayingTime:   int32(playingTime),
			MinPlayTime:   int32(minPlayTime),
			MaxPlayTime:   int32(maxPlayTime),
			MinAge:        int32(minAge),
		})
	}

	return games, nil
}
