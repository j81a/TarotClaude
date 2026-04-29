package com.waveapp.tarotai.presentation.reading

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.waveapp.tarotai.R
import com.waveapp.tarotai.domain.model.SpreadType

/**
 * Pantalla de selección de tipo de tirada.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpreadTypeSelectionScreen(
    onNavigateBack: () -> Unit,
    onSpreadTypeSelected: (SpreadType) -> Unit
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
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(getSpreadTypeOptions()) { option ->
                SpreadTypeCard(
                    title = option.title,
                    description = option.description,
                    onClick = { onSpreadTypeSelected(option.type) }
                )
            }
        }
    }
}

/**
 * Tarjeta para un tipo de tirada.
 */
@Composable
private fun SpreadTypeCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Opciones de tipos de tirada con sus textos.
 */
private data class SpreadTypeOption(
    val type: SpreadType,
    val title: String,
    val description: String
)

private fun getSpreadTypeOptions(): List<SpreadTypeOption> {
    return listOf(
        SpreadTypeOption(
            type = SpreadType.SIMPLE,
            title = "Carta Simple",
            description = "Una carta para orientación general"
        ),
        SpreadTypeOption(
            type = SpreadType.YES_NO,
            title = "Sí o No",
            description = "Una carta para responder tu pregunta"
        ),
        SpreadTypeOption(
            type = SpreadType.PRESENT,
            title = "Presente",
            description = "3 cartas sobre tu situación actual"
        ),
        SpreadTypeOption(
            type = SpreadType.TENDENCY,
            title = "Tendencia",
            description = "3 cartas sobre pasado, presente y futuro"
        ),
        SpreadTypeOption(
            type = SpreadType.CROSS,
            title = "Cruz",
            description = "5 cartas en disposición de cruz"
        )
    )
}
