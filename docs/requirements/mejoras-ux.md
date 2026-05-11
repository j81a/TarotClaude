# Mejoras de UX - v1.2.0

> **Versión**: 1.2.0
> **Estado**: ⏳ En desarrollo
> **Fecha**: 2026-05-07

---

## Contexto

Durante las pruebas de v1.1.0, se identificaron varios problemas de usabilidad que afectan la experiencia del usuario. Esta versión se enfoca en corregir estos problemas sin agregar nuevas funcionalidades.

---

## RF-13: Manejo Correcto del Teclado en Formularios

### Descripción
Los formularios de entrada (especialmente QuestionScreen) no manejan correctamente el teclado virtual, ocultando campos y botones cuando este se despliega.

### Problema Actual
- Cuando el teclado se despliega, el botón "Continuar" queda oculto
- Los campos de texto no quedan visibles cuando el usuario los está editando
- No hay scroll para acceder al contenido oculto

### Solución Requerida
- **Botón fijo sobre el teclado**: El botón "Continuar" debe quedar fijo sobre el teclado usando `bottomBar` en Scaffold
- **Scroll en contenido**: El contenido debe tener scroll para que los campos sean visibles
- **Campo activo visible**: Cuando el cursor está en un campo, ese campo debe verse completo

### Criterios de Aceptación
- [ ] El botón "Continuar" es siempre visible y accesible con el teclado desplegado
- [ ] El contenido tiene scroll y los campos se mantienen visibles al editarlos
- [ ] Uso correcto de `imePadding()` en Scaffold y contenido
- [ ] El layout se adapta correctamente a diferentes tamaños de pantalla

### Pantallas Afectadas
- `QuestionScreen` (principal)

### Prioridad
**Alta** - Afecta la usabilidad básica de entrada de datos

---

## RF-14: Guardado Manual en Historial

### Descripción
Las lecturas se guardan automáticamente en el historial, llenándolo con lecturas de prueba y quitando control al usuario.

### Problema Actual
- Las lecturas se guardan automáticamente sin confirmación
- El usuario no tiene control sobre qué lecturas guardar
- El historial se llena con lecturas de prueba o experimentales

### Solución Requerida
- **Botón explícito "Guardar en Historial"**: Debe aparecer ANTES del mensaje de la tirada
- **Guardado manual**: Solo se guarda cuando el usuario presiona el botón
- **No pedir consultante**: El nombre ya se ingresó en QuestionScreen
- **Confirmación visual**: Card confirmando "Lectura guardada"
- **Flujo consistente**: Aplicable tanto a tiradas automáticas como manuales
- **Mostrar consultante**: En ReadingScreen mostrar "Consultante: [nombre]" arriba de la pregunta

### Criterios de Aceptación
- [x] `ReadingScreen` tiene botón "Guardar en Historial" ANTES del mensaje de la tirada
- [x] `ReadingScreen` muestra consultante arriba de la pregunta
- [ ] `ManualLoadScreen` NO guarda automáticamente, solo cuando el usuario lo solicita
- [x] Se muestra confirmación visual cuando se guarda exitosamente
- [x] El botón se deshabilita o desaparece después de guardar
- [x] El flujo funciona igual para tiradas automáticas y manuales
- [x] NO se pide consultante al guardar (se usa el que ya ingresó)

### Impacto en Código
- `ReadingViewModel`: Agregar función `saveToHistory()` y estado `readingSaved`
- `ManualLoadViewModel`: Remover guardado automático
- `ReadingScreen`: Agregar botón condicional
- `ManualLoadScreen` / `ReadingDetailScreen`: Agregar botón de guardado

### Prioridad
**Media** - Afecta la calidad del historial pero no es bloqueante

---

## RF-15: Botones de Cierre Rápido

### Descripción
Los usuarios deben retroceder múltiples veces para volver al inicio de la app, sin opción de cierre rápido del flujo actual.

