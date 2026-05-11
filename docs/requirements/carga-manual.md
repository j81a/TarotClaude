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
2. Ingresar **nombre del consultante** (opcional, valor por defecto: "Lectura personal")
3. Ingresar la **pregunta**
4. **Ver las cartas de dorso** en sus posiciones (layout visual igual a ReadingScreen)
5. **Seleccionar manualmente cada carta** tocando el dorso
6. **Ver las cartas seleccionadas** reveladas en su posición
7. **Modificar cartas** antes de interpretar (por si se equivocó)
8. Solicitar **interpretación** una vez cargadas todas las cartas
9. Ver la interpretación **en la misma pantalla** (ManualLoadScreen)
10. **Guardar manualmente** en el historial después de ver la interpretación

---

## Diferencias con Nueva Lectura Automática

| Aspecto | Nueva Lectura (Automática) | Cargar Lectura (Manual) |
|---------|---------------------------|------------------------|
| **Flujo de pantallas** | SpreadTypeScreen → QuestionScreen → ReadingScreen | SpreadTypeScreen → QuestionScreen → ManualLoadScreen |
| **Campo consultante** | Opcional (toggle OFF por defecto) | Opcional (toggle OFF por defecto) |
| **Valor por defecto consultante** | "Lectura personal" | "Lectura personal" |
| **Selección de cartas** | Automática (aleatoria) | Manual (tarotista elige) |
| **Visualización inicial** | Cartas de dorso → flip → reveladas | Cartas de dorso (icono +) → selección → reveladas (icono lápiz) |
| **Iconos en cartas** | Icono 'i' (info) abajo al centro | Icono '+' (agregar), lápiz (editar), 'i' (info) según estado |
| **Momento de interpretación** | Inmediato después de revelar | Tras botón "Generar Interpretación" |
| **Pantalla de interpretación** | ReadingScreen (scroll hacia abajo) | ManualLoadScreen (scroll hacia abajo) |
| **Edición de cartas** | No se puede | Sí, antes de interpretar |
| **Orientación de cartas** | Automática (50/50) | Manual (tarotista elige) |
| **Guardado en historial** | Manual (botón después de interpretación) | Manual (botón después de interpretación) |

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

**Elementos de UI** (idénticos en ambos modos):
- **Título**: "Configuración de Lectura"
- **Campo de texto**: "Pregunta" (obligatorio excepto para Carta Simple, mín 10 caracteres)
- **Toggle/Checkbox**: "Esta lectura es para alguien más" (OFF por defecto en ambos modos)
- **Campo de texto**: "Nombre del consultante" (visible solo si toggle ON, mín 2 caracteres)
- **Hint visual**:
  - Modo automático: "Las cartas se seleccionarán automáticamente"
  - Modo manual: "Cargarás las cartas manualmente en el siguiente paso"
- **Botón**: "Continuar"

**Validaciones**:
- Nombre del consultante: opcional en ambos modos, si se ingresa debe tener 2-100 caracteres
- Pregunta: obligatoria (excepto Carta Simple), 10-500 caracteres

**Sin diferencias en comportamiento**: QuestionScreen funciona igual en ambos modos, solo cambia el hint visual y la navegación destino.

---

### 3. Pantalla de Carga de Cartas (`ManualLoadScreen`)

Esta es la pantalla principal donde se cargan las cartas manualmente.

**Layout visual** (igual a ReadingScreen pero con cartas de dorso):
```
┌─────────────────────────────┐
│  ← Cargar Lectura        X  │  ← Botón X para volver a Home
│                             │
│  María González             │  ← Nombre consultante (o "Lectura personal")
│  Tirada: Cruz               │  ← Tipo de tirada
│  "¿Qué me depara el amor?"  │  ← Pregunta
│                             │
│  ┌─────────────────────┐    │
│  │    [DORSO]          │    │  ← Carta de dorso (imagen card_back)
│  │     Ayuda           │    │
│  └─────────────────────┘    │
│                             │
│  [DORSO]  [DORSO]  [DORSO]  │  ← Layout tipo Cruz
│  Pasado   Centro   Futuro   │
│                             │
│  ┌─────────────────────┐    │
│  │    [DORSO]          │    │
│  │   Obstáculo         │    │
│  └─────────────────────┘    │
│                             │
│  Progreso: 0/5 cartas       │  ← Progress indicator
│                             │
│  [Generar Interpretación]   │  ← Botón deshabilitado
│                             │
└─────────────────────────────┘
```

