package com.waveapp.tarotai.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waveapp.tarotai.R
import com.waveapp.tarotai.presentation.screens.components.HomeOptionCard

/**
 * Pantalla principal (Home) de TarotAI.
 *
 * @Composable: Marca esta función como un componente de UI de Compose.
 *
 * Muestra el menú principal modernizado con:
 * - Logo de la aplicación (nombre_solo.png)
 * - Cards clickeables para navegación principal:
 *   - Nueva Lectura (primaria, destacada)
 *   - Cargar Lectura Manual
 *   - Enciclopedia de Cartas
 *   - Historial de Lecturas
 *
 * Diseño moderno sin TopAppBar, respetando márgenes del sistema
 * (status bar y navigation bar) para una experiencia edge-to-edge.
 *
 * @param onNavigateToEncyclopedia Lambda ejecutada al presionar "Enciclopedia"
 * @param onNavigateToReadingSelection Lambda ejecutada al presionar "Nueva Lectura"
 * @param onNavigateToManualLoad Lambda ejecutada al presionar "Cargar Lectura Manual"
 * @param onNavigateToHistory Lambda ejecutada al presionar "Historial"
 */
@Composable
fun HomeScreen(
    onNavigateToEncyclopedia: () -> Unit,
    onNavigateToReadingSelection: () -> Unit,
    onNavigateToManualLoad: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()        // Respeta status bar del sistema
            .navigationBarsPadding()    // Respeta navigation bar del sistema
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),  // Permite scroll si es necesario
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Logo de la aplicación
        Image(
            painter = painterResource(id = R.drawable.nombre_solo),
            contentDescription = "Arcana - Logo de la aplicación",
            modifier = Modifier
                .fillMaxWidth(0.6f)    // 90% del ancho de pantalla
                .padding(top = 64.dp, bottom = 16.dp),
            contentScale = ContentScale.Fit  // Mantener proporciones
        )

        // Subtítulo debajo del logo
        Text(
            text = stringResource(R.string.home_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 64.dp)
        )

        // Card primaria (acción principal destacada)
        HomeOptionCard(
            icon = Icons.Default.Star,
            title = stringResource(R.string.home_new_reading),
            description = stringResource(R.string.home_new_reading_desc),
            onClick = onNavigateToReadingSelection,
            isPrimary = true  // Color destacado (primaryContainer)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Cards secundarias
        HomeOptionCard(
            icon = Icons.Default.AddCircle,
            title = stringResource(R.string.home_manual_load),
            description = stringResource(R.string.home_manual_load_desc),
            onClick = onNavigateToManualLoad
        )

        Spacer(modifier = Modifier.height(12.dp))

        HomeOptionCard(
            icon = Icons.Default.Info,  // Enciclopedia (información de cartas)
            title = stringResource(R.string.home_encyclopedia),
            description = stringResource(R.string.home_encyclopedia_desc),
            onClick = onNavigateToEncyclopedia
        )

        Spacer(modifier = Modifier.height(12.dp))

        HomeOptionCard(
            icon = Icons.Default.List,  // Historial (lista de lecturas)
            title = stringResource(R.string.home_history),
            description = stringResource(R.string.home_history_desc),
            onClick = onNavigateToHistory
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}
