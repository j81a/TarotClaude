package com.waveapp.tarotai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.waveapp.tarotai.core.ui.theme.TarotAITheme
import com.waveapp.tarotai.presentation.navigation.NavGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

/**
 * Activity principal de TarotAI.
 *
 * @AndroidEntryPoint: Marca esta Activity para que Hilt pueda inyectar dependencias.
 *
 * Esta Activity configura:
 * - Splash screen personalizado con imagen completa (sin contenedor)
 * - Jetpack Compose como sistema de UI
 * - TarotAITheme como tema personalizado
 * - NavController para navegación entre pantallas
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Instalar SplashScreen API y configurarlo para que se mantenga hasta que Compose esté listo
        val splashScreen = installSplashScreen()

        // Mantener el splash visible mientras cargamos
        var keepSplashOnScreen = true
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }

        super.onCreate(savedInstanceState)

        // Configurar status bar con el color del background de la app
        window.statusBarColor = Color(0xFF030F0F).toArgb()
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = false

        // setContent: Configura Compose como el sistema de UI de esta Activity
        setContent {
            var showSplash by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                // Ocultar el splash del sistema inmediatamente
                keepSplashOnScreen = false

                // Mantener nuestro splash de Compose por 2 segundos
                delay(2000)
                showSplash = false
            }

            if (showSplash) {
                // Splash screen con imagen completa y fondo igual al de la app
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
            } else {
                // TarotAITheme: Aplica el tema personalizado místico de la app
                TarotAITheme {
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
}
