package com.waveapp.tarotai

import android.os.Bundle
import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity principal de TarotAI.
 *
 * @AndroidEntryPoint: Marca esta Activity para que Hilt pueda inyectar dependencias.
 *
 * Por ahora es una Activity vacía temporal para poder ejecutar la app.
 * En la Tarea 1.4 agregaremos Jetpack Compose y la navegación.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Por ahora no hay UI, solo una pantalla en blanco
        // En la Tarea 1.4 configuraremos Jetpack Compose aquí
    }
}
