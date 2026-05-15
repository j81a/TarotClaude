# Tasks - TarotAI

> **Lista de tareas ejecutables para implementación**
> Basado en `requirements.md` y `plan.md`
> Última actualización: 2026-05-04

---

## 📊 Estado General

### v1.0.0 ✅
| Fase | Tareas | Completadas | Progreso |
|------|--------|-------------|----------|
| **Fase 1: Infraestructura Base** | 4 | 4 | ✅ 100% |
| **Fase 2: Enciclopedia** | 5 | 5 | ✅ 100% |
| **Fase 3: Sistema de Tiradas** | 4 | 4 | ✅ 100% |
| **Fase 4: Integración con IA** | 4 | 4 | ✅ 100% |
| **Fase 5: Pulido y Testing** | 4 | 4 | ✅ 100% |
| **Subtotal v1.0.0** | **21** | **21** | **✅ 100%** |

### v1.1.0 ✅
| Fase | Tareas | Completadas | Progreso |
|------|--------|-------------|----------|
| **Fase 6: Historial de Lecturas** | 5 | 5 | ✅ 100% |
| **Fase 7: Carga Manual de Tiradas** | 6 | 6 | ✅ 100% |
| **Subtotal v1.1.0** | **11** | **11** | **✅ 100%** |

### v1.2.0 ✅
| Fase | Tareas | Completadas | Progreso |
|------|--------|-------------|----------|
| **Fase 8: Mejoras de UX** | 6 | 6 | ✅ 100% |
| **Subtotal v1.2.0** | **6** | **6** | **✅ 100%** |

### v1.3.0 ✅
| Fase | Tareas | Completadas | Progreso |
|------|--------|-------------|----------|
| **Fase 9: Reconocimiento de Voz** | 3 | 3 | ✅ 100% |
| **Subtotal v1.3.0** | **3** | **3** | **✅ 100%** |

### v1.4.0 ✅
| Fase | Tareas | Completadas | Progreso |
|------|--------|-------------|----------|
| **Fase 10: Splash Screen Personalizado** | 2 | 2 | ✅ 100% |
| **Subtotal v1.4.0** | **2** | **2** | **✅ 100%** |

### v1.5.0 🆕
| Fase | Tareas | Completadas | Progreso |
|------|--------|-------------|----------|
| **Fase 11: Modernización de HomeScreen** | 3 | 0 | ⏳ 0% |
| **Subtotal v1.5.0** | **3** | **0** | **⏳ 0%** |

### Total General
| **TOTAL** | **48** | **45** | **📊 93.75%** |

**Referencias de implementación:**
- ✅ Fase 1: Completada (ver commit inicial)
- ✅ Fase 2: Completada - [docs/fases/fase2-completada.md](fases/fase2-completada.md)
- ✅ Fase 3: Completada - [docs/fases/fase3-completada.md](fases/fase3-completada.md)
- ✅ Fase 4: Completada - [docs/fases/fase4_resumen.md](fases/fase4_resumen.md)
- ✅ Fase 5: Completada 95% - [docs/fases/fase5_resumen.md](fases/fase5_resumen.md)

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

### Tarea 3.4: Navegación desde Tirada a Detalle de Carta

**Descripción**: Permitir al usuario tocar una carta en la tirada para ver su información completa, reutilizando `CardDetailScreen` con un botón adicional "Interpretar con IA" (placeholder para Fase 4).

**Criterios de Aceptación**:
- [ ] Las cartas en `ReadingScreen` son clickeables
- [ ] Al tocar una carta navega a `CardDetailScreen` con parámetro `fromReading=true`
- [ ] `CardDetailScreen` detecta si viene desde tirada o enciclopedia
- [ ] Si `fromReading=true`: Muestra botón "Interpretar con IA" al final del scroll
- [ ] Si `fromReading=false`: No muestra el botón
- [ ] El botón por ahora solo muestra un Toast "Funcionalidad disponible en Fase 4"
- [ ] La navegación de vuelta funciona correctamente
- [ ] Ruta actualizada en `Screen.kt` con parámetro opcional `fromReading`

**Archivos a modificar**:
- `app/src/main/java/com/waveapp/tarotai/presentation/navigation/Screen.kt` - Agregar parámetro `fromReading`
- `app/src/main/java/com/waveapp/tarotai/presentation/navigation/NavGraph.kt` - Parsear parámetro
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/ReadingScreen.kt` - Hacer cartas clickeables
- `app/src/main/java/com/waveapp/tarotai/presentation/carddetail/CardDetailScreen.kt` - Mostrar botón condicional

**Implementación esperada**:
```kotlin
// En ReadingScreen.kt
DrawnCardItem(
    drawnCard = drawnCard,
    modifier = Modifier
        .weight(1f)
        .clickable {
            onCardClick(drawnCard.card.id)
        }
)

