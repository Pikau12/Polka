package domain

type GameSearchInfo struct {
	ID            int64  `json:"id"`
	BggID         int64  `json:"bgg_id"`
	Name          string `json:"name"`
	YearPublished *int32 `json:"year_published,omitempty"`
}
