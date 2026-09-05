package domain

type BggGameThing struct {
	BggID int64

	Thumbnail string
	Image     string

	Name string

	Description string

	YearPublished int32

	MinPlayers int32
	MaxPlayers int32

	PlayingTime int32

	MinPlayTime int32
	MaxPlayTime int32

	MinAge int32
}
