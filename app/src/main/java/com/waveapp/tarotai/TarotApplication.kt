package com.waveapp.tarotai

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Clase Application de TarotAI
 *
 * @HiltAndroidApp: Anotación de Hilt que genera el código necesario
 * para la inyección de dependencias en toda la aplicación.
 *
 * Esta clase se ejecuta cuando la app se inicia y es el punto de entrada
 * para Hilt.
 */
@HiltAndroidApp
class TarotApplication : Application()
