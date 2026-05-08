package com.waveapp.tarotai.presentation.history

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waveapp.tarotai.domain.model.ReadingHistory
import com.waveapp.tarotai.domain.model.ReadingNote
import com.waveapp.tarotai.domain.usecase.history.GetReadingByIdUseCase
import com.waveapp.tarotai.domain.usecase.history.UpdateReadingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel para la pantalla de detalle de una lectura guardada.
 *
 * v1.2.0: Refactorizado para sistema de notas mejorado:
 * - Agregar notas individuales con timestamp
 * - Editar notas existentes
 * - Eliminar notas
 *
 * Responsabilidades:
 * - Cargar los detalles de una lectura por ID
 * - Manejar CRUD de notas
 * - Gestionar el estado de carga y errores
 *
 * @param savedStateHandle Para obtener el ID de la lectura de la navegación
 * @param getReadingByIdUseCase Caso de uso para obtener la lectura
 * @param updateReadingUseCase Caso de uso para actualizar la lectura completa
 *
 * @since v1.1.0
 */
@HiltViewModel
class ReadingDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getReadingByIdUseCase: GetReadingByIdUseCase,
    private val updateReadingUseCase: UpdateReadingUseCase
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
     * Agrega una nueva nota a la lectura.
     *
     * @param text Texto de la nota
     */
    fun addNote(text: String) {
        val currentReading = _reading.value ?: return
        if (text.isBlank() || text.length > 2000) return

        val newNote = ReadingNote(
            id = UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis(),
            text = text.trim()
        )

        val updatedNotes = currentReading.notes + newNote
        val updatedReading = currentReading.copy(notes = updatedNotes)

        // Actualizar estado local inmediatamente
        _reading.value = updatedReading

        // Guardar en repositorio
        viewModelScope.launch {
            val result = updateReadingUseCase(updatedReading)
            result.onFailure { exception ->
                _error.value = "Error al guardar nota: ${exception.message}"
                // Revertir cambio local
                _reading.value = currentReading
            }
        }
    }

    /**
     * Edita una nota existente.
     *
     * @param note Nota a editar
     * @param newText Nuevo texto
     */
    fun editNote(note: ReadingNote, newText: String) {
        val currentReading = _reading.value ?: return
        if (newText.isBlank() || newText.length > 2000) return

        val updatedNote = note.copy(
            text = newText.trim(),
            timestamp = System.currentTimeMillis() // Actualizar timestamp al editar
        )

        val updatedNotes = currentReading.notes.map {
            if (it.id == note.id) updatedNote else it
        }
        val updatedReading = currentReading.copy(notes = updatedNotes)

        // Actualizar estado local inmediatamente
        _reading.value = updatedReading

        // Guardar en repositorio
        viewModelScope.launch {
            val result = updateReadingUseCase(updatedReading)
            result.onFailure { exception ->
                _error.value = "Error al editar nota: ${exception.message}"
                // Revertir cambio local
                _reading.value = currentReading
            }
        }
    }

    /**
     * Elimina una nota.
     *
     * @param note Nota a eliminar
     */
    fun deleteNote(note: ReadingNote) {
        val currentReading = _reading.value ?: return

        val updatedNotes = currentReading.notes.filter { it.id != note.id }
        val updatedReading = currentReading.copy(notes = updatedNotes)

        // Actualizar estado local inmediatamente
        _reading.value = updatedReading

        // Guardar en repositorio
        viewModelScope.launch {
            val result = updateReadingUseCase(updatedReading)
            result.onFailure { exception ->
                _error.value = "Error al eliminar nota: ${exception.message}"
                // Revertir cambio local
                _reading.value = currentReading
            }
        }
    }

    /**
     * Limpia el mensaje de error.
     */
    fun clearError() {
        _error.value = null
    }
}
