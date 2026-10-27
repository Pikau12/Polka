package com.polka.android.data.usecase.collection

import com.polka.android.data.CollectionRepository
import com.polka.android.data.GameRepository
import com.polka.android.data.image.ImageRepository
import com.polka.android.data.model.Game
import com.polka.android.data.model.CollectionSortQuery
import com.polka.android.data.model.isSortQueryEmpty // не забудь импорт
import com.polka.android.presentation.model.CollectionItem
import com.polka.android.presentation.model.toUIStatusSet
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveCollectionUseCase @Inject constructor (
    private val collectionRepository: CollectionRepository,
    private val gameRepository: GameRepository,
    private val mapper: CollectionItemMapper
) {
    operator fun invoke(sort: CollectionSortQuery?): Flow<List<CollectionItem>> {
        return collectionRepository.observeCollection()
            .map { items ->
                if (sort == null || isSortQueryEmpty(sort)) {
                    items.sortedBy { it.displayOrder }
                } else {
                    // Sort with usage of sort.comparator
                    // items.sortedWith(...)
                    items
                }
            }
            .map { sortedItems ->
                sortedItems.map { entity ->
                    val game = gameRepository.getGame(entity.id.gameId)
                    mapper.map(entity, game)
                }
            }
    }
}

class CollectionItemMapper @Inject constructor(
    private val imageRepository: ImageRepository
) {
    fun map(
        entity: com.polka.android.data.model.CollectionItem,
        game: Game?
    ): CollectionItem {
        return CollectionItem(
            // Если игры нет, ставим заглушку
            name = game?.name ?: "Unknown Game",
            id = CollectionItem.Id(entity.id.ownerId, entity.id.gameId),
            image = game?.gameImage?.let { imageRepository.toRequest(it) },
            status = entity.status.toUIStatusSet(),
            rating = entity.rating
        )
    }
}