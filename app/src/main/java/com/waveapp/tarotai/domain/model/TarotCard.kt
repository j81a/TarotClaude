package com.waveapp.tarotai.domain.model

/**
 * Modelo de dominio para una carta del tarot.
 *
 * Este es el modelo que se usa en toda la capa de presentación.
 * Es independiente de la implementación de la base de datos (Room).
 *
 * @param id ID único de la carta (0-77)
 * @param name Nombre de la carta en español
 * @param arcanaType Tipo de arcano (Mayor o Menor)
 * @param suit Palo de la carta (solo para Arcanos Menores)
 * @param imagePath Nombre del archivo de imagen en drawable
 * @param generalMeaning Significado general de la carta
 * @param uprightMeaning Significado en posición derecha
 * @param reversedMeaning Significado en posición invertida
 * @param symbolism Explicación del simbolismo de la carta
 * @param keywords Lista de palabras clave asociadas
 */
data class TarotCard(
    val id: Int,
    val name: String,
    val arcanaType: ArcanaType,
    val suit: Suit?,
    val imagePath: String,
    val generalMeaning: String,
    val uprightMeaning: String,
    val reversedMeaning: String,
    val symbolism: String,
    val keywords: List<String>
)

/**
 * Tipo de arcano: Mayor o Menor.
 */
enum class ArcanaType {
    MAJOR,  // Arcanos Mayores (0-21)
    MINOR   // Arcanos Menores (22-77)
}

/**
 * Palos de los Arcanos Menores.
 */
enum class Suit {
    WANDS,      // Bastos
    CUPS,       // Copas
    SWORDS,     // Espadas
    PENTACLES   // Oros/Pentáculos
}
