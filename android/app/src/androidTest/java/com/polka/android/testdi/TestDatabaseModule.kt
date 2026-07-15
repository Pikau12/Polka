package com.polka.android.testdi

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import com.polka.android.data.ModelRepository
import com.polka.android.data.di.DataModule
import com.polka.android.data.di.FakeModelRepository

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
interface FakeDataModule {

    @Binds
    abstract fun bindRepository(
        fakeRepository: FakeModelRepository
    ): ModelRepository
}
