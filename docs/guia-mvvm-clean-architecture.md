# Guía: MVVM + Clean Architecture para TarotAI

> **Documento educativo para entender la arquitectura del proyecto**
> Léelo mientras trabajamos en las siguientes fases

---

## 📚 ¿Qué es MVVM?

**MVVM = Model-View-ViewModel**

Es un patrón de arquitectura que separa tu código en 3 capas:

### 1. **View (Vista)**
- **QUÉ ES**: La interfaz que ve el usuario (pantallas, botones, textos)
- **EN TAROTAI**: Jetpack Compose (archivos en `presentation/*/ui/`)
- **RESPONSABILIDAD**: Mostrar datos y capturar acciones del usuario
- **NO HACE**: No tiene lógica de negocio, solo muestra lo que el ViewModel le dice

```kotlin
// Ejemplo: Pantalla que muestra una lista de cartas
@Composable
fun EncyclopediaScreen(viewModel: EncyclopediaViewModel) {
    val cards by viewModel.cardsState.collectAsState()  // Observa el estado

    when (cards) {
        is UiState.Loading -> LoadingSpinner()          // Muestra cargando
        is UiState.Success -> CardList(cards.data)      // Muestra las cartas
        is UiState.Error -> ErrorMessage(cards.message) // Muestra error
    }
}
```

### 2. **ViewModel (Modelo de Vista)**
- **QUÉ ES**: El "cerebro" que conecta la Vista con los datos
- **EN TAROTAI**: Clases que terminan en `ViewModel` (archivos en `presentation/*/viewmodel/`)
- **RESPONSABILIDAD**:
  - Gestionar el estado de la UI (qué se muestra)
  - Llamar a los Use Cases (lógica de negocio)
  - Manejar eventos del usuario (clicks, entradas de texto)
- **NO HACE**: No sabe nada de Android, no accede directamente a la base de datos

```kotlin
// Ejemplo: ViewModel que gestiona la enciclopedia
@HiltViewModel
class EncyclopediaViewModel @Inject constructor(
    private val getAllCardsUseCase: GetAllCardsUseCase  // Inyección de dependencias
) : ViewModel() {

    // Estado: privado para modificar (_), público para leer
    private val _cardsState = MutableStateFlow<UiState<List<TarotCard>>>(UiState.Idle)
    val cardsState: StateFlow<UiState<List<TarotCard>>> = _cardsState.asStateFlow()

    // Función: cargar cartas
    fun loadCards() {
        viewModelScope.launch {  // Coroutine: tarea asíncrona
            _cardsState.value = UiState.Loading  // Cambia estado a "cargando"

            getAllCardsUseCase()  // Llama al caso de uso
                .onSuccess { cards ->
                    _cardsState.value = UiState.Success(cards)  // Éxito
                }
                .onFailure { error ->
                    _cardsState.value = UiState.Error(error.message ?: "Error")  // Error
                }
        }
    }
}
```

### 3. **Model (Modelo)**
- **QUÉ ES**: Los datos y la lógica de negocio
- **EN TAROTAI**: Data classes, Use Cases, Repositorios
- **RESPONSABILIDAD**: Representar los datos de la app
- **NO HACE**: No sabe nada de UI

```kotlin
// Ejemplo: Modelo de una carta
data class TarotCard(
    val id: Int,
    val name: String,
    val arcanaType: ArcanaType,
    val generalMeaning: String
)
```

---

## 🏛️ ¿Qué es Clean Architecture?

**Clean Architecture** divide tu código en **3 capas independientes**:

```
┌─────────────────────────────────┐
│      PRESENTATION               │  ← UI y ViewModels
│  (Capa de Presentación)         │     Lo que VE el usuario
└───────────┬─────────────────────┘
            │
            ↓ Llama a Use Cases

┌───────────▼─────────────────────┐
│         DOMAIN                   │  ← Lógica de negocio
│  (Capa de Dominio)               │     Las REGLAS de la app
│  - Use Cases                     │
│  - Modelos de dominio            │
│  - Interfaces de repositorios    │
└───────────┬─────────────────────┘
            │
            ↓ Llama a Repositorios

┌───────────▼─────────────────────┐
│          DATA                    │  ← Fuentes de datos
│  (Capa de Datos)                 │     De DÓNDE vienen los datos
│  - Room Database                 │
│  - API de Claude                 │
│  - Implementación de repositorios│
└─────────────────────────────────┘
```

### **Regla de Oro**: Las flechas solo van hacia abajo

- ✅ Presentation puede usar Domain
- ✅ Domain puede usar Data (interfaces)
- ❌ Data NO puede usar Presentation
- ❌ Domain NO conoce detalles de Data (solo interfaces)

