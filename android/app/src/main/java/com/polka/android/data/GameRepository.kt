package com.polka.android.data

import com.polka.android.data.database.dao.GameDao
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

class DefaultGameRepository @Inject constructor(private val gameDao: GameDao) : GameRepository {
    override suspend fun getGame(gameId: Long): Game? {
        val game = gameDao.get(gameId) ?: return null
        return Game(game)
    }

    override suspend fun getGameByBggId(bggId: Long): Game? {
        val game = gameDao.getByBggId(bggId) ?: return null
        return Game(game)
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
