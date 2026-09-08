package com.polka.android.data.usecase.sessions

import coil3.request.ImageRequest
import com.polka.android.data.CollectionRepository
import com.polka.android.data.GameRepository
import com.polka.android.data.image.ImageRepository
import com.polka.android.data.model.Game
import com.polka.android.presentation.model.CollectionItem
import com.polka.android.presentation.model.CollectionItemSummary
import com.polka.android.presentation.model.toUIStatusSet
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ObserveCollectionSummaryUseCase @Inject constructor(
    private val collectionRepository: CollectionRepository,
    private val gameRepository: GameRepository,
    private val mapper: CollectionItemSummaryMapper
) {
    operator fun invoke() : Flow<List<CollectionItemSummary>> {
        return collectionRepository.observeCollection()
            .map { items ->
                items.map { collectionItem ->
                    val game = gameRepository.getGame(collectionItem.id.gameId)
                        ?: error("Motherfucker, how did you do that? CollectionItem ALWAYS HAS A GAME to which it correspond, " +
                                "nevertheless, we couldn't find the corresponding game in ObserveCollectionSummaryUseCase. Jesus Crist...")
                    mapper.map(collectionItem, game)
                }
            }
    }
}

class CollectionItemSummaryMapper @Inject constructor(
    private val imageRepository: ImageRepository
) {
    fun map(
        collectionItem: com.polka.android.data.model.CollectionItem,
        game: Game
    ): CollectionItemSummary {
        return CollectionItemSummary(
            id = CollectionItemSummary.Id(collectionItem.id.ownerId, collectionItem.id.gameId),
            name = game.name,
            image = game.image?.let { imageRepository.toRequest(it) },
            releaseYear = 2026, // TODO: CHANGE!!!!!
            communityRating = game.bggAverageRating,
            userRating = collectionItem.rating,
            designer = game.designers?.get(0)
        )
    }
}