### Problema Actual
- Solo hay botón "Atrás" que navega pantalla por pantalla
- Para volver al Home desde una pantalla profunda, hay que retroceder 4-5 veces
- No hay forma rápida de cancelar el flujo actual

### Solución Requerida
- **Botón X en TopAppBar**: Todas las pantallas (excepto Home) deben tener un botón X arriba a la derecha
- **Navegación directa al Home**: Al presionar X, se navega directamente al Home limpiando el back stack
- **Icono estándar**: Usar `Icons.Default.Close`

### Criterios de Aceptación
- [ ] Todas las pantallas secundarias tienen botón X visible
- [ ] Al presionar X, se navega inmediatamente al Home
- [ ] El back stack se limpia correctamente con `popUpTo`
- [ ] El botón no aparece en HomeScreen

### Pantallas a Modificar
1. EncyclopediaScreen
2. CardDetailScreen
3. SpreadTypeSelectionScreen
4. QuestionScreen
5. ReadingScreen
6. HistoryScreen
7. ReadingDetailScreen
8. ManualLoadScreen
9. CardSelectorScreen

### Patrón de Implementación
```kotlin
actions = {
    IconButton(onClick = {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) { inclusive = false }
        }
    }) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Volver al inicio"
        )
    }
}
```

### Prioridad
**Media** - Mejora significativa de UX pero no crítica

---

## RF-16: Limpieza de UI del Historial

### Descripción
El botón "+" (FloatingActionButton) en HistoryScreen es confuso porque sugiere que se pueden agregar lecturas desde ahí.

### Problema Actual
- El botón + en HistoryScreen es engañoso
- Los usuarios esperan agregar lecturas manualmente desde el historial
- Inconsistente con el flujo real donde las lecturas se agregan desde ReadingScreen

### Solución Requerida
- **Remover el FAB**: Eliminar el FloatingActionButton del HistoryScreen
- **Actualizar documentación**: Las lecturas solo se agregan desde interpretación
- **UI más limpia**: El historial es solo para visualizar, no para agregar

### Criterios de Aceptación
- [ ] No hay botón + en HistoryScreen
- [ ] El parámetro `onNewReading` se elimina de la función
- [ ] NavGraph no pasa callback `onNewReading`
- [ ] La UI se ve más limpia y clara

### Impacto en Código
- `HistoryScreen.kt`: Remover FAB y parámetro
- `NavGraph.kt`: Remover callback en navegación

### Prioridad
**Baja** - Mejora estética y de claridad, pero no afecta funcionalidad

---

## RF-17: Verificación de Guardado en Historial (Bug Fix)

### Descripción
El guardado en historial no está funcionando en ningún caso, ni siquiera con el botón + (que se va a remover).

### Problema Actual
- Las lecturas no se guardan en Room Database
- `SaveReadingUseCase` o `ReadingHistoryDao` pueden tener bugs
- Las conversiones de entidades pueden ser incorrectas
- GetAllReadingsUseCase no recupera datos

### Investigación Requerida
1. Verificar que `SaveReadingUseCase` llama correctamente al Repository
2. Verificar que `ReadingHistoryDao.insert()` retorna el ID correctamente
3. Verificar conversiones de `ReadingHistory` ↔ `ReadingHistoryEntity`
4. Verificar que `drawnCards` y `interpretation` se serializan/deserializan bien
5. Agregar logs para debugging

### Solución Requerida
- **Corregir el bug**: Identificar y solucionar el problema de guardado
- **Agregar logs**: Para facilitar debugging futuro
- **Probar persistencia**: Verificar que los datos persisten después de cerrar la app

### Criterios de Aceptación
- [ ] `SaveReadingUseCase` guarda correctamente en Room
- [ ] El DAO inserta y retorna el ID de la lectura
- [ ] `GetAllReadingsUseCase` recupera las lecturas guardadas
- [ ] La UI de HistoryScreen muestra las lecturas
- [ ] Los datos persisten después de cerrar y reabrir la app
- [ ] Se pueden abrir los detalles de una lectura guardada