**Comportamiento de cada carta**:

1. **Estado inicial**: Carta de dorso (imagen `card_back.jpg`) clickeable + **icono '+' circular** abajo al centro
2. **Al tocar la carta de dorso**: Abre `CardSelectorScreen`
3. **Después de seleccionar** (antes de interpretar): Carta muestra la imagen seleccionada (frente) + badge de orientación + **icono 'lápiz' circular** abajo al centro
4. **Después de interpretar**: Carta muestra la imagen seleccionada + **icono 'i' circular** abajo al centro (solo info, no editable)
5. **Editar carta ya cargada** (antes de interpretar): Tocar nuevamente abre `CardSelectorScreen` con carta actual

**Iconos visuales en cartas** (v1.2.0):
- **Icono '+'**: Circle con símbolo '+' dentro, indica "agregar carta"
- **Icono 'lápiz'**: Circle con símbolo 'edit' dentro, indica "editar carta"
- **Icono 'i'**: Circle con símbolo 'info' dentro, indica "ver detalle" (abre CardDetailScreen)

**Botón "Generar Interpretación"**:
- **Deshabilitado** (gris) si faltan cartas por cargar
- **Habilitado** cuando todas las cartas están cargadas
- **Loading state** mientras se genera interpretación
- **Al presionar**: Llamada a API de Claude → **Muestra interpretación en la misma pantalla** (scroll hacia abajo)

**Después de generar interpretación**:
- Las cartas **ya no son clickeables** (no se pueden editar)
- Se muestra la interpretación completa (igual que ReadingScreen)
- Aparece botón **"Guardar en Historial"** (como ReadingScreen)
- Al guardar, navega a `ReadingDetailScreen` del historial

---

### 4. Selector de Cartas (`CardSelectorScreen`)

Pantalla similar a `EncyclopediaScreen` pero con **filtros excluyentes** (solo uno activo a la vez) y **mostrando imágenes de cartas**.

**Elementos de UI**:

```
┌─────────────────────────────┐
│  ← Seleccionar Carta     X  │  ← Botón X para volver a Home
│                             │
│  Posición: Centro           │  ← Muestra qué posición está llenando
│                             │
│  Filtros:                   │
│  [Todos] [Mayores] [Menores]│  ← Chips de filtro
│                             │
│  [Bastos][Copas][Espadas][Oros] │ ← Subfilters (solo si "Menores")
│                             │
│  ┌─────┐ ┌─────┐ ┌─────┐   │
│  │IMG  │ │IMG  │ │IMG  │   │  ← Grid con IMÁGENES de cartas
│  │Loco │ │Mago │ │Papisa│   │
│  └─────┘ └─────┘ └─────┘   │
│  ┌─────┐ ┌─────┐ ┌─────┐   │
│  │IMG  │ │IMG  │ │IMG  │   │  ← Cada carta muestra su imagen
│  │Impe-│ │Empe-│ │Hierof│   │
│  │ratriz│ │rador│ │ante  │   │
│  └─────┘ └─────┘ └─────┘   │
│  [más cartas scrolleables] │
│                             │
└─────────────────────────────┘
```

**Cambio importante**: Cada carta muestra su **imagen visual** (no solo el nombre), igual que en EncyclopediaScreen.

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

### 5. Visualización de Interpretación (en `ManualLoadScreen`)

**Una vez generada la interpretación**, ManualLoadScreen muestra el contenido **en la misma pantalla** (scroll hacia abajo):

**Comportamiento post-interpretación**:
- ✅ Muestra interpretación general (igual que ReadingScreen)
- ✅ Para tiradas Sí/No: muestra respuesta destacada
- ✅ Las cartas ya NO son clickeables (no se pueden editar después de interpretar)
- ✅ Aparece botón **"Guardar en Historial"** (igual que ReadingScreen)
- ✅ Al guardar exitosamente, navega a `ReadingDetailScreen` del historial

