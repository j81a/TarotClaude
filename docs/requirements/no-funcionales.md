# Requisitos No Funcionales - TarotAI

## RNF-01: Funcionalidad Sin Conexión a Internet

**Descripción**: La enciclopedia de cartas funciona completamente sin conexión a internet.

**Contexto**: El usuario debe poder consultar información de cartas en cualquier lugar, incluso sin acceso a red.

**Comportamiento esperado**:
- Toda la información de las 78 cartas está almacenada localmente
- Las imágenes de las cartas están disponibles localmente
- La enciclopedia es totalmente funcional sin conexión

**Criterios de Aceptación**:
- [ ] La app funciona sin conexión para la enciclopedia de cartas
- [ ] Todas las imágenes se cargan desde almacenamiento local
- [ ] La información de cartas se accede sin API externas

**Impacto**: Crítico para experiencia de usuario

---

## RNF-02: Tiempo de Respuesta de Interpretación por IA

**Descripción**: La generación de interpretación por IA completarse dentro de un tiempo razonable.

**Contexto**: El usuario debe recibir feedback sin esperas excesivas.

**Comportamiento esperado**:
- La API de Claude genera la interpretación rápidamente
- Se muestra indicador de carga durante la espera
- Máximo 30 segundos de espera (dependiendo de velocidad de red)

**Criterios de Aceptación**:
- [ ] La generación de interpretación por IA toma menos de 30 segundos
- [ ] Se muestra indicador de carga visible
- [ ] Existe opción de reintentar si la respuesta tarda más de lo esperado

**Impacto**: Alto - Afecta la experiencia de usuario

---

## RNF-03: Tiempo de Carga de Imágenes

**Descripción**: Las imágenes de las cartas se cargan rápidamente en la interfaz.

**Contexto**: El usuario necesita ver las cartas sin demoras innecesarias.

**Comportamiento esperado**:
- Imágenes locales de alta calidad
- Carga optimizada para diferentes densidades de pantalla
- Transiciones suaves al mostrar las cartas

**Criterios de Aceptación**:
- [ ] Las imágenes de las cartas cargan en menos de 2 segundos
- [ ] La interfaz responde rápidamente al seleccionar cartas
- [ ] No hay bloqueos en la interfaz durante carga de imágenes

**Impacto**: Alto - Afecta la experiencia visual

---

## RNF-04: Plataforma y Versión Mínima

**Descripción**: La aplicación está optimizada para Android con versión mínima específica.

**Contexto**: Asegurar compatibilidad con dispositivos Android actuales.

**Requisitos técnicos**:
- **Plataforma**: Exclusivamente Android
- **API mínima**: Nivel 24 (Android 7.0)
- **Orientación**: Vertical (portrait) obligatoria
- **Tamaños de pantalla**: Optimizado para teléfonos móviles

**Criterios de Aceptación**:
- [ ] La app corre en Android 7.0 (API 24) o superior
- [ ] La interfaz es responsive en diferentes tamaños de pantalla
- [ ] La orientación está bloqueada en modo vertical
- [ ] No se requiere soporte para tablet o modo horizontal

**Impacto**: Crítico - Define el alcance de la plataforma

---

## RNF-05: Rendimiento de Tirada

**Descripción**: El usuario puede completar una tirada completa en tiempo razonable.

**Contexto**: La experiencia debe ser fluida y no tomar demasiado tiempo.

**Comportamiento esperado**:
- Selección de tipo de tirada rápida
- Entrada de pregunta sin demoras
- Animación y revelación de cartas suave
- Generación de interpretación en tiempo aceptable

**Criterios de Aceptación**:
- [ ] El usuario puede completar una tirada desde la selección hasta ver la interpretación en menos de 2 minutos
- [ ] Las transiciones entre pantallas son suaves
- [ ] No hay bloqueos de interfaz durante procesos

**Impacto**: Alto - Afecta la experiencia global

---

## Restricciones Adicionales

### Dependencias Externas

- **API de Claude**: Requerida para generar interpretaciones contextualizadas
- **Imágenes de dominio público**: Necesarias las 78 cartas del Tarot de Marsella

### Fuera del Alcance (MVP)

Las siguientes funcionalidades **NO** forman parte de la versión inicial:
- [ ] Sistema de usuarios y autenticación
- [ ] Historial de tiradas guardadas
- [ ] Backend propio
- [ ] Compartir resultados en redes sociales
- [ ] Tiradas colaborativas
- [ ] Sistema de favoritos
- [ ] Personalización de mazos

---

## Resumen

**Total de Requisitos No Funcionales: 5**

| ID | Requisito | Prioridad | Categoría |
|----|-----------|-----------|-----------|
| RNF-01 | Funcionalidad Sin Conexión | Crítica | Conectividad |
| RNF-02 | Tiempo de Respuesta IA | Alta | Rendimiento |
| RNF-03 | Tiempo de Carga de Imágenes | Alta | Rendimiento |
| RNF-04 | Plataforma y Versión Mínima | Crítica | Compatibilidad |
| RNF-05 | Rendimiento de Tirada | Alta | Rendimiento |

---

## Criterios de Éxito No Funcionales

- ✅ La app funciona sin conexión para la enciclopedia de cartas
- ✅ La generación de interpretación por IA toma menos de 30 segundos (depende de red)
- ✅ Las imágenes de las cartas cargan en menos de 2 segundos
- ✅ Compatible con Android 7.0 (API 24) o superior
- ✅ El usuario completa una tirada en menos de 2 minutos

---

*Documento generado automáticamente desde `requirements.md`*
