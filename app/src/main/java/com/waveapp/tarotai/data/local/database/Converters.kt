package com.waveapp.tarotai.data.local.database

import androidx.room.TypeConverter
import com.waveapp.tarotai.domain.model.DrawnCard
import com.waveapp.tarotai.domain.model.Interpretation
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Converters para Room.
 *
 * Room solo puede almacenar tipos primitivos directamente (String, Int, etc.).
 * Para tipos complejos como List<String>, DrawnCard, Interpretation, etc.,
 * necesitamos convertirlos a String (JSON) y viceversa.
 *
 * @TypeConverter: Marca los métodos que Room debe usar para convertir tipos.
 *
 * v1.1.0: Agregados converters para List<DrawnCard> e Interpretation
 * para soportar el historial de lecturas.
 */
class Converters {

    /**
     * JSON configurado para serialización.
     * ignoreUnknownKeys = true permite deserializar incluso si el JSON tiene campos extra.
     */
    private val json = Json {
        ignoreUnknownKeys = true
    }

    // ========== Converters existentes (v1.0.0) ==========

    /**
     * Convierte una List<String> a String JSON para almacenarla en la BD.
     * Ejemplo: ["inicio", "locura", "libertad"] → "[\"inicio\",\"locura\",\"libertad\"]"
     */
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return json.encodeToString(value)
    }

    /**
     * Convierte un String JSON de la BD a List<String>.
     * Ejemplo: "[\"inicio\",\"locura\",\"libertad\"]" → ["inicio", "locura", "libertad"]
     */
    @TypeConverter
    fun toStringList(value: String): List<String> {
        return json.decodeFromString(value)
    }

    // ========== Nuevos converters para historial (v1.1.0) ==========

    /**
     * Convierte una List<DrawnCard> a String JSON para almacenarla en la BD.
     * Usado en ReadingHistoryEntity.drawnCardsJson
     */
    @TypeConverter
    fun fromDrawnCardList(value: List<DrawnCard>): String {
        return json.encodeToString(value)
    }

    /**
     * Convierte un String JSON de la BD a List<DrawnCard>.
     * Usado para recuperar las cartas guardadas en el historial.
     */
    @TypeConverter
    fun toDrawnCardList(value: String): List<DrawnCard> {
        return json.decodeFromString(value)
    }

    /**
     * Convierte un objeto Interpretation a String JSON para almacenarlo en la BD.
     * Usado en ReadingHistoryEntity.interpretationJson
     */
    @TypeConverter
    fun fromInterpretation(value: Interpretation): String {
        return json.encodeToString(value)
    }

    /**
     * Convierte un String JSON de la BD a Interpretation.
     * Usado para recuperar la interpretación guardada en el historial.
     */
    @TypeConverter
    fun toInterpretation(value: String): Interpretation {
        return json.decodeFromString(value)
    }
}
