# Tasks - TarotAI

> **Lista de tareas ejecutables para implementación**
> Basado en `requirements.md` y `plan.md`
> Última actualización: 2026-04-27

---

## 📊 Estado General

| Fase | Tareas | Completadas | Progreso |
|------|--------|-------------|----------|
| **Fase 1: Infraestructura Base** | 4 | 0 | 0% |
| **Fase 2: Enciclopedia** | 5 | 0 | 0% |
| **Fase 3: Sistema de Tiradas** | 3 | 0 | 0% |
| **Fase 4: Integración con IA** | 4 | 0 | 0% |
| **Fase 5: Pulido y Testing** | 4 | 0 | 0% |
| **TOTAL** | **20** | **0** | **0%** |

---

## 🏗️ FASE 1: Infraestructura Base

### Tarea 1.1: Configurar Hilt (Inyección de Dependencias)

**Descripción**: Añadir y configurar Hilt para dependency injection en todo el proyecto.

**Criterios de Aceptación**:
- [ ] Añadidas dependencias de Hilt en `build.gradle.kts`
- [ ] Plugin de Hilt configurado
- [ ] Clase `TarotApplication` creada con `@HiltAndroidApp`
- [ ] Registrada en `AndroidManifest.xml`
- [ ] El proyecto compila sin errores

**Archivos a crear/modificar**:
- `gradle/libs.versions.toml` - Añadir versiones de Hilt
- `build.gradle.kts` (root) - Plugin de Hilt
- `app/build.gradle.kts` - Dependencias y plugins de Hilt
- `app/src/main/java/com/waveapp/tarotai/TarotApplication.kt` - Clase Application
- `app/src/main/AndroidManifest.xml` - Registrar Application

**Dependencias**:
```toml
[versions]
hilt = "2.52"

[libraries]
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-compiler", version.ref = "hilt" }
hilt-navigation-compose = { group = "androidx.hilt", name = "hilt-navigation-compose", version = "1.2.0" }
```

**Código esperado**:
```kotlin
// TarotApplication.kt
@HiltAndroidApp
class TarotApplication : Application()
```

**Tiempo estimado**: 30 minutos

---

### Tarea 1.2: Configurar Room Database

**Descripción**: Implementar la base de datos local con Room para almacenar las 78 cartas del tarot.

**Criterios de Aceptación**:
- [ ] Dependencias de Room añadidas
- [ ] `TarotCardEntity` creado con todas las propiedades
- [ ] `TarotCardDao` creado con queries básicas
- [ ] `TarotDatabase` configurado como singleton
- [ ] Módulo de Hilt para proveer la DB
- [ ] Converters para tipos complejos (List<String>)
- [ ] El proyecto compila y la DB se inicializa

**Archivos a crear**:
- `app/src/main/java/com/waveapp/tarotai/data/local/entities/TarotCardEntity.kt`
- `app/src/main/java/com/waveapp/tarotai/data/local/dao/TarotCardDao.kt`
- `app/src/main/java/com/waveapp/tarotai/data/local/database/TarotDatabase.kt`
- `app/src/main/java/com/waveapp/tarotai/data/local/database/Converters.kt`
- `app/src/main/java/com/waveapp/tarotai/core/di/DatabaseModule.kt`

**Estructura de la entidad**:
```kotlin
@Entity(tableName = "tarot_cards")
data class TarotCardEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val arcanaType: String,  // "MAJOR" o "MINOR"
    val suit: String?,       // null para Arcanos Mayores
    val imagePath: String,
    val generalMeaning: String,
    val uprightMeaning: String,
    val reversedMeaning: String,
    val symbolism: String,
    val keywords: String     // JSON string
)
```

**Tiempo estimado**: 1 hora

---

### Tarea 1.3: Configurar Retrofit y Cliente de Claude API

**Descripción**: Configurar Retrofit para hacer llamadas a la API de Claude (Anthropic).

**Criterios de Aceptación**:
- [ ] Dependencias de Retrofit y OkHttp añadidas
- [ ] Interface `ClaudeApiService` creada
- [ ] DTOs para request/response creados
- [ ] Módulo de Hilt para proveer Retrofit
- [ ] API Key configurada desde BuildConfig
- [ ] Interceptor para añadir headers (x-api-key, anthropic-version)
- [ ] El proyecto compila