### Archivos a Revisar
- `domain/usecase/history/SaveReadingUseCase.kt`
- `data/repository/ReadingHistoryRepositoryImpl.kt`
- `data/local/dao/ReadingHistoryDao.kt`
- `data/local/mapper/ReadingHistoryMapper.kt`
- `data/local/database/Converters.kt` (TypeConverters)

### Prioridad
**Alta** - Es un bug crítico que impide usar el historial

---

## RF-18: Correcciones UX Adicionales (v1.2.0)

### Descripción
Ajustes finos de UX identificados durante testing:
1. Mostrar consultante en ReadingScreen
2. Reordenar botón de guardado
3. Simplificar guardado (no pedir consultante dos veces)
4. Remover justificación redundante en tiradas Sí/No

### Cambios Requeridos

#### 1. Mostrar Consultante y Pregunta
En `ReadingScreen`, antes del card de pregunta:
```
Consultante: [nombre]
Pregunta: [pregunta]
```

#### 2. Reordenar Botón de Guardado
- Botón "Guardar en Historial" debe estar ANTES del card "Mensaje de la Tirada"
- No después de la interpretación

#### 3. Simplificar Guardado
- NO mostrar diálogo pidiendo consultante
- Usar el consultante que ya se ingresó en QuestionScreen
- Pasar consultantName como parámetro a ReadingScreen

#### 4. Remover Justificación en Sí/No
- En tiradas YES_NO, el card con la justificación es redundante
- Solo mostrar la respuesta Sí/No (el mensaje general ya explica el porqué)
- Remover `YesNoAnswerCard.justification` de la UI

### Criterios de Aceptación
- [ ] ReadingScreen muestra "Consultante: [nombre]" arriba de la pregunta
- [ ] Botón "Guardar en Historial" está ANTES del mensaje de la tirada
- [ ] NO se muestra diálogo al guardar (usa consultante de QuestionScreen)
- [ ] Tiradas Sí/No NO muestran la justificación (solo respuesta)

### Impacto en Código
- `ReadingScreen.kt`: Agregar parámetro `consultantName`, mostrar antes de pregunta
- `ReadingScreen.kt`: Mover botón de guardado antes de interpretación
- `ReadingViewModel.kt`: Función `saveToHistory()` recibe consultantName como parámetro
- `ReadingScreen.kt`: Remover diálogo `SaveReadingDialog`
- `YesNoAnswerCard.kt`: Remover campo de justificación

### Prioridad
**Alta** - Mejoras importantes de UX identificadas en testing

---

## RF-19: Mejoras Finales de UX (v1.2.0)

### Descripción
Correcciones finales identificadas durante testing de v1.2.0:
1. Agregar botón X (cerrar) en todas las pantallas secundarias
2. ReadingDetailScreen debe mostrar cartas igual que ReadingScreen (layout visual)
3. ReadingDetailScreen debe mostrar interpretación con cards igual que ReadingScreen
4. Sistema de notas mejorado: listado con fecha, editar/eliminar individual

### Cambios Requeridos

#### 1. Botón X en TopAppBar (Cierre Rápido)
Todas las pantallas secundarias deben tener botón X arriba a la derecha:
- EncyclopediaScreen
- CardDetailScreen
- SpreadTypeSelectionScreen
- QuestionScreen
- ReadingScreen
- HistoryScreen
- ReadingDetailScreen
- ManualLoadScreen
- CardSelectorScreen

**Comportamiento**: Al presionar X, navega directo a Home limpiando back stack.

```kotlin
actions = {
    IconButton(onClick = {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Home.route) { inclusive = false }
        }
    }) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Volver al inicio"
        )
    }
}
```

