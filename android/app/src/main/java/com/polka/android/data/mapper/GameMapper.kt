package com.polka.android.data.mapper

import com.polka.android.data.database.model.GameEntity
import com.polka.android.data.image.ImageSource
import com.polka.android.data.model.Game

/**
 * Converts [Game] to [GameEntity] for saving to the database.
 *
 * @param updatedAt value for the `updatedAt` column. By default it is left unchanged,
 * the repository passes the current time when the game is really modified.
 */
fun Game.toEntity(updatedAt: Long = this.updatedAt): GameEntity {
    // Only saved images may reach the database. Unsaved ones must be saved
    // via ImageRepository before the game is mapped.
    require(gameImage == null || gameImage is ImageSource.Saved) {
        "Game image must be saved before it can be stored in the database"
    }

    return GameEntity(
        id = id,
        serverId = serverId,
        name = name,
        description = description,
        shortDescription = shortDescription,
        image = gameImage as ImageSource.Saved?,
        userImages = userImages,
        playerCount = playerCount.orEmpty(),
        bestPlayerCount = bestPlayerCount.orEmpty(),
        minPlayTimeMinutes = timeRange?.start,
        maxPlayTimeMinutes = timeRange?.endInclusive,
        ageRestriction = ageRestriction,
        weight = weight,
        designers = designers,
        artists = artists,
        publishers = publishers,
        type = type,
        categories = categories,
        mechanics = mechanics,
        tags = tags,
        bggId = bggId,
        bggAverageRating = bggAverageRating,
        polkaAverageRating = polkaAverageRating,
        bggNumberOfRatings = bggNumberOfRatings,
        polkaNumberOfRatings = polkaNumberOfRatings,
        averageSessionTime = averageSessionTime,
        releaseYear = releaseYear,
        creatorId = creatorId,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}