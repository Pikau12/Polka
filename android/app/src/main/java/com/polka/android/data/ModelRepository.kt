package com.polka.android.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.polka.android.data.local.database.Model
import com.polka.android.data.local.database.ModelDao
import javax.inject.Inject

interface ModelRepository {
    val models: Flow<List<String>>

    suspend fun add(name: String)
}

class DefaultModelRepository @Inject constructor(
    private val modelDao: ModelDao
) : ModelRepository {

    override val models: Flow<List<String>> =
        modelDao.getModels().map { items -> items.map { it.name } }

    override suspend fun add(name: String) {
        modelDao.insertModel(Model(name = name))
    }
}