**Archivos a crear**:
- `app/src/main/java/com/waveapp/tarotai/data/remote/api/ClaudeApiService.kt`
- `app/src/main/java/com/waveapp/tarotai/data/remote/dto/ClaudeRequest.kt`
- `app/src/main/java/com/waveapp/tarotai/data/remote/dto/ClaudeResponse.kt`
- `app/src/main/java/com/waveapp/tarotai/data/remote/client/ClaudeAuthInterceptor.kt`
- `app/src/main/java/com/waveapp/tarotai/core/di/NetworkModule.kt`
- Modificar `app/build.gradle.kts` para BuildConfig

**API Service**:
```kotlin
interface ClaudeApiService {
    @POST("v1/messages")
    suspend fun sendMessage(@Body request: ClaudeRequest): ClaudeResponse
}
```

**BuildConfig setup**:
```kotlin
// En app/build.gradle.kts
android {
    buildTypes {
        debug {
            buildConfigField("String", "CLAUDE_API_KEY", "\"${project.findProperty("CLAUDE_API_KEY")}\"")
        }
    }
    buildFeatures {
        buildConfig = true
    }
}
```

**Tiempo estimado**: 1 hora

---

### Tarea 1.4: Implementar Navegación Básica con Compose

**Descripción**: Configurar Jetpack Compose Navigation con las rutas principales.

**Criterios de Aceptación**:
- [ ] Dependencias de Compose y Navigation añadidas
- [ ] Sealed class `Screen` con todas las rutas
- [ ] `TarotNavHost` creado con NavHost básico
- [ ] MainActivity configurada con Compose
- [ ] Tema Material 3 básico aplicado
- [ ] Pantalla temporal "Main" que muestra "TarotAI"
- [ ] La app corre y muestra la pantalla principal

**Archivos a crear**:
- `app/src/main/java/com/waveapp/tarotai/core/navigation/Screen.kt`
- `app/src/main/java/com/waveapp/tarotai/core/navigation/TarotNavHost.kt`
- `app/src/main/java/com/waveapp/tarotai/core/ui/theme/TarotAITheme.kt`
- `app/src/main/java/com/waveapp/tarotai/presentation/main/MainActivity.kt`

**Rutas definidas**:
```kotlin
sealed class Screen(val route: String) {
    object Main : Screen("main")
    object SpreadType : Screen("spread_type")
    object Question : Screen("question/{spreadType}")
    object Reading : Screen("reading")
    object Interpretation : Screen("interpretation")
    object Encyclopedia : Screen("encyclopedia")
    object CardDetail : Screen("card_detail/{cardId}")
}
```

**Tiempo estimado**: 1 hora

---

## 📚 FASE 2: Enciclopedia (Funcionalidad Offline)

### Tarea 2.1: Conseguir y Procesar Imágenes de las 78 Cartas

**Descripción**: Obtener imágenes de dominio público del Tarot de Marsella y procesarlas para la app.

**Criterios de Aceptación**:
- [ ] 78 imágenes descargadas (22 Mayores + 56 Menores)
- [ ] Imágenes renombradas según nomenclatura:
  - `card_major_00.jpg` hasta `card_major_21.jpg`
  - `card_wands_01.jpg` hasta `card_wands_14.jpg` (y otros palos)
- [ ] Imágenes optimizadas (máx 200KB cada una)
- [ ] Colocadas en `app/src/main/res/drawable/`
- [ ] Imagen placeholder `card_placeholder.jpg` creada
- [ ] Imagen error `card_error.jpg` creada
- [ ] Imagen dorso `card_back.jpg` creada

**Fuentes recomendadas**:
- Wikimedia Commons: https://commons.wikimedia.org/wiki/Category:Tarot_of_Marseilles
- Public Domain Tarot Decks

**Herramienta de optimización**:
```bash
# Comprimir imágenes con ImageMagick
convert original.jpg -quality 85 -resize 600x1000 card_major_00.jpg
```

**Tiempo estimado**: 2 horas

---

### Tarea 2.2: Generar Contenido JSON de la Enciclopedia

**Descripción**: Crear el archivo JSON con la información completa de las 78 cartas.

