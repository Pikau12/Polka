package dto

import "github.com/polka/backend/internal/model"

type CreateCollectionItemRequest struct {
	GameID int64 `json:"game_id"`
}

type DeleteFromCollectionRequest struct {
	GameID int64 `json:"game_id"`
}
type GetCollectionResponse struct {
	Games []model.Game `json:"games"`
}
