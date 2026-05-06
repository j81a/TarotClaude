# RF-12: Carga Manual de Tiradas (v1.1.0) 🆕

## Descripción

El sistema permite a tarotistas cargar manualmente las cartas de una tirada física para obtener interpretación por IA, facilitando el trabajo profesional con mazos reales.

---

## Contexto

Los tarotistas profesionales realizan lecturas con mazos físicos por diversas razones:
- **Ritual personal**: Conexión espiritual con su mazo físico
- **Consultas presenciales**: Cliente está físicamente presente
- **Preferencia táctil**: Experiencia sensorial de barajar cartas reales
- **Necesidad de interpretación**: Quieren usar la IA para interpretar sin perder su ritual

**Caso de uso real**: Un tarotista tiene un consultante presencial, tira las cartas con su mazo físico, y luego usa la app para generar la interpretación en lugar de hacerlo mentalmente o a mano.

Esta funcionalidad diferencia TarotAI de apps genéricas y la posiciona como herramienta profesional.

---

## Comportamiento Esperado

**Dado** que el usuario quiere cargar una tirada realizada físicamente
**Cuando** accede a "Cargar Lectura" desde el menú principal
**Entonces** debe poder:

1. Seleccionar el **tipo de tirada** (mismos 5 tipos que tirada automática)
2. Ingresar **nombre del consultante**
3. Ingresar la **pregunta**
4. **Seleccionar manualmente cada carta** en el orden correcto
5. Ver las cartas cargadas **boca abajo inicialmente**
6. **Modificar cartas** antes de interpretar (por si se equivocó)
7. Solicitar **interpretación** una vez cargadas todas las cartas
8. Ver la interpretación **exactamente igual** que en una tirada automática

---

## Diferencias con Nueva Lectura Automática

| Aspecto | Nueva Lectura (Automática) | Cargar Lectura (Manual) |
|---------|---------------------------|------------------------|
| **Flujo de pantallas** | SpreadTypeScreen → QuestionScreen → ReadingScreen | SpreadTypeScreen → QuestionScreen (modo manual) → ManualLoadScreen |
| **Campo consultante en QuestionScreen** | Opcional (toggle puede estar OFF) | Obligatorio (toggle forzado a ON) |
| **Selección de cartas** | Automática (aleatoria) | Manual (tarotista elige) |
| **Momento de interpretación** | Inmediato después de revelar | Tras botón "Interpretar" |
| **Edición de cartas** | No se puede | Sí, antes de interpretar |
| **Orientación de cartas** | Automática (50/50) | Manual (tarotista elige) |
| **Animaciones** | Revelar cartas (flip) | Cargar cartas (fade in) |
| **Pantalla de cartas** | ReadingScreen (cartas reveladas) | ManualLoadScreen (cartas con "+" icono) |
| **Guardado en historial** | ✅ Sí | ✅ Sí (idéntico) |

---

## Flujo de Pantallas

### 1. Selección de Tipo de Tirada (`SpreadTypeScreen` - Reutilizada)

**Pantalla compartida con tirada automática** (sin cambios).

El usuario selecciona uno de los 5 tipos de tirada:
- Carta Simple
- Sí o No
- Presente
- Tendencia
- Cruz

**Navegación**: Al seleccionar, navega a `QuestionScreen` con parámetro `isManualLoad=true`

---

### 2. Pantalla de Pregunta y Datos (`QuestionScreen` - Compartida con Modo Manual)

**Pantalla compartida con tirada automática**, pero con comportamiento específico para carga manual.

**Elementos de UI en Modo Manual**:
- **Título**: "Configuración de Lectura"
- **Campo de texto**: "Pregunta" (obligatorio excepto para Carta Simple, mín 10 caracteres)
- **Toggle/Checkbox**: "Esta lectura es para alguien más"
  - **En modo manual**: Forzado a ON (no se puede desactivar)
- **Campo de texto**: "Nombre del consultante" (visible y obligatorio, mín 2 caracteres)
- **Hint visual**: "Cargarás las cartas manualmente en el siguiente paso"
- **Botón**: "Continuar" (deshabilitado hasta que campos obligatorios estén completos)

**Validaciones**:
- Nombre del consultante: obligatorio en modo manual, 2-100 caracteres
- Pregunta: obligatoria (excepto Carta Simple), 10-500 caracteres

