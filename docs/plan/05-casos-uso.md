# 5. Casos de Uso (Use Cases)

## 5.1 Feature: Realizar Tirada

```kotlin
// UC1: Obtener configuración de tirada
// Devuelve la configuración (cantidad de cartas, posiciones) según el tipo
class GetSpreadConfigurationUseCase {
    operator fun invoke(type: SpreadType): SpreadConfiguration
}

// UC2: Realizar tirada (seleccionar cartas aleatorias)
// Selecciona cartas al azar sin repetir, asigna orientaciones
class PerformReadingUseCase(
    private val cardRepository: TarotCardRepository  // Repositorio para obtener las cartas
) {
    suspend operator fun invoke(
        spreadType: SpreadType,    // Tipo de tirada elegida
        question: String?          // Pregunta del usuario (opcional)
    ): Result<TarotReading>        // Resultado: éxito con TarotReading o error
}

// UC3: Generar interpretación con IA
// Envía la tirada a la API de Claude y obtiene la interpretación
class GenerateInterpretationUseCase(
    private val claudeRepository: ClaudeRepository  // Repositorio para API de Claude
) {
    suspend operator fun invoke(
        reading: TarotReading      // Tirada a interpretar
    ): Result<Interpretation>      // Resultado: interpretación o error
}
```

## 5.2 Feature: Enciclopedia

```kotlin
// UC4: Obtener todas las cartas
// Devuelve las 78 cartas de la base de datos local
class GetAllCardsUseCase(
    private val cardRepository: TarotCardRepository
) {
    suspend operator fun invoke(): Result<List<TarotCard>>  // Lista completa de cartas
}

// UC5: Filtrar cartas por tipo
// Filtra las cartas según sean Arcanos Mayores o Menores
class FilterCardsByTypeUseCase(
    private val cardRepository: TarotCardRepository
) {
    suspend operator fun invoke(
        arcanaType: ArcanaType     // MAJOR o MINOR
    ): Result<List<TarotCard>>     // Lista filtrada
}

// UC6: Obtener detalle de carta
// Obtiene una carta específica por su ID
class GetCardByIdUseCase(
    private val cardRepository: TarotCardRepository
) {
    suspend operator fun invoke(id: Int): Result<TarotCard?>  // Carta o null si no existe
}
```