---

## 🔄 Flujo Completo: Un Ejemplo Real

**Escenario**: El usuario abre la enciclopedia de cartas

### Paso 1: Usuario toca botón "Ver Enciclopedia"
```kotlin
// UI - EncyclopediaScreen.kt
Button(onClick = { viewModel.loadCards() }) {
    Text("Ver Enciclopedia")
}
```

### Paso 2: ViewModel recibe la acción
```kotlin
// PRESENTATION - EncyclopediaViewModel.kt
fun loadCards() {
    viewModelScope.launch {
        _cardsState.value = UiState.Loading  // Actualiza UI a "cargando"

        // Llama al Use Case
        getAllCardsUseCase()
            .onSuccess { _cardsState.value = UiState.Success(it) }
            .onFailure { _cardsState.value = UiState.Error(it.message) }
    }
}
```

### Paso 3: Use Case ejecuta la lógica de negocio
```kotlin
// DOMAIN - GetAllCardsUseCase.kt
class GetAllCardsUseCase(
    private val cardRepository: TarotCardRepository  // Interface (NO implementación)
) {
    suspend operator fun invoke(): Result<List<TarotCard>> {
        // Lógica: obtener todas las cartas
        return cardRepository.getAllCards()
    }
}
```

### Paso 4: Repository accede a la base de datos
```kotlin
// DATA - TarotCardRepositoryImpl.kt
class TarotCardRepositoryImpl(
    private val cardDao: TarotCardDao  // Acceso a Room
) : TarotCardRepository {

    override suspend fun getAllCards(): Result<List<TarotCard>> {
        return try {
            val entities = cardDao.getAllCards()  // Query SQL
            val cards = entities.map { it.toDomainModel() }  // Convierte a modelo de dominio
            Result.success(cards)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### Paso 5: Room ejecuta la consulta SQL
```kotlin
// DATA - TarotCardDao.kt
@Dao
interface TarotCardDao {
    @Query("SELECT * FROM tarot_cards ORDER BY id ASC")
    suspend fun getAllCards(): List<TarotCardEntity>
}
```

### Paso 6: Los datos regresan por el mismo camino
```
Room → Repository → Use Case → ViewModel → UI
```

### Paso 7: UI se actualiza automáticamente
```kotlin
// UI observa el StateFlow y se redibuja automáticamente
val cards by viewModel.cardsState.collectAsState()

when (cards) {
    is UiState.Loading -> CircularProgressIndicator()  // Muestra spinner
    is UiState.Success -> CardList(cards.data)         // Muestra las 78 cartas
    is UiState.Error -> ErrorMessage(cards.message)    // Muestra error
}
```

---

## 🎯 ¿Por Qué Esta Arquitectura?

### ✅ Ventajas

1. **Separación de responsabilidades**: Cada capa hace UNA cosa
2. **Fácil de testear**: Puedes probar cada capa independientemente
3. **Mantenible**: Si cambias la UI, no tocas la lógica de negocio
4. **Escalable**: Puedes agregar features sin romper lo existente
5. **Reemplazable**: Si cambias Room por otra DB, solo cambias la capa Data

### 📝 Ejemplo de Reemplazabilidad

**Hoy**: Room Database (local)
```kotlin
class TarotCardRepositoryImpl(private val dao: TarotCardDao)
```

**Mañana**: Decides usar Firebase (sin cambiar Use Cases ni UI)
```kotlin
class TarotCardRepositoryImpl(private val firestore: FirebaseFirestore)
```

**El ViewModel y la UI NO necesitan cambios** porque usan la interface, no la implementación:
```kotlin
// Esto sigue igual, no importa de dónde vengan los datos
val cards = getAllCardsUseCase()
```

---

## 🧩 Componentes Clave en TarotAI

### 1. **Use Cases (Casos de Uso)**
Cada Use Case es UNA acción del usuario:

```kotlin
// UC1: Obtener todas las cartas
GetAllCardsUseCase()

// UC2: Realizar una tirada
PerformReadingUseCase(spreadType, question)