**Diferencias con Modo Automático**:
| Aspecto | Modo Automático | Modo Manual |
|---------|-----------------|-------------|
| Toggle "Esta lectura es para alguien más" | Opcional (OFF por defecto) | Forzado a ON |
| Campo consultante | Visible solo si toggle ON | Siempre visible y obligatorio |
| Hint visual | "Las cartas se seleccionarán automáticamente" | "Cargarás las cartas manualmente" |

---

### 2. Pantalla de Carga de Cartas (`ManualLoadScreen`)

Esta es la pantalla principal donde se cargan las cartas manualmente.

**Layout visual**:
```
┌─────────────────────────────┐
│  ← Cargar Lectura           │
│                             │
│  María González             │  ← Nombre consultante
│  Tirada: Cruz               │  ← Tipo de tirada
│                             │
│       [?]  (Ayuda)          │  ← Carta boca abajo
│        ↑                    │
│   (Arriba - Ayuda)          │
│                             │
│  [?] ← [?] → [?]            │  ← Layout tipo Cruz
│  (Pasado) (Centro) (Futuro) │
│                             │
│       [?]  ↓                │
│   (Abajo - Obstáculo)       │
│                             │
│  ─────────────────────────  │
│                             │
│  Cartas cargadas: 0/5       │  ← Progress indicator
│                             │
│  [ Interpretar ]            │  ← Botón deshabilitado
│                             │
└─────────────────────────────┘
```

**Comportamiento de cada carta**:

1. **Estado inicial**: Carta boca abajo con ícono "+" en el centro
2. **Al tocar la carta**: Abre `CardSelectorScreen`
3. **Después de seleccionar**: Carta muestra la imagen seleccionada + badge de orientación
4. **Editar carta ya cargada**: Tocar nuevamente abre `CardSelectorScreen` con carta pre-seleccionada

**Botón "Interpretar"**:
- **Deshabilitado** (gris) si faltan cartas por cargar
- **Habilitado** (verde) cuando todas las cartas están cargadas
- **Loading state** mientras se genera interpretación
- **Al presionar**: Llamada a API de Claude → Navega a `InterpretationScreen`

---

### 3. Selector de Cartas (`CardSelectorScreen`)

Pantalla similar a `EncyclopediaScreen` pero con **filtros excluyentes** (solo uno activo a la vez).

**Elementos de UI**:

```
┌─────────────────────────────┐
│  ← Seleccionar Carta        │
│                             │
│  Posición: Centro           │  ← Muestra qué posición está llenando
│                             │
│  Filtros:                   │
│  [Todos] [Mayores] [Menores]│  ← Chips de filtro
│                             │
│  [Bastos][Copas][Espadas][Oros] │ ← Subfilters (solo si "Menores")
│                             │
│  ┌─────┐ ┌─────┐ ┌─────┐   │
│  │ El  │ │ La  │ │ La  │   │  ← Grid de cartas
│  │ Loco│ │Papisa│ │Impe-│   │
│  └─────┘ └─────┘ │ratriz│   │
│                  └─────┘   │
│  [más cartas scrolleables] │
│                             │
└─────────────────────────────┘
```

**Filtros disponibles** (exclusivos):
- **Todos** (sin filtro, muestra las 78 cartas)
- **Arcanos Mayores** (22 cartas)
- **Arcanos Menores**:
  - Bastos (14 cartas)
  - Copas (14 cartas)
  - Espadas (14 cartas)
  - Oros (14 cartas)

**Comportamiento de filtros**:
- Al tocar un filtro, se **desactivan los demás**
- Solo un filtro puede estar activo a la vez
- Transición visual clara del filtro activo (color primario)

**Flujo de selección**:
1. Usuario toca una carta del grid
2. Aparece **diálogo de orientación**:
   ```
   ┌──────────────────────┐
   │ ¿Orientación?        │
   │                      │
   │  [ Derecha ]         │
   │  [ Invertida ]       │
   │                      │
   │  [Cancelar]          │
   └──────────────────────┘
   ```
3. Usuario selecciona orientación
4. Carta se asigna a la posición
5. Navega de vuelta a `ManualLoadScreen`

