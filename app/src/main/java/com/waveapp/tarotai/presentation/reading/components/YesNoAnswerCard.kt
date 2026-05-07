package com.waveapp.tarotai.presentation.reading.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.waveapp.tarotai.domain.model.YesNoAnswer

/**
 * Card destacado que muestra la respuesta Sí/No de una tirada.
 *
 * Muestra:
 * - Respuesta clara y destacada (Sí / No / Indefinido)
 * - Icono visual según la respuesta
 * - Justificación educativa de por qué la carta significa esa respuesta (opcional)
 *
 * v1.2.0: justification es opcional. Si es null, no se muestra la sección "¿Por qué?"
 * para evitar redundancia con el mensaje general de la tirada.
 *
 * @param answer Respuesta binaria (YES, NO, UNCLEAR)
 * @param justification Explicación educativa de la respuesta (nullable desde v1.2.0)
 */
@Composable
fun YesNoAnswerCard(
    answer: YesNoAnswer,
    justification: String?,
    modifier: Modifier = Modifier
) {
    val (answerText, answerIcon, answerColor) = when (answer) {
        YesNoAnswer.YES -> Triple(
            "Sí",
            Icons.Default.CheckCircle,
            Color(0xFF4CAF50) // Verde
        )
        YesNoAnswer.NO -> Triple(
            "No",
            Icons.Default.Close,
            Color(0xFFF44336) // Rojo
        )
        YesNoAnswer.UNCLEAR -> Triple(
            "Indefinido",
            Icons.Default.Info,
            Color(0xFFFF9800) // Naranja
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = answerColor.copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: "Tu Respuesta"
            Text(
                text = "Tu Respuesta",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            // Respuesta destacada con icono
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = answerIcon,
                    contentDescription = null,
                    tint = answerColor,
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = answerText,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = answerColor
                )
            }

            // Sección: ¿Por qué? (v1.2.0: opcional, solo si hay justification)
            justification?.let { text ->
                // Divider
                HorizontalDivider(
                    color = answerColor.copy(alpha = 0.3f),
                    thickness = 2.dp
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "¿Por qué esta respuesta?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Nota educativa
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = "Esta interpretación te ayuda a entender el simbolismo de la carta en contexto.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
    }
}
