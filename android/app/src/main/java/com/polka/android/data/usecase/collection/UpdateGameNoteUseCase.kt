package com.polka.android.data.usecase.collection

import com.polka.android.data.CollectionRepository
import com.polka.android.presentation.model.CollectionItem
import jakarta.inject.Inject

class UpdateGameNoteUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository
) {
    /** A blank [note] clears the note. */
    suspend operator fun invoke(id: CollectionItem.Id, note: String) {
        collectionRepository.updateItemNote(
            com.polka.android.data.model.CollectionItem.Id(id.ownerId, id.gameId),
            note
        )
    }
}