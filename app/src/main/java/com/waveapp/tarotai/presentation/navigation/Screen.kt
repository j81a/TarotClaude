package com.waveapp.tarotai.presentation.navigation

/**
 * Define todas las rutas de navegación de la app.
 *
 * sealed class: Solo puede tener subclases declaradas en este mismo archivo.
 * Útil para representar un conjunto cerrado de opciones (como las pantallas de la app).
 *
 * @param route: String que identifica cada pantalla en el NavController.
 */
sealed class Screen(val route: String) {

    /**
     * Pantalla principal / Home.
     * Muestra opciones para ir a Enciclopedia o iniciar una lectura.
     */
    data object Home : Screen("home")

    /**
     * Pantalla de la Enciclopedia.
     * Lista de todas las cartas del tarot.
     */
    data object Encyclopedia : Screen("encyclopedia")

    /**
     * Pantalla de detalle de una carta.
     * Requiere el ID de la carta como argumento en la ruta.
     * Opcionalmente recibe fromReading para mostrar botón "Interpretar con IA".
     * Cuando viene de tirada, también recibe posición y orientación.
     *
     * Ejemplo:
     * - "card_detail/5" - desde enciclopedia
     * - "card_detail/5?fromReading=true&position=Pasado&orientation=UPRIGHT" - desde tirada
     */
    data object CardDetail : Screen("card_detail/{cardId}?fromReading={fromReading}&position={position}&orientation={orientation}") {
        /**
         * Crea la ruta completa con el ID de la carta.
         * @param cardId: ID de la carta (0-77)
         * @param fromReading: true si se accede desde una tirada, false desde enciclopedia
         * @param position: Nombre de la posición en la tirada (ej: "Pasado", "Presente")
         * @param orientation: Orientación de la carta ("UPRIGHT" o "REVERSED")
         * @return Ruta navegable
         */
        fun createRoute(
            cardId: Int,
            fromReading: Boolean = false,
            position: String? = null,
            orientation: String? = null
        ) = buildString {
            append("card_detail/$cardId?fromReading=$fromReading")
            if (position != null) append("&position=$position")
            if (orientation != null) append("&orientation=$orientation")
        }
    }

    /**
     * Pantalla de selección del tipo de lectura.
     * El usuario elige entre: Simple, Sí/No, Presente, Tendencia, Cruz.
     */
    data object SpreadTypeSelection : Screen("spread_type_selection")

    /**
     * Pantalla de ingreso de pregunta.
     * Requiere el tipo de tirada como argumento.
     */
    data object Question : Screen("question/{spreadType}") {
        fun createRoute(spreadType: String) = "question/$spreadType"
    }

    /**
     * Pantalla de lectura activa.
     * Muestra las cartas seleccionadas.
     * Requiere tipo de tirada y pregunta (opcional).
     */
    data object Reading : Screen("reading/{spreadType}?question={question}") {
        fun createRoute(spreadType: String, question: String?) =
            "reading/$spreadType?question=${question ?: ""}"
    }

    /**
     * Deprecated: Mantener para compatibilidad.
     */
    data object ReadingSelection : Screen("reading_selection")

    /**
     * Pantalla de configuración.
     * Permite configurar la API Key de Claude.
     */
    data object Settings : Screen("settings")

    /**
     * Pantalla de historial de lecturas.
     * Muestra todas las lecturas guardadas anteriormente.
     */
    data object History : Screen("history")
}