// UC3: Generar interpretación con IA
GenerateInterpretationUseCase(reading)
```

**Principio**: 1 Use Case = 1 responsabilidad

### 2. **Repositories (Repositorios)**
Son el "punto de acceso" a los datos:

```kotlin
interface TarotCardRepository {
    suspend fun getAllCards(): Result<List<TarotCard>>
    suspend fun getCardById(id: Int): Result<TarotCard?>
    suspend fun filterByType(type: ArcanaType): Result<List<TarotCard>>
}
```

**¿Por qué interface?**
- Domain solo conoce la interface (QUÉ hace)
- Data implementa la interface (CÓMO lo hace)
- Podemos tener múltiples implementaciones (Room, Firebase, Mock para tests)

### 3. **ViewModels**
Gestionan el estado de la UI:

```kotlin
@HiltViewModel
class ReadingViewModel @Inject constructor(
    private val performReadingUseCase: PerformReadingUseCase,
    private val generateInterpretationUseCase: GenerateInterpretationUseCase
) : ViewModel() {

    // Estados observables
    val readingState: StateFlow<UiState<TarotReading>>
    val interpretationState: StateFlow<UiState<Interpretation>>

    // Funciones públicas para la UI
    fun performReading(type: SpreadType, question: String?)
    fun generateInterpretation(reading: TarotReading)
}
```

### 4. **StateFlow y Coroutines**

**StateFlow**: Flujo de datos observable (la UI se actualiza automáticamente)
```kotlin
private val _state = MutableStateFlow<UiState<Data>>(UiState.Idle)
val state: StateFlow<UiState<Data>> = _state.asStateFlow()
```

**Coroutines**: Tareas asíncronas (no bloquean la UI)
```kotlin
viewModelScope.launch {  // Inicia una coroutine
    val result = suspendingFunction()  // Función que puede tardar
    _state.value = UiState.Success(result)
}
```

---

## 📦 Inyección de Dependencias con Hilt

**Problema**: ¿Cómo creamos los objetos con sus dependencias?

**Sin Hilt** (manual):
```kotlin
// ¡Tienes que crear TODO manualmente!
val database = Room.databaseBuilder(...)
val dao = database.cardDao()
val repository = TarotCardRepositoryImpl(dao)
val useCase = GetAllCardsUseCase(repository)
val viewModel = EncyclopediaViewModel(useCase)
```

**Con Hilt** (automático):
```kotlin
@HiltViewModel
class EncyclopediaViewModel @Inject constructor(
    private val getAllCardsUseCase: GetAllCardsUseCase  // Hilt lo inyecta automáticamente
) : ViewModel()
```

**Hilt se encarga de**:
1. Crear las instancias
2. Gestionar el ciclo de vida
3. Evitar duplicados (singletons cuando corresponde)

---

## 🎓 Conceptos Clave para Recordar

### 1. **Unidirectional Data Flow (Flujo Unidireccional)**
```
Usuario interactúa → ViewModel cambia estado → UI se actualiza
```
La UI nunca modifica datos directamente, solo observa el estado.

### 2. **Single Source of Truth (Única Fuente de Verdad)**
```kotlin
// MAL: Dos fuentes de verdad
var cards1 = listOf(...)  // En ViewModel
var cards2 = listOf(...)  // En UI

// BIEN: Una sola fuente
private val _cards = MutableStateFlow<List<Card>>(emptyList())  // ViewModel
val cards: StateFlow<List<Card>> = _cards.asStateFlow()         // UI solo lee
```

### 3. **Separation of Concerns (Separación de Responsabilidades)**
- **UI**: Mostrar datos
- **ViewModel**: Gestionar estado
- **Use Case**: Lógica de negocio
- **Repository**: Acceso a datos
- **Data Source**: Implementación (Room, API)

Cada componente tiene UNA responsabilidad.

---

## 🔍 Resumen Visual para TarotAI

```
[Usuario toca "Nueva Tirada"]
         ↓
[UI llama a viewModel.performReading()]
         ↓
[ViewModel llama a PerformReadingUseCase]
         ↓
[Use Case obtiene cartas del Repository]
         ↓
[Repository consulta Room Database]
         ↓
[Room ejecuta SELECT * FROM tarot_cards]
         ↓
[Datos regresan: Room → Repository → Use Case → ViewModel]
         ↓
[ViewModel actualiza _readingState.value = Success(reading)]
         ↓
[UI observa el cambio y se redibuja automáticamente]
         ↓
[Usuario ve las cartas en pantalla]
```

---

## 📚 Próximos Pasos

1. ✅ **Entiendes MVVM**: View, ViewModel, Model
2. ✅ **Entiendes Clean Architecture**: Presentation, Domain, Data
3. ✅ **Entiendes el flujo**: UI → ViewModel → Use Case → Repository → Data Source
4. 📖 **Lee la guía de Jetpack Compose** (siguiente documento)
5. 🚀 **Empezaremos a implementar** siguiendo este patrón

---

**¿Dudas?** Pregúntame mientras implementamos. La mejor forma de aprender es viendo código real en acción.
