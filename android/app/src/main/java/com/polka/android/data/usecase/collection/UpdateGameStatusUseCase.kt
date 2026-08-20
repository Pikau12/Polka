package com.polka.android.data.usecase.collection

import com.polka.android.data.CollectionRepository
import com.polka.android.presentation.model.CollectionItem
import com.polka.android.presentation.model.toDataStatusSet
import jakarta.inject.Inject
import java.util.EnumSet

class UpdateGameStatusUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
){
suspend operator fun invoke(id: CollectionItem.Id, status: EnumSet<CollectionItem.Status>){
        collectionRepository.updateItemStatus(
            com.polka.android.data.model.CollectionItem.Id(id.ownerId, id.gameId),
            status.toDataStatusSet()
        )
    }
}