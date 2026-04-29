package com.waveapp.tarotai.domain.model

/**
 * Configuración de cada tipo de tirada.
 * Define cuántas cartas se usan, las posiciones y el layout visual.
 *
 * @property type Tipo de tirada
 * @property cardCount Cantidad de cartas en la tirada
 * @property positions Nombres de las posiciones de cada carta
 * @property requiresQuestion Indica si la tirada requiere una pregunta del usuario
 * @property layout Tipo de disposición visual (horizontal o cruz)
 */
sealed class SpreadConfiguration(
    val type: SpreadType,
    val cardCount: Int,
    val positions: List<String>,
    val requiresQuestion: Boolean,
    val layout: LayoutType
) {
    /**
     * Carta Simple: 1 carta para orientación general.
     */
    object Simple : SpreadConfiguration(
        type = SpreadType.SIMPLE,
        cardCount = 1,
        positions = listOf("Respuesta"),
        requiresQuestion = false,
        layout = LayoutType.HORIZONTAL
    )

    /**
     * Sí o No: 1 carta para responder una pregunta específica.
     */
    object YesNo : SpreadConfiguration(
        type = SpreadType.YES_NO,
        cardCount = 1,
        positions = listOf("Respuesta"),
        requiresQuestion = true,
        layout = LayoutType.HORIZONTAL
    )

    /**
     * Presente: 3 cartas sobre la situación actual.
     */
    object Present : SpreadConfiguration(
        type = SpreadType.PRESENT,
        cardCount = 3,
        positions = listOf("Presente", "Obstáculo", "Ayuda"),
        requiresQuestion = true,
        layout = LayoutType.HORIZONTAL
    )

    /**
     * Tendencia: 3 cartas sobre pasado, presente y futuro.
     */
    object Tendency : SpreadConfiguration(
        type = SpreadType.TENDENCY,
        cardCount = 3,
        positions = listOf("De dónde vengo", "Dónde estoy", "A dónde voy"),
        requiresQuestion = true,
        layout = LayoutType.HORIZONTAL
    )

    /**
     * Cruz: 5 cartas en disposición de cruz.
     */
    object Cross : SpreadConfiguration(
        type = SpreadType.CROSS,
        cardCount = 5,
        positions = listOf(
            "De dónde vengo",    // Izquierda
            "Hacia dónde voy",   // Derecha
            "Ayuda",             // Arriba
            "Obstáculo",         // Abajo
            "Conclusión"         // Centro
        ),
        requiresQuestion = true,
        layout = LayoutType.CROSS
    )

    companion object {
        /**
         * Obtiene la configuración correspondiente a un tipo de tirada.
         */
        fun fromType(type: SpreadType): SpreadConfiguration {
            return when (type) {
                SpreadType.SIMPLE -> Simple
                SpreadType.YES_NO -> YesNo
                SpreadType.PRESENT -> Present
                SpreadType.TENDENCY -> Tendency
                SpreadType.CROSS -> Cross
            }
        }
    }
}
