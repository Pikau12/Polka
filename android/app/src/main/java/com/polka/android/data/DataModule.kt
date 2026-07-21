package com.polka.android.data

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    /*
    TODO: remove when implemented at least one function
    If need to dependency inject an interface, write function similar to this
    @Singleton
    @Binds
    fun bindsModelRepository(
        modelRepository: ConcreteModelRepository
    ): ModelRepository
    */
}
