package com.polka.android.data

import jakarta.inject.Inject

interface GameRepository {
}

class DefaultGameRepository @Inject constructor() : GameRepository {
}