**Prevención de duplicados**:
- Cartas ya seleccionadas en la tirada aparecen con overlay gris y no son clickeables
- Muestra badge "En uso" sobre la carta

---

### 4. Pantalla de Interpretación (`InterpretationScreen`)

**Una vez interpretada**, la lectura manual funciona **exactamente igual** que una lectura automática:

- ✅ Muestra interpretación individual y general
- ✅ Las cartas son clickeables → Navega a `CardDetailScreen`
- ✅ Se puede guardar en historial con nombre de consultante
- ✅ Botón "Nueva Lectura" reinicia el flujo
- ✅ Comportamiento idéntico al de lectura automática

**Diferencia visual**: Badge "Carga Manual" en el encabezado (opcional, para historial)

---

## Criterios de Aceptación

### Acceso y Configuración
- [ ] **CA-12.1**: Existe opción "Cargar Lectura" en el menú principal (HomeScreen)
- [ ] **CA-12.2**: Primera pantalla pide: tipo de tirada, nombre consultante, pregunta
- [ ] **CA-12.3**: Validaciones de campos obligatorios funcionan correctamente
- [ ] **CA-12.4**: Botón "Continuar" deshabilitado hasta completar campos obligatorios

### Pantalla de Carga de Cartas
- [ ] **CA-12.5**: Segunda pantalla muestra N cartas boca abajo (según tipo de tirada)
- [ ] **CA-12.6**: Cada carta tiene ícono "+" visible claramente
- [ ] **CA-12.7**: Layout de tirada Cruz muestra disposición en cruz (no lineal)
- [ ] **CA-12.8**: Progress indicator muestra "X/N cartas cargadas"
- [ ] **CA-12.9**: Botón "Interpretar" está deshabilitado si faltan cartas

### Selector de Cartas
- [ ] **CA-12.10**: Al tocar "+" abre selector con grid de cartas
- [ ] **CA-12.11**: Los filtros son excluyentes (solo uno activo a la vez)
- [ ] **CA-12.12**: Filtro "Arcanos Mayores" muestra solo 22 cartas
- [ ] **CA-12.13**: Filtros de palos muestran solo 14 cartas cada uno
- [ ] **CA-12.14**: Cartas ya seleccionadas no son clickeables (overlay gris)
- [ ] **CA-12.15**: Al seleccionar una carta, pregunta orientación (Derecha/Invertida)

### Edición de Cartas
- [ ] **CA-12.16**: Se pueden cambiar cartas ya cargadas antes de interpretar
- [ ] **CA-12.17**: Al tocar una carta cargada, abre selector con carta pre-seleccionada
- [ ] **CA-12.18**: Cambiar una carta libera la anterior para ser usada nuevamente

### Interpretación
- [ ] **CA-12.19**: Al presionar "Interpretar" se llama a la API de Claude
- [ ] **CA-12.20**: Muestra indicador de carga durante generación
- [ ] **CA-12.21**: Manejo de errores si falla la API (reintentar)
- [ ] **CA-12.22**: Una vez interpretada, las cartas ya no se pueden editar

### Comportamiento Post-Interpretación
- [ ] **CA-12.23**: Tocar una carta interpretada → CardDetailScreen (igual que tirada normal)
- [ ] **CA-12.24**: La lectura cargada se puede guardar en historial
- [ ] **CA-12.25**: En el historial, no se distingue de una lectura automática (mismos datos)

---

## Navegación (Actualizada)

### Rutas de Navegación

**Rutas reutilizadas** (ya existen en v1.0.0):
```kotlin
object SpreadType : Screen("spread_type")
object Question : Screen("question/{spreadType}/{isManualLoad}") {
    fun createRoute(spreadType: SpreadType, isManualLoad: Boolean): String {
        return "question/${spreadType.name}/$isManualLoad"
    }
}
```

**Rutas nuevas** (v1.1.0):
```kotlin
object ManualLoad : Screen(
    "manual_load/{spreadType}/{consultantName}/{question}"
) {
    fun createRoute(
        spreadType: SpreadType,
        consultantName: String,
        question: String?
    ): String {
        return "manual_load/${spreadType.name}/$consultantName/${question ?: "none"}"
    }
}

object CardSelector : Screen(
    "card_selector/{position}/{excludedIds}"
) {
    fun createRoute(
        position: Int,
        excludedCardIds: List<Int>
    ): String {
        val idsString = excludedCardIds.joinToString(",")
        return "card_selector/$position/$idsString"
    }
}
```

