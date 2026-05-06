# Requisitos Funcionales - TarotAI

## RF-01: Seleccionar Tipo de Tirada

**Descripción**: El usuario puede elegir entre 5 tipos predefinidos de tiradas.

**Contexto**: El usuario quiere hacer una consulta al tarot y debe seleccionar el formato adecuado para su necesidad.

**Comportamiento esperado**:
- El usuario accede a la función de nueva tirada
- Selecciona uno de los 5 tipos disponibles:
  - Carta Simple (1 carta)
  - Sí o No (1 carta)
  - Presente (3 cartas)
  - Tendencia (3 cartas)
  - Cruz (5 cartas)

**Criterios de Aceptación**:
- [ ] El usuario puede elegir entre los 5 tipos de tirada
- [ ] Cada tipo muestra clara descripción de su propósito
- [ ] La selección es intuitiva e accesible en la interfaz

---

## RF-02: Formular Pregunta y Datos del Consultante (Actualizado v1.1.0) 🔄

**Descripción**: El usuario puede ingresar una pregunta de texto libre y opcionalmente el nombre del consultante para contextualizar su tirada.

**Contexto**: La IA necesita la pregunta del usuario para generar interpretaciones personalizadas. Además, para poder guardar lecturas en el historial (RF-11), es útil capturar el nombre del consultante desde el inicio.

**Comportamiento esperado**:
- Campo de entrada de texto para la pregunta
- **Nuevo**: Toggle "Esta lectura es para alguien más"
- **Nuevo**: Campo de nombre del consultante (visible cuando toggle está ON)
- Validación de entrada (mínimo 10 caracteres)
- Pregunta obligatoria para todas las tiradas excepto "Carta Simple"

**Modos de Operación**:

| Modo | Toggle | Campo Consultante | Uso |
|------|--------|-------------------|-----|
| **Automático** | Opcional (OFF por defecto) | Visible solo si toggle ON | Tirada normal para uno mismo o para otro |
| **Manual** | Forzado a ON | Siempre visible y obligatorio | Carga manual requiere consultante |

**Criterios de Aceptación**:
- [ ] El usuario puede ingresar una pregunta de texto (mínimo 10 caracteres)
- [ ] La pregunta es obligatoria para todas las tiradas excepto "Carta Simple"
- [ ] Se muestra mensaje de error si la pregunta es muy corta
- [ ] **Nuevo**: Existe toggle "Esta lectura es para alguien más"
- [ ] **Nuevo**: Campo consultante visible solo cuando toggle está ON (modo automático)
- [ ] **Nuevo**: En modo manual (`isManualLoad=true`), toggle forzado a ON y campo obligatorio
- [ ] **Nuevo**: Validación: consultante mínimo 2 caracteres, máximo 100
- [ ] La pregunta y consultante se mantienen durante todo el proceso de tirada

---

## RF-03: Seleccionar Cartas Aleatoriamente

**Descripción**: El sistema selecciona automáticamente cartas aleatorias del mazo de 78 cartas.

**Contexto**: Simular el proceso real de tirada aleatoria.

**Comportamiento esperado**:
- Selección aleatoria del mazo de 78 cartas (22 Arcanos Mayores + 56 Arcanos Menores)
- Las cartas pueden aparecer en posición normal o invertida (50% probabilidad cada una)
- No se pueden repetir cartas en una misma tirada

**Criterios de Aceptación**:
- [ ] Las cartas se seleccionan aleatoriamente del mazo de 78 cartas
- [ ] Las cartas pueden aparecer en posición normal o invertida (50% probabilidad)
- [ ] No se pueden repetir cartas en una misma tirada
- [ ] La cantidad de cartas corresponde al tipo de tirada seleccionado

---

## RF-04: Mostrar Cartas con Orientación

**Descripción**: Las cartas seleccionadas se muestran visualmente en su posición correcta (normal o invertida).

**Contexto**: El usuario necesita ver claramente qué cartas salieron y en qué posición.

**Comportamiento esperado**:
- Mostrar imagen de la carta del Tarot de Marsella
- Cartas invertidas mostradas rotadas 180°
- Disposición espacial según tipo de tirada

**Criterios de Aceptación**:
- [ ] La interfaz muestra visualmente las cartas en su posición correspondiente
- [ ] Las cartas invertidas se muestran claramente rotadas
- [ ] La tirada "Cruz" se muestra con disposición en forma de cruz (no lineal)
- [ ] Otras tiradas se muestran en disposición horizontal
- [ ] Existe animación al revelar las cartas

---

## RF-05: Recibir Interpretación Personalizada

**Descripción**: El sistema genera una interpretación contextualizada a la pregunta del usuario basada en las cartas de la tirada.

