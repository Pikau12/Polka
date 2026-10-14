package com.polka.android.data.model

import coil3.request.ImageRequest
import com.polka.android.data.database.model.GameEntity
import com.polka.android.data.image.ImageSource


/*
    Represents a certain board game, either from the BGG database, or created by the user.
 */
data class Game(
    val id: Long,
    val serverId: Long?,
    var name: String,
    val description: String?,
    var shortDescription: String?,
    val gameImage: ImageSource?,
    val userImages: List<ImageSource.Saved>,
    var playerCount: List<Int>?,
    var bestPlayerCount: List<Int>?,
    var timeRange: ClosedRange<Int>?,
    var ageRestriction: Int?,
    var weight: Double?,
    var designers: List<String>,
    var artists: List<String>,
    var publishers: List<String>,
    var type: List<String>,
    var categories: List<String>,
    var mechanics: List<String>,
    var tags: List<String>,
    var bggId: Long?,
    var bggAverageRating: Double?,
    var polkaAverageRating: Double?,
    var bggNumberOfRatings: Int?,
    var polkaNumberOfRatings: Int?,
    val averageSessionTime: String?,
    val releaseYear: String?,
    ) {
    constructor(
        entity: GameEntity,
        toRequest: (ImageSource) -> ImageRequest,
    ) : this(
        entity.id,
        entity.serverId,
        entity.name,
        entity.description,
        entity.shortDescription,
        entity.image,
        entity.userImages,
        entity.playerCount,
        entity.bestPlayerCount,
        range(entity.minPlayTimeMinutes, entity.maxPlayTimeMinutes),
        entity.ageRestriction,
        entity.weight,
        entity.designers,
        entity.artists,
        entity.publishers,
        entity.type,
        entity.categories,
        entity.mechanics,
        entity.tags,
        entity.bggId,
        entity.bggAverageRating,
        entity.polkaAverageRating,
        entity.bggNumberOfRatings,
        entity.polkaNumberOfRatings,
        entity.averageSessionTime,
        entity.releaseYear,
    )

    val bggUrl: String?
        get() {
            return if (bggId != null) {
                "https://boardgamegeek.com/boardgame/$bggId"
            } else {
                null
            }
        }
}

private fun range(min: Int?, max: Int?): ClosedRange<Int>? {
    if (min == null || max == null) {
        return null
    }
    return IntRange(min, max)
}