package com.waveapp.tarotai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.waveapp.tarotai.data.local.entities.TarotCardEntity

/**
 * DAO (Data Access Object) para la tabla tarot_cards.
 *
 * @Dao: Marca esta interface como un DAO de Room.
 * Room genera automáticamente la implementación de estos métodos.
 *
 * Contiene todas las queries SQL necesarias para acceder a las cartas.
 */
@Dao
interface TarotCardDao {

    /**
     * Obtiene todas las cartas ordenadas por ID.
     * suspend: Función asíncrona que se ejecuta en una coroutine.
     */
    @Query("SELECT * FROM tarot_cards ORDER BY id ASC")
    suspend fun getAllCards(): List<TarotCardEntity>

    /**
     * Obtiene una carta por su ID.
     * @param cardId: ID de la carta (0-77)
     * @return La carta o null si no existe
     */
    @Query("SELECT * FROM tarot_cards WHERE id = :cardId")
    suspend fun getCardById(cardId: Int): TarotCardEntity?

    /**
     * Filtra cartas por tipo de arcano.
     * @param arcanaType: "MAJOR" o "MINOR"
     * @return Lista de cartas del tipo especificado
     */
    @Query("SELECT * FROM tarot_cards WHERE arcanaType = :arcanaType ORDER BY id ASC")
    suspend fun getCardsByArcanaType(arcanaType: String): List<TarotCardEntity>

    /**
     * Filtra cartas por palo (solo para Arcanos Menores).
     * @param suit: "WANDS", "CUPS", "SWORDS", "PENTACLES"
     * @return Lista de cartas del palo especificado
     */
    @Query("SELECT * FROM tarot_cards WHERE suit = :suit ORDER BY id ASC")
    suspend fun getCardsBySuit(suit: String): List<TarotCardEntity>

    /**
     * Inserta múltiples cartas en la base de datos.
     * OnConflictStrategy.REPLACE: Si ya existe una carta con el mismo ID, la reemplaza.
     * Se usa para poblar la DB inicialmente con las 78 cartas.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<TarotCardEntity>)

    /**
     * Inserta una sola carta.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: TarotCardEntity)

    /**
     * Cuenta el total de cartas en la base de datos.
     * Útil para verificar si la DB ya está poblada.
     */
    @Query("SELECT COUNT(*) FROM tarot_cards")
    suspend fun getCardsCount(): Int
}
