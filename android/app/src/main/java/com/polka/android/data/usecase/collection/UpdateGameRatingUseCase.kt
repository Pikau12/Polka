package com.polka.android.data.usecase.collection

import com.polka.android.data.CollectionRepository
import com.polka.android.presentation.model.CollectionItem
import jakarta.inject.Inject

class UpdateGameRatingUseCase @Inject constructor (
    private val collectionRepository: CollectionRepository
){
    suspend operator fun invoke(id: CollectionItem.Id, rating: Int?){
        collectionRepository.updateItemRating(
            com.polka.android.data.model.CollectionItem.Id(id.ownerId, id.gameId),
            rating
        )
    }
}