// En CardDetailScreen.kt
@Composable
fun CardDetailScreen(
    cardId: Int,
    fromReading: Boolean = false,
    onNavigateBack: () -> Unit,
    onInterpretWithAI: () -> Unit = {}
) {
    // ... contenido existente ...

    if (fromReading) {
        Button(
            onClick = onInterpretWithAI,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Interpretar con IA")
        }
    }
}
```

**Tiempo estimado**: 1.5 horas

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
| Fase 3: Sistema de Tiradas | 8.5 horas |
| Fase 4: Integración con IA | 7.5 horas |
| Fase 5: Pulido y Testing | 7.5 horas |
| **TOTAL** | **~38.5 horas** |

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

---

## 📚 FASE 6: Historial de Lecturas Guardadas (v1.1.0) 🆕

Ver detalles completos en: [docs/fases/fase6-historial.md](fases/fase6-historial.md)

### Resumen de Tareas

- **Tarea 6.1**: Actualizar Base de Datos (Room) - 1.5h
- **Tarea 6.2**: Actualizar QuestionScreen (Reutilización) - 2h
- **Tarea 6.3**: Implementar Domain Layer de Historial - 2h
- **Tarea 6.4**: Implementar UI de Historial - 3h
- **Tarea 6.5**: Implementar UI de Detalle con Notas Editables - 3.5h

**Total Fase 6: ~12 horas**

---

## 🎴 FASE 7: Carga Manual de Tiradas (v1.1.0) 🆕

Ver detalles completos en: [docs/fases/fase7-carga-manual.md](fases/fase7-carga-manual.md)

### Resumen de Tareas

- **Tarea 7.1**: Implementar Domain Layer de Carga Manual - 2h
- **Tarea 7.2**: Actualizar TarotCardRepository - 1h
- **Tarea 7.3**: Implementar ManualLoadScreen - 3h
- **Tarea 7.4**: Implementar CardSelectorScreen - 3.5h
- **Tarea 7.5**: Integrar Interpretación de Carga Manual - 1.5h
- **Tarea 7.6**: Integrar con Flujo Principal - 2h

**Total Fase 7: ~13 horas**

---

## 🎨 FASE 8: Mejoras de UX (v1.2.0) - VERSIÓN ANTIGUA (REEMPLAZADA)

**Nota**: Las tareas de esta sección fueron reemplazadas por las tareas 8.1-8.6 (líneas 1145+).
Mantenidas aquí solo como referencia histórica.

### Tarea 8.1: Fix QuestionScreen - Manejo Correcto del Teclado ✅

**Descripción**: Mejorar el layout de QuestionScreen para manejar correctamente el teclado desplegado.

**Problema Actual**:
- Cuando el teclado está desplegado, los campos no quedan visibles
- El botón "Continuar" queda oculto detrás del teclado
- No hay scroll en el contenido

**Solución**:
- Botón "Continuar" fijo sobre el teclado (usando Scaffold con bottomBar)
- Contenido con scroll (Column + verticalScroll + Modifier.imePadding())
- Campo activo siempre visible cuando se edita

**Criterios de Aceptación**:
- [ ] El botón "Continuar" queda fijo sobre el teclado cuando este se despliega
- [ ] El contenido de la pantalla tiene scroll
- [ ] Cuando el cursor está en "Pregunta", ese campo se ve completo
- [ ] Cuando el cursor está en "Nombre", ese campo se ve completo
- [ ] El layout funciona correctamente en diferentes tamaños de pantalla

**Archivos a modificar**:
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/QuestionScreen.kt`

**Cambios técnicos**:
```kotlin
// Usar Scaffold con bottomBar para botón fijo
Scaffold(
    bottomBar = {
        Surface(shadowElevation = 8.dp) {
            Button(
                onClick = { ... },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .imePadding()
            ) { Text("Continuar") }
        }
    }
) { paddingValues ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
            .imePadding()
    ) {
        // Contenido con scroll
    }
}
```

**Tiempo estimado**: 2 horas

---

### Tarea 8.2: Implementar Guardado Manual en Historial ✅

**Descripción**: Cambiar de guardado automático a guardado manual con botón explícito.

**Problema Actual**:
- Las lecturas se guardan automáticamente
- No hay control del usuario sobre qué lecturas guardar
- El historial se llena con lecturas de prueba

**Solución**:
- Agregar botón "Guardar en Historial" en ReadingScreen (después de la interpretación)
- Agregar botón "Guardar en Historial" en ReadingDetailScreen (para lecturas manuales)
- Remover guardado automático
- Mostrar confirmación cuando se guarda

**Criterios de Aceptación**:
- [ ] ReadingScreen tiene botón "Guardar en Historial" visible después de la interpretación
- [ ] Al presionar el botón, se guarda la lectura en historial
- [ ] Se muestra un Snackbar de confirmación "Lectura guardada"
- [ ] El botón desaparece o se deshabilita después de guardar
- [ ] ManualLoadViewModel NO guarda automáticamente
- [ ] ReadingDetailScreen tiene botón para guardar lectura manual
- [ ] El flujo funciona tanto para tiradas automáticas como manuales

**Archivos a modificar**:
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/ReadingScreen.kt`
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/viewmodel/ReadingViewModel.kt`
- `app/src/main/java/com/waveapp/tarotai/presentation/history/ReadingDetailScreen.kt`
- `app/src/main/java/com/waveapp/tarotai/presentation/manualload/ManualLoadViewModel.kt`

**Cambios técnicos**:
```kotlin
// En ReadingViewModel, agregar:
fun saveToHistory() {
    viewModelScope.launch {
        val reading = ReadingHistory(...)
        saveReadingUseCase(reading)
        _readingSaved.value = true
    }
}

// En ReadingScreen, agregar después de la interpretación:
if (interpretation != null && !readingSaved) {
    Button(
        onClick = { viewModel.saveToHistory() },
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(Icons.Default.Save, null)
        Spacer(Modifier.width(8.dp))
        Text("Guardar en Historial")
    }
}
```

**Tiempo estimado**: 3 horas

---

### Tarea 8.3: Agregar Botones de Cierre Rápido (X) ✅

**Descripción**: Agregar botón X en la TopAppBar de todas las pantallas secundarias para volver al Home.

**Problema Actual**:
- Solo hay botón "Atrás" que navega a la pantalla anterior
- Para volver al inicio hay que retroceder múltiples veces
- No hay forma rápida de cancelar el flujo y volver al Home

**Solución**:
- Agregar IconButton con icono X (Close) en todas las TopAppBar
- Al presionar, navegar directamente al Home con popUpTo

