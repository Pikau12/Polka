package com.polka.android.data

import android.content.Context
import androidx.room.Room
import com.polka.android.data.database.AppDatabase
import com.polka.android.data.database.dao.SessionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "polka_database"
        ).build()
    }

    @Provides
    fun provideSessionDao(
        database: AppDatabase
    ): SessionDao {
        return database.sessionDao()
    }
}
