package com.polka.android.data

import com.polka.android.data.model.Game
import jakarta.inject.Inject

interface GameRepository {

    suspend fun getGame(gameId: Long): Game?
    suspend fun getGameByBggId(bggId: Long): Game?
    suspend fun createGame(name: String): Game
    suspend fun importFromBgg(bggId: Long): Game
    suspend fun mergeGameData(localGame: Game,bggGame: Game): Game
    suspend fun updateGame(game: Game): Game
}

class DefaultGameRepository @Inject constructor() : GameRepository {
    override suspend fun getGame(gameId: Long): Game? {
        TODO("Not implemented yet")
    }

    override suspend fun getGameByBggId(bggId: Long): Game? {
        TODO("Not implemented yet")
    }

    override suspend fun createGame(name: String): Game {
        TODO("Not implemented yet")
    }

    override suspend fun importFromBgg(bggId: Long): Game {
        TODO("Not implemented yet")
    }

    override suspend fun mergeGameData(localGame: Game, bggGame: Game): Game {
        TODO("Not implemented yet")
    }

    override suspend fun updateGame(game: Game): Game {
        TODO("Not implemented yet")
    }
}
