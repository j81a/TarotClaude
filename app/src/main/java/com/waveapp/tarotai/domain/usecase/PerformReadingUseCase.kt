package com.waveapp.tarotai.domain.usecase

import android.util.Log
import com.waveapp.tarotai.domain.model.CardOrientation
import com.waveapp.tarotai.domain.model.DrawnCard
import com.waveapp.tarotai.domain.model.SpreadType
import com.waveapp.tarotai.domain.model.TarotCard
import com.waveapp.tarotai.domain.model.TarotReading
import com.waveapp.tarotai.domain.repository.TarotCardRepository
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

/**
 * Caso de uso: Realizar tirada de tarot.
 *
 * Selecciona cartas aleatorias sin repetir, asigna orientaciones y crea una TarotReading.
 */
class PerformReadingUseCase @Inject constructor(
    private val cardRepository: TarotCardRepository,
    private val getSpreadConfigurationUseCase: GetSpreadConfigurationUseCase
) {
    suspend operator fun invoke(
        spreadType: SpreadType,
        question: String?
    ): Result<TarotReading> {
        return try {
            // Obtener configuración de la tirada
            val config = getSpreadConfigurationUseCase(spreadType)
            Log.d("PerformReadingUseCase", "SpreadType: $spreadType, CardCount: ${config.cardCount}")

            // Obtener todas las cartas disponibles
            val allCards: List<TarotCard> = cardRepository.getAllCards().first()
            Log.d("PerformReadingUseCase", "Total cards available: ${allCards.size}")

            // Seleccionar cartas aleatorias sin repetir
            val shuffled = allCards.shuffled()
            val selectedCount = minOf(config.cardCount, shuffled.size)
            Log.d("PerformReadingUseCase", "Selected count: $selectedCount")

            val drawnCards = mutableListOf<DrawnCard>()
            for (i in 0 until selectedCount) {
                val card = shuffled[i]
                drawnCards.add(
                    DrawnCard(
                        card = card,
                        position = i,
                        positionName = config.positions[i],
                        orientation = if (Random.nextBoolean()) {
                            CardOrientation.UPRIGHT
                        } else {
                            CardOrientation.REVERSED
                        }
                    )
                )
            }

            // Crear la tirada
            val reading = TarotReading(
                id = UUID.randomUUID().toString(),
                spreadType = spreadType,
                question = question,
                drawnCards = drawnCards.toList()
            )

            Log.d("PerformReadingUseCase", "Reading created with ${reading.drawnCards.size} cards")
            reading.drawnCards.forEachIndexed { index, card ->
                Log.d("PerformReadingUseCase", "Card $index: ${card.card.name} at position ${card.positionName}")
            }

            Result.success(reading)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
