package com.waveapp.tarotai.presentation.reading.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.waveapp.tarotai.domain.model.CardInterpretation

/**
 * Card que muestra la interpretación individual de una carta.
 *
 * Contiene:
 * - Nombre de la carta
 * - Posición en la tirada
 * - Texto de interpretación
 * - Indicador visual de que es clickeable (para ver detalle de la carta)
 *
 * @param cardInterpretation Interpretación de la carta
 * @param onClick Callback al hacer click (navega a detalle de la carta)
 */
@Composable
fun CardInterpretationCard(
    cardInterpretation: CardInterpretation,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header: Nombre de la carta y posición
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = cardInterpretation.cardName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Posición: ${cardInterpretation.position}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Icono indicando que es clickeable
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Ver detalles",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Divider
            HorizontalDivider()

            // Interpretación
            Text(
                text = cardInterpretation.interpretation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