### Flujo de Navegación

```
HomeScreen
    ↓ (opción "Cargar Lectura")
SpreadTypeScreen (reutilizada)
    ↓ (seleccionar tipo de tirada)
QuestionScreen (modo manual: isManualLoad=true)
    ↓ (ingresar consultante + pregunta)
ManualLoadScreen
    ↓ (tocar carta con "+")
CardSelectorScreen
    ↓ (seleccionar carta + orientación)
ManualLoadScreen (actualizada)
    ↓ (botón "Interpretar")
InterpretationScreen
    ↓ (tocar carta)
CardDetailScreen
```

---

## Modelos de Dominio (Actualizados)

### ManualLoadConfiguration
```kotlin
data class ManualLoadConfiguration(
    val spreadType: SpreadType,
    val consultantName: String,
    val question: String?
)
```

### ManualLoadState
```kotlin
data class ManualLoadState(
    val configuration: ManualLoadConfiguration,
    val selectedCards: Map<Int, DrawnCard>,  // posición → carta
    val isComplete: Boolean,
    val isInterpreting: Boolean
) {
    val cardsRemaining: Int
        get() = configuration.spreadType.cardCount - selectedCards.size
}
```

---

## Casos de Uso (Nuevos)

### 1. ValidateManualLoadConfigUseCase
- **Input**: `spreadType`, `consultantName`, `question`
- **Output**: `Result<ManualLoadConfiguration>`
- **Lógica**: Valida campos obligatorios

### 2. AddCardToManualLoadUseCase
- **Input**: `position: Int`, `card: TarotCard`, `orientation: CardOrientation`
- **Output**: `Result<ManualLoadState>`
- **Lógica**: Agrega carta a la posición especificada

### 3. RemoveCardFromManualLoadUseCase
- **Input**: `position: Int`
- **Output**: `Result<ManualLoadState>`
- **Lógica**: Remueve carta de una posición

### 4. GetAvailableCardsUseCase
- **Input**: `excludedCardIds: List<Int>`, `filter: CardFilter`
- **Output**: `Flow<List<TarotCard>>`
- **Lógica**: Obtiene cartas disponibles (no usadas) según filtro

### 5. GenerateInterpretationFromManualLoadUseCase
- **Input**: `ManualLoadState`
- **Output**: `Result<Interpretation>`
- **Lógica**: Genera interpretación igual que tirada automática

---

## Estados de UI

### ManualLoadConfigScreen

```kotlin
data class ManualLoadConfigUiState(
    val spreadType: SpreadType = SpreadType.CARTA_SIMPLE,
    val consultantName: String = "",
    val question: String = "",
    val isValid: Boolean = false,
    val validationErrors: Map<String, String> = emptyMap()
)
```

### ManualLoadScreen

```kotlin
data class ManualLoadUiState(
    val configuration: ManualLoadConfiguration,
    val selectedCards: Map<Int, DrawnCard> = emptyMap(),
    val isComplete: Boolean = false,
    val isInterpreting: Boolean = false,
    val interpretation: Interpretation? = null,
    val error: String? = null
)
```

### CardSelectorScreen

```kotlin
data class CardSelectorUiState(
    val position: Int,
    val availableCards: List<TarotCard> = emptyList(),
    val selectedFilter: CardFilter = CardFilter.ALL,
    val isLoading: Boolean = false
)

enum class CardFilter {
    ALL,
    MAJOR_ARCANA,
    WANDS,
    CUPS,
    SWORDS,
    PENTACLES
}
```

---

## UI/UX - Especificaciones Visuales

### Indicadores Visuales en ManualLoadScreen

**Carta sin cargar** (estado inicial):
```
┌─────────┐
│         │
│    +    │  ← Ícono grande centrado
│         │
└─────────┘
  Posición
```

**Carta cargada**:
```
┌─────────┐
│ ┌─────┐ │  ← Badge de orientación (↑ o ↓)
│ │Carta│ │
│ │     │ │
│ └─────┘ │
└─────────┘
  Posición
```

