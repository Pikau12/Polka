package bgg

type thingResponseXML struct {
	Items []thingItemXML `xml:"item"`
}

type thingItemXML struct {
	ID int64 `xml:"id,attr"`

	Thumbnail string `xml:"thumbnail"`
	Image     string `xml:"image"`

	Names []thingNameXML `xml:"name"`

	Description string `xml:"description"`

	YearPublished thingValueXML `xml:"yearpublished"`

	MinPlayers thingValueXML `xml:"minplayers"`
	MaxPlayers thingValueXML `xml:"maxplayers"`

	PlayingTime thingValueXML `xml:"playingtime"`

	MinPlayTime thingValueXML `xml:"minplaytime"`
	MaxPlayTime thingValueXML `xml:"maxplaytime"`

	MinAge thingValueXML `xml:"minage"`
}

type thingNameXML struct {
	Type      string `xml:"type,attr"`
	SortIndex string `xml:"sortindex,attr"`
	Value     string `xml:"value,attr"`
}

type thingValueXML struct {
	Value string `xml:"value,attr"`
}
