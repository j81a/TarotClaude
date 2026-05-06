package com.waveapp.tarotai.presentation.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.waveapp.tarotai.domain.model.ReadingHistory
import java.text.SimpleDateFormat
import java.util.*

/**
 * Componente de UI para mostrar un elemento de lectura en la lista de historial.
 *
 * Muestra:
 * - Nombre del consultante (título)
 * - Fecha y hora de la lectura
 * - Tipo de tirada
 * - Pregunta (truncada si es muy larga)
 * - Ícono de navegación
 *
 * @param reading Lectura a mostrar
 * @param onClick Callback cuando se hace click en el elemento
 *
 * @since v1.1.0
 */
@Composable
fun ReadingHistoryItem(
    reading: ReadingHistory,
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
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Contenido principal
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Nombre del consultante
                Text(
                    text = reading.consultantName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Fecha y hora
                Text(
                    text = formatTimestamp(reading.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Tipo de tirada
                Text(
                    text = getSpreadTypeName(reading.spreadType.name),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                // Pregunta (si existe)
                if (!reading.question.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = reading.question,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Ícono de navegación
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Ver detalles",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Formatea un timestamp a formato legible "DD/MM/YYYY - HH:MM"
 */
private fun formatTimestamp(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}

/**
 * Obtiene el nombre legible del tipo de tirada.
 */
private fun getSpreadTypeName(spreadType: String): String {
    return when (spreadType) {
        "YES_NO" -> "Tirada Sí o No"
        "PRESENT" -> "Tirada de 3 Cartas (Presente)"
        "TENDENCY" -> "Tirada de 3 Cartas (Tendencia)"
        "CROSS" -> "Cruz Celta"
        "SIMPLE" -> "Carta Simple"
        else -> spreadType
    }
}