**Criterios de Aceptación**:
- [ ] Archivo `tarot_data.json` creado en `app/src/main/assets/`
- [ ] Las 78 cartas tienen toda la información:
  - id, name, arcana_type, suit
  - image_path, general_meaning
  - upright_meaning, reversed_meaning
  - symbolism, keywords (array)
- [ ] Validado que el JSON es válido
- [ ] Información en español
- [ ] Información precisa y consistente

**Estructura esperada**:
```json
{
  "cards": [
    {
      "id": 0,
      "name": "El Loco",
      "arcana_type": "MAJOR",
      "suit": null,
      "image_path": "card_major_00",
      "general_meaning": "Representa el inicio del viaje, la inocencia...",
      "upright_meaning": "Nuevos comienzos, espontaneidad, aventura...",
      "reversed_meaning": "Imprudencia, negligencia, falta de dirección...",
      "symbolism": "El Loco lleva un zurrón que representa...",
      "keywords": ["inicio", "locura", "libertad", "espontaneidad", "riesgo"]
    },
    ...78 cartas
  ]
}
```

**Estrategia**:
1. Usar Claude para generar el contenido inicial
2. Validar manualmente la precisión
3. Asegurar consistencia en el tono y formato

**Tiempo estimado**: 3 horas (con IA + validación manual)

---

### Tarea 2.3: Implementar Importación de Datos a Room

**Descripción**: Crear la lógica para cargar el JSON y poblar la base de datos en el primer inicio.

**Criterios de Aceptación**:
- [ ] `JsonParser` creado para leer y parsear `tarot_data.json`
- [ ] `DatabaseSeeder` que inserta las cartas en Room
- [ ] Lógica de "primera ejecución" con SharedPreferences
- [ ] La DB se puebla automáticamente al instalar la app
- [ ] Logs de verificación implementados
- [ ] Manejo de errores si el JSON falla

**Archivos a crear**:
- `app/src/main/java/com/waveapp/tarotai/data/local/seeder/DatabaseSeeder.kt`
- `app/src/main/java/com/waveapp/tarotai/data/local/seeder/JsonParser.kt`

**Código esperado**:
```kotlin
class DatabaseSeeder(
    private val database: TarotDatabase,
    private val context: Context
) {
    suspend fun seedDatabaseIfNeeded() {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val isSeeded = prefs.getBoolean("db_seeded", false)

        if (!isSeeded) {
            val cards = JsonParser.parseCardsFromAssets(context)
            database.cardDao().insertAll(cards)
            prefs.edit().putBoolean("db_seeded", true).apply()
        }
    }
}
```

**Tiempo estimado**: 1.5 horas

---

### Tarea 2.4: Implementar Domain Layer para Enciclopedia

**Descripción**: Crear modelos de dominio, repositorio y casos de uso para la enciclopedia.

**Criterios de Aceptación**:
- [ ] Modelos de dominio creados (TarotCard, ArcanaType, Suit)
- [ ] Interface `TarotCardRepository` definida
- [ ] Implementación `TarotCardRepositoryImpl`
- [ ] 3 Use Cases implementados:
  - `GetAllCardsUseCase`
  - `FilterCardsByTypeUseCase`
  - `GetCardByIdUseCase`
- [ ] Mappers entre Entity y Domain Model
- [ ] Tests unitarios para Use Cases

**Archivos a crear**:
- `app/src/main/java/com/waveapp/tarotai/domain/model/TarotCard.kt`
- `app/src/main/java/com/waveapp/tarotai/domain/model/ArcanaType.kt`
- `app/src/main/java/com/waveapp/tarotai/domain/model/Suit.kt`
- `app/src/main/java/com/waveapp/tarotai/domain/repository/TarotCardRepository.kt`
- `app/src/main/java/com/waveapp/tarotai/data/repository/TarotCardRepositoryImpl.kt`
- `app/src/main/java/com/waveapp/tarotai/domain/usecase/GetAllCardsUseCase.kt`
- `app/src/main/java/com/waveapp/tarotai/domain/usecase/FilterCardsByTypeUseCase.kt`
- `app/src/main/java/com/waveapp/tarotai/domain/usecase/GetCardByIdUseCase.kt`
- `app/src/main/java/com/waveapp/tarotai/data/mapper/CardMapper.kt`
- `app/src/test/java/com/waveapp/tarotai/domain/usecase/GetAllCardsUseCaseTest.kt`

