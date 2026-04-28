package com.waveapp.tarotai.data.local.mapper

import com.waveapp.tarotai.data.local.dto.TarotCardDto
import com.waveapp.tarotai.data.local.entities.TarotCardEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Mapper para convertir TarotCardDto (JSON) a TarotCardEntity (Room).
 *
 * Se usa solo al inicializar la base de datos por primera vez,
 * leyendo el archivo JSON de assets y poblando Room.
 */

/**
 * Convierte un TarotCardDto del JSON a TarotCardEntity para Room.
 */
fun TarotCardDto.toEntity(): TarotCardEntity {
    return TarotCardEntity(
        id = id,
        name = name,
        arcanaType = arcanaType,
        suit = suit,
        imagePath = imagePath,
        generalMeaning = generalMeaning,
        uprightMeaning = uprightMeaning,
        reversedMeaning = reversedMeaning,
        symbolism = symbolism,
        keywords = Json.encodeToString(keywords)  // Convierte List<String> a JSON string
    )
}

/**
 * Convierte una lista de DTOs a lista de Entities.
 */
fun List<TarotCardDto>.toEntities(): List<TarotCardEntity> {
    return map { it.toEntity() }
}
