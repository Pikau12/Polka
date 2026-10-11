package com.polka.android.data.usecase.collection

import com.polka.android.data.CollectionRepository
import com.polka.android.data.GameRepository
import com.polka.android.data.model.Game
import com.polka.android.data.model.CollectionSortQuery
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
                items.map { entity ->
                    val game = gameRepository.getGame(entity.id.gameId)
                    mapper.map(entity, game)
                }
            }
//            .map { items -> // How it must be: ... (ваш закомментированный блок без изменений)
//            }
    }
}

class CollectionItemMapper @Inject constructor() {
    fun map(
        entity: com.polka.android.data.model.CollectionItem,
        game: Game?
    ): CollectionItem {
        return CollectionItem(
            name = game?.name ?: error("Game name is null"),
            id = CollectionItem.Id(entity.id.ownerId, entity.id.gameId),
            image = game.gameImage,
            status = entity.status.toUIStatusSet(),
            rating = entity.rating
        )
    }
}