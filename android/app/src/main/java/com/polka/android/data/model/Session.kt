package com.polka.android.data.model

data class Session(
    val id: Long = 0,
    val gameId: Long,
    val creatorId: Long,

    val note: String = "",
    /*
        A list of URL to images.
        TODO: when implementing utility for list conversion, replace this with an empty list from that utility.
     */
    val images: String = "[]",

    val startedAt: Long = System.currentTimeMillis(),
    val finishedAt: Long? = null,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)