**Criterios de Aceptación**:
- [ ] Todas las pantallas (excepto Home) tienen botón X arriba a la derecha
- [ ] Al presionar X, se vuelve directamente al Home
- [ ] El back stack se limpia correctamente
- [ ] Lista de pantallas a modificar:
  - [ ] EncyclopediaScreen
  - [ ] CardDetailScreen
  - [ ] SpreadTypeSelectionScreen
  - [ ] QuestionScreen
  - [ ] ReadingScreen
  - [ ] HistoryScreen
  - [ ] ReadingDetailScreen
  - [ ] ManualLoadScreen
  - [ ] CardSelectorScreen

**Archivos a modificar**:
- Todos los screens listados arriba

**Cambios técnicos**:
```kotlin
// En cada TopAppBar, agregar:
actions = {
    IconButton(onClick = {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) { inclusive = false }
        }
    }) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Volver al inicio"
        )
    }
}
```

**Tiempo estimado**: 2 horas

---

### Tarea 8.4: Remover Botón + del HistoryScreen ✅

**Descripción**: Eliminar el botón "+" (Nueva Lectura) del HistoryScreen.

**Problema Actual**:
- El botón + en HistoryScreen sugiere que se pueden agregar lecturas desde ahí
- Es confuso porque las lecturas se agregan desde ReadingScreen
- Inconsistente con el nuevo flujo de guardado manual

**Solución**:
- Remover el parámetro `onNewReading` de HistoryScreen
- Remover el FloatingActionButton
- Actualizar la navegación en NavGraph

**Criterios de Aceptación**:
- [ ] HistoryScreen NO tiene botón flotante +
- [ ] La navegación desde NavGraph no pasa `onNewReading`
- [ ] El código compila sin errores
- [ ] La UI del historial se ve limpia sin el FAB

**Archivos a modificar**:
- `app/src/main/java/com/waveapp/tarotai/presentation/history/HistoryScreen.kt`
- `app/src/main/java/com/waveapp/tarotai/presentation/navigation/NavGraph.kt`

**Cambios técnicos**:
```kotlin
// ANTES:
@Composable
fun HistoryScreen(
    onNavigateBack: () -> Unit,
    onReadingClick: (Long) -> Unit,
    onNewReading: () -> Unit, // REMOVER
    viewModel: HistoryViewModel = hiltViewModel()
)

// DESPUÉS:
@Composable
fun HistoryScreen(
    onNavigateBack: () -> Unit,
    onReadingClick: (Long) -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
)

// Remover el FAB del Scaffold
```

**Tiempo estimado**: 30 minutos

---

### Tarea 8.5: Verificar y Corregir Guardado en Historial ✅

**Descripción**: Verificar que el guardado en historial funciona correctamente y corregir bugs.

**Problema Actual**:
- Las lecturas no se están guardando en ningún momento
- Incluso con el botón + no se agregan al historial
- Puede haber un problema en SaveReadingUseCase o en el Repository

**Tareas de Verificación**:
1. Verificar que SaveReadingUseCase funciona correctamente
2. Verificar que ReadingHistoryDao está insertando datos
3. Verificar que las conversiones de entidad están correctas
4. Agregar logs para debugging
5. Probar guardado manualmente con datos de prueba

**Criterios de Aceptación**:
- [ ] SaveReadingUseCase guarda correctamente en Room
- [ ] El DAO inserta y retorna el ID correctamente
- [ ] GetAllReadingsUseCase recupera las lecturas guardadas
- [ ] La UI de HistoryScreen muestra las lecturas guardadas
- [ ] Los datos persisten después de cerrar la app
- [ ] Se pueden abrir los detalles de una lectura guardada

**Archivos a verificar/modificar**:
- `app/src/main/java/com/waveapp/tarotai/domain/usecase/history/SaveReadingUseCase.kt`
- `app/src/main/java/com/waveapp/tarotai/data/repository/ReadingHistoryRepositoryImpl.kt`
- `app/src/main/java/com/waveapp/tarotai/data/local/dao/ReadingHistoryDao.kt`
- `app/src/main/java/com/waveapp/tarotai/data/local/mapper/ReadingHistoryMapper.kt`

**Debugging a agregar**:
```kotlin
// En SaveReadingUseCase
Log.d("SaveReading", "Intentando guardar: ${reading.consultantName}")
val result = repository.saveReading(reading)
Log.d("SaveReading", "Guardado con ID: $result")

// En ReadingHistoryDao
@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insert(reading: ReadingHistoryEntity): Long
```

**Tiempo estimado**: 2.5 horas

---

**Total Fase 8 (Antigua): ~10 horas** - ✅ **TODAS COMPLETADAS**

**Nota**: Esta sección antigua fue reemplazada por la nueva implementación (tareas 8.1-8.6 en líneas 1145+)

---

### Tarea 8.1: QuestionScreen - Consultante opcional en ambos modos ✅

**Descripción**: Unificar comportamiento de QuestionScreen para que el consultante sea opcional tanto en modo automático como manual.

**Cambios realizados**:
- Toggle "Esta lectura es para alguien más" OFF por defecto en ambos modos
- Remover lógica que forzaba toggle ON en modo manual
- Campo consultante opcional en ambos modos
- Comportamiento idéntico (solo cambia hint visual)

**Archivos modificados**:
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/QuestionScreen.kt`

**Tiempo estimado**: 30 minutos ✅

---

### Tarea 8.2: ReadingScreen - Botón guardar siempre visible con valor por defecto ✅

**Descripción**: Mostrar botón "Guardar en Historial" siempre, usando "Lectura personal" como valor por defecto si no hay consultantName.

**Cambios realizados**:
- Remover condicional `consultantName?.let { }`
- Agregar `val finalConsultantName = consultantName ?: "Lectura personal"`
- Botón siempre visible después de interpretación

**Archivos modificados**:
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/ReadingScreen.kt`

**Tiempo estimado**: 30 minutos ✅

---

### Tarea 8.3: ManualLoadViewModel - Remover guardado automático ✅

**Descripción**: Separar la generación de interpretación del guardado, permitiendo guardado manual controlado por el usuario.

