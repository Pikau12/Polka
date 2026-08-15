package model

type CollectionItemImage struct {
	OwnerID int64 `gorm:"primaryKey;autoIncrement:false"`
	GameID  int64 `gorm:"primaryKey;autoIncrement:false"`
	ImageID int64 `gorm:"primaryKey;autoIncrement:false"`

	CollectionItem CollectionItem `gorm:"foreignKey:OwnerID,GameID;references:OwnerID,GameID"`
	Image          Image
}
