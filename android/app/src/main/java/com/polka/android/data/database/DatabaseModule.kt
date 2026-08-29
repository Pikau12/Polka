package com.polka.android.data.database

import android.content.Context
import androidx.room.Room
import com.polka.android.data.database.dao.CollectionDao
import com.polka.android.data.database.dao.GameDao
import com.polka.android.data.database.dao.SearchDao
import com.polka.android.data.database.dao.SessionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "Model"
        ).build()
    }

    @Provides
    fun provideGameDao(appDatabase: AppDatabase): GameDao {
        return appDatabase.gameDao()
    }

    @Provides
    fun provideSessionDao(appDatabase: AppDatabase): SessionDao {
        return appDatabase.sessionDao()
    }

    @Provides
    fun provideSearchDao(appDatabase: AppDatabase): SearchDao {
        return appDatabase.searchDao()
    }

    @Provides
    fun provideCollectionDao(appDatabase: AppDatabase): CollectionDao {
        return appDatabase.collectionDao()
    }
}
