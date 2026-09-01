package com.polka.android.presentation.model

import coil3.request.ImageRequest
import java.time.LocalDate

data class SessionSummary (
    var sessionId: Long,
    var gameName: String,
    var gameImage: ImageRequest,
    var date: LocalDate,
    var duration: Int?,
    var place: String?,
    var players: List<String>?,
    var winners: Map<String, Boolean>?,
)