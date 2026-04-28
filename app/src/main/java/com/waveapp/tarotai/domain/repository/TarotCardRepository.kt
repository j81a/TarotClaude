package com.waveapp.tarotai.domain.repository

import com.waveapp.tarotai.domain.model.ArcanaType
import com.waveapp.tarotai.domain.model.Suit
import com.waveapp.tarotai.domain.model.TarotCard
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz del repositorio de cartas del tarot.
 *
 * Define las operaciones que se pueden realizar con las cartas.
 * La implementación concreta está en la capa de datos.
 *
 * Esta interfaz pertenece a la capa de dominio, por lo que:
 * - No conoce detalles de Room, Retrofit, etc.
 * - Trabaja con modelos de dominio (TarotCard), no con entities
 * - Define el contrato que la capa de datos debe cumplir
 */
interface TarotCardRepository {

    /**
     * Obtiene todas las cartas.
     * @return Flow que emite la lista completa de 78 cartas
     */
    fun getAllCards(): Flow<List<TarotCard>>

    /**
     * Obtiene una carta por su ID.
     * @param cardId ID de la carta (0-77)
     * @return Flow que emite la carta o null si no existe
     */
    fun getCardById(cardId: Int): Flow<TarotCard?>

    /**
     * Obtiene cartas por tipo de arcano.
     * @param arcanaType MAJOR o MINOR
     * @return Flow que emite la lista de cartas del tipo especificado
     */
    fun getCardsByArcanaType(arcanaType: ArcanaType): Flow<List<TarotCard>>

    /**
     * Obtiene cartas por palo (solo Arcanos Menores).
     * @param suit WANDS, CUPS, SWORDS o PENTACLES
     * @return Flow que emite la lista de cartas del palo especificado
     */
    fun getCardsBySuit(suit: Suit): Flow<List<TarotCard>>

    /**
     * Busca cartas por nombre o keywords.
     * @param query Texto de búsqueda
     * @return Flow que emite la lista de cartas que coinciden con la búsqueda
     */
    fun searchCards(query: String): Flow<List<TarotCard>>

    /**
     * Inicializa la base de datos con las 78 cartas desde el JSON.
     * Solo se ejecuta si la base de datos está vacía.
     * @return true si se inicializó, false si ya tenía datos
     */
    suspend fun initializeDatabaseIfNeeded(): Boolean
}
