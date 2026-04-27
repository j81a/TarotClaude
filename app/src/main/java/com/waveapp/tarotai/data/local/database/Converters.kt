package com.waveapp.tarotai.data.local.database

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Converters para Room.
 *
 * Room solo puede almacenar tipos primitivos directamente (String, Int, etc.).
 * Para tipos complejos como List<String>, necesitamos convertirlos a String (JSON) y viceversa.
 *
 * @TypeConverter: Marca los métodos que Room debe usar para convertir tipos.
 */
class Converters {

    /**
     * Convierte una List<String> a String JSON para almacenarla en la BD.
     * Ejemplo: ["inicio", "locura", "libertad"] → "[\"inicio\",\"locura\",\"libertad\"]"
     */
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Json.encodeToString(value)
    }

    /**
     * Convierte un String JSON de la BD a List<String>.
     * Ejemplo: "[\"inicio\",\"locura\",\"libertad\"]" → ["inicio", "locura", "libertad"]
     */
    @TypeConverter
    fun toStringList(value: String): List<String> {
        return Json.decodeFromString(value)
    }
}
