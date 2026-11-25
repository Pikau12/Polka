package com.polka.android.data.model

/**
 * A player of a session: either a Polka user ([userId] is set) or a guest
 * without an account (only [name] is set).
 */
data class SessionParticipant(
    val id: Long = 0,
    val sessionId: Long = 0,
    val userId: Long?,
    val name: String?,
    val isWinner: Boolean = false,
    val score: Double? = null,
    val status: Status = Status.ACCEPTED,
) {
    init {
        require(userId != null || !name.isNullOrBlank()) {
            "A participant must be either a user or have a name"
        }
    }

    /**
     * ACCEPTED: takes part (friends and own sessions need no confirmation).
     * PENDING: another Polka user was added and has not answered yet.
     * DECLINED: the user refused, the player is shown as a plain name.
     */
    enum class Status { ACCEPTED, PENDING, DECLINED }
}