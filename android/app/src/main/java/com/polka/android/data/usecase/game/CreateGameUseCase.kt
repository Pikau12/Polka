package com.polka.android.data.usecase.game

import com.polka.android.data.CollectionRepository
import com.polka.android.data.GameRepository
import com.polka.android.data.model.Game
import jakarta.inject.Inject

/**
 * Creates a new game and adds it to the collection of the current user.
 * It does not check for duplicates: use [FindGameByNameUseCase] before calling it.
 */
class CreateGameUseCase @Inject constructor(
    private val gameRepository: GameRepository,
    private val collectionRepository: CollectionRepository,
) {
    suspend operator fun invoke(name: String): Game {
        val game = gameRepository.createGame(name)
        collectionRepository.addGameToCollection(game.id)
        return game
    }
}