# RF-11: Historial de Lecturas Guardadas (v1.1.0) 🆕

## Descripción

El sistema permite guardar lecturas realizadas con información del consultante y notas personales, para realizar seguimiento de consultas a lo largo del tiempo.

---

## Contexto

Los tarotistas profesionales necesitan llevar un registro de las consultas realizadas para:
- Realizar seguimiento de consultantes
- Revisar interpretaciones pasadas
- Agregar notas reflexivas posteriores a la consulta
- Llevar un registro profesional de su práctica

Esta funcionalidad convierte la app en una herramienta profesional de gestión de consultas, no solo una app de entretenimiento.

---

## Comportamiento Esperado

**Dado** que el usuario ha realizado una tirada con interpretación
**Cuando** decide guardar la lectura
**Entonces** debe poder:

1. Guardar con **nombre de consultante** (opcional, por defecto: "Lectura personal")
2. Ver la lectura guardada en el **historial**
3. Acceder al historial desde el **menú principal**
4. **Agregar notas** a una lectura guardada
5. **Editar las notas** existentes en cualquier momento
6. Ver la **interpretación completa** de lecturas pasadas
7. Acceder al detalle de cualquier carta desde el historial

---

## Datos de una Lectura Guardada

Cada lectura en el historial debe incluir los siguientes 7 elementos:

| # | Campo | Tipo | Descripción |
|---|-------|------|-------------|
| 1 | **Fecha y hora** | Timestamp | Cuándo se realizó la tirada |
| 2 | **Nombre del consultante** | String | Campo de texto libre (por defecto: "Lectura personal") |
| 3 | **Tipo de tirada** | SpreadType | Cuál de los 5 tipos fue utilizado |
| 4 | **Pregunta** | String | La pregunta formulada por el consultante |
| 5 | **Cartas seleccionadas** | List\<DrawnCard\> | Las cartas que salieron (con posición y orientación) |
| 6 | **Interpretación completa** | Interpretation | La interpretación generada por la IA |
| 7 | **Notas personales** | List\<Note\> | Lista de notas editables del tarotista |

---

## Pantalla de Historial

La pantalla de historial (`HistoryScreen`) debe mostrar:

### Lista de Lecturas

- **Orden**: Cronológico descendente (más reciente primero)
- **Información visible por entrada**:
  - Nombre del consultante (destacado)
  - Fecha y hora (formato: "DD/MM/YYYY - HH:MM")
  - Tipo de tirada (ej: "Cruz", "Presente")
  - Pregunta (truncada si es muy larga, máx 80 caracteres con "...")

### Interacción

- **Al tocar una lectura** → Navega al detalle completo
- **Botón FAB "+"** → Opción de nueva lectura
- **Empty state** → Si no hay historial, mostrar mensaje invitando a realizar primera lectura

---

## Pantalla de Detalle de Lectura Guardada

Al acceder al detalle de una lectura guardada, se muestra:

### 1. Encabezado
- Nombre del consultante
- Fecha y hora
- Tipo de tirada

### 2. Pregunta
- Pregunta completa (scrolleable si es larga)

### 3. Cartas de la Tirada
- Visualización igual que en `ReadingScreen`
- **Cartas son clickeables** → Navega a `CardDetailScreen`

### 4. Interpretación
- Interpretación individual de cada carta
- Interpretación general
- (Si es Sí/No) Respuesta y justificación

### 5. Notas Personales
- **Campo de texto editable** (siempre visible)
- **Placeholder**: "Agregar notas sobre esta lectura..."
- **Autoguardado** al salir de la pantalla o después de 2 segundos de inactividad
- **Máximo de caracteres**: 2000
- **Contador de caracteres** visible cuando hay más de 1800 caracteres

---

## Gestión de Notas

### Comportamiento de Notas

- **Tipo de campo**: TextField multilinea (mínimo 3 líneas visibles, expandible)
- **Agregar nota**: Escribir directamente en el campo
- **Editar nota**: Tocar el campo y modificar el texto
- **Guardar**: Automático (sin botón "Guardar" explícito)
- **Indicador de guardado**: Ícono de checkmark verde cuando se guarda
- **Sin límite de longitud razonable**: Máximo 2000 caracteres

### Estados del Campo de Notas

| Estado | Descripción | Visual |
|--------|-------------|--------|
| **Vacío** | No hay notas | Placeholder gris claro |
| **Editando** | Usuario está escribiendo | Borde azul, cursor visible |
| **Guardando** | Autosave en progreso | Ícono de "guardando..." |
| **Guardado** | Guardado exitoso | Checkmark verde (2 segundos) |
| **Error** | Falló el guardado | Ícono de error rojo + botón "Reintentar" |

---

## Flujo de Usuario: Captura de Consultante

### Enfoque Recomendado: Captura en QuestionScreen (v1.1.0 actualizado)

