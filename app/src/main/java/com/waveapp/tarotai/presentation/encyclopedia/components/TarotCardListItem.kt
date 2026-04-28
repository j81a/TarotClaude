package com.waveapp.tarotai.presentation.encyclopedia.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.waveapp.tarotai.R
import com.waveapp.tarotai.domain.model.ArcanaType
import com.waveapp.tarotai.domain.model.Suit
import com.waveapp.tarotai.domain.model.TarotCard

/**
 * Item de lista para una carta del tarot.
 *
 * Muestra:
 * - Nombre de la carta
 * - Badge de tipo de arcano (Mayor/Menor)
 * - Badge de palo (si es arcano menor)
 * - Primeras keywords
 *
 * @param card Carta a mostrar
 * @param onClick Callback cuando se hace clic
 */
@Composable
fun TarotCardListItem(
    card: TarotCard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen de la carta
            Card(
                modifier = Modifier.size(64.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Image(
                    painter = painterResource(
                        id = getDrawableResourceId(card.imagePath)
                    ),
                    contentDescription = card.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Contenido de la carta
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Nombre de la carta
                Text(
                    text = card.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Badges
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Badge de tipo de arcano
                    ArcanaTypeBadge(arcanaType = card.arcanaType)

                    // Badge de palo (solo si es menor)
                    card.suit?.let { suit ->
                        SuitBadge(suit = suit)
                    }
                }

                // Keywords (primeras 3)
                if (card.keywords.isNotEmpty()) {
                    Text(
                        text = card.keywords.take(3).joinToString(", "),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

/**
 * Badge para tipo de arcano.
 */
@Composable
private fun ArcanaTypeBadge(arcanaType: ArcanaType) {
    val text = when (arcanaType) {
        ArcanaType.MAJOR -> stringResource(R.string.arcana_type_major)
        ArcanaType.MINOR -> stringResource(R.string.arcana_type_minor)
    }

    val color = when (arcanaType) {
        ArcanaType.MAJOR -> MaterialTheme.colorScheme.primaryContainer
        ArcanaType.MINOR -> MaterialTheme.colorScheme.secondaryContainer
    }

    Surface(
        color = color,
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

/**
 * Badge para palo.
 */
@Composable
private fun SuitBadge(suit: Suit) {
    val text = when (suit) {
        Suit.WANDS -> stringResource(R.string.suit_wands)
        Suit.CUPS -> stringResource(R.string.suit_cups)
        Suit.SWORDS -> stringResource(R.string.suit_swords)
        Suit.PENTACLES -> stringResource(R.string.suit_pentacles)
    }

    Surface(
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

/**
 * Función helper para obtener el ID de recurso drawable desde el nombre.
 * Convierte "card_major_00" al ID R.drawable.card_major_00
 */
@Composable
private fun getDrawableResourceId(imagePath: String): Int {
    val context = androidx.compose.ui.platform.LocalContext.current
    return context.resources.getIdentifier(
        imagePath,
        "drawable",
        context.packageName
    ).takeIf { it != 0 } ?: R.drawable.ic_launcher_foreground // Fallback si no existe
}

