package com.polka.android

import android.content.Context
import android.content.pm.ApplicationInfo
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import com.polka.android.data.api.AuthApi
import com.polka.android.data.network.AuthInterceptor
import com.polka.android.data.network.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Named
import jakarta.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

private const val BASE_URL = "http://10.0.2.2:8080/api/"

@Module
@InstallIn(SingletonComponent::class)
class PolkaModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    @Named("plain")
    fun providePlainOkHttpClient(@ApplicationContext appContext: Context): OkHttpClient {
        val builder = OkHttpClient.Builder()

        val isDebuggable = appContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        if (isDebuggable) {
            builder.addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC // BODY would print passwords
                    redactHeader("Authorization")
                }
            )
        }
        return builder.build()
    }

    @Provides
    @Singleton
    @Named("authorized")
    fun provideAuthorizedOkHttpClient(
        @Named("plain") plainClient: OkHttpClient,
        authInterceptor: AuthInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient = plainClient.newBuilder()
        .addInterceptor(authInterceptor)
        .authenticator(tokenAuthenticator)
        .build()

    @Provides
    @Singleton
    @Named("plain")
    fun providePlainRetrofit(
        @Named("plain") okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit = buildRetrofit(okHttpClient, json)

    @Provides
    @Singleton
    @Named("authorized")
    fun provideAuthorizedRetrofit(
        @Named("authorized") okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit = buildRetrofit(okHttpClient, json)

    @Provides
    @Singleton
    fun provideAuthApi(@Named("plain") retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Provides
    fun provideImageLoader(
        @ApplicationContext appContext: Context,
        @Named("plain") okHttpClient: OkHttpClient
    ): ImageLoader = ImageLoader.Builder(appContext)
        .components {
            add(OkHttpNetworkFetcherFactory(callFactory = { okHttpClient }))
        }
        .build()

    private fun buildRetrofit(client: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
}