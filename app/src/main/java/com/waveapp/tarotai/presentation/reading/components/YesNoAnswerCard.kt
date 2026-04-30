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
 * - Justificación educativa de por qué la carta significa esa respuesta
 *
 * Este componente cumple el objetivo educativo de enseñar al usuario
 * POR QUÉ una carta específica se interpreta como Sí, No o Indefinido.
 *
 * @param answer Respuesta binaria (YES, NO, UNCLEAR)
 * @param justification Explicación educativa de la respuesta
 */
@Composable
fun YesNoAnswerCard(
    answer: YesNoAnswer,
    justification: String,
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

            // Divider
            HorizontalDivider(
                color = answerColor.copy(alpha = 0.3f),
                thickness = 2.dp
            )

            // Sección: ¿Por qué?
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
                    text = justification,
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
