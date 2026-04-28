package com.waveapp.tarotai.presentation.encyclopedia.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.waveapp.tarotai.R
import com.waveapp.tarotai.domain.model.ArcanaType
import com.waveapp.tarotai.domain.model.Suit

/**
 * Fila de chips de filtros para arcano y palo.
 *
 * Muestra:
 * - Chip "Arcanos Mayores"
 * - Chip "Arcanos Menores"
 * - Chips de palos (Bastos, Copas, Espadas, Oros)
 * - Botón "Limpiar filtros" si hay algún filtro activo
 *
 * @param selectedArcana Tipo de arcano seleccionado
 * @param selectedSuit Palo seleccionado
 * @param onArcanaSelected Callback cuando se selecciona un tipo de arcano
 * @param onSuitSelected Callback cuando se selecciona un palo
 * @param onClearFilters Callback para limpiar todos los filtros
 */
@Composable
fun FilterChipsRow(
    selectedArcana: ArcanaType?,
    selectedSuit: Suit?,
    onArcanaSelected: (ArcanaType?) -> Unit,
    onSuitSelected: (Suit?) -> Unit,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Chip para Arcanos Mayores
        FilterChip(
            selected = selectedArcana == ArcanaType.MAJOR,
            onClick = {
                onArcanaSelected(
                    if (selectedArcana == ArcanaType.MAJOR) null else ArcanaType.MAJOR
                )
            },
            label = { Text(stringResource(R.string.arcana_type_major)) }
        )

        // Chip para Arcanos Menores
        FilterChip(
            selected = selectedArcana == ArcanaType.MINOR,
            onClick = {
                onArcanaSelected(
                    if (selectedArcana == ArcanaType.MINOR) null else ArcanaType.MINOR
                )
            },
            label = { Text(stringResource(R.string.arcana_type_minor)) }
        )

        // Chips de palos (solo si arcanos mayores NO está seleccionado)
        if (selectedArcana != ArcanaType.MAJOR) {
            FilterChip(
                selected = selectedSuit == Suit.WANDS,
                onClick = {
                    onSuitSelected(if (selectedSuit == Suit.WANDS) null else Suit.WANDS)
                },
                label = { Text(stringResource(R.string.suit_wands)) }
            )

            FilterChip(
                selected = selectedSuit == Suit.CUPS,
                onClick = {
                    onSuitSelected(if (selectedSuit == Suit.CUPS) null else Suit.CUPS)
                },
                label = { Text(stringResource(R.string.suit_cups)) }
            )

            FilterChip(
                selected = selectedSuit == Suit.SWORDS,
                onClick = {
                    onSuitSelected(if (selectedSuit == Suit.SWORDS) null else Suit.SWORDS)
                },
                label = { Text(stringResource(R.string.suit_swords)) }
            )

            FilterChip(
                selected = selectedSuit == Suit.PENTACLES,
                onClick = {
                    onSuitSelected(if (selectedSuit == Suit.PENTACLES) null else Suit.PENTACLES)
                },
                label = { Text(stringResource(R.string.suit_pentacles)) }
            )
        }

        // Botón para limpiar filtros (solo si hay algún filtro activo)
        if (selectedArcana != null || selectedSuit != null) {
            TextButton(onClick = onClearFilters) {
                Text(stringResource(R.string.clear_filters))
            }
        }
    }
}
