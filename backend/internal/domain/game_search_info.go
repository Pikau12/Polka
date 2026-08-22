package domain

type GameSearchInfo struct {
	ID          int64   `json:"id"`
	Name        string  `json:"name"`
	Description string  `json:"description"`
	PolkaRating float64 `json:"polka_rating"`
	BggRating   float64 `json:"bgg_rating"`
}
