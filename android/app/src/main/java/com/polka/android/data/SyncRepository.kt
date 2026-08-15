package com.polka.android.data

import jakarta.inject.Inject

interface SyncRepository {
}

class DefaultSyncRepository @Inject constructor() : SyncRepository {
}
