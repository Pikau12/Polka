package com.polka.android.presentation.model

import coil3.request.ImageRequest
import java.time.LocalDate

data class SessionSummary (
    var sessionId: Long,
    var gameName: String,
    var gameImage: ImageRequest? = null,
    var date: LocalDate,
    var duration: Long? = null,
    var place: String? = null,
    var players: List<String>? = null,
    var winners: Map<String, Boolean>? = null,
)