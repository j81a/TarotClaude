# Plan de Implementación - TarotAI

> **Documento de arquitectura y decisiones técnicas**
> Define CÓMO implementar los requisitos definidos en `requirements.md`
> Última actualización: 2026-04-24

---

## 1. Arquitectura General

### 1.1 Patrón Arquitectónico

**MVVM (Model-View-ViewModel) + Clean Architecture Básica**

```
┌─────────────────────────────────────────────────┐
│                 Presentation                     │
│  (Jetpack Compose UI + ViewModels)              │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│                  Domain                          │
│  (Use Cases + Domain Models + Repositories)     │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│                   Data                           │
│  (Room DB + API Client + Data Models)           │
└─────────────────────────────────────────────────┘
```

**Justificación:**
- MVVM es el estándar recomendado por Google para Android
- Clean Architecture garantiza separación de responsabilidades
- Facilita testing y mantenibilidad
- Jetpack Compose requiere ViewModels para gestión de estado

### 1.2 Módulos de la Aplicación

El proyecto tendrá un **único módulo** (`app`) con paquetes organizados por feature:

```
com.waveapp.tarotai/
├── core/                    # Código compartido
│   ├── di/                 # Dependency Injection (Hilt)
│   ├── navigation/         # Navegación de Compose
│   └── ui/                 # Componentes UI reutilizables
│
├── data/                   # Capa de datos
│   ├── local/             # Room Database
│   │   ├── entities/      # Entidades de Room
│   │   ├── dao/           # Data Access Objects
│   │   └── database/      # Database singleton
│   ├── remote/            # API de Claude
│   │   ├── api/           # Interface del API
│   │   ├── dto/           # Data Transfer Objects
│   │   └── client/        # Cliente HTTP (Retrofit)
│   └── repository/        # Implementación de repositorios
│
├── domain/                # Capa de dominio
│   ├── model/            # Modelos de dominio
│   ├── repository/       # Interfaces de repositorios
│   └── usecase/          # Casos de uso
│
└── presentation/         # Capa de presentación
    ├── reading/         # Feature: Realizar tiradas
    │   ├── ui/         # Screens y componentes
    │   └── viewmodel/  # ViewModels
    ├── encyclopedia/    # Feature: Enciclopedia de cartas
    │   ├── ui/
    │   └── viewmodel/
    └── main/           # Pantalla principal y navegación
        ├── ui/
        └── viewmodel/
```

**Justificación de estructura modular por feature:**
- Cada feature (tirada, enciclopedia) es independiente
- Facilita encontrar código relacionado
- Preparado para escalar a multi-módulo si crece el proyecto

---

## 2. Estándares de Código

### 2.1 Documentación en Español

**REQUERIMIENTO OBLIGATORIO:** Todo el código debe estar documentado en español.

**Reglas de documentación:**

1. **Clases y sealed classes:**
   - Comentario de clase con descripción breve
   - `@property` para cada parámetro del constructor explicando su propósito

2. **Funciones y métodos:**
   - Comentario explicando qué hace la función
   - `@param` para cada parámetro
   - `@return` para el valor de retorno si aplica

3. **Enums:**
   - Comentario de enum explicando el propósito
   - Comentarios inline para cada valor si es necesario

**Ejemplo correcto:**

```kotlin
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
) { ... }

/**
 * Realiza una tirada de tarot seleccionando cartas aleatorias.
 *
 * @param spreadType Tipo de tirada a realizar
 * @param question Pregunta del usuario (opcional, según el tipo de tirada)
 * @return Result con la tirada generada o un error
 */
suspend fun performReading(
    spreadType: SpreadType,
    question: String?
): Result<TarotReading> { ... }
```

**Justificación:**
- Facilita la comprensión del código por todos los desarrolladores del equipo
- Genera documentación automática (KDoc) en español
- Mantiene consistencia con el idioma de la aplicación

---

## 3. Stack Tecnológico

### 3.1 UI y Navegación

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **Jetpack Compose** | 1.7.x | UI declarativa moderna |
| **Compose Navigation** | 2.8.x | Navegación entre pantallas |
| **Material 3** | 1.3.x | Diseño y componentes UI |
| **Coil** | 2.7.x | Carga de imágenes |