**Contexto**: El usuario necesita comprender el significado de las cartas en relación con su pregunta específica.

**Comportamiento esperado**:
- Sistema procesa las cartas seleccionadas y la pregunta
- API de Claude genera interpretación contextualizada
- Se muestra indicador de carga durante generación
- Opción de reintentar si falla la generación

**Criterios de Aceptación**:
- [ ] Las interpretaciones son generadas por la API de Claude
- [ ] La generación de interpretación debe tener un indicador de carga visible
- [ ] Si falla la generación, mostrar mensaje de error y opción de reintentar

---

## RF-06: Interpretación Individual de Cartas

**Descripción**: El sistema proporciona explicación de cada carta considerando su posición, orientación y contexto.

**Contexto**: El usuario necesita comprender el significado específico de cada carta en la tirada.

**Comportamiento esperado**:
- Explicación para cada carta que incluya:
  - Su posición en la tirada (ej: "como pasado", "como obstáculo")
  - Su orientación (derecha o invertida)
  - Su relación con la pregunta formulada

**Criterios de Aceptación**:
- [ ] La interpretación individual explica cada carta considerando su posición
- [ ] Considera la orientación (derecha o invertida)
- [ ] Relaciona la carta con la pregunta formulada

---

## RF-07: Interpretación General Holística

**Descripción**: El sistema sintetiza un mensaje global que relaciona todas las cartas en conjunto.

**Contexto**: El usuario necesita una perspectiva global del significado de la tirada completa.

**Comportamiento esperado**:
- Análisis holístico que integra todas las cartas
- Relaciona el conjunto de cartas con la pregunta
- Caso especial para tirada "Sí o No": respuesta binaria clara con justificación

**Criterios de Aceptación**:
- [ ] La interpretación general sintetiza el mensaje de todas las cartas en conjunto
- [ ] Para la tirada "Sí o No": da respuesta clara (Sí/No/Indefinido)
- [ ] Explica por qué esa carta significa esa respuesta (objetivo educativo)
- [ ] Relaciona el simbolismo de la carta con la interpretación binaria

---

## RF-08: Acceder a Enciclopedia de Cartas

**Descripción**: El usuario puede consultar información de cualquiera de las 78 cartas sin realizar una tirada.

**Contexto**: El usuario quiere aprender sobre el significado de las cartas de forma independiente.

**Comportamiento esperado**:
- Navegación a la enciclopedia desde el menú principal
- Visualización de lista completa de 78 cartas
- Búsqueda/filtrado por tipo (Arcanos Mayores/Menores)
- Selección de cualquier carta para ver detalle completo
- Información disponible sin conexión a internet

**Criterios de Aceptación**:
- [ ] Todas las 78 cartas del Tarot de Marsella están disponibles
- [ ] La información está almacenada localmente (no requiere IA ni internet)
- [ ] La lista de cartas es navegable y se puede filtrar por tipo
- [ ] Desde una tirada realizada, el usuario puede tocar una carta para ver su detalle completo
- [ ] Al ver el detalle desde una tirada, aparece un botón adicional "Interpretar con IA" (funcionalidad de Fase 4)

---

## RF-09: Información Detallada de Cartas

**Descripción**: Cada carta en la enciclopedia incluye información completa y estructurada.

**Contexto**: El usuario necesita aprender sobre cada carta del tarot.

**Información incluida en cada carta**:
1. **Nombre**: Nombre completo de la carta (ej: "El Loco", "As de Copas")
2. **Imagen**: Representación visual del Tarot de Marsella
3. **Significado General**: Descripción del arquetipo o concepto que representa
4. **Significado en Posición Derecha**: Interpretación cuando aparece normal
5. **Significado en Posición Invertida**: Interpretación cuando aparece al revés
6. **Simbolismo e Iconografía**: Explicación de los símbolos visuales en la carta
7. **Palabras Clave**: Lista de 5-8 conceptos principales asociados a la carta

**Criterios de Aceptación**:
- [ ] Todas las 78 cartas incluyen los 7 elementos de información
- [ ] Las imágenes de las cartas son de dominio público
- [ ] La información es clara, comprensible y educativa

---

## RF-10: Cargar Imágenes de Cartas

**Descripción**: Las imágenes de las 78 cartas se cargan y se muestran en la interfaz.

**Contexto**: El usuario necesita visualizar representaciones reales del Tarot de Marsella.

**Comportamiento esperado**:
- Carga de imágenes locales del Tarot de Marsella
- Visualización clara en diferentes tamaños de pantalla

**Criterios de Aceptación**:
- [ ] Las cartas se muestran con las imágenes reales del Tarot de Marsella
- [ ] Las imágenes cargan en menos de 2 segundos
- [ ] La interfaz es usable y clara en pantallas de diferentes tamaños

