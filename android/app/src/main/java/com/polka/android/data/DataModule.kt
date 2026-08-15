package com.polka.android.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindsAnalyticsRepository(repository: DefaultAnalyticsRepository): AnalyticsRepository

    @Binds
    fun bindsAuthRepository(repository: DefaultAuthRepository): AuthRepository

    @Binds
    fun bindsCollectionRepository(repository: DefaultCollectionRepository): CollectionRepository

    @Binds
    fun bindsFriendRepository(repository: DefaultFriendRepository): FriendRepository

    @Binds
    fun bindsGameRepository(repository: DefaultGameRepository): GameRepository

    @Binds
    fun bindsSearchRepository(repository: DefaultSearchRepository): SearchRepository

    @Binds
    fun bindsSessionRepository(repository: DefaultSessionRepository): SessionRepository

    @Binds
    fun bindsSettingsRepository(repository: DefaultSettingsRepository): SettingsRepository

    @Binds
    fun bindsSyncRepository(repository: DefaultSyncRepository): SyncRepository
}