#### 2. ReadingDetailScreen - Mostrar Cartas como Layout Visual
**Problema actual**: Muestra cartas como lista de strings
**Solución**: Reutilizar componentes de ReadingScreen:
- `HorizontalCardsLayout` para tiradas Simple/YesNo/Present/Tendency
- `CrossCardsLayout` para tirada Cross
- Mostrar imágenes de cartas con orientación

#### 3. ReadingDetailScreen - Interpretación con Cards
**Problema actual**: Muestra interpretación como texto plano
**Solución**: Reutilizar componentes:
- `YesNoAnswerCard` (si aplica)
- `GeneralInterpretationCard`
- Mismo estilo visual que ReadingScreen

#### 4. Sistema de Notas Mejorado
**Problema actual**: Un solo TextField de notas
**Solución**: Listado de notas con:
- Cada nota tiene: timestamp, texto, acciones
- Botón "Agregar Nota"
- Diálogo para agregar/editar nota
- Cada item muestra:
  - Fecha/hora formateada
  - Texto de la nota
  - Botones: Editar (ícono lápiz), Eliminar (ícono basura)

**Modelo de datos**:
```kotlin
data class ReadingNote(
    val id: String,  // UUID
    val timestamp: Long,
    val text: String
)
```

### Criterios de Aceptación
- [ ] Todas las pantallas secundarias tienen botón X funcional
- [ ] ReadingDetailScreen muestra cartas en layout visual (igual que ReadingScreen)
- [ ] ReadingDetailScreen muestra interpretación con cards (igual que ReadingScreen)
- [ ] Sistema de notas permite agregar múltiples notas
- [ ] Cada nota tiene fecha, editar y eliminar
- [ ] Notas se guardan en Room Database

### Impacto en Código
- 9 archivos: Agregar botón X en TopAppBar
- `ReadingDetailScreen.kt`: Refactorizar completamente
- `ReadingHistory.kt`: Cambiar `notes: String?` a `notes: List<ReadingNote>`
- `ReadingHistoryEntity.kt`: TypeConverter para `List<ReadingNote>`
- `Converters.kt`: Agregar serialización de notas

### Prioridad
**Alta** - Mejoras críticas de UX identificadas en testing final

---

## RF-20: Iconos Visuales en Cartas (v1.2.0) 🆕

### Descripción
Agregar iconos visuales en las cartas para mejorar la intuitividad de las interacciones, haciendo explícito qué acciones están disponibles en cada estado.

### Problema Actual
- No es intuitivo que las cartas son click able en ReadingScreen
- En ManualLoadScreen no queda claro que se pueden tocar las cartas de dorso
- Después de seleccionar una carta no es obvio que se puede editar antes de interpretar

### Solución Requerida

#### Iconos en ReadingScreen (Tiradas Automáticas)
- **Icono 'i' (info)**: Circle con símbolo 'info' dentro, ubicado abajo al centro de cada carta
- **Acción**: Al tocar la carta (o el ícono), abre CardDetailScreen con información de la carta
- **Aplicable a**: Todas las cartas reveladas en una lectura automática

#### Iconos en ManualLoadScreen (Carga Manual)
Los iconos cambian según el estado de la carta:

1. **Estado inicial (dorso sin seleccionar)**:
   - **Icono '+'**: Circle con símbolo '+' dentro
   - **Ubicación**: Abajo al centro del card_back
   - **Acción**: Al tocar, abre CardSelectorScreen

2. **Estado seleccionado (antes de interpretar)**:
   - **Icono 'lápiz'** (edit): Circle con símbolo 'edit' dentro
   - **Ubicación**: Abajo al centro de la carta revelada
   - **Acción**: Al tocar, abre CardSelectorScreen para cambiar la carta

3. **Estado interpretado (después de generar interpretación)**:
   - **Icono 'i'** (info): Circle con símbolo 'info' dentro
   - **Ubicación**: Abajo al centro de la carta revelada
   - **Acción**: Al tocar, abre CardDetailScreen (solo lectura)

