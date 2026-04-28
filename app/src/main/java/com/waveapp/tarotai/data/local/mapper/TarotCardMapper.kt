package com.waveapp.tarotai.data.local.mapper

import com.waveapp.tarotai.data.local.entities.TarotCardEntity
import com.waveapp.tarotai.domain.model.ArcanaType
import com.waveapp.tarotai.domain.model.Suit
import com.waveapp.tarotai.domain.model.TarotCard
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

/**
 * Mapper para convertir entre TarotCardEntity (capa de datos) y TarotCard (capa de dominio).
 *
 * Siguiendo Clean Architecture, las capas deben estar desacopladas.
 * Los mappers permiten convertir entre modelos de diferentes capas.
 */

/**
 * Convierte una TarotCardEntity (Room) a TarotCard (Domain).
 */
fun TarotCardEntity.toDomainModel(): TarotCard {
    return TarotCard(
        id = id,
        name = name,
        arcanaType = ArcanaType.valueOf(arcanaType),
        suit = suit?.let { Suit.valueOf(it) },
        imagePath = imagePath,
        generalMeaning = generalMeaning,
        uprightMeaning = uprightMeaning,
        reversedMeaning = reversedMeaning,
        symbolism = symbolism,
        keywords = Json.decodeFromString(ListSerializer(String.serializer()), keywords)  // Convierte JSON string a List<String>
    )
}

/**
 * Convierte un TarotCard (Domain) a TarotCardEntity (Room).
 */
fun TarotCard.toEntity(): TarotCardEntity {
    return TarotCardEntity(
        id = id,
        name = name,
        arcanaType = arcanaType.name,
        suit = suit?.name,
        imagePath = imagePath,
        generalMeaning = generalMeaning,
        uprightMeaning = uprightMeaning,
        reversedMeaning = reversedMeaning,
        symbolism = symbolism,
        keywords = Json.encodeToString(ListSerializer(String.serializer()), keywords)  // Convierte List<String> a JSON string
    )
}

/**
 * Convierte una lista de entities a lista de domain models.
 */
fun List<TarotCardEntity>.toDomainModels(): List<TarotCard> {
    return map { it.toDomainModel() }
}
