package dto

type ImageUploadResponse struct {
	ID int64 `json:"id"`
}

type GetImageResponse struct {
	ID int64 `json:"id"`
}

type GetImagesResponse struct {
	IDs []GetImageResponse `json:"images"`
}