### Especificaciones Técnicas

**Diseño del ícono circular**:
```kotlin
// Material Icons a usar:
Icons.Filled.Info       // Para icono 'i'
Icons.Filled.Add        // Para icono '+'
Icons.Filled.Edit       // Para icono 'lápiz'

// Estilo visual:
Box(
    modifier = Modifier
        .size(32.dp)
        .background(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
            shape = CircleShape
        ),
    contentAlignment = Alignment.Center
) {
    Icon(
        imageVector = icon,
        contentDescription = description,
        tint = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.size(18.dp)
    )
}
```

**Posicionamiento**:
- Usar `Box` con `Alignment.BottomCenter`
- Padding bottom: 8.dp
- Z-index arriba de la imagen de la carta

### Criterios de Aceptación
- [ ] ReadingScreen muestra icono 'i' en todas las cartas reveladas
- [ ] ManualLoadScreen muestra icono '+' en cartas de dorso (sin seleccionar)
- [ ] ManualLoadScreen muestra icono 'lápiz' en cartas seleccionadas (antes de interpretar)
- [ ] ManualLoadScreen muestra icono 'i' en cartas después de interpretar
- [ ] Los iconos son círculos con color primary y símbolo onPrimary
- [ ] Los iconos están ubicados abajo al centro de cada carta
- [ ] Al tocar el icono (o la carta), ejecuta la acción correcta