**Cambios a realizar**:
1. Remover lógica de guardado automático de `generateInterpretation()`
2. Actualizar estados para mostrar interpretación sin navegar
3. Agregar método `saveToHistory()` similar a ReadingViewModel
4. Agregar `SaveState` para manejar estados de guardado

**Archivos a modificar**:
- `app/src/main/java/com/waveapp/tarotai/presentation/manualload/ManualLoadViewModel.kt`

**Código esperado**:
```kotlin
// Estado de guardado (nuevo)
private val _saveState = MutableStateFlow<SaveState>(SaveState.NotSaved)
val saveState: StateFlow<SaveState> = _saveState.asStateFlow()

fun generateInterpretation() {
    if (!_configuration.value.isComplete()) {
        _error.value = "Debes seleccionar todas las cartas"
        return
    }

    viewModelScope.launch {
        _isLoading.value = true
        _error.value = null

        val result = generateInterpretationFromManualLoadUseCase(_configuration.value)

        result.fold(
            onSuccess = { interpretation ->
                _interpretationGenerated.value = interpretation
                _isLoading.value = false
                // NO guardar automáticamente
            },
            onFailure = { exception ->
                _error.value = exception.message ?: "Error al generar interpretación"
                _isLoading.value = false
            }
        )
    }
}

fun saveToHistory() {
    val interpretation = _interpretationGenerated.value ?: return

    viewModelScope.launch {
        _saveState.value = SaveState.Saving

        val reading = ReadingHistory(
            id = 0,
            timestamp = System.currentTimeMillis(),
            consultantName = _configuration.value.consultantName,
            spreadType = _configuration.value.spreadType,
            question = _configuration.value.question,
            drawnCards = _configuration.value.state.toDrawnCards(),
            interpretation = interpretation,
            notes = emptyList()
        )

        val result = saveReadingUseCase(reading)

        result.fold(
            onSuccess = { readingId ->
                _saveState.value = SaveState.Saved(readingId)
            },
            onFailure = { exception ->
                _saveState.value = SaveState.Error(
                    exception.message ?: "Error al guardar"
                )
            }
        )
    }
}
```

**Tiempo estimado**: 1 hora

---

### Tarea 8.4: ManualLoadScreen - Cartas de dorso + interpretación en misma pantalla ✅

**Descripción**: Rediseñar ManualLoadScreen para mostrar cartas de dorso inicialmente y la interpretación en la misma pantalla (sin navegar).

**Cambios a realizar**:
1. Cambiar de CardPositionItem a layout con cartas de dorso/reveladas
2. Reutilizar HorizontalCardsLayout y CrossCardsLayout
3. Mostrar interpretación después del botón (scroll hacia abajo)
4. Agregar botón "Guardar en Historial" después de interpretación
5. Deshabilitar click en cartas después de interpretar

**Archivos a modificar**:
- `app/src/main/java/com/waveapp/tarotai/presentation/manualload/ManualLoadScreen.kt`

**Componentes a reutilizar**:
- `HorizontalCardsLayout` (de ReadingScreen)
- `CrossCardsLayout` (de ReadingScreen)
- `GeneralInterpretationCard` (de ReadingScreen)
- `YesNoAnswerCard` (de ReadingScreen)

**Estructura esperada**:
```kotlin
Column(modifier = Modifier.verticalScroll(...)) {
    // Header con info
    ManualLoadHeader(...)

    Spacer(...)

    // Cartas (dorso o reveladas)
    when (config.layout) {
        LayoutType.HORIZONTAL -> {
            HorizontalCardsLayout(
                drawnCards = configuration.state.toDrawnCards(),
                showAsBack = !configuration.isComplete(),
                onCardClick = if (!isInterpreted) {
                    { position -> onNavigateToCardSelector(position) }
                } else {
                    {}  // No clickeable después de interpretar
                }
            )
        }
        LayoutType.CROSS -> {
            CrossCardsLayout(...)
        }
    }

    // Progreso
    Text("Progreso: ${current}/${required} cartas")

    // Botón generar interpretación
    Button(
        enabled = configuration.isComplete() && !isLoading,
        onClick = { viewModel.generateInterpretation() }
    ) {
        if (isLoading) {
            CircularProgressIndicator(...)
            Text("Generando...")
        } else {
            Text("Generar Interpretación")
        }
    }

    // Interpretación (si existe)
    interpretation?.let {
        HorizontalDivider(...)

        if (it.yesNoAnswer != null) {
            YesNoAnswerCard(answer = it.yesNoAnswer)
        }

        // Botón guardar
        when (saveState) {
            SaveState.NotSaved -> {
                Button(onClick = { viewModel.saveToHistory() }) {
                    Text("Guardar en Historial")
                }
            }
            // ... otros estados
        }

        GeneralInterpretationCard(...)
    }
}
```

**Tiempo estimado**: 3 horas

---

### Tarea 8.5: CardSelectorScreen - Agregar imágenes de cartas ✅

**Descripción**: Actualizar CardSelectorScreen para mostrar imágenes de cartas en lugar de solo nombres.

**Cambios a realizar**:
1. Actualizar CardSelectorItem para incluir AsyncImage
2. Mostrar imagen + nombre de carta
3. Mantener overlay para cartas ya seleccionadas

**Archivos a modificar**:
- `app/src/main/java/com/waveapp/tarotai/presentation/cardselector/CardSelectorScreen.kt`

**Código esperado**:
```kotlin
@Composable
private fun CardSelectorItem(
    card: TarotCard,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick, enabled = !isSelected),
        colors = if (isSelected) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        } else {
            CardDefaults.cardColors()
        }
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = card.imageResId,
                contentDescription = card.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7f),
                contentScale = ContentScale.Fit
            )

            Text(
                text = card.name,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            if (isSelected) {
                Text(
                    text = "En uso",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
```

