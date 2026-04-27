package com.waveapp.tarotai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.waveapp.tarotai.presentation.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity principal de TarotAI.
 *
 * @AndroidEntryPoint: Marca esta Activity para que Hilt pueda inyectar dependencias.
 *
 * Esta Activity configura:
 * - Jetpack Compose como sistema de UI
 * - Material3 como sistema de diseño
 * - NavController para navegación entre pantallas
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setContent: Configura Compose como el sistema de UI de esta Activity
        setContent {
            // MaterialTheme: Aplica el tema de Material Design 3
            MaterialTheme {
                // Surface: Contenedor base que aplica el color de fondo del tema
                Surface {
                    // rememberNavController: Crea y recuerda el controlador de navegación
                    val navController = rememberNavController()

                    // NavGraph: Define todas las pantallas y rutas de navegación
                    NavGraph(navController = navController)
                }
            }
        }
    }
}
