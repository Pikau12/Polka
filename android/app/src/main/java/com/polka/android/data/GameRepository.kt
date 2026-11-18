package com.polka.android.data

import com.polka.android.data.database.dao.GameDao
import com.polka.android.data.database.model.GameEntity
import com.polka.android.data.image.ImageRepository
import com.polka.android.data.mapper.toEntity
import com.polka.android.data.model.Game
import jakarta.inject.Inject

interface GameRepository {

    suspend fun getGame(gameId: Long): Game?
    suspend fun getGameByBggId(bggId: Long): Game?

    /** Returns an existing game with the same name (ignoring case and surrounding spaces) or null. */
    suspend fun findGameByName(name: String): Game?

    suspend fun createGame(name: String): Game
    suspend fun importFromBgg(bggId: Long): Game
    suspend fun mergeGameData(localGame: Game, bggGame: Game): Game
    suspend fun updateGame(game: Game): Game
}

class DefaultGameRepository @Inject constructor(
    private val gameDao: GameDao,
    private val imageRepository: ImageRepository,
) : GameRepository {
    override suspend fun getGame(gameId: Long): Game? {
        val game = gameDao.get(gameId) ?: return null
        return Game(game, imageRepository::toRequest)
    }

    override suspend fun getGameByBggId(bggId: Long): Game? {
        val game = gameDao.getByBggId(bggId) ?: return null
        return Game(game, imageRepository::toRequest)
    }

    override suspend fun findGameByName(name: String): Game? {
        val wanted = name.trim()
        val game = gameDao.getAll().firstOrNull { it.name.trim().equals(wanted, ignoreCase = true) }
            ?: return null
        return Game(game, imageRepository::toRequest)
    }

    override suspend fun createGame(name: String): Game {
        val trimmed = name.trim()
        require(trimmed.isNotEmpty()) { "Game name must not be empty" }

        val id = gameDao.insert(
            GameEntity(
                serverId = null,
                name = trimmed,
                shortDescription = null,
                image = null,
                playerCount = emptyList(),
                bestPlayerCount = emptyList(),
                minPlayTimeMinutes = null,
                maxPlayTimeMinutes = null,
                ageRestriction = null,
                weight = null,
                designers = emptyList(),
                artists = emptyList(),
                publishers = emptyList(),
                type = emptyList(),
                categories = emptyList(),
                mechanics = emptyList(),
                tags = emptyList(),
                bggId = null,
                bggAverageRating = null,
                polkaAverageRating = null,
                bggNumberOfRatings = null,
                polkaNumberOfRatings = null,
            )
        )
        return checkNotNull(getGame(id)) { "Game $id was not found right after insert" }
    }

    override suspend fun importFromBgg(bggId: Long): Game {
        TODO("Not implemented yet")
    }

    override suspend fun mergeGameData(localGame: Game, bggGame: Game): Game {
        TODO("Not implemented yet")
    }

    override suspend fun updateGame(game: Game): Game {
        val updated = gameDao.update(game.toEntity(updatedAt = System.currentTimeMillis()))
        check(updated > 0) { "Game ${game.id} does not exist" }
        return checkNotNull(getGame(game.id)) { "Game ${game.id} disappeared after update" }
    }
}