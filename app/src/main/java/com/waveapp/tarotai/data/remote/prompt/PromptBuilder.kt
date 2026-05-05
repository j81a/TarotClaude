package com.waveapp.tarotai.data.remote.prompt

import com.waveapp.tarotai.domain.model.CardOrientation
import com.waveapp.tarotai.domain.model.SpreadType
import com.waveapp.tarotai.domain.model.TarotReading

/**
 * Constructor de prompts dinámicos para la API de Claude.
 *
 * Genera prompts personalizados según el tipo de tirada, las cartas seleccionadas
 * y la pregunta del usuario. Los prompts incluyen:
 * - Contexto del tipo de tirada
 * - Pregunta del usuario
 * - Lista de cartas con posiciones y orientaciones
 * - Instrucciones específicas para el tipo de tirada
 * - Template JSON para la respuesta
 */
object PromptBuilder {

    /**
     * Modelo de Claude a utilizar.
     * claude-haiku-4-5-20251001 es Haiku 4.5 (más rápido y económico, ~75% más barato que Sonnet).
     * Alternativa: claude-sonnet-4-6 (mejor calidad narrativa, más caro).
     */
    private const val CLAUDE_MODEL = "claude-haiku-4-5-20251001"
    //private const val CLAUDE_MODEL = "claude-sonnet-4-6"
    /**
     * Máximo de tokens en la respuesta (1 token ≈ 4 caracteres).
     * 1500 tokens ≈ 6,000 caracteres, optimizado para interpretaciones concisas.
     */
    private const val MAX_TOKENS = 1500

    /**
     * Prompt del sistema que define el comportamiento de Claude.
     * Establece que Claude es un experto en Tarot Rider-Waite-Smith.
     */
    private const val SYSTEM_PROMPT = """
Eres un experto en Tarot Rider-Waite-Smith. Proporciona interpretaciones claras,
concisas y prácticas. Usa un tono neutral, directo y accesible para todo público hispanohablante.
Evita lenguaje florido o excesivamente místico.
"""

    /**
     * Construye el prompt completo para interpretar una tirada.
     *
     * @param reading Tirada de tarot a interpretar
     * @return Prompt formateado con toda la información necesaria
     */
    fun buildPrompt(reading: TarotReading): String {
        val spreadTypeName = getSpreadTypeName(reading.spreadType)
        val cardsDescription = buildCardsDescription(reading)
        val responseFormat = buildResponseFormat(reading.spreadType)

        return """
Interpreta la siguiente tirada de Tarot Rider-Waite-Smith:

TIPO DE TIRADA: $spreadTypeName
PREGUNTA: ${reading.question ?: "Lectura general"}

CARTAS:
$cardsDescription

INSTRUCCIONES:
Proporciona una interpretación general concisa (máximo 4 párrafos) que:
- Analice el conjunto de cartas y su relación con la pregunta
- Sea directa y práctica
- Use lenguaje neutral y accesible

${buildSpecificInstructions(reading.spreadType)}

FORMATO JSON (obligatorio):
$responseFormat

IMPORTANTE:
- Solo JSON puro, sin markdown ni texto adicional
- Interpretación concisa y útil
- Máximo 1500 tokens
""".trimIndent()
    }

    /**
     * Obtiene el nombre legible del tipo de tirada.
     */
    private fun getSpreadTypeName(spreadType: SpreadType): String = when (spreadType) {
        SpreadType.SIMPLE -> "Carta Simple"
        SpreadType.YES_NO -> "Sí o No"
        SpreadType.PRESENT -> "Presente (3 cartas)"
        SpreadType.TENDENCY -> "Tendencia Temporal (3 cartas)"
        SpreadType.CROSS -> "Cruz (5 cartas)"
    }

    /**
     * Construye la descripción de todas las cartas de la tirada.
     */
    private fun buildCardsDescription(reading: TarotReading): String {
        return reading.drawnCards.joinToString("\n") { drawnCard ->
            val orientation = when (drawnCard.orientation) {
                CardOrientation.UPRIGHT -> "Posición Derecha"
                CardOrientation.REVERSED -> "Posición Invertida"
            }
            "- **${drawnCard.positionName}**: ${drawnCard.card.name} ($orientation)"
        }
    }

    /**
     * Construye instrucciones específicas según el tipo de tirada.
     */
    private fun buildSpecificInstructions(spreadType: SpreadType): String = when (spreadType) {
        SpreadType.YES_NO -> """
3. **RESPUESTA SÍ O NO**: Basándote en la carta obtenida, proporciona:
   - Una respuesta clara: "Sí", "No" o "Indefinido"
   - Una **justificación educativa** que explique POR QUÉ esa carta específica significa esa respuesta
   - Relaciona el simbolismo y significado de la carta con la interpretación binaria
   - Ayuda al consultante a APRENDER por qué se interpreta de esa manera
"""
        else -> ""
    }

    /**
     * Construye el template JSON de respuesta según el tipo de tirada.
     */
    private fun buildResponseFormat(spreadType: SpreadType): String {
        val yesNoFields = if (spreadType == SpreadType.YES_NO) {
            """,
  "yes_no_answer": "Sí",
  "yes_no_justification": "Justificación breve"
"""
        } else ""

        return """
{
  "general_interpretation": "Interpretación general concisa de la tirada"$yesNoFields
}
""".trimIndent()
    }

    /**
     * Obtiene el ID del modelo de Claude a usar.
     */
    fun getModel(): String = CLAUDE_MODEL

    /**
     * Obtiene el máximo de tokens para la respuesta.
     */
    fun getMaxTokens(): Int = MAX_TOKENS

    /**
     * Obtiene el prompt del sistema.
     */
    fun getSystemPrompt(): String = SYSTEM_PROMPT
}
