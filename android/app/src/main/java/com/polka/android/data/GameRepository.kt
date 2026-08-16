package com.polka.android.data

import jakarta.inject.Inject

interface GameRepository {

    suspend fun getGame(gameId: Long): Game?
    suspend fun getGameByBggId(bggId: Long): Game?
    suspend fun createGame(game: Game): Game
    suspend fun importFromBgg(bggId: Long): Game
    suspend fun mergeGameData(localGame: Game,bggGame: Game): Game
    suspend fun updateGame(game: Game): Game
}

class DefaultGameRepository @Inject constructor() : GameRepository {
}
