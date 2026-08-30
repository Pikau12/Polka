package bgg

import (
	"net/http"
	"time"

	"github.com/polka/backend/internal/config"
)

type Client struct {
	httpClient *http.Client
	baseURL    string
	token      string
}

func NewClient(config *config.BggConfig) *Client {
	return &Client{
		httpClient: &http.Client{
			Timeout: 10 * time.Second,
		},

		baseURL: config.BaseURL,
		token:   config.Token,
	}
}