**Decisión: Jetpack Compose**
- ✅ Estándar moderno de Android (vs XML)
- ✅ Menos código boilerplate
- ✅ Animaciones más fáciles de implementar
- ✅ Mejor integración con MVVM

**Internacionalización (i18n):**
- Todos los textos de UI deben estar en `res/values/strings.xml`
- NO hardcodear strings en código Kotlin
- Usar `stringResource(R.string.key)` en Composables
- Preparado para soportar múltiples idiomas (español, inglés, etc.)
- Los datos de las cartas (nombres, significados) también irán en recursos localizables

### 3.2 Inyección de Dependencias

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **Hilt** | 2.52 | DI basado en Dagger |

**Decisión: Hilt**
- ✅ Oficial de Google para Android
- ✅ Menos configuración que Dagger puro
- ✅ Integración perfecta con ViewModels

### 3.3 Persistencia Local

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **Room** | 2.6.x | Base de datos SQLite |
| **Kotlin Serialization** | 1.7.x | Serialización JSON |

**Decisión: Room**
- ✅ ORM oficial de Android
- ✅ Verificación en tiempo de compilación
- ✅ Integración con Kotlin Coroutines
- ✅ Ideal para almacenar enciclopedia de cartas

**Esquema de Base de Datos:**

```sql
-- Tabla: Cartas del Tarot
CREATE TABLE tarot_cards (
    id INTEGER PRIMARY KEY,          -- 0-77 (0-21 Mayores, 22-77 Menores)
    name TEXT NOT NULL,              -- "El Loco", "As de Copas"
    arcana_type TEXT NOT NULL,       -- "MAJOR" o "MINOR"
    suit TEXT,                       -- "WANDS", "CUPS", "SWORDS", "PENTACLES" (null para Mayores)
    image_path TEXT NOT NULL,        -- "arcana_major_0.jpg"
    general_meaning TEXT NOT NULL,
    upright_meaning TEXT NOT NULL,
    reversed_meaning TEXT NOT NULL,
    symbolism TEXT NOT NULL,
    keywords TEXT NOT NULL           -- JSON array: ["inicio", "locura", "libertad"]
);

-- Índices para búsqueda rápida
CREATE INDEX idx_arcana_type ON tarot_cards(arcana_type);
CREATE INDEX idx_suit ON tarot_cards(suit);
```

### 3.4 Networking

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **Retrofit** | 2.11.x | Cliente HTTP |
| **OkHttp** | 4.12.x | Cliente HTTP subyacente |
| **Kotlin Coroutines** | 1.9.x | Asincronía |

**API de Claude (Anthropic):**
- Endpoint: `https://api.anthropic.com/v1/messages`
- Autenticación: API Key en header `x-api-key`
- Modelo: `claude-3-5-sonnet-20241022` (o versión disponible)

**Estructura de Request para Interpretación:**

```kotlin
data class ClaudeRequest(
    val model: String,
    val max_tokens: Int,
    val messages: List<Message>
)

data class Message(
    val role: String,  // "user"
    val content: String
)
```

**Prompt Template para Interpretación:**

```
Eres un experto en Tarot de Marsella. Interpreta la siguiente tirada:

TIPO DE TIRADA: {spreadType}
PREGUNTA DEL USUARIO: {question}

CARTAS:
{forEach carta}
- Posición {position}: {cardName} ({orientation})
{end}

Proporciona:
1. INTERPRETACIÓN INDIVIDUAL de cada carta considerando su posición y orientación
2. INTERPRETACIÓN GENERAL de toda la tirada en conjunto

{if spreadType == "Sí o No"}
3. RESPUESTA CLARA: Sí / No / Indefinido
4. JUSTIFICACIÓN EDUCATIVA: Explica por qué esta carta específica significa esa respuesta
{end}

Formato de respuesta en JSON:
{
  "individual_interpretations": [
    {
      "card": "nombre",
      "position": "posición",
      "interpretation": "texto"
    }
  ],
  "general_interpretation": "texto",
  "yes_no_answer": "Sí|No|Indefinido" // solo para tirada Sí o No
  "yes_no_justification": "texto" // solo para tirada Sí o No
}
```

### 3.5 Testing

| Tecnología | Propósito |
|-----------|-----------|
| **JUnit 5** | Tests unitarios |
| **Mockk** | Mocking para Kotlin |
| **Turbine** | Testing de Flows |
| **Compose UI Test** | Testing de UI |

