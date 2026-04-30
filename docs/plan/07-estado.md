# 7. Gestión de Estado

## 7.1 Estados de UI

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
