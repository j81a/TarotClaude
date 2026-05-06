package com.waveapp.tarotai.presentation.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waveapp.tarotai.domain.model.ReadingHistory
import com.waveapp.tarotai.domain.usecase.history.GetReadingByIdUseCase
import com.waveapp.tarotai.domain.usecase.history.UpdateReadingNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de detalle de una lectura guardada.
 *
 * Responsabilidades:
 * - Cargar los detalles de una lectura por ID
 * - Manejar la edición de notas con autosave (2 segundos de inactividad)
 * - Gestionar el estado de carga y errores
 *
 * @param savedStateHandle Para obtener el ID de la lectura de la navegación
 * @param getReadingByIdUseCase Caso de uso para obtener la lectura
 * @param updateReadingNotesUseCase Caso de uso para actualizar notas
 *
 * @since v1.1.0
 */
@HiltViewModel
class ReadingDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getReadingByIdUseCase: GetReadingByIdUseCase,
    private val updateReadingNotesUseCase: UpdateReadingNotesUseCase
) : ViewModel() {

    private val readingId: Long = savedStateHandle.get<Long>("readingId") ?: 0L

    // Estado de la lectura
    private val _reading = MutableStateFlow<ReadingHistory?>(null)
    val reading: StateFlow<ReadingHistory?> = _reading.asStateFlow()

    // Estado de carga
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Estado de guardado de notas
    private val _isSavingNotes = MutableStateFlow(false)
    val isSavingNotes: StateFlow<Boolean> = _isSavingNotes.asStateFlow()

    // Indicador visual de guardado exitoso
    private val _notesSaved = MutableStateFlow(false)
    val notesSaved: StateFlow<Boolean> = _notesSaved.asStateFlow()

    // Job para el autosave (para cancelar si el usuario sigue escribiendo)
    private var autoSaveJob: Job? = null

    init {
        loadReading()
    }

    /**
     * Carga la lectura desde el repositorio.
     */
    private fun loadReading() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val result = getReadingByIdUseCase(readingId)

            result.fold(
                onSuccess = { reading ->
                    _reading.value = reading
                    _isLoading.value = false
                },
                onFailure = { exception ->
                    _error.value = exception.message ?: "Error al cargar la lectura"
                    _isLoading.value = false
                }
            )
        }
    }

    /**
     * Maneja el cambio de las notas.
     * Implementa autosave después de 2 segundos sin actividad.
     *
     * @param notes Nuevo contenido de las notas
     */
    fun onNotesChanged(notes: String) {
        // Actualizar el estado local inmediatamente
        _reading.value = _reading.value?.copy(notes = notes.ifBlank { null })

        // Cancelar job anterior si existe
        autoSaveJob?.cancel()

        // Resetear el indicador de guardado
        _notesSaved.value = false

        // Iniciar nuevo job con delay de 2 segundos
        autoSaveJob = viewModelScope.launch {
            delay(2000) // Esperar 2 segundos de inactividad
            saveNotes(notes)
        }
    }

    /**
     * Guarda las notas en el repositorio.
     */
    private suspend fun saveNotes(notes: String) {
        _isSavingNotes.value = true

        val result = updateReadingNotesUseCase(
            id = readingId,
            notes = notes.ifBlank { null }
        )

        result.fold(
            onSuccess = {
                _isSavingNotes.value = false
                _notesSaved.value = true

                // Ocultar el indicador de guardado después de 2 segundos
                viewModelScope.launch {
                    delay(2000)
                    _notesSaved.value = false
                }
            },
            onFailure = { exception ->
                _isSavingNotes.value = false
                _error.value = "Error al guardar notas: ${exception.message}"
            }
        )
    }

    /**
     * Limpia el mensaje de error.
     */
    fun clearError() {
        _error.value = null
    }
}
