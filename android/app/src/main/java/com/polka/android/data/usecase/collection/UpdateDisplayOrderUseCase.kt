package com.polka.android.data.usecase.collection

import com.polka.android.data.CollectionRepository
import com.polka.android.presentation.model.CollectionItem
import com.polka.android.presentation.model.toDataId
import jakarta.inject.Inject

class UpdateDisplayOrderUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(
        itemToMove: CollectionItem.Id,
        aboveItem: CollectionItem.Id?,
        belowItem: CollectionItem.Id?
    ) {
        collectionRepository.moveItemInBetween(
            itemToMove.toDataId(),
            aboveItem?.toDataId(),
            belowItem?.toDataId()
        )
    }
}