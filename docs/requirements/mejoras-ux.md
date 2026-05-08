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

*Última actualización: 2026-05-07*
