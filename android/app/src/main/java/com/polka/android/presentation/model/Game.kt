package com.polka.android.presentation.model

import coil3.request.ImageRequest

data class Game (
    val isUserCreated: Boolean = false,
    val isInUserCollection: Boolean = false,

    val status: Set<CollectionItem.Status>?, // TODO: make status public and move it to separate file (maybe merge status from UI and data layers)
    val userRating: String,

    val id: Long,
    val name: String, //
    val shortDescription: String?,
    val description: String?, // TODO: add to game in data layer
    val releaseYear: String?, // TODO: add to game in data layer //
    val gameImage: ImageRequest?,
    val userImages: List<ImageRequest>?,
    val playerCount: String?, //
    val bestPlayerCount: String?, //
    val timeRange: String?, //
    val ageRestriction: String?,
    val weight: String?,
    val designers: List<String>,
    val artists: List<String>,
    val publishers: List<String>,
    val type: List<String>,
    val categories: List<String>,
    val mechanics: List<String>,
    val tags: List<String>,
    val bggAverageRating: String?,
    val polkaAverageRating: String?,
    val bggNumberOfRatings: String?,
    val polkaNumberOfRatings: String?,
)