**Tiempo estimado**: 2 horas

---

### Tarea 2.5: Implementar UI de Enciclopedia

**Descripción**: Crear las pantallas de lista de cartas y detalle de carta con Jetpack Compose.

**Criterios de Aceptación**:
- [ ] `EncyclopediaViewModel` implementado con StateFlow
- [ ] `EncyclopediaScreen` muestra lista de cartas con LazyColumn
- [ ] `CardDetailScreen` muestra toda la info de una carta
- [ ] Navegación entre pantalla de lista y detalle funciona
- [ ] Filtro por tipo de arcano (Mayor/Menor) funciona
- [ ] Imágenes se cargan con Coil
- [ ] Estados de loading, success, error manejados
- [ ] UI responsiva y con buen diseño

**Archivos a crear**:
- `app/src/main/java/com/waveapp/tarotai/presentation/encyclopedia/viewmodel/EncyclopediaViewModel.kt`
- `app/src/main/java/com/waveapp/tarotai/presentation/encyclopedia/ui/EncyclopediaScreen.kt`
- `app/src/main/java/com/waveapp/tarotai/presentation/encyclopedia/ui/CardDetailScreen.kt`
- `app/src/main/java/com/waveapp/tarotai/presentation/encyclopedia/ui/components/CardListItem.kt`
- `app/src/main/java/com/waveapp/tarotai/core/ui/components/ErrorView.kt`
- `app/src/main/java/com/waveapp/tarotai/core/ui/components/LoadingView.kt`

**Tiempo estimado**: 3 horas

---

## 🎴 FASE 3: Sistema de Tiradas

### Tarea 3.1: Implementar Lógica de Selección de Cartas

**Descripción**: Crear el algoritmo para seleccionar cartas aleatorias sin repetir y asignar orientaciones.

**Criterios de Aceptación**:
- [ ] `SpreadConfiguration` sealed class con las 5 tiradas
- [ ] `GetSpreadConfigurationUseCase` implementado
- [ ] `PerformReadingUseCase` selecciona cartas aleatoriamente
- [ ] No se repiten cartas en una misma tirada
- [ ] Orientación (derecha/invertida) asignada con 50% probabilidad
- [ ] Posiciones asignadas correctamente según tipo de tirada
- [ ] Tests unitarios para el algoritmo de selección

**Archivos a crear**:
- `app/src/main/java/com/waveapp/tarotai/domain/model/SpreadType.kt`
- `app/src/main/java/com/waveapp/tarotai/domain/model/SpreadConfiguration.kt`
- `app/src/main/java/com/waveapp/tarotai/domain/model/TarotReading.kt`
- `app/src/main/java/com/waveapp/tarotai/domain/model/DrawnCard.kt`
- `app/src/main/java/com/waveapp/tarotai/domain/model/CardOrientation.kt`
- `app/src/main/java/com/waveapp/tarotai/domain/usecase/GetSpreadConfigurationUseCase.kt`
- `app/src/main/java/com/waveapp/tarotai/domain/usecase/PerformReadingUseCase.kt`
- `app/src/test/java/com/waveapp/tarotai/domain/usecase/PerformReadingUseCaseTest.kt`

**Algoritmo esperado**:
```kotlin
suspend operator fun invoke(spreadType: SpreadType, question: String?): Result<TarotReading> {
    val config = GetSpreadConfigurationUseCase()(spreadType)
    val allCards = cardRepository.getAllCards().getOrThrow()

    val selectedCards = allCards.shuffled().take(config.cardCount)

    val drawnCards = selectedCards.mapIndexed { index, card ->
        DrawnCard(
            card = card,
            position = index,
            positionName = config.positions[index],
            orientation = if (Random.nextBoolean()) CardOrientation.UPRIGHT else CardOrientation.REVERSED
        )
    }

    return Result.success(TarotReading(spreadType, question, drawnCards, null))
}
```

**Tiempo estimado**: 2 horas

---

### Tarea 3.2: Implementar UI de Flujo de Tirada

**Descripción**: Crear las pantallas para seleccionar tipo de tirada, ingresar pregunta y ver resultado.

