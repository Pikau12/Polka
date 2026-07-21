package com.polka.android.data.model

import android.os.Environment
import coil3.Image

/*
    Represents a certain board game, either from the BGG database, or created by the user.
 */
data class Game(
    val id: Long,
    val name: String,
    val shortDescription: String?,
    val image: Image?,
    val bggAverageRating: Double?,
    val bggNumberOfRatings: Int?,
    val playerCount: ClosedRange<Int>?,
    val bestPlayerCount: ClosedRange<Int>?,
    val timeRange: ClosedRange<Int>?,
    val ageRestriction: Int?,
    val weight: Double?,
    val designers: List<String>,
    val artists: List<String>,
    val publishers: List<String>,
    val type: List<String>,
    val categories: List<String>,
    val mechanics: List<String>,
    val tags: List<String>,
    val bggId: Long?,
) {
    val bggUrl: String? get() {
        return if (bggId != null) {
            "https://boardgamegeek.com/boardgame/$bggId"
        } else {
            null
        }
    }
}
