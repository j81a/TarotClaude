package com.waveapp.tarotai.presentation.reading.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * ViewModel para QuestionScreen con soporte de reconocimiento de voz.
 *
 * Responsabilidades:
 * - Gestionar estado de reconocimiento de voz (Idle, Listening, Processing, Error)
 * - Inicializar y destruir SpeechRecognizer
 * - Manejar callbacks de reconocimiento (resultados parciales y finales)
 * - Actualizar pregunta con texto reconocido
 * - Manejo de errores de reconocimiento con mensajes en español
 *
 * @param savedStateHandle Para obtener parámetros de navegación (si son necesarios)
 *
 * @since v1.3.0
 */
@HiltViewModel
class QuestionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _speechState = MutableStateFlow<SpeechRecognitionState>(
        SpeechRecognitionState.Idle
    )
    val speechState: StateFlow<SpeechRecognitionState> = _speechState.asStateFlow()

    private val _consultantSpeechState = MutableStateFlow<SpeechRecognitionState>(
        SpeechRecognitionState.Idle
    )
    val consultantSpeechState: StateFlow<SpeechRecognitionState> = _consultantSpeechState.asStateFlow()

    private val _question = MutableStateFlow("")
    val question: StateFlow<String> = _question.asStateFlow()

    private val _consultantName = MutableStateFlow("")
    val consultantName: StateFlow<String> = _consultantName.asStateFlow()

    private var speechRecognizer: SpeechRecognizer? = null
    private var consultantSpeechRecognizer: SpeechRecognizer? = null

    /**
     * Inicializa el reconocedor de voz con listener para callbacks.
     *
     * @param context Contexto de la aplicación para crear SpeechRecognizer
     */
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

    /**
     * Inicia el reconocimiento de voz configurado en español.
     */
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

    /**
     * Detiene el reconocimiento de voz.
     */
    fun stopListening() {
        speechRecognizer?.stopListening()
        _speechState.value = SpeechRecognitionState.Idle
    }

    /**
     * Actualiza manualmente el texto de la pregunta.
     * Usado cuando el usuario escribe con el teclado.
     *
     * @param text Nuevo texto de la pregunta
     */
    fun updateQuestion(text: String) {
        _question.value = text
    }

    /**
     * Inicializa el reconocedor de voz para el campo de consultante.
     *
     * @param context Contexto de la aplicación para crear SpeechRecognizer
     */
    fun initConsultantSpeechRecognizer(context: Context) {
        consultantSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        consultantSpeechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matches?.firstOrNull()?.let { recognizedText ->
                    _consultantName.value = recognizedText
                }
                _consultantSpeechState.value = SpeechRecognitionState.Idle
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matches?.firstOrNull()?.let { partialText ->
                    _consultantName.value = partialText
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
                _consultantSpeechState.value = SpeechRecognitionState.Error(errorMessage)
            }

            override fun onBeginningOfSpeech() {
                _consultantSpeechState.value = SpeechRecognitionState.Listening
            }

            override fun onEndOfSpeech() {
                _consultantSpeechState.value = SpeechRecognitionState.Processing
            }

            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    /**
     * Inicia el reconocimiento de voz para el campo de consultante.
     */
    fun startConsultantListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        consultantSpeechRecognizer?.startListening(intent)
        _consultantSpeechState.value = SpeechRecognitionState.Listening
    }

    /**
     * Detiene el reconocimiento de voz del consultante.
     */
    fun stopConsultantListening() {
        consultantSpeechRecognizer?.stopListening()
        _consultantSpeechState.value = SpeechRecognitionState.Idle
    }

    /**
     * Actualiza manualmente el nombre del consultante.
     * Usado cuando el usuario escribe con el teclado.
     *
     * @param text Nuevo nombre del consultante
     */
    fun updateConsultantName(text: String) {
        _consultantName.value = text
    }

    /**
     * Limpia recursos al destruir el ViewModel.
     * Destruye los SpeechRecognizers para evitar memory leaks.
     */
    override fun onCleared() {
        super.onCleared()
        speechRecognizer?.destroy()
        consultantSpeechRecognizer?.destroy()
    }
}

/**
 * Estados posibles del reconocimiento de voz.
 */
sealed class SpeechRecognitionState {
    /** Estado inicial o después de completar reconocimiento */
    object Idle : SpeechRecognitionState()

    /** Escuchando voz del usuario */
    object Listening : SpeechRecognitionState()

    /** Procesando audio capturado */
    object Processing : SpeechRecognitionState()

    /** Error durante reconocimiento */
    data class Error(val message: String) : SpeechRecognitionState()
}