---

## 4. Modelo de Datos

### 4.1 Domain Models

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

### 4.2 Configuración de Tiradas

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

---

## 5. Casos de Uso (Use Cases)

### 5.1 Feature: Realizar Tirada

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

### 5.2 Feature: Enciclopedia

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

---

## 6. Flujo de Navegación

### 6.1 Estructura de Pantallas

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

### 6.2 Rutas de Navegación

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

---

## 7. Gestión de Estado

### 7.1 Estados de UI

**Patrón: Sealed Class para UI State**

```kotlin
// Estados posibles de la UI
sealed class UiState<out T> {
    object Idle : UiState<Nothing>()           // Estado inicial (sin acción)
    object Loading : UiState<Nothing>()        // Cargando datos
    data class Success<T>(val data: T) : UiState<T>()  // Éxito con datos
    data class Error(val message: String) : UiState<Nothing>()  // Error con mensaje
}
```

**Ejemplo: ReadingViewModel**

```kotlin
@HiltViewModel  // Anotación de Hilt para inyección de dependencias
class ReadingViewModel @Inject constructor(  // Constructor con dependencias inyectadas
    private val performReadingUseCase: PerformReadingUseCase,           // Caso de uso: realizar tirada
    private val generateInterpretationUseCase: GenerateInterpretationUseCase  // Caso de uso: generar interpretación
) : ViewModel() {

    // Estado de la tirada (privado para modificar, público para observar)
    private val _readingState = MutableStateFlow<UiState<TarotReading>>(UiState.Idle)
    val readingState: StateFlow<UiState<TarotReading>> = _readingState.asStateFlow()

    // Estado de la interpretación
    private val _interpretationState = MutableStateFlow<UiState<Interpretation>>(UiState.Idle)
    val interpretationState: StateFlow<UiState<Interpretation>> = _interpretationState.asStateFlow()

    // Función: Realizar tirada
    fun performReading(spreadType: SpreadType, question: String?) {
        viewModelScope.launch {  // Ejecuta en coroutine
            _readingState.value = UiState.Loading  // Cambia a estado "cargando"
            performReadingUseCase(spreadType, question)
                .onSuccess { _readingState.value = UiState.Success(it) }  // Éxito
                .onFailure { _readingState.value = UiState.Error(it.message ?: "Error") }  // Error
        }
    }

    // Función: Generar interpretación con IA
    fun generateInterpretation(reading: TarotReading) {
        viewModelScope.launch {
            _interpretationState.value = UiState.Loading
            generateInterpretationUseCase(reading)
                .onSuccess { _interpretationState.value = UiState.Success(it) }
                .onFailure { _interpretationState.value = UiState.Error(it.message ?: "Error") }
        }
    }
}
```

---

## 8. Assets y Recursos

### 8.1 Imágenes de Cartas

**Ubicación:** `app/src/main/res/drawable/`

**Nomenclatura:**
- Arcanos Mayores: `card_major_00.jpg` hasta `card_major_21.jpg`
- Arcanos Menores:
  - `card_wands_01.jpg` hasta `card_wands_14.jpg`
  - `card_cups_01.jpg` hasta `card_cups_14.jpg`
  - `card_swords_01.jpg` hasta `card_swords_14.jpg`
  - `card_pentacles_01.jpg` hasta `card_pentacles_14.jpg`

**Tamaño recomendado:** 600x1000px (ratio 3:5)

**Fuente:** Wikimedia Commons - Tarot de Marsella de dominio público

### 8.2 Contenido de Enciclopedia

**Ubicación:** `app/src/main/assets/tarot_data.json`

**Estructura JSON:**

```json
{
  "cards": [
    {
      "id": 0,
      "name": "El Loco",
      "arcana_type": "MAJOR",
      "suit": null,
      "image_path": "card_major_00.jpg",
      "general_meaning": "Representa el inicio...",
      "upright_meaning": "En posición derecha...",
      "reversed_meaning": "En posición invertida...",
      "symbolism": "El Loco lleva un zurrón...",
      "keywords": ["inicio", "locura", "libertad", "espontaneidad", "riesgo"]
    }
  ]
}
```

**Estrategia de contenido:**
1. Generar contenido inicial con IA (Claude)
2. Validar manualmente para asegurar precisión
3. Almacenar en archivo JSON
4. Importar a Room DB en primera ejecución

