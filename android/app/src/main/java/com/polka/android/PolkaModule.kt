package com.polka.android

import android.content.Context
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
class PolkaModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient()
    }

    @Provides
    fun provideImageLoader(
        @ApplicationContext appContext: Context, okHttpClient: OkHttpClient
    ): ImageLoader {
        return ImageLoader.Builder(appContext).components {
            add(OkHttpNetworkFetcherFactory(callFactory = { okHttpClient }))
        }.build()
    }
}