```
1. Usuario inicia tirada (automática o manual)
   ↓
2. En QuestionScreen:
   - Activa toggle "Esta lectura es para alguien más"
   - Ingresa nombre del consultante (opcional en modo automático)
   ↓
3. Completa tirada y ve interpretación
   ↓
4. Toca botón "Guardar Lectura"
   - Si YA ingresó consultante → Guarda directamente
   - Si NO ingresó consultante → Muestra diálogo pidiendo nombre
   ↓
5. Toast: "Lectura guardada"
```

### Flujo Alternativo: Captura al Guardar (fallback)

Si el usuario NO ingresó consultante en QuestionScreen:

```
1. Usuario ve la interpretación en InterpretationScreen
   ↓
2. Toca botón "Guardar Lectura"
   ↓
3. Aparece diálogo "Guardar Lectura":
   - Campo: "Nombre del consultante" (obligatorio, mín 2 caracteres)
   - Botón "Cancelar"
   - Botón "Guardar" (deshabilitado si campo vacío)
   ↓
4. Usuario ingresa nombre y toca "Guardar"
   ↓
5. Toast: "Lectura guardada"
```

**Ventaja del enfoque recomendado**: Usuario tiene el contexto fresco al inicio de la lectura

---

## Flujo de Usuario: Ver y Editar Notas

```
1. Usuario abre Historial
   ↓
2. Toca una lectura guardada
   ↓
3. Navega a ReadingDetailScreen
   ↓
4. Scrollea hasta la sección "Notas"
   ↓
5. Toca el campo de texto
   ↓
6. Escribe o edita notas
   ↓
7. (Automático después de 2 segundos) → Se guarda
   ↓
8. Aparece checkmark verde de confirmación
```

---

## Criterios de Aceptación

### Guardado de Lecturas
- [ ] **CA-11.1**: Al completar una tirada con interpretación, existe botón "Guardar Lectura"
- [ ] **CA-11.2**: Al tocar "Guardar", aparece diálogo solicitando nombre del consultante
- [ ] **CA-11.3**: El nombre del consultante es obligatorio (mínimo 2 caracteres)
- [ ] **CA-11.4**: La lectura se guarda en base de datos local (Room) con todos los datos

### Pantalla de Historial
- [ ] **CA-11.5**: Existe opción "Historial" en el menú principal (HomeScreen)
- [ ] **CA-11.6**: El historial muestra todas las lecturas guardadas ordenadas por fecha descendente
- [ ] **CA-11.7**: Cada entrada muestra: consultante, fecha, tipo de tirada, pregunta (truncada)
- [ ] **CA-11.8**: Si no hay lecturas, muestra empty state con mensaje apropiado

### Detalle de Lectura Guardada
- [ ] **CA-11.9**: Al tocar una lectura, se ve el detalle completo con todos los elementos
- [ ] **CA-11.10**: Las cartas son clickeables y navegan a CardDetailScreen
- [ ] **CA-11.11**: La interpretación completa es visible (individual + general)

### Gestión de Notas
- [ ] **CA-11.12**: Existe campo "Notas Personales" en el detalle de lectura
- [ ] **CA-11.13**: Las notas se pueden agregar desde el detalle
- [ ] **CA-11.14**: Las notas se pueden editar en cualquier momento
- [ ] **CA-11.15**: Las notas se guardan automáticamente (sin botón "Guardar")
- [ ] **CA-11.16**: Existe indicador visual de guardado exitoso
- [ ] **CA-11.17**: El límite de 2000 caracteres se respeta con contador visible

### Persistencia
- [ ] **CA-11.18**: Las lecturas guardadas persisten entre sesiones de la app
- [ ] **CA-11.19**: Las notas editadas persisten correctamente en la base de datos
- [ ] **CA-11.20**: No hay pérdida de datos al cerrar y reabrir la app

---

## Modelos de Dominio (Nuevos)

### ReadingHistory
```kotlin
data class ReadingHistory(
    val id: Long,
    val timestamp: Long,
    val consultantName: String,
    val spreadType: SpreadType,
    val question: String?,
    val drawnCards: List<DrawnCard>,
    val interpretation: Interpretation,
    val notes: String?
)
```

### Entidad Room (Nueva)
```kotlin
@Entity(tableName = "reading_history")
data class ReadingHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val consultantName: String,
    val spreadType: String,
    val question: String?,
    val drawnCardsJson: String,        // JSON serializado
    val interpretationJson: String,    // JSON serializado
    val notes: String?
)
```

---

## Navegación (Actualizada)

### Nuevas Rutas

```kotlin
// En Screen.kt
sealed class Screen(val route: String) {
    // ... rutas existentes ...

    object History : Screen("history")
    object ReadingDetail : Screen("reading_detail/{readingId}") {
        fun createRoute(readingId: Long) = "reading_detail/$readingId"
    }
}
```

### Flujo de Navegación