---

## 9. Configuración de Seguridad

### 9.1 API Key de Claude

**Opción elegida: BuildConfig**

```kotlin
// build.gradle.kts (app)
android {
    buildTypes {
        debug {
            buildConfigField("String", "CLAUDE_API_KEY", "\"${project.findProperty("CLAUDE_API_KEY")}\"")
        }
        release {
            buildConfigField("String", "CLAUDE_API_KEY", "\"${project.findProperty("CLAUDE_API_KEY")}\"")
        }
    }
}

// local.properties (git-ignored)
CLAUDE_API_KEY=sk-ant-api03-xxxxx
```

**Justificación:**
- ✅ No expuesta en código fuente
- ✅ Diferente por entorno (debug/release)
- ⚠️ Limitación: La key está en el APK (aceptable para MVP sin backend)

**Mejora futura:** Implementar backend proxy para ocultar completamente la API key.

---

## 10. Animaciones

### 10.1 Animación de Revelado de Cartas

**Enfoque: Compose Animations**

```kotlin
// Animación de flip de carta (dorso → frente)
@Composable
fun CardFlipAnimation(
    isFaceUp: Boolean,
    frontContent: @Composable () -> Unit,
    backContent: @Composable () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFaceUp) 180f else 0f,
        animationSpec = tween(durationMillis = 600)
    )

    Box(modifier = Modifier.graphicsLayer { rotationY = rotation }) {
        if (rotation <= 90f) {
            backContent()
        } else {
            Box(modifier = Modifier.graphicsLayer { rotationY = 180f }) {
                frontContent()
            }
        }
    }
}
```

**Secuencia de tirada:**
1. Aparece dorso de carta con `fadeIn()` (300ms)
2. Espera 200ms
3. Flip animation (600ms)
4. Carta queda visible
5. Repetir para siguiente carta

---

## 11. Manejo de Errores

### 11.1 Estrategias por Capa

**Networking (API Claude):**
- Timeout: 30 segundos
- Reintentos: 2 intentos con backoff exponencial
- Errores HTTP mapeados a mensajes amigables:
  - 401: "Error de autenticación"
  - 429: "Demasiadas solicitudes, intenta más tarde"
  - 500: "Error del servidor, intenta nuevamente"

**Base de Datos:**
- Si falla carga inicial: mostrar mensaje y botón "Reintentar"
- Si falta una carta: usar placeholder

**UI:**
- Todos los estados de error muestran:
  - Mensaje descriptivo
  - Botón "Reintentar"
  - Opción de volver atrás

---

## 12. Performance

### 12.1 Optimizaciones

**Carga de imágenes:**
- Usar Coil con caché en memoria y disco
- Lazy loading en listas (LazyColumn)
- Placeholder mientras carga

**Base de datos:**
- Índices en columnas de búsqueda frecuente
- Queries asíncronas con Coroutines
- Prepoblar DB en background al instalar

**Compose:**
- `remember` para evitar recomposiciones innecesarias
- `derivedStateOf` para cálculos derivados
- `key()` en listas para identificación estable

---

## 13. Testing Strategy

### 13.1 Pirámide de Testing

```
      ┌─────────┐
      │   E2E   │  (Pocos - flujos críticos)
      │  Tests  │
     ┌┴─────────┴┐
     │Integration│  (Algunos - interacción entre capas)
     │   Tests   │
    ┌┴───────────┴┐
    │    Unit     │  (Muchos - lógica de negocio)
    │    Tests    │
    └─────────────┘
```

**Unit Tests (prioritarios):**
- ✅ Use Cases (lógica de negocio)
- ✅ ViewModels (gestión de estado)
- ✅ Algoritmo de selección aleatoria de cartas
- ✅ Parseo de JSON de Claude

**Integration Tests:**
- ✅ Room DAOs
- ✅ Repository con DB

**UI Tests (mínimos para MVP):**
- ✅ Flujo completo de tirada simple
- ✅ Navegación a enciclopedia

---

## 14. Dependencias (build.gradle.kts)

### 14.1 Versiones