**Carta siendo editada** (feedback visual):
```
┌─────────┐
│ ┌─────┐ │
│ │Carta│ │  ← Borde azul pulsante
│ │     │ │
│ └─────┘ │
└─────────┘
```

### Layout Específico para Tirada Cruz

**En ManualLoadScreen** debe mostrar disposición en cruz:

```
        [0]
         ↑
       Ayuda

[1] ← [4] → [2]
Pasado Centro Futuro

         ↓
        [3]
     Obstáculo
```

**Otras tiradas** (lineal, horizontal):
```
[0]    [1]    [2]    [3]    [4]
Pos1   Pos2   Pos3   Pos4   Pos5
```

---

## Testing

### Tests Unitarios

- [ ] `ValidateManualLoadConfigUseCaseTest`: Validaciones de entrada
- [ ] `AddCardToManualLoadUseCaseTest`: Agregar carta a posición
- [ ] `GetAvailableCardsUseCaseTest`: Filtros exclusivos funcionan
- [ ] `ManualLoadViewModelTest`: Estados de carga y completitud

### Tests de UI (Compose)

- [ ] Botón "Continuar" deshabilitado sin campos completos
- [ ] Botón "Interpretar" deshabilitado con cartas faltantes
- [ ] Filtros excluyentes (solo uno activo)
- [ ] Cartas ya usadas no clickeables

### Tests de Integración

- [ ] Flujo completo: Config → Carga → Selector → Interpretación
- [ ] Editar carta cargada y volver a seleccionar
- [ ] Guardar lectura manual en historial

---

## Tiempo Estimado de Implementación

| Subtarea | Tiempo |
|----------|--------|
| Modelos y casos de uso | 2h |
| ManualLoadConfigScreen | 1.5h |
| ManualLoadScreen (layout + estados) | 3h |
| CardSelectorScreen (filtros excluyentes) | 2.5h |
| Integración con InterpretationScreen | 1h |
| Navegación y paso de parámetros | 1.5h |
| Tests | 2h |
| **TOTAL** | **~13.5 horas** |

---

## Notas Técnicas

### Gestión de Estado Compartido

Usar `SavedStateHandle` en el ViewModel para persistir el estado entre navegaciones:

```kotlin
@HiltViewModel
class ManualLoadViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    // ... use cases
) : ViewModel() {

    private val _selectedCards = savedStateHandle.getStateFlow(
        "selected_cards",
        emptyMap<Int, DrawnCard>()
    )

    fun addCard(position: Int, card: DrawnCard) {
        val updated = _selectedCards.value.toMutableMap()
        updated[position] = card
        savedStateHandle["selected_cards"] = updated
    }
}
```

### Prevención de Duplicados en CardSelector

```kotlin
@Composable
fun CardSelectorScreen(
    excludedCardIds: List<Int>,
    // ...
) {
    val availableCards = allCards.filter { card ->
        card.id !in excludedCardIds
    }

    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(availableCards) { card ->
            CardItem(
                card = card,
                enabled = card.id !in excludedCardIds,
                onClick = { onCardSelected(card) }
            )
        }
    }
}
```

---

## Dependencias de Otras Funcionalidades

- ✅ Requiere que la enciclopedia esté implementada (usa mismas cartas)
- ✅ Requiere que la interpretación por IA funcione (Fase 4)
- ✅ Requiere navegación con argumentos complejos
- 🔄 Complementa RF-11 (Historial) pero no depende de él
- ❌ No bloquea ninguna funcionalidad futura

---

## Consideraciones de UX

### ¿Por qué filtros excluyentes?

En la **enciclopedia** (RF-08) los filtros pueden ser múltiples porque el usuario está explorando. En el **selector de cartas para carga manual**, el usuario busca **una carta específica**, por lo que filtros excluyentes simplifican la búsqueda:

| Contexto | Filtros | Razón |
|----------|---------|-------|
| Enciclopedia | Múltiples (checkbox) | Exploración libre |
| Carga Manual | Excluyentes (radio) | Búsqueda específica |

### ¿Por qué pedir orientación al seleccionar?

Porque la orientación de la carta física **ya está determinada** cuando el tarotista la ve. El app solo está registrando lo que ya ocurrió en la realidad física.

---

*Documento creado para v1.1.0 - Última actualización: 2026-05-06*