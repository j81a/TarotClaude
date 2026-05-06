# FASE 6: Historial de Lecturas Guardadas (v1.1.0)

## 🎯 Objetivo
Implementar RF-11: Sistema de historial para guardar lecturas con nombre de consultante y notas editables.

## 📋 Tareas

### Tarea 6.1: Actualizar Base de Datos (Room)
**Tiempo**: 1.5h

**Crear**:
- `ReadingHistoryEntity.kt`
- `ReadingHistoryDao.kt`
- Actualizar `TarotDatabase.kt`
- `Converters.kt` (para serializar DrawnCards e Interpretation)

**Criterios**:
- [ ] Entidad con timestamp, consultantName, spreadType, question, drawnCardsJson, interpretationJson, notes
- [ ] DAO con queries: getAllReadings(), getReadingById(), insertReading(), updateNotes()
- [ ] TypeConverters para JSON
- [ ] Índice en timestamp
- [ ] Database version incrementada
- [ ] Migration implementada

---

### Tarea 6.2: Actualizar QuestionScreen (Reutilización)
**Tiempo**: 2h

**Modificar**:
- `QuestionScreen.kt`
- `QuestionViewModel.kt`
- `Screen.kt` (agregar parámetro `isManualLoad`)

**Agregar**:
- Toggle "Esta lectura es para alguien más"
- Campo consultante (visible si toggle ON)
- Lógica condicional: modo automático (opcional) vs manual (obligatorio)

**Criterios**:
- [ ] Parámetro `isManualLoad: Boolean` agregado
- [ ] Toggle implementado (OFF por defecto en modo automático)
- [ ] Campo consultante visible condicionalmente
- [ ] Validación: consultante 2-100 caracteres
- [ ] En modo manual, toggle forzado a ON
- [ ] Hint visual diferente según modo

---

### Tarea 6.3: Implementar Domain Layer de Historial
**Tiempo**: 2h

**Crear**:
- `ReadingHistory.kt` (domain model)
- `ReadingHistoryRepository.kt` (interface)
- `ReadingHistoryRepositoryImpl.kt`
- `SaveReadingUseCase.kt`
- `GetAllReadingsUseCase.kt`
- `GetReadingByIdUseCase.kt`
- `UpdateReadingNotesUseCase.kt`
- Mappers entre Entity y Domain

**Criterios**:
- [ ] Domain model con 7 campos (timestamp, consultantName, spreadType, question, drawnCards, interpretation, notes)
- [ ] Repositorio con Flow para lecturas
- [ ] 4 Use Cases implementados
- [ ] Mappers bidireccionales
- [ ] Tests unitarios

---

### Tarea 6.4: Implementar UI de Historial
**Tiempo**: 3h

**Crear**:
- `HistoryViewModel.kt`
- `HistoryScreen.kt`
- `ReadingHistoryItem.kt` (componente de lista)

**Criterios**:
- [ ] ViewModel con StateFlow<List<ReadingHistory>>
- [ ] Lista ordenada por fecha descendente
- [ ] Cada entrada muestra: consultante, fecha, tipo tirada, pregunta (truncada)
- [ ] Formato fecha: "DD/MM/YYYY - HH:MM"
- [ ] Empty state si no hay lecturas
- [ ] FAB "+" para nueva lectura
- [ ] Navegación a ReadingDetailScreen
- [ ] Loading y error states

---

### Tarea 6.5: Implementar UI de Detalle con Notas Editables
**Tiempo**: 3.5h

**Crear**:
- `ReadingDetailViewModel.kt`
- `ReadingDetailScreen.kt`
- `NotesTextField.kt` (componente con autosave)

**Criterios**:
- [ ] Muestra encabezado (consultante, fecha, tipo tirada)
- [ ] Muestra pregunta completa
- [ ] Muestra cartas (clickeables → CardDetailScreen)
- [ ] Muestra interpretación completa
- [ ] Campo notas multilinea (mín 3 líneas)
- [ ] Autosave tras 2 segundos de inactividad
- [ ] Indicador visual de guardado (checkmark verde)
- [ ] Contador caracteres (visible > 1800)
- [ ] Máximo 2000 caracteres
- [ ] Manejo de errores de guardado

**Integración con Guardado**:
- [ ] Botón "Guardar Lectura" en InterpretationScreen
- [ ] Si consultante YA ingresado → guarda directamente
- [ ] Si NO ingresado → muestra diálogo pidiendo nombre
- [ ] Toast confirmación "Lectura guardada"
- [ ] Navegar a historial opcional

---

## 🧪 Testing
- [ ] `SaveReadingUseCaseTest`
- [ ] `GetAllReadingsUseCaseTest`
- [ ] `UpdateReadingNotesUseCaseTest`
- [ ] Test integración: guardar y recuperar lectura
- [ ] Test autosave de notas

## ⏱️ Tiempo Total Estimado: ~12 horas
