package com.polka.android.presentation.model

import coil3.request.ImageRequest
import java.time.LocalDate

data class SessionSummary (
    val sessionId: Long,
    val gameName: String,
    val gameImage: ImageRequest? = null,
    val date: LocalDate,
    val duration: Long? = null,
    val place: String? = null,
    val players: List<String>? = null,
    val winners: Map<String, Boolean>? = null,
)