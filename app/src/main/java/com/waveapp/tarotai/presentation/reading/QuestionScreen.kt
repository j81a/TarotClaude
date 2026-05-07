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
 *
 * v1.1.0: Reutilizada para flujo automático y manual.
 * - En modo automático (isManualLoad = false):
 *   - Toggle consultante OFF por defecto (usuario puede activarlo)
 *   - Campo consultante visible solo si toggle ON
 * - En modo manual (isManualLoad = true):
 *   - Toggle forzado a ON (no desactivable)
 *   - Campo consultante siempre visible y obligatorio
 *
 * v1.2.0: Mejorado manejo del teclado
 * - Botón "Continuar" fijo sobre el teclado (bottomBar)
 * - Contenido con scroll para que campos sean visibles cuando se editan
 * - imePadding() para ajustar automáticamente el layout
 *
 * @param spreadType Tipo de tirada seleccionada
 * @param isManualLoad true si es carga manual, false si es tirada automática
 * @param onNavigateBack Callback para navegación atrás
 * @param onContinue Callback al continuar con (question, consultantName)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    spreadType: SpreadType,
    isManualLoad: Boolean = false,
    onNavigateBack: () -> Unit,
    onContinue: (question: String?, consultantName: String?) -> Unit
) {
    var question by remember { mutableStateOf("") }
    var showQuestionError by remember { mutableStateOf(false) }

    // v1.1.0: Estados para consultante
    var isForSomeoneElse by remember { mutableStateOf(isManualLoad) } // En manual, siempre ON
    var consultantName by remember { mutableStateOf("") }
    var showConsultantError by remember { mutableStateOf(false) }

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
        ) {
            // Contenido scrolleable
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
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
                        showQuestionError = false
                    },
                    label = { Text(stringResource(R.string.question_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    isError = showQuestionError
                )

                if (showQuestionError) {
                    Text(
                        text = stringResource(R.string.question_min_length),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // v1.1.0: Toggle y campo consultante
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isManualLoad) {
                            "Nombre del consultante (obligatorio)"
                        } else {
                            "Esta lectura es para alguien más"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )

                    Switch(
                        checked = isForSomeoneElse,
                        onCheckedChange = {
                            if (!isManualLoad) { // Solo permitir cambio en modo automático
                                isForSomeoneElse = it
                                if (!it) {
                                    consultantName = ""
                                    showConsultantError = false
                                }
                            }
                        },
                        enabled = !isManualLoad // Deshabilitado en modo manual
                    )
                }

                // Campo consultante visible condicionalmente
                if (isForSomeoneElse) {
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = consultantName,
                        onValueChange = {
                            if (it.length <= 100) { // Máximo 100 caracteres
                                consultantName = it
                                showConsultantError = false
                            }
                        },
                        label = { Text("Nombre del consultante") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = showConsultantError,
                        supportingText = {
                            Text("${consultantName.length}/100 caracteres")
                        }
                    )

                    if (showConsultantError) {
                        Text(
                            text = "El nombre debe tener entre 2 y 100 caracteres",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // Botón fijo en el fondo
            Button(
                onClick = {
                    // Validar pregunta
                    val isQuestionValid = question.length >= 10
                    showQuestionError = !isQuestionValid

                    // Validar consultante si el toggle está activado
                    val isConsultantValid = if (isForSomeoneElse) {
                        consultantName.trim().length in 2..100
                    } else {
                        true // No se requiere consultante
                    }
                    showConsultantError = !isConsultantValid

                    // Continuar solo si todas las validaciones pasan
                    if (isQuestionValid && isConsultantValid) {
                        val finalConsultant = if (isForSomeoneElse && consultantName.isNotBlank()) {
                            consultantName.trim()
                        } else {
                            null
                        }
                        onContinue(question, finalConsultant)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .imePadding()
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
