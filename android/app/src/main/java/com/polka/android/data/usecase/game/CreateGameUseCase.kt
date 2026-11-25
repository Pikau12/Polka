package com.polka.android.data.usecase.game

import com.polka.android.data.AuthRepository
import com.polka.android.data.CollectionRepository
import com.polka.android.data.GameRepository
import com.polka.android.data.model.Game
import jakarta.inject.Inject

/**
 * Creates a new game (the current user becomes its creator) and adds it to their collection.
 * It does not check for duplicates: use [FindGameByNameUseCase] before calling it.
 */
class CreateGameUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val gameRepository: GameRepository,
    private val collectionRepository: CollectionRepository,
) {
    suspend operator fun invoke(name: String): Game {
        val game = gameRepository.createGame(name, authRepository.getCurrentUser().id)
        collectionRepository.addGameToCollection(game.id)
        return game
    }
}