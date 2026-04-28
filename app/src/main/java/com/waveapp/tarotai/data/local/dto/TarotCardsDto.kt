package com.waveapp.tarotai.data.local.dto

import kotlinx.serialization.Serializable

/**
 * DTOs para deserializar el archivo JSON de cartas.
 *
 * Estos modelos mapean exactamente la estructura del JSON en assets/tarot_cards.json
 * No se usan directamente en la app, solo para leer el archivo inicial.
 */

/**
 * Estructura raíz del JSON.
 */
@Serializable
data class TarotCardsDto(
    val cards: List<TarotCardDto>
)

/**
 * DTO de una carta individual en el JSON.
 */
@Serializable
data class TarotCardDto(
    val id: Int,
    val name: String,
    val arcanaType: String,
    val suit: String?,
    val imagePath: String,
    val generalMeaning: String,
    val uprightMeaning: String,
    val reversedMeaning: String,
    val symbolism: String,
    val keywords: List<String>
)
