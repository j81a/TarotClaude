package com.waveapp.tarotai.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.waveapp.tarotai.R
import com.waveapp.tarotai.domain.model.SpreadType
import com.waveapp.tarotai.presentation.carddetail.CardDetailScreen
import com.waveapp.tarotai.presentation.encyclopedia.EncyclopediaScreen
import com.waveapp.tarotai.presentation.reading.QuestionScreen
import com.waveapp.tarotai.presentation.reading.ReadingScreen
import com.waveapp.tarotai.presentation.reading.SpreadTypeSelectionScreen
import com.waveapp.tarotai.presentation.screens.HomeScreen

/**
 * NavGraph: Define el grafo de navegación de la aplicación.
 *
 * @Composable: Marca esta función como un componente de UI de Compose.
 *
 * @param navController: Controlador de navegación que maneja las transiciones entre pantallas.
 * @param startDestination: Pantalla inicial de la app (por defecto: Home).
 *
 * NavHost es el contenedor que muestra la pantalla actual según la ruta activa.
 * composable() define cada destino/pantalla y su contenido.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Pantalla principal / Home
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToEncyclopedia = {
                    navController.navigate(Screen.Encyclopedia.route)
                },
                onNavigateToReadingSelection = {
                    navController.navigate(Screen.SpreadTypeSelection.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToHistory = {
                    navController.navigate(Screen.History.route)
                }
            )
        }

        // Pantalla de Enciclopedia (lista de cartas)
        composable(route = Screen.Encyclopedia.route) {
            EncyclopediaScreen(
                onCardClick = { cardId ->
                    navController.navigate(Screen.CardDetail.createRoute(cardId))
                }
            )
        }

        // Pantalla de detalle de carta
        // Recibe cardId como argumento de navegación
        composable(
            route = Screen.CardDetail.route,
            arguments = listOf(
                navArgument("cardId") {
                    type = NavType.IntType  // El ID es un entero
                }
            )
        ) {
            CardDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Pantalla de selección de tipo de tirada
        composable(route = Screen.SpreadTypeSelection.route) {
            SpreadTypeSelectionScreen(
                onNavigateBack = { navController.popBackStack() },
                onSpreadTypeSelected = { spreadType ->
                    navController.navigate(Screen.Question.createRoute(spreadType.name))
                }
            )
        }

        // Pantalla de pregunta
        composable(
            route = Screen.Question.route,
            arguments = listOf(
                navArgument("spreadType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val spreadTypeStr = backStackEntry.arguments?.getString("spreadType") ?: ""
            val spreadType = try {
                SpreadType.valueOf(spreadTypeStr)
            } catch (e: Exception) {
                SpreadType.SIMPLE
            }

            QuestionScreen(
                spreadType = spreadType,
                onNavigateBack = { navController.popBackStack() },
                onContinue = { question ->
                    val route = Screen.Reading.createRoute(spreadType.name, question)
                    Log.d("NavGraph", "Navigating from Question to Reading with route: $route")
                    navController.navigate(route)
                }
            )
        }

        // Pantalla de lectura activa
        composable(
            route = Screen.Reading.route,
            arguments = listOf(
                navArgument("spreadType") { type = NavType.StringType },
                navArgument("question") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val spreadTypeStr = backStackEntry.arguments?.getString("spreadType") ?: ""
            val question = backStackEntry.arguments?.getString("question")?.takeIf { it.isNotBlank() }

            Log.d("NavGraph", "Reading route - spreadTypeStr: '$spreadTypeStr', question: '$question'")

            val spreadType = try {
                SpreadType.valueOf(spreadTypeStr)
            } catch (e: Exception) {
                Log.e("NavGraph", "Failed to parse spreadType: '$spreadTypeStr'", e)
                SpreadType.SIMPLE
            }

            Log.d("NavGraph", "Parsed spreadType: $spreadType")

            ReadingScreen(
                spreadType = spreadType,
                question = question,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Mantener compatibilidad con rutas antiguas
        composable(route = Screen.ReadingSelection.route) {
            PlaceholderScreen(
                title = stringResource(R.string.placeholder_reading_selection),
                onBack = { navController.popBackStack() }
            )
        }

        // Pantalla de configuración
        composable(route = Screen.Settings.route) {
            // TODO: Implementar SettingsScreen en Fase 4
            PlaceholderScreen(
                title = stringResource(R.string.placeholder_settings),
                onBack = { navController.popBackStack() }
            )
        }

        // Pantalla de historial de lecturas
        composable(route = Screen.History.route) {
            // TODO: Implementar HistoryScreen en Fase 5
            PlaceholderScreen(
                title = stringResource(R.string.placeholder_history),
                onBack = { navController.popBackStack() }
            )
        }
    }
}
