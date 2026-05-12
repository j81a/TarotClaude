package com.waveapp.tarotai.presentation.reading

import android.Manifest
import android.content.pm.PackageManager
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.waveapp.tarotai.R
import com.waveapp.tarotai.domain.model.SpreadType
import com.waveapp.tarotai.presentation.reading.viewmodel.QuestionViewModel
import com.waveapp.tarotai.presentation.reading.viewmodel.SpeechRecognitionState

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
 * v1.3.0: Reconocimiento de voz
 * - Botón de micrófono en campo de pregunta
 * - Soporte para dictar pregunta con voz
 * - Feedback visual durante reconocimiento
 *
 * @param spreadType Tipo de tirada seleccionada
 * @param isManualLoad true si es carga manual, false si es tirada automática
 * @param onNavigateBack Callback para navegación atrás
 * @param onContinue Callback al continuar con (question, consultantName)
 * @param viewModel ViewModel con lógica de reconocimiento de voz
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    spreadType: SpreadType,
    isManualLoad: Boolean = false,
    onNavigateBack: () -> Unit,
    onContinue: (question: String?, consultantName: String?) -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: QuestionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val speechState by viewModel.speechState.collectAsState()
    val recognizedQuestion by viewModel.question.collectAsState()
    val consultantSpeechState by viewModel.consultantSpeechState.collectAsState()
    val recognizedConsultantName by viewModel.consultantName.collectAsState()

    var question by remember { mutableStateOf("") }
    var showQuestionError by remember { mutableStateOf(false) }

    // v1.3.0: Sincronizar texto reconocido con el campo de pregunta
    LaunchedEffect(recognizedQuestion) {
        if (recognizedQuestion.isNotBlank()) {
            question = recognizedQuestion
        }
    }

    // v1.3.0: Inicializar SpeechRecognizer si está disponible
    LaunchedEffect(Unit) {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            viewModel.initSpeechRecognizer(context)
            viewModel.initConsultantSpeechRecognizer(context)
        }
    }

    // v1.3.0: Manejo de permisos de micrófono para pregunta
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startListening()
        } else {
            Toast.makeText(
                context,
                "Permiso de micrófono requerido para dictar",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // v1.3.0: Manejo de permisos de micrófono para consultante
    val consultantPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startConsultantListening()
        } else {
            Toast.makeText(
                context,
                "Permiso de micrófono requerido para dictar",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // v1.2.0: Estados para consultante (opcional en ambos modos)
    var isForSomeoneElse by remember { mutableStateOf(false) } // OFF por defecto
    var consultantName by remember { mutableStateOf("") }
    var showConsultantError by remember { mutableStateOf(false) }

    // v1.3.0: Sincronizar nombre del consultante reconocido
    LaunchedEffect(recognizedConsultantName) {
        if (recognizedConsultantName.isNotBlank()) {
            consultantName = recognizedConsultantName
        }
    }

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
                },
                actions = {
                    IconButton(onClick = onNavigateToHome) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Volver al inicio"
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

                // v1.3.0: Campo de pregunta con botón de micrófono
                OutlinedTextField(
                    value = question,
                    onValueChange = {
                        question = it
                        viewModel.updateQuestion(it)
                        showQuestionError = false
                    },
                    label = { Text(stringResource(R.string.question_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    isError = showQuestionError,
                    trailingIcon = {
                        // Solo mostrar si SpeechRecognizer está disponible
                        if (SpeechRecognizer.isRecognitionAvailable(context)) {
                            IconButton(
                                onClick = {
                                    when (speechState) {
                                        is SpeechRecognitionState.Listening -> {
                                            viewModel.stopListening()
                                        }
                                        else -> {
                                            when {
                                                ContextCompat.checkSelfPermission(
                                                    context,
                                                    Manifest.permission.RECORD_AUDIO
                                                ) == PackageManager.PERMISSION_GRANTED -> {
                                                    viewModel.startListening()
                                                }
                                                else -> {
                                                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                                }
                                            }
                                        }
                                    }
                                }
                            ) {
                                when (speechState) {
                                    is SpeechRecognitionState.Idle -> {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_mic),
                                            contentDescription = "Dictar pregunta con voz",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    is SpeechRecognitionState.Listening -> {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_mic),
                                            contentDescription = "Detener dictado",
                                            tint = Color.Red,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .scale(1.2f)
                                        )
                                    }
                                    is SpeechRecognitionState.Processing -> {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(24.dp),
                                            strokeWidth = 2.dp
                                        )
                                    }
                                    is SpeechRecognitionState.Error -> {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_mic),
                                            contentDescription = "Error en reconocimiento",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                )

                // Mostrar error de validación de pregunta
                if (showQuestionError) {
                    Text(
                        text = stringResource(R.string.question_min_length),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // v1.3.0: Mostrar error de reconocimiento de voz
                if (speechState is SpeechRecognitionState.Error) {
                    Text(
                        text = (speechState as SpeechRecognitionState.Error).message,
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
                        text = "Esta lectura es para alguien más",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )

                    Switch(
                        checked = isForSomeoneElse,
                        onCheckedChange = {
                            isForSomeoneElse = it
                            if (!it) {
                                consultantName = ""
                                showConsultantError = false
                            }
                        }
                    )
                }

                // Campo consultante visible condicionalmente
                if (isForSomeoneElse) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // v1.3.0: Campo de nombre con botón de micrófono
                    OutlinedTextField(
                        value = consultantName,
                        onValueChange = {
                            if (it.length <= 100) { // Máximo 100 caracteres
                                consultantName = it
                                viewModel.updateConsultantName(it)
                                showConsultantError = false
                            }
                        },
                        label = { Text("Nombre del consultante") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = showConsultantError,
                        supportingText = {
                            Text("${consultantName.length}/100 caracteres")
                        },
                        trailingIcon = {
                            // Solo mostrar si SpeechRecognizer está disponible
                            if (SpeechRecognizer.isRecognitionAvailable(context)) {
                                IconButton(
                                    onClick = {
                                        when (consultantSpeechState) {
                                            is SpeechRecognitionState.Listening -> {
                                                viewModel.stopConsultantListening()
                                            }
                                            else -> {
                                                when {
                                                    ContextCompat.checkSelfPermission(
                                                        context,
                                                        Manifest.permission.RECORD_AUDIO
                                                    ) == PackageManager.PERMISSION_GRANTED -> {
                                                        viewModel.startConsultantListening()
                                                    }
                                                    else -> {
                                                        consultantPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                ) {
                                    when (consultantSpeechState) {
                                        is SpeechRecognitionState.Idle -> {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_mic),
                                                contentDescription = "Dictar nombre con voz",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                        is SpeechRecognitionState.Listening -> {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_mic),
                                                contentDescription = "Detener dictado",
                                                tint = Color.Red,
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .scale(1.2f)
                                            )
                                        }
                                        is SpeechRecognitionState.Processing -> {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(24.dp),
                                                strokeWidth = 2.dp
                                            )
                                        }
                                        is SpeechRecognitionState.Error -> {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_mic),
                                                contentDescription = "Error en reconocimiento",
                                                tint = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }
                            }
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

                    // v1.3.0: Mostrar error de reconocimiento de voz del consultante
                    if (consultantSpeechState is SpeechRecognitionState.Error) {
                        Text(
                            text = (consultantSpeechState as SpeechRecognitionState.Error).message,
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

                    // v1.2.0: Validar consultante SOLO si se escribió algo
                    // Si el campo está vacío, es válido (se usará valor por defecto)
                    val isConsultantValid = if (isForSomeoneElse && consultantName.isNotBlank()) {
                        consultantName.trim().length in 2..100
                    } else {
                        true // Válido si está vacío o toggle OFF
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
