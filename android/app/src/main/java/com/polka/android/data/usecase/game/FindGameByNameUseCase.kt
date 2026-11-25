package com.polka.android.data.usecase.game

import com.polka.android.data.GameRepository
import com.polka.android.data.model.Game
import jakarta.inject.Inject

/**
 * Looks for an already existing game with the same name.
 * The UI calls it before creating a game and, if a game is found,
 * warns the user and offers to open the existing one.
 */
class FindGameByNameUseCase @Inject constructor(
    private val gameRepository: GameRepository,
) {
    suspend operator fun invoke(name: String): Game? = gameRepository.findGameByName(name)
}