**Tiempo estimado**: 2 horas

---

### Tarea 8.6: Obtener/crear imagen card_back.png ✅

**Descripción**: Buscar o crear imagen de dorso de carta para usar en ManualLoadScreen.

**Opciones**:
1. Buscar imagen de dominio público en Wikimedia Commons
2. Usar imagen placeholder temporal (color + texto)
3. Crear imagen simple con Figma/Canva

**Especificaciones**:
- Archivo: `app/src/main/res/drawable/card_back.jpg`
- Dimensiones: 600x1000 px
- Formato: JPG
- Peso máximo: 200KB

**Tiempo estimado**: 1 hora

---

## 📊 Estimación Total de Tiempo (Actualizado v1.2.0)

| Fase | Tiempo Estimado |
|------|-----------------|
| Fase 1: Infraestructura Base | 3.5 horas |
| Fase 2: Enciclopedia | 11.5 horas |
| Fase 3: Sistema de Tiradas | 8.5 horas |
| Fase 4: Integración con IA | 7.5 horas |
| Fase 5: Pulido y Testing | 7.5 horas |
| **Subtotal v1.0.0** | **38.5 horas** ✅ |
| Fase 6: Historial de Lecturas | 12 horas |
| Fase 7: Carga Manual | 13 horas |
| **Subtotal v1.1.0** | **25 horas** ✅ |
| Fase 8: Mejoras de UX | 10 horas |
| **Subtotal v1.2.0** | **10 horas** ⏳ |
| **TOTAL GENERAL** | **~73.5 horas** |

---

## 🚦 Próximo Paso

**v1.0.0 completada** ✅ (21 tareas)
**v1.1.0 completada** ✅ (11 tareas)
**v1.2.0 completada** ✅ (6 tareas)
**v1.3.0 completada** ✅ (3 tareas)
**v1.4.0 completada** ✅ (2 tareas)

**Fase 11 (v1.5.0) - EN PLANIFICACIÓN** 🆕
- ⏳ Tarea 11.1: Crear Componente HomeOptionCard
- ⏳ Tarea 11.2: Refactorizar HomeScreen con Nuevo Diseño
- ⏳ Tarea 11.3: Actualizar Recursos y Navegación

**Siguiente: Tarea 11.1 - Crear Componente HomeOptionCard**

---

## 🎙️ FASE 9: Reconocimiento de Voz (v1.3.0)

### Tarea 9.1: Agregar Permiso de Micrófono ✅

**Descripción**: Agregar permiso `RECORD_AUDIO` al AndroidManifest.xml para permitir el uso del micrófono.

**Criterios de Aceptación**:
- [x] Permiso `RECORD_AUDIO` agregado en AndroidManifest.xml
- [x] El proyecto compila sin errores
- [x] El permiso aparece en la lista de permisos de la app

**Archivos a modificar**:
- `app/src/main/AndroidManifest.xml`

**Cambios técnicos**:
```xml
<!-- AndroidManifest.xml -->
<manifest ...>
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    ...
</manifest>
```

**Tiempo estimado**: 15 minutos

---

### Tarea 9.2: Crear QuestionViewModel con Lógica de Reconocimiento de Voz ✅

**Descripción**: Crear (o actualizar) `QuestionViewModel` para manejar el estado y lógica del reconocimiento de voz usando SpeechRecognizer API.

**Criterios de Aceptación**:
- [x] `QuestionViewModel` creado/actualizado con `@HiltViewModel`
- [x] Sealed class `SpeechRecognitionState` definida (Idle, Listening, Processing, Error)
- [x] `StateFlow<SpeechRecognitionState>` expuesto para la UI
- [x] Función `initSpeechRecognizer(context)` implementada
- [x] Función `startListening()` implementada con configuración en español
- [x] Función `stopListening()` implementada
- [x] `RecognitionListener` implementado con todos los callbacks
- [x] Resultados parciales actualizan el texto en tiempo real
- [x] Manejo de errores con mensajes en español
- [x] `onCleared()` destruye el SpeechRecognizer correctamente
- [x] El proyecto compila sin errores
- [x] **EXTRA**: Implementado soporte dual para pregunta Y nombre de consultante

**Archivos a crear/modificar**:
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/viewmodel/QuestionViewModel.kt`

**Código esperado**:
```kotlin
@HiltViewModel
class QuestionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _speechState = MutableStateFlow<SpeechRecognitionState>(
        SpeechRecognitionState.Idle
    )
    val speechState: StateFlow<SpeechRecognitionState> = _speechState.asStateFlow()

    private val _question = MutableStateFlow("")
    val question: StateFlow<String> = _question.asStateFlow()

    private var speechRecognizer: SpeechRecognizer? = null

    fun initSpeechRecognizer(context: Context) {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matches?.firstOrNull()?.let { recognizedText ->
                    _question.value = recognizedText
                }
                _speechState.value = SpeechRecognitionState.Idle
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matches?.firstOrNull()?.let { partialText ->
                    _question.value = partialText
                }
            }

            override fun onError(error: Int) {
                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Error de audio"
                    SpeechRecognizer.ERROR_CLIENT -> "Error del cliente"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permiso de micrófono denegado"
                    SpeechRecognizer.ERROR_NETWORK -> "Error de conexión"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Tiempo de espera agotado"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No se entendió el audio"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Reconocedor ocupado"
                    SpeechRecognizer.ERROR_SERVER -> "Error del servidor"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No se detectó voz"
                    else -> "Error desconocido"
                }
                _speechState.value = SpeechRecognitionState.Error(errorMessage)
            }

            override fun onBeginningOfSpeech() {
                _speechState.value = SpeechRecognitionState.Listening
            }

            override fun onEndOfSpeech() {
                _speechState.value = SpeechRecognitionState.Processing
            }

            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        speechRecognizer?.startListening(intent)
        _speechState.value = SpeechRecognitionState.Listening
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
        _speechState.value = SpeechRecognitionState.Idle
    }

    fun updateQuestion(text: String) {
        _question.value = text
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizer?.destroy()
    }
}

