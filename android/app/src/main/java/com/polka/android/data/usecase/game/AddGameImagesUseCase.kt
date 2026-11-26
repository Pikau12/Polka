package com.polka.android.data.usecase.game

import android.net.Uri
import com.polka.android.data.GameRepository
import com.polka.android.data.image.ImageRepository
import com.polka.android.data.image.ImageSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddGameImagesUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(gameId: Long, uris: List<Uri>) {
        if (uris.isEmpty()) return

        withContext(Dispatchers.IO) {
            val unsavedSources = uris.map { ImageSource.Unsaved(it) }

            val savedImages = imageRepository.saveAll(unsavedSources)

            val game = gameRepository.getGame(gameId)
                ?: throw IllegalStateException("Game with id $gameId not found")

            val updatedGame = game.copy(
                userImages = game.userImages + savedImages
            )

            gameRepository.updateGame(updatedGame)
        }
    }
}