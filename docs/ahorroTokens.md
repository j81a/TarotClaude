# Guía de Optimización de Tokens en Claude Code

> 
>
> Esta guía explica cómo reducir el consumo de tokens al trabajar con Claude Code,
> permitiendo sesiones más largas y evitando límites diarios.

---

## 📋 Índice

1. [El problema](#el-problema)
2. [Cómo funciona el consumo de tokens](#cómo-funciona-el-consumo-de-tokens)
3. [Los 3 grandes consumidores](#los-3-grandes-consumidores)
4. [Técnicas de optimización](#técnicas-de-optimización)
5. [Chunking de documentos](#chunking-de-documentos)
6. [Checklist de ahorro](#checklist-de-ahorro)
7. [Mejores prácticas](#mejores-prácticas)

---

## El problema

**Síntoma:** El límite diario de tokens se agota en pocas horas de trabajo.

**Causas comunes:**
- Múltiples archivos abiertos en el IDE
- Documentos de especificación muy grandes
- Historial de conversación acumulado
- Uso ineficiente del contexto

**Impacto:**
- Sesiones interrumpidas
- Pérdida de productividad
- Espera de 3-4 horas para reset del límite

---

## Cómo funciona el consumo de tokens

Cada mensaje que envías consume tokens de **3 fuentes**:

```
┌─────────────────────────────────────────┐
│ ENTRADA (tu mensaje)                    │
│ • Tu pregunta/instrucción               │
│ • Archivos abiertos en el IDE          │ ← Automático
│ • System reminders                      │ ← Automático
└─────────────────────────────────────────┘
          ↓
┌─────────────────────────────────────────┐
│ PROCESAMIENTO (contexto del asistente) │
│ • Archivos que el asistente lee        │ ← Controlable
│ • Archivos que edita                   │ ← Necesario
│ • Búsquedas y exploraciones            │ ← Controlable
└─────────────────────────────────────────┘
          ↓
┌─────────────────────────────────────────┐
│ SALIDA (respuesta)                      │
│ • Explicación del asistente            │
│ • Código generado                       │
│ • Resultados de herramientas           │
└─────────────────────────────────────────┘
```

### Ejemplo de consumo típico:

**Mensaje sin optimizar (~50,000 tokens):**
```
Tu pregunta:                        200 tokens
Archivos abiertos (5 archivos):  15,000 tokens ⚠️
Historial de conversación:       20,000 tokens ⚠️
Archivos leídos por asistente:    5,000 tokens ⚠️
Respuesta del asistente:          5,000 tokens
────────────────────────────────────────────────
TOTAL:                           45,200 tokens
```

**Mensaje optimizado (~8,000 tokens):**
```
Tu pregunta:                        200 tokens
Archivos abiertos (0 archivos):       0 tokens ✅
Historial (últimos 5 mensajes):   3,000 tokens ✅
Archivos leídos (solo necesarios): 2,000 tokens ✅
Respuesta del asistente:          3,000 tokens
────────────────────────────────────────────────
TOTAL:                            8,200 tokens
```

**Ahorro: 82% (37,000 tokens por mensaje)**

---

## Los 3 grandes consumidores

### 1️⃣ Archivos abiertos en el IDE (40-50% del consumo)

**El problema:**
Cada pestaña abierta en tu editor se envía automáticamente como contexto.

**Impacto:**
```
1 archivo promedio (200 líneas):  ~3,000 tokens
5 archivos abiertos:             ~15,000 tokens
10 archivos abiertos:            ~30,000 tokens
```

**Solución:**
```
Antes de hacer una pregunta:
Cmd+W (Mac) / Ctrl+W (Windows) → Cerrar todos los archivos
O dejar abierto SOLO el archivo relevante a tu pregunta
```

**Ahorro esperado:** 70-90%

---

### 2️⃣ Historial de conversación (30-40% del consumo)

**El problema:**
Todo el historial de la sesión se mantiene en memoria para contexto.

**Impacto:**
```
Después de 10 mensajes:   ~20,000 tokens
Después de 50 mensajes:  ~100,000 tokens
Después de 100 mensajes: ~200,000 tokens
```

**Solución:**
```
Opción A: Reiniciar sesión entre tareas grandes
- Al terminar una feature
- Al cambiar de módulo/componente
- Documentar antes de reiniciar

Opción B: Pedir al asistente resumir y reiniciar
"Resúmeme lo que hicimos y reiniciemos la conversación"
```

**Ahorro esperado:** 50-80% en sesiones largas

---

### 3️⃣ Documentos grandes (20-30% del consumo)

**El problema:**
Archivos de especificación monolíticos (requirements.md, plan.md de 5,000+ líneas).

**Impacto:**
```
1 archivo de requisitos (3,000 líneas):  ~12,000 tokens
1 archivo de plan técnico (5,000 líneas): ~20,000 tokens
Leídos juntos en cada consulta:          ~32,000 tokens
```

**Solución:**
Usar **chunking de documentos** (ver sección siguiente).

**Ahorro esperado:** 60-80%

---

## Técnicas de optimización

### ✅ Técnica 1: Higiene de IDE

**Antes de cada pregunta:**

```markdown
1. Cierra TODAS las pestañas (Cmd+W o Ctrl+W)
2. Solo abre el archivo si es estrictamente necesario
3. Si no sabes qué archivo necesitas, déjalo al asistente
```

**Ejemplo:**
```
❌ MAL:
   - Tienes 8 archivos abiertos
   - Preguntas: "¿Cómo implemento esta feature?"
   - Consumo: 24,000 tokens en archivos abiertos

✅ BIEN:
   - Cierras todos los archivos
   - Preguntas: "¿Cómo implemento esta feature?"
   - Consumo: 0 tokens en archivos abiertos
```

---

### ✅ Técnica 2: Task tool para exploraciones

**Cuándo usar:**
- Búsquedas en el código ("encuentra todos los ViewModels")
- Análisis de estructura ("cómo funciona la navegación")
- Exploraciones amplias ("qué patrones usa el proyecto")

**Beneficio:**
El Task tool trabaja en su propio contexto aislado y solo devuelve el resultado final.

**Ejemplo:**
```
❌ MAL:
   "Busca todos los archivos que usan Hilt"
   → El asistente hace Grep + lee 20 archivos
   → Consumo: 30,000 tokens

✅ BIEN:
   "Usa Task tool para buscar todos los archivos que usan Hilt"
   → Task tool trabaja aislado
   → Solo devuelve lista de archivos
   → Consumo: 500 tokens
```

---

### ✅ Técnica 3: Lectura selectiva

**Pedir al asistente que lea solo lo necesario:**

```
❌ MAL:
   "Lee plan.md y dime cómo hacer X"
   → Lee 5,000 líneas completas

✅ BIEN:
   "Busca en plan.md la sección sobre X y léela"
   → Solo lee la sección relevante (200 líneas)
```

---

### ✅ Técnica 4: Reinicio estratégico

**Cuándo reiniciar la sesión:**

```markdown
✅ Al terminar una fase/milestone
✅ Al cambiar de módulo completamente diferente
✅ Cuando el contexto supera 100K tokens
✅ Entre features independientes
```

**Antes de reiniciar:**
```markdown
1. Documenta todo lo hecho en un archivo de resumen
2. Actualiza documentos de especificación
3. Reinicia la conversación
4. El asistente lee el resumen en vez del historial completo
```

**Ahorro:** 80-90% en sesiones largas

---

## Chunking de documentos

### El concepto

**Problema:**
Un archivo `requirements.md` de 3,000 líneas (~12,000 tokens) se lee completo
incluso si solo necesitas una sección.

**Solución:**
Partir el archivo en chunks más pequeños con un índice.

---

### Estructura recomendada

#### ❌ ANTES (monolítico):

```
docs/
├── requirements.md          # 3,000 líneas
├── plan.md                  # 5,000 líneas
└── tasks.md                 # 500 líneas
```

#### ✅ DESPUÉS (chunking):

```
docs/
├── requirements/
│   ├── index.md             # Resumen + TOC (200 líneas)
│   ├── funcionales.md       # Solo RF (800 líneas)
│   ├── no-funcionales.md    # Solo RNF (400 líneas)
│   ├── ui-ux.md            # Solo UI/UX (600 líneas)
│   └── integraciones.md    # Solo APIs (500 líneas)
│
├── plan/
│   ├── index.md            # Resumen + TOC (300 líneas)
│   ├── arquitectura.md     # Solo arquitectura (800 líneas)
│   ├── stack.md            # Solo tecnologías (600 líneas)
│   ├── datos.md            # Solo modelos de datos (700 líneas)
│   ├── casos-uso.md        # Solo use cases (500 líneas)
│   └── navegacion.md       # Solo navegación (400 líneas)
│
└── tasks.md                # Se mantiene (actualización frecuente)
```

---

### Cómo crear el índice

**Archivo: `docs/requirements/index.md`**

```markdown
# Requisitos del Proyecto

## Resumen Ejecutivo
[Descripción breve del proyecto en 2-3 párrafos]

## Estructura de Documentación

### Requisitos Funcionales
📄 Ver: [funcionales.md](funcionales.md)
- RF-01 a RF-10: Autenticación y usuarios
- RF-11 a RF-20: Gestión de contenido
- RF-21 a RF-30: Reportes y analytics

### Requisitos No Funcionales
📄 Ver: [no-funcionales.md](no-funcionales.md)
- RNF-01 a RNF-05: Performance
- RNF-06 a RNF-10: Seguridad
- RNF-11 a RNF-15: Escalabilidad

### Diseño UI/UX
📄 Ver: [ui-ux.md](ui-ux.md)
- Wireframes
- Flujos de usuario
- Guía de estilo

### Integraciones
📄 Ver: [integraciones.md](integraciones.md)
- API externa A
- API externa B
- Servicios de terceros
```

---

### Beneficio del chunking

**Ejemplo de consulta:**

```
Pregunta: "¿Cómo debe funcionar la autenticación?"

❌ SIN CHUNKING:
   → Lee requirements.md completo (3,000 líneas)
   → Consumo: 12,000 tokens
   → Solo necesitaba sección de autenticación (200 líneas)

✅ CON CHUNKING:
   → Lee requirements/index.md (200 líneas)
   → Identifica: "Autenticación está en funcionales.md"
   → Lee solo requirements/funcionales.md, sección relevante (200 líneas)
   → Consumo: 1,600 tokens

Ahorro: 86% (10,400 tokens)
```

---

### Cómo partir un archivo grande

**Proceso recomendado:**

```markdown
1. Identifica secciones lógicas en el archivo original
2. Crea carpeta con el nombre del documento
3. Crea index.md con TOC y resumen
4. Crea un archivo por sección lógica
5. Mueve contenido manteniendo enlaces
6. Actualiza referencias en otros documentos
```

**Herramientas:**
- Manual (para control total)
- Script de Python para automatizar
- Pedir al asistente: "Ayúdame a partir requirements.md en chunks"

---

## Checklist de ahorro

### ✅ Antes de CADA mensaje

```markdown
[ ] Cerré todos los archivos en el IDE
[ ] O dejé abierto SOLO el archivo relevante
[ ] Mi pregunta es específica y concisa
```

### ✅ Durante sesiones largas (cada 20-30 mensajes)

```markdown
[ ] ¿El historial es muy largo? Considerar reiniciar
[ ] ¿Estoy cambiando de tema? Reiniciar sesión
[ ] Documenté lo hecho antes de reiniciar
```

### ✅ Al iniciar un proyecto

```markdown
[ ] Estructuré docs/ con chunking desde el inicio
[ ] Creé archivos index.md para documentos grandes
[ ] Definí convención de naming clara
```

### ✅ Al trabajar con el asistente

```markdown
[ ] Uso "dame opciones" cuando hay múltiples enfoques
[ ] Pido "usa Task tool" para exploraciones amplias
[ ] Especifico qué archivo leer cuando lo sé
```

---

## Mejores prácticas

### 🎯 Práctica 1: Sesiones temáticas

**En vez de:**
```
Una sesión de 8 horas haciendo de todo:
- Feature A
- Bug fix B
- Refactor C
- Feature D
```

**Mejor:**
```
Sesión 1 (2h): Feature A completa
→ Reiniciar
Sesión 2 (2h): Bug fix B + Refactor C
→ Reiniciar
Sesión 3 (2h): Feature D completa
```

**Beneficio:** Cada sesión empieza con contexto limpio.

---

### 🎯 Práctica 2: Documentos de resumen

Crear al final de cada sesión/fase:

```markdown
docs/resumen-sesion-YYYY-MM-DD.md

## Qué se hizo
- Feature X implementada
- Bug Y arreglado
- Refactor Z completado

## Decisiones tomadas
- Usamos librería A por motivo B
- Arquitectura C por motivo D

## Próximos pasos
- Implementar feature W
- Revisar performance de X
```

**Beneficio:**
En la próxima sesión, el asistente lee el resumen (500 tokens)
en vez del historial completo (20,000 tokens).

---

### 🎯 Práctica 3: Naming consistente

**Para documentos chunkeados:**

```
✅ BIEN:
   requirements/01-funcionales.md
   requirements/02-no-funcionales.md
   requirements/03-ui-ux.md

❌ MAL:
   requirements/functional_reqs.md
   requirements/NFRs.md
   requirements/design-stuff.md
```

**Beneficio:** Más fácil para el asistente encontrar el archivo correcto.

---

### 🎯 Práctica 4: Límites por archivo

**Regla de oro:**
Ningún archivo de docs debe superar las 500-800 líneas (~2,000-3,000 tokens).

Si supera, partir en chunks.

**Excepción:**
Archivos de código generado o datos (JSON, CSV) que no se leen frecuentemente.

---

## Calculadora de ahorro

### Situación típica SIN optimización:

```
Archivos abiertos (5):         15,000 tokens
Historial (30 mensajes):       25,000 tokens
plan.md completo:              20,000 tokens
requirements.md completo:      12,000 tokens
────────────────────────────────────────────
Total por mensaje:             72,000 tokens

Límite diario (ej: 500K):      500,000 tokens
Mensajes posibles:             ~7 mensajes
Duración estimada:             ~2-3 horas
```

### Situación CON optimización completa:

```
Archivos abiertos (0):              0 tokens
Historial (últimos 5):          3,000 tokens
plan/index.md + sección:        2,500 tokens
requirements/sección:           1,500 tokens
────────────────────────────────────────────
Total por mensaje:              7,000 tokens

Límite diario (500K):         500,000 tokens
Mensajes posibles:            ~70 mensajes
Duración estimada:            ~20+ horas

Mejora: 10x más mensajes
```

---

## Quick wins (implementa ya)

### Nivel 1: Inmediato (0 minutos)

```markdown
✅ Cerrar todos los archivos en IDE antes de preguntar
   Ahorro: 60-70% inmediato
```

### Nivel 2: Rápido (10 minutos)

```markdown
✅ Crear archivo de resumen de la sesión anterior
✅ Reiniciar sesión entre temas grandes
   Ahorro adicional: 20-30%
```

### Nivel 3: Una vez (1-2 horas)

```markdown
✅ Partir plan.md en chunks
✅ Partir requirements.md en chunks
✅ Crear estructura docs/ organizada
   Ahorro adicional: 40-50%
```

---

## Recursos adicionales

### Comandos útiles para el asistente

```
"Usa Task tool para buscar X"
→ Búsqueda sin cargar contexto

"Dame opciones antes de implementar"
→ Evita re-trabajo = ahorro de tokens

"Resume lo hecho y reiniciemos"
→ Limpia historial manteniendo conocimiento

"Lee solo la sección X de plan.md"
→ Lectura selectiva
```

### Plugins/Extensiones útiles

- **Close All Tabs** (VSCode): Un botón para cerrar todo
- **Project Dashboard** (VSCode): Gestión de sesiones por feature
- **Token Counter** (si existe para tu editor): Ver consumo en tiempo real

---

## Métricas de éxito

### Indicadores de buena optimización:

```markdown
✅ Sesiones duran 6+ horas antes de límite
✅ Puedes hacer 50+ mensajes por sesión
✅ Raramente llegas al límite diario
✅ Contexto por mensaje <10,000 tokens
```

### Señales de que necesitas optimizar:

```markdown
⚠️ Límite diario se alcanza en 2-3 horas
⚠️ Solo puedes hacer 10-15 mensajes
⚠️ Contexto por mensaje >40,000 tokens
⚠️ Frecuentes interrupciones por límite
```

---

## Preguntas frecuentes

### ¿Cerrar archivos no afecta la calidad de las respuestas?

**No.** El asistente tiene acceso a leer cualquier archivo cuando lo necesita.
Cerrar archivos solo evita que se envíen automáticamente en cada mensaje.

### ¿El chunking complica el mantenimiento?

**Al principio sí, pero:**
- Se compensa con sesiones más largas
- Una vez estructurado, es fácil mantener
- El índice hace navegación más clara

### ¿Cuándo NO debo reiniciar sesión?

**No reinicies si:**
- Estás en medio de un debugging complejo
- El contexto de mensajes anteriores es crítico
- Estás iterando sobre un mismo archivo

**En esos casos:**
- Mantén sesión pero cierra archivos
- Usa Task tool para búsquedas
- Pide al asistente resumir periodicamente

---

## Conclusión

**3 reglas de oro:**

1. 🚫 **Cerrar archivos en IDE** antes de cada pregunta
2. 📦 **Chunking de documentos** grandes (>500 líneas)
3. 🔄 **Reiniciar sesión** entre temas/fases

**Resultado esperado:**
- De 2-3 horas de sesión → 10-20 horas de sesión
- De 10 mensajes → 70+ mensajes
- De 50K tokens/mensaje → 7K tokens/mensaje

**Inversión:** 1-2 horas de restructuración
**Retorno:** 5-10x más productividad en todas las sesiones futuras

---

**Fin de la guía**

*Comparte esta guía con tu equipo y mejoren juntos la eficiencia con Claude Code.*