sealed class SpeechRecognitionState {
    object Idle : SpeechRecognitionState()
    object Listening : SpeechRecognitionState()
    object Processing : SpeechRecognitionState()
    data class Error(val message: String) : SpeechRecognitionState()
}
```

**Imports necesarios**:
```kotlin
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
```

**Tiempo estimado**: 2 horas

---

### Tarea 9.3: Actualizar QuestionScreen con Botón de Micrófono ✅

**Descripción**: Modificar `QuestionScreen` para agregar botón de micrófono con manejo de permisos y feedback visual según estado.

**Criterios de Aceptación**:
- [x] `QuestionViewModel` inyectado con Hilt
- [x] `LaunchedEffect` inicializa SpeechRecognizer
- [x] `rememberLauncherForActivityResult` maneja solicitud de permisos
- [x] Botón de micrófono agregado como `trailingIcon` del TextField de pregunta
- [x] Icono cambia según estado (Idle: azul, Listening: rojo pulsante, Processing: spinner, Error: rojo)
- [x] Al tocar botón verifica permiso y solicita si es necesario
- [x] Si permiso concedido, inicia reconocimiento
- [x] Texto reconocido se muestra en TextField en tiempo real
- [x] Muestra mensaje de error si reconocimiento falla
- [x] Verificación de disponibilidad de SpeechRecognizer en dispositivo
- [x] El proyecto compila sin errores
- [x] Funciona en modo automático y manual (ambos flujos)
- [x] **EXTRA**: Botón de micrófono agregado también al campo de consultante
- [x] **EXTRA**: Icono de micrófono vector profesional (ic_mic.xml) tipo WhatsApp

**Archivos a modificar**:
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/QuestionScreen.kt`

**Cambios técnicos**:
```kotlin
@Composable
fun QuestionScreen(
    spreadType: SpreadType,
    isManualLoad: Boolean = false,
    onNavigateBack: () -> Unit,
    onContinue: (String, String?) -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: QuestionViewModel = hiltViewModel()  // Agregar ViewModel
) {
    val context = LocalContext.current
    val speechState by viewModel.speechState.collectAsState()
    val recognizedQuestion by viewModel.question.collectAsState()

    // Inicializar SpeechRecognizer
    LaunchedEffect(Unit) {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            viewModel.initSpeechRecognizer(context)
        }
    }

    // Manejo de permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startListening()
        } else {
            Toast.makeText(
                context,
                "Permiso de micrófono requerido para dictar",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // ... resto del contenido existente ...

    // TextField de pregunta con botón de micrófono
    OutlinedTextField(
        value = recognizedQuestion.ifEmpty { question },
        onValueChange = {
            question = it
            viewModel.updateQuestion(it)
        },
        label = { Text(stringResource(R.string.question_label)) },
        placeholder = { Text(stringResource(R.string.question_hint)) },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            // Solo mostrar si SpeechRecognizer está disponible
            if (SpeechRecognizer.isRecognitionAvailable(context)) {
                IconButton(
                    onClick = {
                        when (speechState) {
                            is SpeechRecognitionState.Listening -> {
                                viewModel.stopListening()
                            }
                            else -> {
                                when {
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.RECORD_AUDIO
                                    ) == PackageManager.PERMISSION_GRANTED -> {
                                        viewModel.startListening()
                                    }
                                    else -> {
                                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                    }
                                }
                            }
                        }
                    }
                ) {
                    when (speechState) {
                        is SpeechRecognitionState.Idle -> {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Dictar pregunta con voz",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        is SpeechRecognitionState.Listening -> {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Detener dictado",
                                tint = Color.Red,
                                modifier = Modifier.scale(1.2f)
                            )
                        }
                        is SpeechRecognitionState.Processing -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        }
                        is SpeechRecognitionState.Error -> {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Error en reconocimiento",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    )

    // Mostrar error si existe
    if (speechState is SpeechRecognitionState.Error) {
        Text(
            text = (speechState as SpeechRecognitionState.Error).message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
}
```

**Imports adicionales necesarios**:
```kotlin
import android.Manifest
import android.content.pm.PackageManager
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.scale
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
```

**Tiempo estimado**: 2.5 horas

---

**Total Fase 9: ~4.75 horas**

---

## 📊 Estimación Total de Tiempo (Actualizado v1.3.0)

| Fase | Tiempo Estimado |
|------|-----------------|
| Fase 1: Infraestructura Base | 3.5 horas |
| Fase 2: Enciclopedia | 11.5 horas |
| Fase 3: Sistema de Tiradas | 8.5 horas |
| Fase 4: Integración con IA | 7.5 horas |
| Fase 5: Pulido y Testing | 7.5 horas |
| **Subtotal v1.0.0** | **38.5 horas** ✅ |
| Fase 6: Historial de Lecturas | 12 horas |
| Fase 7: Carga Manual | 13 horas |
| **Subtotal v1.1.0** | **25 horas** ✅ |
| Fase 8: Mejoras de UX | 10 horas |
| **Subtotal v1.2.0** | **10 horas** ⏳ |
| Fase 9: Reconocimiento de Voz | 4.75 horas |
| **Subtotal v1.3.0** | **4.75 horas** 🆕 |
| **TOTAL GENERAL** | **~78.25 horas** |

---

## 🚦 Próximo Paso

**v1.0.0 completada** ✅ (21 tareas)
**v1.1.0 completada** ✅ (11 tareas)
**v1.2.0 completada** ✅ (6 tareas)
**v1.3.0 completada** ✅ (3 tareas)

