package com.waveapp.tarotai.domain.model

/**
 * Interpretación completa de una tirada de tarot.
 *
 * Contiene:
 * 1. Interpretaciones individuales de cada carta
 * 2. Interpretación general que analiza todas las cartas en conjunto
 * 3. Para tiradas "Sí o No": respuesta binaria y justificación educativa
 *
 * @property individualInterpretations Lista de interpretaciones de cada carta individual
 * @property generalInterpretation Análisis holístico de toda la tirada en conjunto
 * @property yesNoAnswer Respuesta binaria (solo para tiradas YES_NO, null en otros casos)
 * @property yesNoJustification Explicación educativa de por qué la carta significa esa respuesta (solo para YES_NO)
 */
data class Interpretation(
    val individualInterpretations: List<CardInterpretation>,
    val generalInterpretation: String,
    val yesNoAnswer: YesNoAnswer? = null,
    val yesNoJustification: String? = null
)
