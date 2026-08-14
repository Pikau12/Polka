package com.polka.android.data.usecase.collection

import com.polka.android.data.CollectionRepository

class UpdateGameRatingUseCase (
    private val collectionRepository: CollectionRepository
){
    suspend operator fun invoke(gameId: Long, rating: Int?){
        collectionRepository.updateItemRating(gameId, rating)
    }
}
