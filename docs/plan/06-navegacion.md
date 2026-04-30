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
│ - Ver cartas       │
└─────┬──────────────┘
      │
┌─────▼──────────────┐
│InterpretationScreen│
│ - Individual       │
│ - General          │
│ - Ver detalles     │────┐
└────────────────────┘    │
                          │
              ┌───────────▼────────┐
              │ CardDetailScreen   │
              │ (desde tirada)     │
              └────────────────────┘
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
    object CardDetail : Screen("card_detail/{cardId}")
}
```