**Criterios de Aceptación**:
- [ ] `SpreadTypeScreen` muestra los 5 tipos de tirada como opciones
- [ ] `QuestionScreen` permite ingresar pregunta (validación mínimo 10 caracteres)
- [ ] `ReadingScreen` muestra las cartas seleccionadas
- [ ] Navegación entre pantallas funciona correctamente
- [ ] Parámetros se pasan correctamente entre pantallas
- [ ] UI es intuitiva y atractiva
- [ ] Validación de entrada funciona

**Archivos a crear**:
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/viewmodel/ReadingViewModel.kt`
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/ui/SpreadTypeScreen.kt`
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/ui/QuestionScreen.kt`
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/ui/ReadingScreen.kt`
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/ui/components/SpreadTypeCard.kt`

**Tiempo estimado**: 3 horas

---

### Tarea 3.3: Implementar Animaciones de Revelado de Cartas

**Descripción**: Añadir animaciones para revelar las cartas con efecto flip.

**Criterios de Aceptación**:
- [ ] `CardFlipAnimation` composable implementado
- [ ] Animación de flip (dorso → frente) funciona
- [ ] Secuencia escalonada (una carta a la vez) implementada
- [ ] Animación de entrada (fadeIn) funciona
- [ ] Layout de Cruz muestra las cartas en forma de cruz
- [ ] Otras tiradas muestran cartas en horizontal
- [ ] Las cartas invertidas se muestran rotadas 180°

**Archivos a crear/modificar**:
- `app/src/main/java/com/waveapp/tarotai/core/ui/animations/CardFlipAnimation.kt`
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/ui/components/TarotCardView.kt`
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/ui/components/CrossLayout.kt`
- Modificar `ReadingScreen.kt` para usar animaciones

**Tiempo estimado**: 2 horas

---

## 🤖 FASE 4: Integración con IA

### Tarea 4.1: Implementar Repositorio de Claude

**Descripción**: Crear la capa de datos para interactuar con la API de Claude.

**Criterios de Aceptación**:
- [ ] Interface `ClaudeRepository` definida
- [ ] `ClaudeRepositoryImpl` implementado
- [ ] Manejo de errores HTTP (401, 429, 500)
- [ ] Timeout configurado (30 segundos)
- [ ] Reintentos con backoff exponencial
- [ ] Parseo de respuesta JSON de Claude
- [ ] Módulo de Hilt para proveer el repositorio

**Archivos a crear**:
- `app/src/main/java/com/waveapp/tarotai/domain/repository/ClaudeRepository.kt`
- `app/src/main/java/com/waveapp/tarotai/data/repository/ClaudeRepositoryImpl.kt`
- `app/src/main/java/com/waveapp/tarotai/data/remote/mapper/InterpretationMapper.kt`

**Tiempo estimado**: 1.5 horas

---

### Tarea 4.2: Implementar Generación de Prompts Dinámicos

**Descripción**: Crear el sistema para generar prompts personalizados según la tirada.

**Criterios de Aceptación**:
- [ ] `PromptBuilder` creado con función para cada tipo de tirada
- [ ] Prompts incluyen: tipo de tirada, pregunta, cartas y posiciones
- [ ] Prompt especial para "Sí o No" con justificación educativa
- [ ] Template de respuesta JSON incluido en el prompt
- [ ] Prompts están en español
- [ ] Tests unitarios para generación de prompts

**Archivos a crear**:
- `app/src/main/java/com/waveapp/tarotai/data/remote/prompt/PromptBuilder.kt`
- `app/src/test/java/com/waveapp/tarotai/data/remote/prompt/PromptBuilderTest.kt`

**Ejemplo de prompt**:
```kotlin
fun buildPrompt(reading: TarotReading): String {
    return """
    Eres un experto en Tarot de Marsella. Interpreta la siguiente tirada:

    TIPO DE TIRADA: ${reading.spreadType.name}
    PREGUNTA DEL USUARIO: ${reading.question ?: "Sin pregunta específica"}

    CARTAS:
    ${reading.drawnCards.joinToString("\n") {
        "- Posición ${it.positionName}: ${it.card.name} (${it.orientation})"
    }}

    Proporciona:
    1. INTERPRETACIÓN INDIVIDUAL de cada carta
    2. INTERPRETACIÓN GENERAL de toda la tirada

    ${if (reading.spreadType == SpreadType.YES_NO) {
        "3. RESPUESTA CLARA: Sí / No / Indefinido\n" +
        "4. JUSTIFICACIÓN EDUCATIVA: Explica por qué"
    } else ""}

    Responde en formato JSON: { ... }
    """.trimIndent()
}
```

**Tiempo estimado**: 1.5 horas

---

### Tarea 4.3: Implementar Use Case de Interpretación

**Descripción**: Crear el caso de uso que orquesta la llamada a Claude y procesa la respuesta.

**Criterios de Aceptación**:
- [ ] `GenerateInterpretationUseCase` implementado
- [ ] Llama a `ClaudeRepository` con el prompt generado
- [ ] Parsea la respuesta JSON de Claude
- [ ] Crea objeto `Interpretation` con los datos
- [ ] Maneja errores de red y parseo
- [ ] Logs de debugging implementados
- [ ] Tests unitarios con mock del repositorio

**Archivos a crear**:
- `app/src/main/java/com/waveapp/tarotai/domain/model/Interpretation.kt`
- `app/src/main/java/com/waveapp/tarotai/domain/model/CardInterpretation.kt`
- `app/src/main/java/com/waveapp/tarotai/domain/model/YesNoAnswer.kt`
- `app/src/main/java/com/waveapp/tarotai/domain/usecase/GenerateInterpretationUseCase.kt`
- `app/src/test/java/com/waveapp/tarotai/domain/usecase/GenerateInterpretationUseCaseTest.kt`

**Tiempo estimado**: 2 horas

---

### Tarea 4.4: Implementar UI de Interpretación

**Descripción**: Crear la pantalla que muestra la interpretación generada por la IA.

**Criterios de Aceptación**:
- [ ] `InterpretationScreen` muestra interpretaciones individuales y general
- [ ] Para "Sí o No" muestra respuesta destacada + justificación
- [ ] Indicador de carga mientras se genera la interpretación
- [ ] Botón "Reintentar" si falla la generación
- [ ] Click en carta navega a `CardDetailScreen`
- [ ] Scroll funciona correctamente
- [ ] Botón "Nueva Tirada" reinicia el flujo

**Archivos a crear/modificar**:
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/ui/InterpretationScreen.kt`
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/ui/components/CardInterpretationCard.kt`
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/ui/components/GeneralInterpretationCard.kt`
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/ui/components/YesNoAnswerCard.kt`
- Modificar `ReadingViewModel.kt` para generar interpretación

**Tiempo estimado**: 2.5 horas

---

## ✨ FASE 5: Pulido y Testing

### Tarea 5.1: Implementar Tests Unitarios

**Descripción**: Escribir tests para los casos de uso más críticos.

**Criterios de Aceptación**:
- [ ] Tests para `PerformReadingUseCase` (selección de cartas)
- [ ] Tests para `GenerateInterpretationUseCase` (mock de API)
- [ ] Tests para `GetAllCardsUseCase` (repositorio)
- [ ] Tests para `PromptBuilder` (generación de prompts)
- [ ] Cobertura > 70% en capa de dominio
- [ ] Todos los tests pasan

**Archivos ya mencionados en tareas anteriores**:
- Tests creados durante la implementación de cada Use Case

**Comando para ejecutar**:
```bash
./gradlew test
```

**Tiempo estimado**: 2 horas

---

### Tarea 5.2: Implementar Pantalla Principal (MainScreen)

**Descripción**: Crear la pantalla inicial con navegación a tirada y enciclopedia.

**Criterios de Aceptación**:
- [ ] `MainScreen` con diseño atractivo
- [ ] Botón "Nueva Tirada" navega a `SpreadTypeScreen`
- [ ] Botón "Enciclopedia" navega a `EncyclopediaScreen`
- [ ] Logo o título de la app visible
- [ ] Tema oscuro místico aplicado
- [ ] Animaciones de entrada sutiles

**Archivos a crear**:
- `app/src/main/java/com/waveapp/tarotai/presentation/main/ui/MainScreen.kt`

**Tiempo estimado**: 1.5 horas

---

### Tarea 5.3: Ajustes de UI/UX

**Descripción**: Refinar la interfaz para una experiencia óptima.

**Criterios de Aceptación**:
- [ ] Colores consistentes en toda la app (tema místico)
- [ ] Tipografía legible y jerárquica
- [ ] Espaciados consistentes (padding/margin)
- [ ] Iconos apropiados en botones y navegación
- [ ] Feedback visual en interacciones (ripple, estados)
- [ ] Transiciones suaves entre pantallas
- [ ] App funciona bien en diferentes tamaños de pantalla

**Archivos a modificar**:
- `app/src/main/java/com/waveapp/tarotai/core/ui/theme/Color.kt`
- `app/src/main/java/com/waveapp/tarotai/core/ui/theme/Typography.kt`
- `app/src/main/java/com/waveapp/tarotai/core/ui/theme/Theme.kt`
- Diversos archivos de UI según necesidad

**Paleta de colores sugerida**:
```kotlin
val Purple = Color(0xFF9C27B0)    // Morado místico
val Gold = Color(0xFFFFD700)      // Dorado
val DarkBg = Color(0xFF121212)    // Negro
val DarkSurface = Color(0xFF1E1E1E) // Gris oscuro
```

**Tiempo estimado**: 2 horas

---

### Tarea 5.4: Testing Final y Optimización

**Descripción**: Probar la app end-to-end y optimizar performance.

**Criterios de Aceptación**:
- [ ] Flujo completo de tirada funciona sin errores
- [ ] Enciclopedia carga y muestra todas las cartas
- [ ] Navegación funciona en todos los casos
- [ ] No hay memory leaks (verificar con LeakCanary)
- [ ] Imágenes se cargan eficientemente (caché de Coil)
- [ ] API de Claude responde correctamente
- [ ] Build de release genera APK funcional
- [ ] APK pesa menos de 50MB

**Checklist de pruebas**:
```
☐ Tirada Simple funciona
☐ Tirada Sí o No funciona con justificación
☐ Tirada Presente funciona
☐ Tirada Tendencia funciona
☐ Tirada Cruz funciona (layout correcto)
☐ Enciclopedia muestra 78 cartas
☐ Detalle de carta muestra toda la info
☐ Filtro por tipo de arcano funciona
☐ Navegación desde tirada a detalle funciona
☐ Animaciones se ven fluidas
☐ Manejo de errores de red funciona
☐ App funciona sin internet (enciclopedia)
```

**Comandos útiles**:
```bash
# Build de release
./gradlew assembleRelease