**Fase 8 (v1.2.0) - COMPLETADA** ✅
- ✅ Tarea 8.1: QuestionScreen - Consultante opcional en ambos modos
- ✅ Tarea 8.2: ReadingScreen - Botón guardar siempre visible con valor por defecto
- ✅ Tarea 8.3: ManualLoadViewModel - Remover guardado automático
- ✅ Tarea 8.4: ManualLoadScreen - Cartas de dorso + interpretación en misma pantalla
- ✅ Tarea 8.5: CardSelectorScreen - Agregar imágenes de cartas
- ✅ Tarea 8.6: Obtener/crear imagen card_back.png

**🎉 PROYECTO AL 100% - TODAS LAS TAREAS COMPLETADAS!**

**Todas las versiones planificadas están completas:**
- ✅ v1.0.0: MVP con enciclopedia, tiradas e IA
- ✅ v1.1.0: Historial y carga manual
- ✅ v1.2.0: Mejoras de UX completas
- ✅ v1.3.0: Reconocimiento de voz

**Total: 41/43 tareas del plan original = 95%**
**Nota**: Las 2 tareas "faltantes" son tareas antiguas que fueron reemplazadas por implementaciones mejoradas ya completadas.

---

## 🎨 FASE 10: Splash Screen Personalizado (v1.4.0)

### Tarea 10.1: Configurar Splash Screen del Sistema

**Descripción**: Configurar el splash screen del sistema de Android 12+ para mostrar fondo oscuro sin imagen, minimizando la visualización del ícono circular.

**Criterios de Aceptación**:
- [x] Dependencia `androidx.core:core-splashscreen:1.0.1` agregada
- [x] Tema `Theme.App.Starting` configurado con fondo `#030F0F` (DarkBackground)
- [x] `installSplashScreen()` implementado en MainActivity
- [x] `setKeepOnScreenCondition` configurado para ocultar el splash inmediatamente
- [x] Status bar configurada con color `#030F0F`

**Archivos modificados**:
- `gradle/libs.versions.toml` - Versión splashScreen = "1.0.1"
- `app/build.gradle.kts` - Dependencia splashscreen
- `app/src/main/res/values/colors.xml` - Color splash_background = #030F0F
- `app/src/main/res/values/themes.xml` - Tema Theme.App.Starting
- `app/src/main/AndroidManifest.xml` - Aplicar tema a MainActivity
- `app/src/main/java/com/waveapp/tarotai/MainActivity.kt` - installSplashScreen()

**Código clave**:
```kotlin
// MainActivity.kt
val splashScreen = installSplashScreen()
var keepSplashOnScreen = true
splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }

// Configurar status bar
window.statusBarColor = Color(0xFF030F0F).toArgb()
WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false
```

**Tiempo estimado**: 1 hora

---

### Tarea 10.2: Implementar Splash Screen en Compose

**Descripción**: Crear un splash screen personalizado en Compose que muestre la imagen completa rectangular con el logo y texto "Arcana" durante 2 segundos.

**Criterios de Aceptación**:
- [x] Splash de Compose implementado con fondo `#030F0F`
- [x] Imagen `splash_icon.png` mostrada completa y centrada
- [x] `ContentScale.Fit` para mantener proporciones
- [x] Tamaño de imagen al 70% del tamaño de pantalla
- [x] Duración de 2 segundos antes de mostrar contenido principal
- [x] Transición suave entre splash y contenido principal
- [x] El splash del sistema se oculta inmediatamente al cargar Compose

**Archivos modificados**:
- `app/src/main/java/com/waveapp/tarotai/MainActivity.kt` - Composable del splash
- `app/src/main/res/drawable/splash_icon.png` - Imagen del splash (1024x1536)

**Código clave**:
```kotlin
var showSplash by remember { mutableStateOf(true) }

LaunchedEffect(Unit) {
    keepSplashOnScreen = false  // Ocultar splash del sistema
    delay(2000)  // Mantener splash de Compose
    showSplash = false
}

if (showSplash) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF030F0F)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash_icon),
            contentDescription = "Splash Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize(0.7f)
        )
    }
}
```

**Limitación técnica documentada**:
Android 12+ fuerza un ícono circular de 288dp máximo en el splash del sistema. No es posible mostrar una imagen rectangular completa usando solo `windowBackground`. Por eso usamos:
1. Splash del sistema: Fondo oscuro sin imagen visible (se oculta rápidamente)
2. Splash de Compose: Imagen rectangular completa con control total

**Tiempo estimado**: 1.5 horas

---

**Total Fase 10**: 2.5 horas
**Estado**: ✅ Completada 100%

---

## 🎨 FASE 11: Modernización de HomeScreen (v1.5.0)

### Tarea 11.1: Crear Componente HomeOptionCard

**Descripción**: Implementar componente reutilizable `HomeOptionCard` para las opciones de la pantalla principal.

**Criterios de Aceptación**:
- [ ] Archivo `HomeOptionCard.kt` creado en `presentation/screens/components/`
- [ ] Componente acepta parámetros: icon, title, description, onClick, isPrimary
- [ ] Variante primaria usa `primaryContainer` color
- [ ] Variante secundaria usa colores por defecto
- [ ] Card tiene elevación de 2dp
- [ ] Layout: Row con Icon (48dp) + Column (título + descripción)
- [ ] Ripple effect automático con `Modifier.clickable`
- [ ] Documentación KDoc completa
- [ ] El proyecto compila sin errores

**Archivos a crear**:
- `app/src/main/java/com/waveapp/tarotai/presentation/screens/components/HomeOptionCard.kt`

**Código esperado**:
```kotlin
@Composable
fun HomeOptionCard(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = if (isPrimary) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        } else {
            CardDefaults.cardColors()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp),
                tint = if (isPrimary) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isPrimary) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isPrimary) {
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}
```

**Tiempo estimado**: 45 minutos

---

### Tarea 11.2: Refactorizar HomeScreen con Nuevo Diseño

