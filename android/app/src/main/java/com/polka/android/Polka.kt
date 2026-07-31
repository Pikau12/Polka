package com.polka.android

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import dagger.hilt.android.HiltAndroidApp
import jakarta.inject.Inject
import jakarta.inject.Provider

@HiltAndroidApp
class Polka : Application(), SingletonImageLoader.Factory {
    @Inject
    lateinit var imageLoader: Provider<ImageLoader>

    override fun newImageLoader(context: Context): ImageLoader = imageLoader.get()
}
