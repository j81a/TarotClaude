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
     * claude-3-5-sonnet-20241022 es el modelo más avanzado disponible.
     */
    private const val CLAUDE_MODEL = "claude-3-5-sonnet-20241022"

    /**
     * Máximo de tokens en la respuesta (1 token ≈ 4 caracteres).
     * 4000 tokens ≈ 16,000 caracteres, suficiente para interpretaciones detalladas.
     */
    private const val MAX_TOKENS = 4000

    /**
     * Prompt del sistema que define el comportamiento de Claude.
     * Establece que Claude es un experto en Tarot de Marsella.
     */
    private const val SYSTEM_PROMPT = """
Eres un experto en Tarot de Marsella con profundo conocimiento de la simbología,
significados e interpretación de las cartas. Tu objetivo es proporcionar interpretaciones
claras, profundas y contextualizadas a la pregunta del consultante.

Debes ser educativo y explicar no solo QUÉ significa cada carta, sino POR QUÉ tiene
ese significado en el contexto de la tirada y la pregunta.
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
Interpreta la siguiente tirada de Tarot de Marsella:

TIPO DE TIRADA: $spreadTypeName
PREGUNTA DEL USUARIO: ${reading.question ?: "Sin pregunta específica (lectura general)"}

CARTAS:
$cardsDescription

INSTRUCCIONES:
1. **INTERPRETACIÓN INDIVIDUAL**: Explica cada carta considerando:
   - Su significado intrínseco
   - Su posición en la tirada
   - Su orientación (derecha o invertida)
   - Su relación con la pregunta del usuario

2. **INTERPRETACIÓN GENERAL**: Analiza todas las cartas en conjunto:
   - Cómo se relacionan entre sí
   - Qué mensaje global transmiten
   - Qué perspectivas o consejos ofrecen para la pregunta planteada

${buildSpecificInstructions(reading.spreadType)}

FORMATO DE RESPUESTA:
Responde ÚNICAMENTE con un JSON válido siguiendo esta estructura exacta:
$responseFormat

IMPORTANTE:
- NO incluyas markdown (```json), solo el JSON puro
- NO agregues texto antes o después del JSON
- Asegúrate de que el JSON sea válido (comillas dobles, sin comas finales)
- Las interpretaciones deben ser claras, profundas y educativas
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
  "yes_no_justification": "Explicación educativa de por qué esta carta significa Sí/No/Indefinido"
"""
        } else ""

        return """
{
  "individual_interpretations": [
    {
      "card_name": "Nombre de la carta",
      "position": "Nombre de la posición",
      "interpretation": "Interpretación detallada de esta carta en su posición"
    }
  ],
  "general_interpretation": "Análisis holístico de toda la tirada en conjunto"$yesNoFields
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