**Layout visual después de interpretación**:
```
[Cartas reveladas - ya no clickeables]
─────────────────────────────
📊 Interpretación

[Card de interpretación general]

[Botón "Guardar en Historial"]
  (si no está guardada todavía)

[Mensaje "✓ Lectura guardada"]
  (si ya se guardó)
```

**Diferencia con ReadingScreen automática**: Ninguna, el comportamiento es idéntico después de la interpretación.

---

## Criterios de Aceptación

### Acceso y Configuración
- [ ] **CA-12.1**: Existe opción "Cargar Lectura" en el menú principal (HomeScreen)
- [ ] **CA-12.2**: QuestionScreen funciona igual en modo manual y automático (toggle OFF por defecto)
- [ ] **CA-12.3**: Campo consultante es opcional en ambos modos
- [ ] **CA-12.4**: Validaciones de campos obligatorios funcionan correctamente

### Pantalla de Carga de Cartas
- [ ] **CA-12.5**: ManualLoadScreen muestra N cartas de dorso (imagen card_back) según tipo de tirada
- [ ] **CA-12.6**: Layout visual es igual a ReadingScreen (con dorsos en lugar de cartas reveladas)
- [ ] **CA-12.7**: Layout de tirada Cruz muestra disposición en cruz (no lineal)
- [ ] **CA-12.8**: Progress indicator muestra "Progreso: X de N cartas"
- [ ] **CA-12.9**: Botón "Generar Interpretación" está deshabilitado si faltan cartas
- [ ] **CA-12.10**: Al tocar carta de dorso, abre CardSelectorScreen

### Selector de Cartas
- [ ] **CA-12.11**: CardSelectorScreen muestra grid con IMÁGENES de cartas (no solo nombres)
- [ ] **CA-12.12**: Los filtros son excluyentes (solo uno activo a la vez)
- [ ] **CA-12.13**: Filtro "Arcanos Mayores" muestra solo 22 cartas con imágenes
- [ ] **CA-12.14**: Filtros de palos muestran solo 14 cartas cada uno con imágenes
- [ ] **CA-12.15**: Cartas ya seleccionadas no son clickeables (overlay gris)
- [ ] **CA-12.16**: Al seleccionar una carta, pregunta orientación (Derecha/Invertida)

### Edición de Cartas
- [ ] **CA-12.17**: Se pueden cambiar cartas ya cargadas antes de interpretar
- [ ] **CA-12.18**: Al tocar una carta ya revelada (antes de interpretar), abre selector
- [ ] **CA-12.19**: Cambiar una carta libera la anterior para ser usada nuevamente

### Interpretación
- [ ] **CA-12.20**: Al presionar "Generar Interpretación" se llama a la API de Claude
- [ ] **CA-12.21**: Muestra indicador de carga durante generación (en el mismo botón)
- [ ] **CA-12.22**: La interpretación se muestra en la misma pantalla (scroll hacia abajo)
- [ ] **CA-12.23**: Manejo de errores si falla la API (mensaje + botón reintentar)
- [ ] **CA-12.24**: Una vez interpretada, las cartas ya NO son clickeables

### Guardado en Historial
- [ ] **CA-12.25**: Después de la interpretación aparece botón "Guardar en Historial"
- [ ] **CA-12.26**: El botón usa valor por defecto "Lectura personal" si no hay consultante
- [ ] **CA-12.27**: Al guardar exitosamente, muestra mensaje "✓ Lectura guardada"
- [ ] **CA-12.28**: Al guardar, navega a ReadingDetailScreen del historial
- [ ] **CA-12.29**: En el historial, no se distingue de una lectura automática

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
SpreadTypeScreen
    ↓ (seleccionar tipo de tirada)
QuestionScreen (isManualLoad=true)
    ↓ (ingresar pregunta + opcional consultante)
ManualLoadScreen (cartas de dorso)
    ↓ (tocar carta de dorso)
CardSelectorScreen (con imágenes)
    ↓ (seleccionar carta + orientación)
ManualLoadScreen (carta revelada)
    ↓ (repetir hasta completar)
ManualLoadScreen (todas las cartas completas)
    ↓ (botón "Generar Interpretación")
ManualLoadScreen (scroll → interpretación)
    ↓ (botón "Guardar en Historial")
ReadingDetailScreen (historial)
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