# Instalar en dispositivo
./gradlew installRelease

# Verificar tamaño del APK
ls -lh app/build/outputs/apk/release/
```

**Tiempo estimado**: 2 horas

---

## 📊 Estimación Total de Tiempo

| Fase | Tiempo Estimado |
|------|-----------------|
| Fase 1: Infraestructura Base | 3.5 horas |
| Fase 2: Enciclopedia | 11.5 horas |
| Fase 3: Sistema de Tiradas | 7 horas |
| Fase 4: Integración con IA | 7.5 horas |
| Fase 5: Pulido y Testing | 7.5 horas |
| **TOTAL** | **~37 horas** |

**Distribución sugerida**:
- Sesiones de 2-3 horas
- ~12-15 sesiones de trabajo
- 2-3 semanas trabajando de forma intermitente

---

## 🎯 Orden de Ejecución Recomendado

**Seguir estrictamente este orden**:

1. ✅ **Infraestructura primero** (Fase 1 completa)
   - Sin esto, nada más funciona

2. ✅ **Enciclopedia completa** (Fase 2 completa)
   - Funcionalidad offline primero
   - Permite probar sin depender de la API

3. ✅ **Sistema de Tiradas** (Fase 3 completa)
   - Mecánica central de la app

4. ✅ **Integración con IA** (Fase 4 completa)
   - Añade el valor diferenciador

5. ✅ **Pulido final** (Fase 5 completa)
   - Refinar y asegurar calidad

---

## ✅ Definición de "Hecho" (Definition of Done)

Una tarea se considera completada cuando:

- [ ] Código implementado y funcional
- [ ] Código compila sin warnings
- [ ] Tests unitarios escritos y pasando (si aplica)
- [ ] Sin errores en tiempo de ejecución
- [ ] Code review (self-review) completado
- [ ] Commit creado con mensaje descriptivo
- [ ] Push a GitHub realizado
- [ ] Checkboxes de criterios de aceptación marcados

---

## 🚦 Próximo Paso

**Empezar con Tarea 1.1: Configurar Hilt**

¿Listo para comenzar la implementación? 🚀
