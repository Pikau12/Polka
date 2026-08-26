package com.polka.android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.polka.android.data.database.dao.CollectionDao
import com.polka.android.data.database.dao.GameDao
import com.polka.android.data.database.dao.SearchDao
import com.polka.android.data.database.dao.SessionDao
import com.polka.android.data.database.model.CollectionItemEntity
import com.polka.android.data.database.model.FriendshipEntity
import com.polka.android.data.database.model.GameEntity
import com.polka.android.data.database.model.SessionEntity
import com.polka.android.data.database.model.SessionParticipantEntity
import com.polka.android.data.database.model.UserEntity

@Database(
    entities = [
        UserEntity::class,
        GameEntity::class,
        SessionEntity::class,
        SessionParticipantEntity::class,
        CollectionItemEntity::class,
        FriendshipEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun sessionDao(): SessionDao
    abstract fun searchDao(): SearchDao
    abstract fun collectionDao(): CollectionDao

    /**
     * Used for updating `updatedAt` columns. Time in milliseconds.
     */
    companion object {
        const val NOW_MS = "(strftime('%s', 'now') * 1000)"
    }
}