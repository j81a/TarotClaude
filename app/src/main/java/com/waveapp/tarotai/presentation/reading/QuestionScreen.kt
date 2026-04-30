package com.waveapp.tarotai.presentation.reading

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.waveapp.tarotai.R
import com.waveapp.tarotai.domain.model.SpreadType

/**
 * Pantalla para ingresar una pregunta antes de realizar la tirada.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    spreadType: SpreadType,
    onNavigateBack: () -> Unit,
    onContinue: (String?) -> Unit
) {
    var question by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.question_screen_title)) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding() // Ajusta el padding cuando aparece el teclado
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Contenido superior - sin centrar, empieza desde arriba
            Column(
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Text(
                    text = getInstructionText(spreadType),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                OutlinedTextField(
                    value = question,
                    onValueChange = {
                        question = it
                        showError = false
                    },
                    label = { Text(stringResource(R.string.question_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    isError = showError
                )

                if (showError) {
                    Text(
                        text = stringResource(R.string.question_min_length),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón en la parte inferior - se mueve con el teclado
            Button(
                onClick = {
                    if (question.length >= 10) {
                        onContinue(question)
                    } else {
                        showError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(stringResource(R.string.question_continue))
            }
        }
    }
}

@Composable
private fun getInstructionText(spreadType: SpreadType): String {
    return when (spreadType) {
        SpreadType.YES_NO -> "Escribe una pregunta que pueda responderse con Sí o No"
        SpreadType.PRESENT -> "Escribe una pregunta sobre tu situación actual"
        SpreadType.TENDENCY -> "Escribe una pregunta sobre tu camino o evolución"
        SpreadType.CROSS -> "Escribe una pregunta sobre una situación compleja"
        SpreadType.SIMPLE -> "Opcional: Escribe una pregunta o tema a explorar"
    }
}
