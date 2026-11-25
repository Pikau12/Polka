package com.polka.android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.polka.android.data.database.dao.CollectionDao
import com.polka.android.data.database.dao.GameDao
import com.polka.android.data.database.dao.SearchDao
import com.polka.android.data.database.dao.SessionDao
import com.polka.android.data.database.dao.UserDao
import com.polka.android.data.database.model.CollectionItemEntity
import com.polka.android.data.database.model.FriendshipEntity
import com.polka.android.data.database.model.GameEntity
import com.polka.android.data.database.model.SessionEntity
import com.polka.android.data.database.model.SessionParticipantEntity
import com.polka.android.data.database.model.UserEntity
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
@Database(
    entities = [
        UserEntity::class,
        GameEntity::class,
        SessionEntity::class,
        SessionParticipantEntity::class,
        CollectionItemEntity::class,
        FriendshipEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
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

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE games ADD COLUMN description TEXT")
        db.execSQL("ALTER TABLE games ADD COLUMN userImages TEXT NOT NULL DEFAULT '[]'")
        db.execSQL("ALTER TABLE games ADD COLUMN averageSessionTime TEXT")
        db.execSQL("ALTER TABLE games ADD COLUMN releaseYear TEXT")
    }
}