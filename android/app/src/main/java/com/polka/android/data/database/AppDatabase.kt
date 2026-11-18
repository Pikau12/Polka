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
    version = 3,
    exportSchema = true
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

/**
 * `sessions.creatorId` becomes nullable (it is set to NULL when the creator is deleted).
 * SQLite cannot change column nullability, so the table is recreated.
 */
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE sessions_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                serverId INTEGER,
                gameId INTEGER NOT NULL,
                creatorId INTEGER,
                note TEXT NOT NULL,
                images TEXT NOT NULL,
                startedAt INTEGER NOT NULL,
                finishedAt INTEGER,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL,
                FOREIGN KEY(gameId) REFERENCES games(id) ON UPDATE NO ACTION ON DELETE RESTRICT,
                FOREIGN KEY(creatorId) REFERENCES users(id) ON UPDATE NO ACTION ON DELETE SET NULL
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            INSERT INTO sessions_new (id, serverId, gameId, creatorId, note, images, startedAt, finishedAt, createdAt, updatedAt)
            SELECT id, serverId, gameId, creatorId, note, images, startedAt, finishedAt, createdAt, updatedAt FROM sessions
            """.trimIndent()
        )
        db.execSQL("DROP TABLE sessions")
        db.execSQL("ALTER TABLE sessions_new RENAME TO sessions")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_sessions_gameId ON sessions(gameId)")
        db.execSQL("CREATE INDEX IF NOT EXISTS index_sessions_creatorId ON sessions(creatorId)")
    }
}