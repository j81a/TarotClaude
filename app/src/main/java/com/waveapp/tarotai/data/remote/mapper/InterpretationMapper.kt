package com.waveapp.tarotai.data.remote.mapper

import com.waveapp.tarotai.domain.model.CardInterpretation
import com.waveapp.tarotai.domain.model.Interpretation
import com.waveapp.tarotai.domain.model.YesNoAnswer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * Mapper para convertir la respuesta JSON de Claude a modelos de dominio.
 *
 * Claude responde con un JSON que parseamos a DTOs intermedios,
 * y luego convertimos a nuestros modelos de dominio.
 */
object InterpretationMapper {

    /**
     * JSON parser configurado para ser flexible con el parseo.
     */
    private val json = Json {
        ignoreUnknownKeys = true  // Ignora campos desconocidos
        isLenient = true           // Permite JSON menos estricto
    }

    /**
     * Parsea el texto de respuesta de Claude y lo convierte a Interpretation.
     *
     * @param jsonText Texto JSON de la respuesta de Claude
     * @return Objeto Interpretation del dominio
     * @throws Exception Si el JSON es inválido o no tiene el formato esperado
     */
    fun parseInterpretation(jsonText: String): Interpretation {
        val dto = json.decodeFromString<InterpretationDto>(jsonText)
        return dto.toDomain()
    }

    /**
     * DTO que mapea el JSON de respuesta de Claude.
     *
     * @property individualInterpretations Lista de interpretaciones individuales
     * @property generalInterpretation Interpretación general de la tirada
     * @property yesNoAnswer Respuesta Sí/No (opcional, solo para tiradas YES_NO)
     * @property yesNoJustification Justificación de la respuesta (opcional)
     */
    @Serializable
    private data class InterpretationDto(
        @SerialName("individual_interpretations")
        val individualInterpretations: List<CardInterpretationDto>,

        @SerialName("general_interpretation")
        val generalInterpretation: String,

        @SerialName("yes_no_answer")
        val yesNoAnswer: String? = null,

        @SerialName("yes_no_justification")
        val yesNoJustification: String? = null
    ) {
        /**
         * Convierte el DTO a modelo de dominio.
         */
        fun toDomain(): Interpretation {
            return Interpretation(
                individualInterpretations = individualInterpretations.map { it.toDomain() },
                generalInterpretation = generalInterpretation,
                yesNoAnswer = yesNoAnswer?.let { parseYesNoAnswer(it) },
                yesNoJustification = yesNoJustification
            )
        }
    }

    /**
     * DTO para interpretación individual de una carta.
     *
     * @property cardName Nombre de la carta
     * @property position Posición de la carta en la tirada
     * @property interpretation Texto de interpretación
     */
    @Serializable
    private data class CardInterpretationDto(
        @SerialName("card_name")
        val cardName: String,

        @SerialName("position")
        val position: String,

        @SerialName("interpretation")
        val interpretation: String
    ) {
        /**
         * Convierte el DTO a modelo de dominio.
         */
        fun toDomain(): CardInterpretation {
            return CardInterpretation(
                cardName = cardName,
                position = position,
                interpretation = interpretation
            )
        }
    }

    /**
     * Parsea la respuesta de Sí/No desde el string de Claude.
     *
     * Claude puede responder con variaciones como:
     * - "Sí", "Si", "YES", "Yes"
     * - "No", "NO"
     * - "Indefinido", "Unclear", "Incierto"
     *
     * @param answer String de respuesta
     * @return YesNoAnswer enum correspondiente
     */
    private fun parseYesNoAnswer(answer: String): YesNoAnswer {
        return when (answer.trim().lowercase()) {
            "sí", "si", "yes" -> YesNoAnswer.YES
            "no" -> YesNoAnswer.NO
            else -> YesNoAnswer.UNCLEAR
        }
    }
}
