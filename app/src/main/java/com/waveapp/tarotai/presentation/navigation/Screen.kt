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
     *
     * Ejemplo: "card_detail/5" para ver la carta con ID 5.
     */
    data object CardDetail : Screen("card_detail/{cardId}") {
        /**
         * Crea la ruta completa con el ID de la carta.
         * @param cardId: ID de la carta (0-77)
         * @return Ruta navegable, ej: "card_detail/5"
         */
        fun createRoute(cardId: Int) = "card_detail/$cardId"
    }

    /**
     * Pantalla de selección del tipo de lectura.
     * El usuario elige entre: 1 carta, Sí/No, Presente, Tendencia, Cruz.
     */
    data object ReadingSelection : Screen("reading_selection")

    /**
     * Pantalla de lectura activa.
     * Muestra las cartas seleccionadas y su interpretación.
     * Requiere el tipo de lectura como argumento.
     *
     * Ejemplo: "reading/yes_no" para lectura de Sí/No.
     */
    data object Reading : Screen("reading/{readingType}") {
        /**
         * Crea la ruta completa con el tipo de lectura.
         * @param readingType: Tipo de lectura (ej: "one_card", "yes_no", etc.)
         * @return Ruta navegable, ej: "reading/yes_no"
         */
        fun createRoute(readingType: String) = "reading/$readingType"
    }

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
