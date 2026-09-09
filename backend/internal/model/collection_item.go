package model

import (
	"time"
)

type CollectionItemStatus string

const (
	CollectionItemStatusOwn                CollectionItemStatus = "OWN"
	CollectionItemStatusPreviouslyOwned    CollectionItemStatus = "PREVIOUSLY_OWNED"
	CollectionItemStatusForTrade           CollectionItemStatus = "FOR_TRADE"
	CollectionItemStatusWantInTrade        CollectionItemStatus = "WANT_IN_TRADE"
	CollectionItemStatusWantToPlay         CollectionItemStatus = "WANT_TO_PLAY"
	CollectionItemStatusWantToBuy          CollectionItemStatus = "WANT_TO_BUY"
	CollectionItemStatusPreordered         CollectionItemStatus = "PREORDERED"
	CollectionItemStatusWishlistMustHave   CollectionItemStatus = "WISHLIST_MUST_HAVE"
	CollectionItemStatusWishlistLoveToHave CollectionItemStatus = "WISHLIST_LOVE_TO_HAVE"
	CollectionItemStatusWishlistLikeToHave CollectionItemStatus = "WISHLIST_LIKE_TO_HAVE"
	CollectionItemStatusWishlistThinking   CollectionItemStatus = "WISHLIST_THINKING"
	CollectionItemStatusWishlistDoNotBuy   CollectionItemStatus = "WISHLIST_DO_NOT_BUY"
)

type CollectionItemStatuses []CollectionItemStatus

type CollectionItem struct {
	OwnerID int64 `gorm:"primaryKey;autoIncrement:false"`
	GameID  int64 `gorm:"primaryKey;autoIncrement:false"`

	Note   *string
	Rating *int32
	Status CollectionItemStatuses `gorm:"serializer:json;type:jsonb"`

	CreatedAt time.Time
	UpdatedAt time.Time

	Owner User
	Game  Game
}
