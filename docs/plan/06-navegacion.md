# 6. Flujo de Navegación

## 6.1 Estructura de Pantallas

```
┌─────────────────────┐
│   MainScreen        │  Pantalla inicial
│  - Nueva Tirada     │
│  - Enciclopedia     │
└─────┬───────┬───────┘
      │       │
      │       └──────────────────┐
      │                          │
┌─────▼──────────────┐   ┌──────▼─────────────┐
│ SpreadTypeScreen   │   │ EncyclopediaScreen │
│ - Elegir tipo      │   │ - Lista de cartas  │
│   tirada           │   │ - Filtros          │
└─────┬──────────────┘   └──────┬─────────────┘
      │                          │
┌─────▼──────────────┐   ┌──────▼─────────────┐
│ QuestionScreen     │   │ CardDetailScreen   │
│ - Ingresar pregunta│   │ - Info completa    │
└─────┬──────────────┘   └────────────────────┘
      │
┌─────▼──────────────┐
│ ReadingScreen      │
│ - Animación cartas │
│ - Ver cartas       │◄──────────┐
└─────┬──────────┬───┘           │
      │          │                │
      │          └───────────┐    │
      │                      │    │
┌─────▼──────────────┐   ┌──▼────▼────────────┐
│InterpretationScreen│   │ CardDetailScreen   │
│ - Individual       │   │ (desde tirada)     │
│ - General          │   │ + Botón "IA"       │
│ - Ver detalles     │───┤ (Fase 4)           │
└────────────────────┘   └────────────────────┘
```

## 6.2 Rutas de Navegación

```kotlin
sealed class Screen(val route: String) {
    object Main : Screen("main")
    object SpreadType : Screen("spread_type")
    object Question : Screen("question/{spreadType}")
    object Reading : Screen("reading/{spreadType}/{question}")
    object Interpretation : Screen("interpretation/{readingId}")
    object Encyclopedia : Screen("encyclopedia")
    object CardDetail : Screen("card_detail/{cardId}?fromReading={fromReading}")
    // Parámetro fromReading indica si se accede desde tirada (muestra botón IA)
}
```

## 6.3 Decisiones de Navegación

### Reutilización de CardDetailScreen

**Decisión**: Reutilizar `CardDetailScreen` tanto para enciclopedia como para tirada.

**Implementación**:
- Parámetro opcional `fromReading: Boolean` en la ruta
- Si `fromReading = true`: Mostrar botón "Interpretar con IA" al final
- Si `fromReading = false`: Solo mostrar información estática

**Rationale**:
- Evita duplicación de código
- Mantiene consistencia en la UI
- El botón de IA aparece solo cuando tiene sentido (hay contexto de tirada)
- Preparado para Fase 4 sin necesidad de refactorizar
