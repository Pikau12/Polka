package com.polka.android.data

import com.polka.android.data.model.User
import jakarta.inject.Inject

interface SyncRepository {
    suspend fun getUserOffline(): User?
}

class DefaultSyncRepository @Inject constructor() : SyncRepository {
    override suspend fun getUserOffline(): User? {
        TODO("Not implemented yet")
    }
}
