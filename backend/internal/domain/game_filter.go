package domain

type GameFilter struct {
	Name string

	MinPlayTimeMinutes *int32
	MaxPlayTimeMinutes *int32

	MinAge *int32

	AvailableCountPlayers []int32

	MinWeight *float64
	MaxWeight *float64

	MinPolkaRating *float64
	MinBggRating   *float64

	Mechanics  []string
	Categories []string
	Publishers []string
	Designers  []string

	Offset int32
	Limit  int32
}
