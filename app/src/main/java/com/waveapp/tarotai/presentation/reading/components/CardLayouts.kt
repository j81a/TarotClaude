package com.waveapp.tarotai.presentation.reading.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.waveapp.tarotai.R
import com.waveapp.tarotai.domain.model.CardOrientation
import com.waveapp.tarotai.domain.model.DrawnCard

/**
 * Componentes compartidos para mostrar layouts de cartas del tarot.
 *
 * Estos componentes son reutilizables entre ReadingScreen y ReadingDetailScreen
 * para mantener consistencia visual.
 *
 * @since v1.2.0
 */

/**
 * Layout horizontal para cartas (tiradas Simple, Yes/No, Present, Tendency).
 * Si hay más de 1 carta, se muestran en fila horizontal ajustándose al ancho de pantalla.
 */
@Composable
fun HorizontalCardsLayout(
    drawnCards: List<DrawnCard>,
    onCardClick: (DrawnCard) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (drawnCards.size == 1) {
        // Una sola carta: centrada verticalmente, ancho fijo
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DrawnCardItem(
                drawnCard = drawnCards[0],
                onCardClick = onCardClick,
                modifier = Modifier.width(220.dp)
            )
        }
    } else {
        // Múltiples cartas: en fila horizontal, cada una ocupa el mismo espacio
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            drawnCards.forEach { drawnCard ->
                DrawnCardItem(
                    drawnCard = drawnCard,
                    onCardClick = onCardClick,
                    modifier = Modifier.weight(1f) // Cada carta ocupa el mismo peso
                )
            }
        }
    }
}

/**
 * Layout en cruz para tirada Cross.
 * Posiciones: 0-Izquierda, 1-Derecha, 2-Arriba, 3-Abajo, 4-Centro
 * Todas las cartas tienen el mismo tamaño.
 */
@Composable
fun CrossCardsLayout(
    drawnCards: List<DrawnCard>,
    onCardClick: (DrawnCard) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val cardWidth = 110.dp // Mismo ancho para todas las cartas (más pequeño para que quepan 3)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Arriba (posición 2)
        if (drawnCards.size > 2) {
            DrawnCardItem(
                drawnCard = drawnCards[2],
                onCardClick = onCardClick,
                modifier = Modifier
                    .width(cardWidth)
                    .wrapContentHeight()
            )
        }

        // Fila central: Izquierda, Centro, Derecha
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Izquierda (posición 0)
            if (drawnCards.isNotEmpty()) {
                DrawnCardItem(
                    drawnCard = drawnCards[0],
                    onCardClick = onCardClick,
                    modifier = Modifier
                        .width(cardWidth)
                        .wrapContentHeight()
                )
            }

            // Centro (posición 4)
            if (drawnCards.size > 4) {
                DrawnCardItem(
                    drawnCard = drawnCards[4],
                    onCardClick = onCardClick,
                    modifier = Modifier
                        .width(cardWidth)
                        .wrapContentHeight()
                )
            }

            // Derecha (posición 1)
            if (drawnCards.size > 1) {
                DrawnCardItem(
                    drawnCard = drawnCards[1],
                    onCardClick = onCardClick,
                    modifier = Modifier
                        .width(cardWidth)
                        .wrapContentHeight()
                )
            }
        }

        // Abajo (posición 3)
        if (drawnCards.size > 3) {
            DrawnCardItem(
                drawnCard = drawnCards[3],
                onCardClick = onCardClick,
                modifier = Modifier
                    .width(cardWidth)
                    .wrapContentHeight()
            )
        }
    }
}

/**
 * Item individual de carta en la tirada.
 * Todas las cartas tienen la misma estructura y tamaño.
 */
@Composable
fun DrawnCardItem(
    drawnCard: DrawnCard,
    onCardClick: (DrawnCard) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onCardClick(drawnCard) }
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nombre de la posición - Altura fija para que todas ocupen lo mismo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = drawnCard.positionName,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    fontSize = TextUnit(10f, TextUnitType.Sp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Imagen de la carta con icono 'i' - Altura fija
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(
                            id = getDrawableResourceId(drawnCard.card.imagePath)
                        ),
                        contentDescription = drawnCard.card.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .then(
                                if (drawnCard.orientation == CardOrientation.REVERSED) {
                                    Modifier.rotate(180f)
                                } else {
                                    Modifier
                                }
                            ),
                        contentScale = ContentScale.Fit
                    )
                }

                // v1.2.0: Icono 'i' (info) circular abajo al centro
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                        .size(32.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Ver detalle de la carta",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Nombre de la carta - Altura fija para que todas ocupen lo mismo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = drawnCard.card.name,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                        fontSize = TextUnit(11f, TextUnitType.Sp)
                    )

                    // Orientación
                    Text(
                        text = if (drawnCard.orientation == CardOrientation.UPRIGHT) {
                            stringResource(R.string.orientation_upright)
                        } else {
                            stringResource(R.string.orientation_reversed)
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = TextUnit(9f, TextUnitType.Sp)
                    )
                }
            }
        }
    }
}

/**
 * Helper para obtener ID de drawable desde nombre.
 */
@Composable
private fun getDrawableResourceId(imagePath: String): Int {
    val context = androidx.compose.ui.platform.LocalContext.current
    return context.resources.getIdentifier(
        imagePath,
        "drawable",
        context.packageName
    ).takeIf { it != 0 } ?: R.drawable.ic_launcher_foreground
}