### Impacto en Código
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/components/`:
  - `HorizontalCardsLayout.kt` - Agregar icono 'i' a CardItem
  - `CrossCardsLayout.kt` - Agregar icono 'i' a CardItem
- `app/src/main/java/com/waveapp/tarotai/presentation/manualload/`:
  - `ManualLoadScreen.kt` - Agregar iconos '+', 'lápiz', 'i' según estado en ManualLoadCardItem

### Prioridad
**Media** - Mejora significativa de UX pero no bloqueante

---

## Resumen de Cambios

### Impacto en Documentación
| Documento | Cambios |
|-----------|---------|
| `funcionales.md` | Actualizar RF-06 (guardado) y RF-10 (historial) |
| `ui-ux.md` | Agregar especificaciones de teclado y botones de cierre |
| `index.md` | Agregar referencia a v1.2.0 |

### Impacto en Código
| Componente | Archivos Afectados |
|------------|-------------------|
| **QuestionScreen** | 1 archivo modificado |
| **ReadingViewModel** | 1 archivo modificado |
| **ManualLoadViewModel** | 1 archivo modificado |
| **HistoryScreen** | 1 archivo modificado |
| **NavGraph** | 1 archivo modificado |
| **Todas las pantallas** | 9 archivos modificados (botón X) |
| **Persistencia** | 4 archivos revisados/corregidos |

### Total Estimado
- **Archivos modificados**: ~15
- **Requisitos nuevos/modificados**: 5 (RF-13 a RF-17)
- **Tiempo estimado**: ~10 horas

---

## Notas de Implementación

### Orden Sugerido de Tareas
1. **RF-16** (Remover botón +) - 30 min ✅
2. **RF-13** (Teclado en QuestionScreen) - 2h ✅
3. **RF-14** (Guardado manual) - 3h ✅
4. **RF-14.1** (Correcciones UX guardado) - 1h ⏳
5. **RF-17** (Bug fix guardado) - 2.5h ⏳
6. **RF-15** (Botones X cierre) - 2h ⏳

### Testing Requerido
- **Manual**: Probar cada pantalla con teclado virtual
- **Manual**: Verificar guardado y recuperación de lecturas
- **Manual**: Probar navegación con botón X desde cada pantalla
- **Persistencia**: Cerrar app y verificar que datos persisten

---

## RF-21: Entrada de Voz en QuestionScreen (v1.3.0) 🆕

### Descripción
Permitir al usuario dictar su pregunta usando reconocimiento de voz, facilitando la entrada de texto especialmente para preguntas largas o consultas reflexivas.

### Problema Actual
- El usuario debe escribir manualmente la pregunta
- Puede ser tedioso en dispositivos móviles
- Algunas preguntas son largas y reflexivas (difícil escribir con teclado)

### Solución Requerida
- **Botón de micrófono**: Icono de micrófono al lado del campo de texto de pregunta
- **Reconocimiento de voz nativo**: Usar SpeechRecognizer API de Android (gratuito, integrado)
- **Transcripción en tiempo real**: El texto reconocido se escribe directamente en el TextField
- **Feedback visual**: Indicador animado mientras está escuchando
- **Idioma español**: Configurar reconocimiento para español (es-ES o es-MX)
- **Manejo de permisos**: Solicitar permiso de micrófono en tiempo de ejecución
- **Manejo de errores**: Si no hay conexión, no hay micrófono, o no se entiende el audio

### Criterios de Aceptación
- [ ] Botón de micrófono visible al lado del campo "Pregunta"
- [ ] Al tocar el micrófono, se solicita permiso si es primera vez
- [ ] Al activarse, muestra indicador visual (animación o cambio de color)
- [ ] El texto reconocido se escribe en el TextField en tiempo real
- [ ] Se puede detener manualmente o se detiene automáticamente por timeout
- [ ] Funciona en español (es-ES)
- [ ] Muestra mensaje de error si falla el reconocimiento
- [ ] El botón funciona tanto en modo automático como manual

### Especificaciones Técnicas

#### Android Manifest - Permisos
```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.RECORD_AUDIO" />
```

#### SpeechRecognizer Setup
```kotlin
// Configuración del reconocedor
val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")  // Español de España
    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)  // Resultados parciales en tiempo real
    putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
}
```

#### Estados de Reconocimiento
```kotlin
sealed class SpeechRecognitionState {
    object Idle : SpeechRecognitionState()
    object Listening : SpeechRecognitionState()
    object Processing : SpeechRecognitionState()
    data class Error(val message: String) : SpeechRecognitionState()
}
```

#### Componente UI
```kotlin
// En QuestionScreen.kt
OutlinedTextField(
    value = question,
    onValueChange = { question = it },
    label = { Text("Tu pregunta") },
    trailingIcon = {
        IconButton(
            onClick = {
                if (speechState is SpeechRecognitionState.Listening) {
                    // Detener reconocimiento
                    viewModel.stopListening()
                } else {
                    // Iniciar reconocimiento
                    viewModel.startListening()
                }
            }
        ) {
            when (speechState) {
                is SpeechRecognitionState.Idle -> {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Dictar pregunta",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                is SpeechRecognitionState.Listening -> {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Detener dictado",
                        tint = Color.Red,
                        modifier = Modifier.scale(1.2f)  // Animación pulsante
                    )
                }
                is SpeechRecognitionState.Processing -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    },
    modifier = Modifier.fillMaxWidth()
)

// Mostrar error si existe
if (speechState is SpeechRecognitionState.Error) {
    Text(
        text = speechState.message,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall
    )
}
```

### Manejo de Permisos

#### ViewModel
```kotlin
// QuestionViewModel.kt
class QuestionViewModel : ViewModel() {

    private val _speechState = MutableStateFlow<SpeechRecognitionState>(SpeechRecognitionState.Idle)
    val speechState: StateFlow<SpeechRecognitionState> = _speechState.asStateFlow()

    private var speechRecognizer: SpeechRecognizer? = null

    fun initSpeechRecognizer(context: Context) {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matches?.firstOrNull()?.let { recognizedText ->
                    _question.value = recognizedText
                }
                _speechState.value = SpeechRecognitionState.Idle
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                matches?.firstOrNull()?.let { partialText ->
                    _question.value = partialText  // Actualizar en tiempo real
                }
            }

            override fun onError(error: Int) {
                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Error de audio"
                    SpeechRecognizer.ERROR_CLIENT -> "Error del cliente"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permiso de micrófono denegado"
                    SpeechRecognizer.ERROR_NETWORK -> "Error de conexión"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Tiempo de espera agotado"
                    SpeechRecognizer.ERROR_NO_MATCH -> "No se entendió el audio"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Reconocedor ocupado"
                    SpeechRecognizer.ERROR_SERVER -> "Error del servidor"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No se detectó voz"
                    else -> "Error desconocido"
                }
                _speechState.value = SpeechRecognitionState.Error(errorMessage)
            }

            override fun onBeginningOfSpeech() {
                _speechState.value = SpeechRecognitionState.Listening
            }

            override fun onEndOfSpeech() {
                _speechState.value = SpeechRecognitionState.Processing
            }

            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        speechRecognizer?.startListening(intent)
        _speechState.value = SpeechRecognitionState.Listening
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
        _speechState.value = SpeechRecognitionState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizer?.destroy()
    }
}
```

#### Solicitud de Permisos en Compose
```kotlin
// QuestionScreen.kt
val context = LocalContext.current
val permissionLauncher = rememberLauncherForActivityResult(
    ActivityResultContracts.RequestPermission()
) { isGranted ->
    if (isGranted) {
        viewModel.startListening()
    } else {
        // Mostrar mensaje explicativo
        Toast.makeText(context, "Permiso de micrófono requerido", Toast.LENGTH_SHORT).show()
    }
}

