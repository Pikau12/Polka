package com.polka.android.data.usecase.game

import com.polka.android.data.GameRepository
import com.polka.android.data.model.Game
import jakarta.inject.Inject

class UpdateGameUseCase @Inject constructor(
    private val gameRepository: GameRepository,
) {
    suspend operator fun invoke(game: Game): Game = gameRepository.updateGame(game)
}