---

---

## RF-11: Historial de Lecturas Guardadas (v1.1.0) 🆕

**Descripción**: El usuario puede guardar lecturas con nombre de consultante y agregar notas personales.

**Contexto**: Los tarotistas profesionales necesitan llevar un registro de las consultas realizadas para realizar seguimiento de consultantes y revisar interpretaciones pasadas.

**Comportamiento esperado**:
- Guardar lectura después de interpretación
- Asignar nombre de consultante (obligatorio)
- Acceder al historial desde menú principal
- Ver lista de lecturas ordenadas por fecha
- Agregar y editar notas en lecturas guardadas
- Ver interpretación completa de lecturas pasadas

**Criterios de Aceptación**:
- [ ] Botón "Guardar Lectura" visible después de interpretación
- [ ] Diálogo solicita nombre de consultante (obligatorio, mín 2 caracteres)
- [ ] Opción "Historial" en menú principal
- [ ] Historial muestra lecturas ordenadas por fecha descendente
- [ ] Cada entrada muestra: consultante, fecha, tipo de tirada, pregunta
- [ ] Al tocar lectura, se ve detalle completo con interpretación
- [ ] Campo "Notas Personales" editable en detalle de lectura
- [ ] Notas se guardan automáticamente (sin botón "Guardar")
- [ ] Lecturas persisten entre sesiones
- [ ] Cartas en detalle son clickeables → CardDetailScreen

**Documentación completa**: Ver [historial.md](./historial.md)

---

## RF-12: Carga Manual de Tiradas (v1.1.0) 🆕

**Descripción**: El usuario puede cargar manualmente las cartas de una tirada física para obtener interpretación por IA.

**Contexto**: Los tarotistas profesionales realizan lecturas con mazos físicos y necesitan que la app interprete las cartas que ellos seleccionaron manualmente.

**Comportamiento esperado**:
- Opción "Cargar Lectura" desde menú principal
- Seleccionar tipo de tirada y datos (consultante, pregunta)
- Pantalla muestra cartas boca abajo con ícono "+"
- Al tocar "+", abre selector de cartas con filtros excluyentes
- Seleccionar carta y orientación (Derecha/Invertida)
- Editar cartas antes de interpretar
- Botón "Interpretar" habilitado cuando todas las cartas están cargadas
- Después de interpretación, comportamiento idéntico a tirada automática

**Criterios de Aceptación**:
- [ ] Opción "Cargar Lectura" en menú principal
- [ ] Primera pantalla pide: tipo de tirada, consultante, pregunta
- [ ] Segunda pantalla muestra N cartas boca abajo (según tipo de tirada)
- [ ] Cada carta tiene ícono "+" visible
- [ ] Al tocar "+", abre selector con filtros excluyentes (Todos/Mayores/Menores por palo)
- [ ] Los filtros son excluyentes (solo uno activo a la vez)
- [ ] Al seleccionar carta, pregunta orientación (Derecha/Invertida)
- [ ] Cartas ya seleccionadas no son clickeables en el selector
- [ ] Se pueden cambiar cartas ya cargadas antes de interpretar
- [ ] Botón "Interpretar" deshabilitado si faltan cartas
- [ ] Una vez interpretada, las cartas ya no se pueden editar
- [ ] Tocar carta interpretada → CardDetailScreen
- [ ] La lectura se puede guardar en historial igual que tirada automática

**Documentación completa**: Ver [carga-manual.md](./carga-manual.md)

---

## Resumen

**Total de Requisitos Funcionales: 12**

### v1.0.0 ✅

| ID | Requisito | Prioridad |
|----|-----------|-----------|
| RF-01 | Seleccionar Tipo de Tirada | Alta |
| RF-02 | Formular Pregunta | Alta |
| RF-03 | Seleccionar Cartas Aleatoriamente | Alta |
| RF-04 | Mostrar Cartas con Orientación | Alta |
| RF-05 | Recibir Interpretación Personalizada | Alta |
| RF-06 | Interpretación Individual de Cartas | Media |
| RF-07 | Interpretación General Holística | Media |
| RF-08 | Acceder a Enciclopedia de Cartas | Alta |
| RF-09 | Información Detallada de Cartas | Media |
| RF-10 | Cargar Imágenes de Cartas | Alta |

### v1.1.0 🆕

| ID | Requisito | Prioridad |
|----|-----------|-----------|
| RF-11 | Historial de Lecturas Guardadas | Alta |
| RF-12 | Carga Manual de Tiradas | Alta |

---

*Documento actualizado para v1.1.0 - Última actualización: 2026-05-06*
