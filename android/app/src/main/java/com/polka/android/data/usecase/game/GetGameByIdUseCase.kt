package com.polka.android.data.usecase.game

import com.polka.android.data.AuthRepository
import com.polka.android.data.CollectionRepository
import com.polka.android.data.GameRepository
import com.polka.android.data.image.ImageRepository
import com.polka.android.presentation.model.Game
import com.polka.android.presentation.model.toUIStatusSet
import com.polka.android.presentation.theme.PolkaWeightHard
import com.polka.android.presentation.theme.PolkaWeightLow
import com.polka.android.utils.colorOverlay
import com.polka.android.utils.getRatingColor
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
class GetGameByIdUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val gameRepository: GameRepository,
    private val collectionRepository: CollectionRepository,
    private val mapper: GameAndCollectionItemMapper
) {
    operator fun invoke(gameId: Long): Flow<Game?> {
        val userId = authRepository.getCurrentUser().id
        val id = com.polka.android.data.model.CollectionItem.Id(userId, gameId)

        return gameRepository.getGameFlow(gameId)
            .flatMapLatest { game ->
                if (game == null) {
                    flowOf(null)
                } else {
                    collectionRepository.getUserCollectionItem(id)
                        .map { collectionItem ->
                            mapper.map(game, collectionItem)
                        }
                }
            }
    }
}

class GameAndCollectionItemMapper @Inject constructor(
    private val imageRepository: ImageRepository
) {
    fun map(
        game: com.polka.android.data.model.Game?,
        collectionItem: com.polka.android.data.model.CollectionItem?,
    ): Game? {
        if (game == null) return null

        return Game( // TODO : update
            isUserCreated = game.bggId == null,
            isInUserCollection = collectionItem != null,

            status = collectionItem?.status?.toUIStatusSet(),
            userRating = collectionItem?.rating,
            userRatingColor = collectionItem?.rating?.let { rating ->
                getRatingColor(rating.toDouble())
            },

            averageSessionTime = game.averageSessionTime,

            id = game.id,
            name = game.name,
            shortDescription = game.shortDescription,
            description = game.description,
            releaseYear = game.releaseYear,

            gameImage = game.gameImage?.let{ imageRepository.toRequest(it) },
            userImages = collectionItem?.images?.map { imageRepository.toRequest(it) }
                ?: emptyList(),

            playerCount = game.playerCount?.joinToString("-"),
            bestPlayerCount = game.bestPlayerCount?.joinToString("-"),
            timeRange = game.timeRange?.let { "${it.start}-${it.endInclusive}" },
            ageRestriction = game.ageRestriction?.toString(),
            weight = game.weight?.toString(),
            weightColor = game.weight?.let { w ->
                colorOverlay(
                    colorBack = PolkaWeightLow,
                    colorFront = PolkaWeightHard,
                    alpha = w.toFloat() / 5
                )
            },

            designers = game.designers,
            artists = game.artists,
            publishers = game.publishers,
            type = game.type,
            categories = game.categories,
            mechanics = game.mechanics,
            tags = game.tags,

            bggAverageRating = game.bggAverageRating?.toString(),
            bggAverageRatingColor = game.bggAverageRating?.let { rating ->
                getRatingColor(rating)
            },
            polkaAverageRating = game.polkaAverageRating?.toString(),
            polkaAverageRatingColor = game.polkaAverageRating?.let { rating ->
                getRatingColor(rating)
            },
            bggNumberOfRatings = game.bggNumberOfRatings?.toString(),
            polkaNumberOfRatings = game.polkaNumberOfRatings?.toString()
        )
    }
}