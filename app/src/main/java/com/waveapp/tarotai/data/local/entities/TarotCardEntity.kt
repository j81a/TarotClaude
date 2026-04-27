package com.waveapp.tarotai.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de Room que representa una carta de tarot en la base de datos.
 *
 * @Entity: Marca esta clase como una tabla de la base de datos
 * tableName: Nombre de la tabla en SQLite
 *
 * Esta entidad almacena toda la información de una carta del Tarot de Marsella.
 */
@Entity(tableName = "tarot_cards")
data class TarotCardEntity(
    @PrimaryKey
    val id: Int,                    // ID único (0-77)

    val name: String,               // Nombre: "El Loco", "As de Copas"

    val arcanaType: String,         // Tipo: "MAJOR" (Arcano Mayor) o "MINOR" (Arcano Menor)

    val suit: String?,              // Palo: "WANDS", "CUPS", "SWORDS", "PENTACLES" (null para Arcanos Mayores)

    val imagePath: String,          // Ruta de la imagen: "card_major_00"

    val generalMeaning: String,     // Significado general de la carta

    val uprightMeaning: String,     // Significado en posición derecha (normal)

    val reversedMeaning: String,    // Significado en posición invertida (al revés)

    val symbolism: String,          // Explicación del simbolismo e iconografía

    val keywords: String            // Palabras clave en formato JSON: ["inicio", "locura", "libertad"]
)
