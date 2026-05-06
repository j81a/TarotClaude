# FASE 7: Carga Manual de Tiradas (v1.1.0)

## 🎯 Objetivo
Implementar RF-12: Permitir a tarotistas cargar manualmente cartas de tiradas físicas para interpretación por IA.

## 📋 Tareas

### Tarea 7.1: Implementar Domain Layer de Carga Manual
**Tiempo**: 2h

**Crear**:
- `ManualLoadConfiguration.kt`
- `ManualLoadState.kt`
- `CardFilter.kt` (enum)
- `ValidateManualLoadConfigUseCase.kt`
- `AddCardToManualLoadUseCase.kt`
- `RemoveCardFromManualLoadUseCase.kt`
- `GetAvailableCardsUseCase.kt` (con filtros)
- `GenerateInterpretationFromManualLoadUseCase.kt`

**Criterios**:
- [ ] ManualLoadConfiguration con spreadType, consultantName, question
- [ ] ManualLoadState con Map<position, DrawnCard>
- [ ] CardFilter: ALL, MAJOR_ARCANA, WANDS, CUPS, SWORDS, PENTACLES
- [ ] 5 Use Cases implementados
- [ ] GetAvailableCardsUseCase excluye cartas ya usadas
- [ ] Tests unitarios

---

### Tarea 7.2: Actualizar TarotCardRepository
**Tiempo**: 1h

**Modificar**:
- `TarotCardRepository.kt`
- `TarotCardRepositoryImpl.kt`

**Agregar**:
```kotlin
suspend fun getAvailableCards(
    excludedIds: List<Int>,
    filter: CardFilter
): Flow<List<TarotCard>>
```

**Criterios**:
- [ ] Método nuevo agregado a interface
- [ ] Implementación con query condicional según filtro
- [ ] Exclusión de IDs funciona
- [ ] Tests para cada tipo de filtro

---

### Tarea 7.3: Implementar ManualLoadScreen
**Tiempo**: 3h

**Crear**:
- `ManualLoadViewModel.kt` (con SavedStateHandle)
- `ManualLoadScreen.kt`
- `CardPlaceholder.kt` (carta boca abajo con "+")
- `CrossLayout.kt` (reutilizar o ajustar)

**Criterios**:
- [ ] ViewModel maneja Map<position, DrawnCard> en SavedStateHandle
- [ ] Layout según tipo de tirada (horizontal o cruz)
- [ ] Cartas boca abajo con ícono "+" centrado
- [ ] Cartas cargadas muestran imagen + badge orientación
- [ ] Progress indicator "X/N cartas cargadas"
- [ ] Botón "Interpretar" deshabilitado si incompleto
- [ ] Al tocar "+" navega a CardSelectorScreen
- [ ] Al tocar carta cargada, permite editar
- [ ] Loading state durante interpretación

---

### Tarea 7.4: Implementar CardSelectorScreen
**Tiempo**: 3.5h

**Crear**:
- `CardSelectorViewModel.kt`
- `CardSelectorScreen.kt`
- `CardFilterChip.kt` (filtros excluyentes)
- `OrientationDialog.kt`

**Criterios**:
- [ ] Grid con LazyVerticalGrid (3 columnas)
- [ ] Filtros como chips (solo uno activo)
- [ ] Al tocar filtro, desactiva los demás
- [ ] Transición visual del filtro activo
- [ ] Cartas ya usadas con overlay gris
- [ ] Badge "En uso" sobre cartas excluidas
- [ ] Al seleccionar carta → diálogo orientación
- [ ] Diálogo con 2 opciones: Derecha / Invertida
- [ ] Botón "Cancelar" en diálogo
- [ ] Loading state mientras carga cartas

---

### Tarea 7.5: Integrar Interpretación de Carga Manual
**Tiempo**: 1.5h

**Modificar**:
- `ManualLoadViewModel.kt`
- `GenerateInterpretationUseCase.kt`

**Criterios**:
- [ ] Botón "Interpretar" llama a GenerateInterpretationUseCase
- [ ] Use Case acepta tanto TarotReading como ManualLoadState
- [ ] Prompt generado igual que tirada automática
- [ ] Navega a InterpretationScreen al finalizar
- [ ] InterpretationScreen funciona idéntico
- [ ] Se puede guardar en historial

---

### Tarea 7.6: Integrar con Flujo Principal
**Tiempo**: 2h

**Modificar**:
- `Screen.kt` (agregar rutas)
- `TarotNavHost.kt`
- `HomeScreen.kt` (agregar botón "Cargar Lectura")

**Agregar rutas**:
```kotlin
object ManualLoad : Screen("manual_load/{spreadType}/{consultantName}/{question}")
object CardSelector : Screen("card_selector/{position}/{excludedIds}")
```

**Criterios**:
- [ ] Botón "Cargar Lectura" en HomeScreen
- [ ] Navega a SpreadTypeScreen con parámetro isManual=true
- [ ] SpreadTypeScreen → QuestionScreen(isManualLoad=true)
- [ ] QuestionScreen → ManualLoadScreen
- [ ] ManualLoadScreen → CardSelectorScreen → ManualLoadScreen
- [ ] ManualLoadScreen → InterpretationScreen
- [ ] Navegación back funciona correctamente
- [ ] Parámetros se pasan correctamente

---

## 🧪 Testing
- [ ] `ValidateManualLoadConfigUseCaseTest`
- [ ] `AddCardToManualLoadUseCaseTest`
- [ ] `GetAvailableCardsUseCaseTest` (todos los filtros)
- [ ] Test integración: flujo completo manual
- [ ] Test edición de cartas antes de interpretar
- [ ] Test prevención de duplicados

## ⏱️ Tiempo Total Estimado: ~13 horas