```kotlin
// versions.gradle.kts o libs.versions.toml
[versions]
compose = "1.7.5"
composeBom = "2024.12.01"
hilt = "2.52"
room = "2.6.1"
retrofit = "2.11.0"
coil = "2.7.0"
kotlinSerialization = "1.7.3"

[libraries]
# Compose
androidx-compose-bom = { group = "androidx.compose", name = "compose-bom", version.ref = "composeBom" }
androidx-compose-ui = { group = "androidx.compose.ui", name = "ui" }
androidx-compose-material3 = { group = "androidx.compose.material3", name = "material3" }
androidx-compose-navigation = { group = "androidx.navigation", name = "navigation-compose", version = "2.8.5" }
androidx-lifecycle-viewmodel-compose = { group = "androidx.lifecycle", name = "lifecycle-viewmodel-compose", version = "2.8.7" }

# Hilt
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-compiler", version.ref = "hilt" }
hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version = "1.2.0" }

# Room
room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }

# Retrofit
retrofit = { group = "com.squareup.retrofit2", name = "retrofit", version.ref = "retrofit" }
retrofit-kotlinx-serialization = { group = "com.squareup.retrofit2", name = "converter-kotlinx-serialization", version.ref = "retrofit" }
okhttp = { group = "com.squareup.okhttp3", name = "okhttp", version = "4.12.0" }

# Kotlin Serialization
kotlinx-serialization-json = { group = "org.jetbrains.kotlinx", name = "kotlinx-serialization-json", version.ref = "kotlinSerialization" }

# Coil
coil-compose = { group = "io.coil-kt", name = "coil-compose", version.ref = "coil" }

# Testing
junit = { group = "junit", name = "junit", version = "4.13.2" }
mockk = { group = "io.mockk", name = "mockk", version = "1.13.13" }
turbine = { group = "app.cash.turbine", name = "turbine", version = "1.2.0" }
coroutines-test = { group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-test", version = "1.9.0" }
```

---

## 15. Fases de Implementación

### Fase 1: Infraestructura Base
1. Configurar Hilt
2. Configurar Room con esquema de DB
3. Configurar Retrofit para API Claude
4. Implementar navegación básica

### Fase 2: Enciclopedia (funcionalidad offline)
5. Conseguir y procesar imágenes de cartas
6. Generar contenido JSON de enciclopedia
7. Implementar importación de datos a Room
8. Pantalla de lista de cartas
9. Pantalla de detalle de carta

### Fase 3: Sistema de Tiradas
10. Implementar algoritmo de selección de cartas
11. Pantallas de flujo de tirada (tipo, pregunta, resultado)
12. Animaciones de revelado

### Fase 4: Integración con IA
13. Implementar cliente de API Claude
14. Generación de prompts dinámicos
15. Parseo de respuestas
16. Pantalla de interpretación

### Fase 5: Pulido y Testing
17. Testing unitario de use cases
18. Testing de integración
19. Ajustes de UI/UX
20. Optimización de performance

---

## 16. Riesgos y Mitigaciones

| Riesgo | Probabilidad | Impacto | Mitigación |
|--------|--------------|---------|------------|
| **API de Claude inaccesible** | Media | Alto | Implementar caché de última interpretación + mensaje claro de error |
| **Imágenes de cartas de baja calidad** | Media | Medio | Buscar múltiples fuentes, validar visualmente antes de integrar |
| **Contenido de enciclopedia impreciso** | Media | Alto | Validación manual + consulta con expertos en tarot |
| **Límites de API de Claude (rate limiting)** | Alta | Medio | Implementar backoff exponencial + mensaje al usuario |
| **Tamaño del APK por imágenes** | Alta | Bajo | Comprimir imágenes WebP, máx 200KB por carta |
| **Performance en animaciones** | Baja | Medio | Testing en dispositivos de gama baja |

---

## 17. Métricas de Éxito Técnico

**Criterios de aceptación técnicos:**

- [ ] Build exitoso sin warnings de compilación
- [ ] Cobertura de tests > 70% en capa de dominio
- [ ] Tiempo de respuesta de API < 30 segundos (percentil 95)
- [ ] Tamaño de APK < 50MB
- [ ] Tiempo de carga inicial < 3 segundos
- [ ] Sin memory leaks detectados con LeakCanary
- [ ] Compatibilidad con Android 7.0+ verificada

---

## 18. Aprobación

Este documento requiere aprobación explícita antes de proceder con `tasks.md`.

**Estado**: ⏳ Pendiente de revisión

---

**Fin del documento de planificación**