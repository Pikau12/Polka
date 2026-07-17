package com.polka.android.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import com.polka.android.data.ModelRepository
import com.polka.android.data.DefaultModelRepository
import javax.inject.Inject
import javax.inject.Singleton

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