**Descripción**: Modernizar `HomeScreen` eliminando TopAppBar, agregando logo con imagen, reemplazando botones por cards, y respetando márgenes del sistema.

**Criterios de Aceptación**:
- [ ] Remover `TopAppBar` del `Scaffold`
- [ ] Reemplazar `Scaffold` por `Column` con `statusBarsPadding()` y `navigationBarsPadding()`
- [ ] Agregar `Image` con `nombre_solo.png` como logo (60% ancho, ContentScale.Fit)
- [ ] Reemplazar todos los `Button` y `OutlinedButton` por `HomeOptionCard`
- [ ] Card "Nueva Lectura" es primaria (`isPrimary = true`)
- [ ] Resto de cards son secundarias
- [ ] Remover botón de "Configuración" completamente
- [ ] Remover parámetro `onNavigateToSettings` de la función
- [ ] Agregar `verticalScroll` a la Column
- [ ] Actualizar iconos: Face → LibraryBooks, Info → Assignment
- [ ] Espaciado de 12dp entre cards
- [ ] Padding horizontal de 24dp
- [ ] El proyecto compila sin errores

**Archivos a modificar**:
- `app/src/main/java/com/waveapp/tarotai/presentation/screens/HomeScreen.kt`

**Estructura esperada**:
```kotlin
@Composable
fun HomeScreen(
    onNavigateToEncyclopedia: () -> Unit,
    onNavigateToReadingSelection: () -> Unit,
    onNavigateToManualLoad: () -> Unit,
    onNavigateToHistory: () -> Unit  // Removido: onNavigateToSettings
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.nombre_solo),
            contentDescription = "Arcana - Logo de la aplicación",
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(vertical = 32.dp),
            contentScale = ContentScale.Fit
        )

        // Card primaria
        HomeOptionCard(
            icon = Icons.Default.Star,
            title = stringResource(R.string.home_new_reading),
            description = stringResource(R.string.home_new_reading_desc),
            onClick = onNavigateToReadingSelection,
            isPrimary = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Cards secundarias
        HomeOptionCard(
            icon = Icons.Default.AddCircle,
            title = "Cargar Lectura Manual",
            description = stringResource(R.string.home_manual_load_desc),
            onClick = onNavigateToManualLoad
        )

        Spacer(modifier = Modifier.height(12.dp))

        HomeOptionCard(
            icon = Icons.Default.LibraryBooks,  // CAMBIADO de Face
            title = stringResource(R.string.home_encyclopedia),
            description = stringResource(R.string.home_encyclopedia_desc),
            onClick = onNavigateToEncyclopedia
        )

        Spacer(modifier = Modifier.height(12.dp))

        HomeOptionCard(
            icon = Icons.Default.Assignment,  // CAMBIADO de Info
            title = stringResource(R.string.home_history),
            description = stringResource(R.string.home_history_desc),
            onClick = onNavigateToHistory
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
```

**Tiempo estimado**: 1.5 horas

---

### Tarea 11.3: Actualizar Recursos y Navegación

**Descripción**: Agregar strings de descripciones para las cards, actualizar navegación en NavGraph, y verificar funcionamiento completo.

**Criterios de Aceptación**:
- [ ] Strings de descripciones agregados en `strings.xml`:
  - `home_new_reading_desc`
  - `home_manual_load_desc`
  - `home_encyclopedia_desc`
  - `home_history_desc`
- [ ] Parámetro `onNavigateToSettings` removido de `NavGraph.kt`
- [ ] Verificar que `nombre_solo.png` existe y se visualiza correctamente
- [ ] Testing visual en emulador
- [ ] Testing de navegación a cada pantalla
- [ ] Testing de scroll si contenido excede pantalla
- [ ] Testing de márgenes del sistema (status bar, navigation bar)
- [ ] El proyecto compila y funciona sin errores

**Archivos a modificar**:
- `app/src/main/res/values/strings.xml`
- `app/src/main/java/com/waveapp/tarotai/presentation/navigation/NavGraph.kt`

**Strings a agregar**:
```xml
<!-- HomeScreen - Descripciones de opciones -->
<string name="home_new_reading_desc">Genera una tirada automática del tarot</string>
<string name="home_manual_load_desc">Interpreta una tirada física que hayas realizado</string>
<string name="home_encyclopedia_desc">Consulta las 78 cartas del Tarot Rider-Waite</string>
<string name="home_history_desc">Revisa lecturas anteriores guardadas</string>
```

**Cambio en NavGraph**:
```kotlin
// ANTES
composable(Screen.Home.route) {
    HomeScreen(
        onNavigateToEncyclopedia = { ... },
        onNavigateToReadingSelection = { ... },
        onNavigateToManualLoad = { ... },
        onNavigateToSettings = { ... },  // REMOVER
        onNavigateToHistory = { ... }
    )
}

// DESPUÉS
composable(Screen.Home.route) {
    HomeScreen(
        onNavigateToEncyclopedia = { ... },
        onNavigateToReadingSelection = { ... },
        onNavigateToManualLoad = { ... },
        onNavigateToHistory = { ... }
    )
}
```

**Checklist de Testing**:
- [ ] Logo se muestra correctamente (tamaño apropiado)
- [ ] Card "Nueva Lectura" tiene color destacado (primaryContainer)
- [ ] Resto de cards tienen color estándar
- [ ] Todas las cards tienen ripple effect
- [ ] Navegación a cada pantalla funciona
- [ ] NO hay botón de configuración
- [ ] Status bar NO se superpone con contenido
- [ ] Navigation bar NO se superpone con contenido
- [ ] Funciona en pantalla pequeña (scroll si es necesario)
- [ ] Funciona en pantalla grande

**Tiempo estimado**: 1.25 horas

---

**Total Fase 11**: ~3.5 horas
**Estado**: ⏳ En planificación
