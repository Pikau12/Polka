package com.polka.android.data.usecase.collection

import com.polka.android.data.CollectionRepository
import com.polka.android.data.GameRepository
import com.polka.android.data.image.ImageRepository
import com.polka.android.data.model.Game
import com.polka.android.data.model.SortQuery
import com.polka.android.data.model.isSortQueryEmpty
import com.polka.android.presentation.model.CollectionItem
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveCollectionUseCase(
    private val collectionRepository: CollectionRepository,
    private val gameRepository: GameRepository,
    private val mapper: CollectionItemMapper
) {
    operator fun invoke(sort: SortQuery?): Flow<List<CollectionItem>> {
        return collectionRepository.observeCollection()
            .map { items ->
                items.map { entity ->
                    val game = gameRepository.getGameById(entity.gameId)
                    mapper.map(entity, game)
                }
            }
            .map { items -> // How it must be: if sortQuery is null or if sortQuery doesn't have
                // a sort part we need to sort collection by displayOrder otherwise
                // we need to specify our sort by sort part of sortQuery and after all of this
                // apply a filter part of sortQuery
                when {
                    isSortQueryEmpty(sort) -> items.sortedBy { it.name }
                    else -> items.sortedBy(sort.comparator)
                }
            }
    }
}

class CollectionItemMapper @Inject constructor(
    private val imageRepository: ImageRepository
) {
    fun map(
        entity: com.polka.android.data.model.CollectionItem,
        game: Game
    ): CollectionItem {
        return CollectionItem(
            name = game.name,
            ownerId = entity.id.ownerId,
            gameId = entity.id.gameId,
            image = game.image?.let { imageRepository.toRequest(it) },
            status = entity.status.toString(),
            rating = entity.rating
        )
    }
}