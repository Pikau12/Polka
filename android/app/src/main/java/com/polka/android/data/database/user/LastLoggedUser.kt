package com.polka.android.data.database.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "last_logged_in_user")
data class LastLoggedInUser(
    @PrimaryKey
    val userId: String
)