```
HomeScreen
    ↓ (opción "Historial")
HistoryScreen
    ↓ (tocar lectura)
ReadingDetailScreen
    ↓ (tocar carta)
CardDetailScreen
```

---

## Casos de Uso (Nuevos)

### 1. SaveReadingUseCase
- **Input**: `TarotReading`, `consultantName: String`
- **Output**: `Result<Long>` (ID de la lectura guardada)
- **Lógica**: Guarda lectura con timestamp actual

### 2. GetAllReadingsUseCase
- **Input**: Ninguno
- **Output**: `Flow<List<ReadingHistory>>`
- **Lógica**: Obtiene todas las lecturas ordenadas por fecha

### 3. GetReadingByIdUseCase
- **Input**: `readingId: Long`
- **Output**: `Result<ReadingHistory>`
- **Lógica**: Obtiene lectura específica por ID

### 4. UpdateReadingNotesUseCase
- **Input**: `readingId: Long`, `notes: String`
- **Output**: `Result<Unit>`
- **Lógica**: Actualiza solo las notas de una lectura

### 5. DeleteReadingUseCase (opcional para v1.1.0)
- **Input**: `readingId: Long`
- **Output**: `Result<Unit>`
- **Lógica**: Elimina lectura del historial

---

## UI/UX - Especificaciones Visuales

### Pantalla de Historial

```
┌─────────────────────────────┐
│  ← Historial                │
├─────────────────────────────┤
│                             │
│  ┌───────────────────────┐  │
│  │ María González        │  │
│  │ 05/05/2026 - 14:30    │  │
│  │ Tirada: Cruz          │  │
│  │ "¿Qué debo hacer..." │   │
│  └───────────────────────┘  │
│                             │
│  ┌───────────────────────┐  │
│  │ Juan Pérez            │  │
│  │ 04/05/2026 - 10:15    │  │
│  │ Tirada: Presente      │  │
│  │ "¿Cómo está mi tra..." │  │
│  └───────────────────────┘  │
│                             │
│             [+]             │  ← FAB
└─────────────────────────────┘
```

### Detalle de Lectura con Notas

```
┌─────────────────────────────┐
│  ← Lectura de María         │
├─────────────────────────────┤
│  05/05/2026 - 14:30         │
│  Tirada: Cruz               │
│                             │
│  Pregunta:                  │
│  "¿Qué debo hacer para..." │
│                             │
│  [Cartas en layout de cruz] │
│                             │
│  Interpretación:            │
│  [Contenido scrolleable]    │
│                             │
│  ─────────────────────────  │
│                             │
│  Notas personales: ✓        │  ← Checkmark si guardado
│  ┌─────────────────────────┐│
│  │ La consultante se       ││
│  │ mostró muy receptiva... ││
│  │                         ││
│  └─────────────────────────┘│
│  1245/2000 caracteres       │  ← Contador
│                             │
└─────────────────────────────┘
```

---

## Testing

### Tests Unitarios

- [ ] `SaveReadingUseCaseTest`: Verifica guardado correcto
- [ ] `GetAllReadingsUseCaseTest`: Verifica orden descendente
- [ ] `UpdateReadingNotesUseCaseTest`: Verifica actualización de notas
- [ ] `HistoryViewModelTest`: Verifica estados (loading, success, error)

### Tests de Integración

- [ ] Guardar lectura y recuperarla del historial
- [ ] Editar notas y verificar persistencia
- [ ] Navegación desde historial a detalle y a detalle de carta

---

## Tiempo Estimado de Implementación

| Subtarea | Tiempo |
|----------|--------|
| Entidades y DAO de Room | 1.5h |
| Casos de uso | 2h |
| ViewModels | 1.5h |
| UI - HistoryScreen | 2h |
| UI - ReadingDetailScreen | 2.5h |
| UI - Diálogo de guardado | 1h |
| Tests | 2h |
| **TOTAL** | **~12.5 horas** |

---

## Notas Técnicas

### Serialización de Objetos Complejos

Para guardar `List<DrawnCard>` y `Interpretation` en Room:

```kotlin
@TypeConverter
fun fromDrawnCardsList(cards: List<DrawnCard>): String {
    return Json.encodeToString(cards)
}

@TypeConverter
fun toDrawnCardsList(cardsJson: String): List<DrawnCard> {
    return Json.decodeFromString(cardsJson)
}
```

### Índices en la Base de Datos

Para optimizar queries por fecha:

```kotlin
@Entity(
    tableName = "reading_history",
    indices = [Index(value = ["timestamp"], unique = false)]
)
```

---

## Dependencias de Otras Funcionalidades

- ✅ Requiere que el sistema de tiradas esté implementado (Fase 3)
- ✅ Requiere que la interpretación por IA funcione (Fase 4)
- ✅ Requiere Room Database configurado (Fase 1)
- ❌ No bloquea la implementación de RF-12 (Carga Manual)

---

*Documento creado para v1.1.0 - Última actualización: 2026-05-06*