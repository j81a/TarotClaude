# 4. Modelo de Datos

## 4.1 Domain Models

```kotlin
// Enumeraciones (Enums)
enum class ArcanaType {
    MAJOR,      // Arcanos Mayores
    MINOR       // Arcanos Menores
}

enum class Suit {
    WANDS,      // Bastos
    CUPS,       // Copas
    SWORDS,     // Espadas
    PENTACLES   // Oros
}

enum class SpreadType {
    SIMPLE,     // Carta Simple
    YES_NO,     // Sí o No
    PRESENT,    // Presente
    TENDENCY,   // Tendencia
    CROSS       // Cruz
}

enum class CardOrientation {
    UPRIGHT,    // Derecha (normal)
    REVERSED    // Invertida (al revés)
}

// Modelos principales (Domain Models)
data class TarotCard(
    val id: Int,                        // ID único de la carta (0-77)
    val name: String,                   // Nombre: "El Loco", "As de Copas"
    val arcanaType: ArcanaType,         // Tipo: Arcano Mayor o Menor
    val suit: Suit?,                    // Palo (solo para Arcanos Menores)
    val imagePath: String,              // Ruta de la imagen
    val generalMeaning: String,         // Significado general
    val uprightMeaning: String,         // Significado en posición derecha
    val reversedMeaning: String,        // Significado en posición invertida
    val symbolism: String,              // Simbolismo e iconografía
    val keywords: List<String>          // Palabras clave
)

data class DrawnCard(
    val card: TarotCard,                // La carta seleccionada
    val position: Int,                  // Posición numérica (0, 1, 2...)
    val positionName: String,           // Nombre de la posición: "Pasado", "Presente", etc.
    val orientation: CardOrientation    // Orientación: derecha o invertida
)

data class TarotReading(
    val spreadType: SpreadType,         // Tipo de tirada
    val question: String?,              // Pregunta del usuario (opcional)
    val drawnCards: List<DrawnCard>,    // Cartas seleccionadas
    val interpretation: Interpretation? // Interpretación de la IA (nullable hasta que se genera)
)

data class Interpretation(
    val individualInterpretations: List<CardInterpretation>,  // Interpretación de cada carta
    val generalInterpretation: String,                        // Interpretación general de la tirada
    val yesNoAnswer: YesNoAnswer? = null,                     // Respuesta Sí/No (solo para tirada YES_NO)
    val yesNoJustification: String? = null                    // Justificación educativa (solo para YES_NO)
)

data class CardInterpretation(
    val cardName: String,       // Nombre de la carta interpretada
    val position: String,       // Posición en la tirada
    val interpretation: String  // Texto de la interpretación
)

enum class YesNoAnswer {
    YES,        // Sí
    NO,         // No
    UNCLEAR     // Indefinido
}
```

## 4.2 Configuración de Tiradas

```kotlin
// Configuración de cada tipo de tirada
sealed class SpreadConfiguration(
    val type: SpreadType,               // Tipo de tirada
    val cardCount: Int,                 // Cantidad de cartas
    val positions: List<String>,        // Nombres de las posiciones
    val requiresQuestion: Boolean,      // ¿Requiere pregunta del usuario?
    val layout: LayoutType              // Tipo de disposición visual
) {
    object Simple : SpreadConfiguration(
        type = SpreadType.SIMPLE,
        cardCount = 1,
        positions = listOf("Respuesta"),
        requiresQuestion = false,       // No requiere pregunta
        layout = LayoutType.HORIZONTAL
    )

    object YesNo : SpreadConfiguration(
        type = SpreadType.YES_NO,
        cardCount = 1,
        positions = listOf("Respuesta"),
        requiresQuestion = true,        // SÍ requiere pregunta
        layout = LayoutType.HORIZONTAL
    )

    object Present : SpreadConfiguration(
        type = SpreadType.PRESENT,
        cardCount = 3,
        positions = listOf("Presente", "Obstáculo", "Ayuda"),
        requiresQuestion = true,
        layout = LayoutType.HORIZONTAL
    )

    object Tendency : SpreadConfiguration(
        type = SpreadType.TENDENCY,
        cardCount = 3,
        positions = listOf("De dónde vengo", "Dónde estoy", "A dónde voy"),
        requiresQuestion = true,
        layout = LayoutType.HORIZONTAL
    )

    object Cross : SpreadConfiguration(
        type = SpreadType.CROSS,
        cardCount = 5,
        positions = listOf(
            "De dónde vengo",    // 0 - Izquierda
            "Hacia dónde voy",   // 1 - Derecha
            "Ayuda",             // 2 - Arriba
            "Obstáculo",         // 3 - Abajo
            "Conclusión"         // 4 - Centro
        ),
        requiresQuestion = true,
        layout = LayoutType.CROSS       // Disposición especial en cruz
    )
}

enum class LayoutType {
    HORIZONTAL,  // Disposición lineal horizontal
    CROSS        // Disposición en forma de cruz
}
```
