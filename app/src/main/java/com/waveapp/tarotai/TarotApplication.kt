package com.waveapp.tarotai

import android.app.Application
import com.waveapp.tarotai.domain.repository.TarotCardRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Clase Application de TarotAI
 *
 * @HiltAndroidApp: Anotación de Hilt que genera el código necesario
 * para la inyección de dependencias en toda la aplicación.
 *
 * Esta clase se ejecuta cuando la app se inicia y es el punto de entrada
 * para Hilt. Aquí inicializamos la base de datos con las 78 cartas.
 */
@HiltAndroidApp
class TarotApplication : Application() {

    /**
     * Repository inyectado por Hilt.
     * @Inject indica a Hilt que debe proveer esta dependencia.
     */
    @Inject
    lateinit var tarotCardRepository: TarotCardRepository

    /**
     * Scope de coroutines para operaciones de la aplicación.
     * SupervisorJob: Si una coroutine falla, no cancela las demás.
     */
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        // Inicializar la base de datos en background
        applicationScope.launch {
            val initialized = tarotCardRepository.initializeDatabaseIfNeeded()
            if (initialized) {
                android.util.Log.d("TarotApplication", "Base de datos inicializada con 78 cartas")
            } else {
                android.util.Log.d("TarotApplication", "Base de datos ya estaba inicializada")
            }
        }
    }
}
