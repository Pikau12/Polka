package com.polka.android.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Game(val name: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
