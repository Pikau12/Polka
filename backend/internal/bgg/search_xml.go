package bgg

type searchResponseXML struct {
	Items []searchItemXML `xml:"item"`
}

type searchItemXML struct {
	ID int64 `xml:"id,attr"`

	Name searchNameXML `xml:"name"`

	YearPublished *searchValueXML `xml:"yearpublished"`
}

type searchNameXML struct {
	Type  string `xml:"type,attr"`
	Value string `xml:"value,attr"`
}

type searchValueXML struct {
	Value string `xml:"value,attr"`
}
