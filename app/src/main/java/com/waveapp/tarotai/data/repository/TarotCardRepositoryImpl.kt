package com.waveapp.tarotai.data.repository

import android.content.Context
import com.waveapp.tarotai.data.local.dao.TarotCardDao
import com.waveapp.tarotai.data.local.dto.TarotCardsDto
import com.waveapp.tarotai.data.local.mapper.toDomainModel
import com.waveapp.tarotai.data.local.mapper.toDomainModels
import com.waveapp.tarotai.data.local.mapper.toEntities
import com.waveapp.tarotai.domain.model.ArcanaType
import com.waveapp.tarotai.domain.model.Suit
import com.waveapp.tarotai.domain.model.TarotCard
import com.waveapp.tarotai.domain.repository.TarotCardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

/**
 * Implementación del repositorio de cartas del tarot.
 *
 * @param context Contexto de Android para acceder a assets
 * @param cardDao DAO de Room para acceder a la base de datos
 *
 * Responsabilidades:
 * - Leer el JSON de assets y poblar la BD inicial
 * - Proveer acceso a las cartas desde Room
 * - Convertir entities a domain models
 * - Implementar búsqueda y filtrado
 */
class TarotCardRepositoryImpl @Inject constructor(
    private val context: Context,
    private val cardDao: TarotCardDao
) : TarotCardRepository {

    /**
     * JSON parser con configuración para ignorar campos desconocidos.
     */
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override fun getAllCards(): Flow<List<TarotCard>> {
        return flow {
            val entities = cardDao.getAllCards()
            emit(entities.toDomainModels())
        }
    }

    override fun getCardById(cardId: Int): Flow<TarotCard?> {
        return flow {
            val entity = cardDao.getCardById(cardId)
            emit(entity?.toDomainModel())
        }
    }

    override fun getCardsByArcanaType(arcanaType: ArcanaType): Flow<List<TarotCard>> {
        return flow {
            val entities = cardDao.getCardsByArcanaType(arcanaType.name)
            emit(entities.toDomainModels())
        }
    }

    override fun getCardsBySuit(suit: Suit): Flow<List<TarotCard>> {
        return flow {
            val entities = cardDao.getCardsBySuit(suit.name)
            emit(entities.toDomainModels())
        }
    }

    override fun searchCards(query: String): Flow<List<TarotCard>> {
        return getAllCards().map { cards ->
            if (query.isBlank()) {
                cards
            } else {
                cards.filter { card ->
                    // Buscar en nombre
                    card.name.contains(query, ignoreCase = true) ||
                    // Buscar en keywords
                    card.keywords.any { it.contains(query, ignoreCase = true) } ||
                    // Buscar en significados
                    card.generalMeaning.contains(query, ignoreCase = true) ||
                    card.uprightMeaning.contains(query, ignoreCase = true) ||
                    card.reversedMeaning.contains(query, ignoreCase = true)
                }
            }
        }
    }

    override suspend fun initializeDatabaseIfNeeded(): Boolean {
        // Verificar si la BD ya tiene datos
        val count = cardDao.getCardsCount()
        if (count > 0) {
            return false  // Ya está inicializada
        }

        // Leer el archivo JSON desde assets
        val jsonString = context.assets.open("tarot_cards.json")
            .bufferedReader()
            .use { it.readText() }

        // Parsear el JSON a DTOs
        val cardsDto = json.decodeFromString<TarotCardsDto>(jsonString)

        // Convertir DTOs a Entities
        val entities = cardsDto.cards.toEntities()

        // Insertar en la base de datos
        cardDao.insertAll(entities)

        return true  // Se inicializó correctamente
    }
}