// Al tocar el botón de micrófono
IconButton(onClick = {
    when {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED -> {
            // Permiso ya concedido
            viewModel.startListening()
        }
        else -> {
            // Solicitar permiso
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }
})
```

### Flujo de Usuario

1. **Usuario llega a QuestionScreen**
2. **Ve campo "Pregunta" con icono de micrófono**
3. **Toca el icono de micrófono**
4. **Si es primera vez**: Sistema solicita permiso de micrófono
5. **Si acepta**: Ícono cambia a rojo (pulsante) - indica escuchando
6. **Usuario habla su pregunta**
7. **Texto se transcribe en tiempo real** en el TextField
8. **Usuario detiene** (tocando icono nuevamente) o espera timeout automático
9. **Ícono vuelve a estado normal**
10. **Usuario revisa/edita** el texto si es necesario
11. **Continúa** con el flujo normal

### Consideraciones de Diseño

#### UX
- **Icono intuitivo**: Micrófono es universalmente reconocido
- **Feedback visual claro**: Rojo pulsante = escuchando
- **No intrusivo**: No bloquea el teclado manual, es complementario
- **Editable**: El usuario puede corregir errores de transcripción

#### Performance
- **Reconocimiento en dispositivo si disponible**: Android 12+ puede usar reconocimiento offline
- **Fallback a nube**: Si no hay soporte local, usa API de Google (requiere conexión)
- **Timeout automático**: 10 segundos sin hablar = detiene automáticamente

#### Accesibilidad
- **ContentDescription**: Describir estado del botón para lectores de pantalla
- **Alternativa de teclado**: Siempre disponible si voz falla
- **Mensajes de error claros**: Guiar al usuario en caso de problemas

### Dependencias

**No requiere dependencias externas** - API nativa de Android

### Impacto en Código

**Archivos a crear**:
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/viewmodel/QuestionViewModel.kt` (si no existe)

**Archivos a modificar**:
- `app/src/main/AndroidManifest.xml` - Agregar permiso RECORD_AUDIO
- `app/src/main/java/com/waveapp/tarotai/presentation/reading/QuestionScreen.kt` - Agregar botón y lógica de voz

### Prioridad
**Media** - Funcionalidad útil pero no bloqueante, mejora significativa de UX

---

*Última actualización: 2026-05-11*
