package com.waveapp.tarotai.presentation.reading

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.waveapp.tarotai.R
import com.waveapp.tarotai.domain.model.SpreadType

/**
 * Pantalla de selección de tipo de tirada.
 * v1.5.1: Modernizada con iconos, badges y mejor jerarquía visual.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpreadTypeSelectionScreen(
    onNavigateBack: () -> Unit,
    onSpreadTypeSelected: (SpreadType) -> Unit,
    onNavigateToHome: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.reading_type_selection_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.nav_back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToHome) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Volver al inicio"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(getSpreadTypeOptions()) { option ->
                SpreadTypeCard(
                    icon = option.icon,
                    title = option.title,
                    cardCount = option.cardCount,
                    description = option.description,
                    onClick = { onSpreadTypeSelected(option.type) }
                )
            }
        }
    }
}

/**
 * Tarjeta modernizada para un tipo de tirada.
 * v1.5.1: Con icono, badge de número de cartas y mejor diseño.
 */
@Composable
private fun SpreadTypeCard(
    icon: ImageVector,
    title: String,
    cardCount: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono grande a la izquierda
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Título
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Badge con número de cartas
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            text = cardCount,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Descripción
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Opciones de tipos de tirada con sus textos e iconos.
 * v1.5.1: Agregados iconos representativos y número de cartas.
 */
private data class SpreadTypeOption(
    val type: SpreadType,
    val icon: ImageVector,
    val title: String,
    val cardCount: String,
    val description: String
)

private fun getSpreadTypeOptions(): List<SpreadTypeOption> {
    return listOf(
        SpreadTypeOption(
            type = SpreadType.SIMPLE,
            icon = Icons.Default.Star,
            title = "Carta Simple",
            cardCount = "1 carta",
            description = "Una carta para orientación general"
        ),
        SpreadTypeOption(
            type = SpreadType.YES_NO,
            icon = Icons.Default.Check,
            title = "Sí o No",
            cardCount = "1 carta",
            description = "Una carta para responder tu pregunta"
        ),
        SpreadTypeOption(
            type = SpreadType.PRESENT,
            icon = Icons.Default.LocationOn,
            title = "Presente",
            cardCount = "3 cartas",
            description = "3 cartas sobre tu situación actual"
        ),
        SpreadTypeOption(
            type = SpreadType.TENDENCY,
            icon = Icons.Default.ArrowForward,
            title = "Tendencia",
            cardCount = "3 cartas",
            description = "3 cartas sobre pasado, presente y futuro"
        ),
        SpreadTypeOption(
            type = SpreadType.CROSS,
            icon = Icons.Default.Add,
            title = "Cruz",
            cardCount = "5 cartas",
            description = "5 cartas en disposición de cruz"
        )
    )
}
