# Criterios de Aceptación Consolidados - TarotAI

## Introducción

Este documento consolida todos los criterios de aceptación de los requisitos funcionales, no funcionales y visuales de TarotAI. Cada criterio es una condición verificable que debe cumplirse para dar por validado el requisito.

---

## Criterios Funcionales

### Requisito RF-01: Seleccionar Tipo de Tirada

- [ ] El usuario puede elegir entre los 5 tipos de tirada desde la interfaz principal
- [ ] Cada tipo de tirada muestra su descripción clara
- [ ] La selección persiste durante el flujo de tirada
- [ ] Los 5 tipos disponibles son exactamente: Carta Simple, Sí o No, Presente, Tendencia y Cruz

### Requisito RF-02: Formular Pregunta

- [ ] El usuario puede ingresar una pregunta de texto (mínimo 10 caracteres)
- [ ] La pregunta es obligatoria para todas las tiradas excepto "Carta Simple"
- [ ] Se valida en tiempo real (feedback visual)
- [ ] Se muestra mensaje de error si no cumple el mínimo
- [ ] La pregunta se preserva durante toda la tirada

### Requisito RF-03: Seleccionar Cartas Aleatoriamente

- [ ] Las cartas se seleccionan aleatoriamente del mazo de 78 cartas (22 Arcanos Mayores + 56 Arcanos Menores)
- [ ] Las cartas pueden aparecer en posición normal o invertida (50% probabilidad cada una)
- [ ] No se pueden repetir cartas en una misma tirada
- [ ] La cantidad de cartas corresponde exactamente al tipo de tirada

### Requisito RF-04: Mostrar Cartas con Orientación

- [ ] La interfaz muestra visualmente las cartas en su posición correspondiente
- [ ] Las cartas invertidas se muestran claramente rotadas 180°
- [ ] La tirada "Cruz" se muestra con disposición en forma de cruz (no lineal)
- [ ] Otras tiradas se muestran en disposición horizontal
- [ ] Existe animación al revelar las cartas (no aparecen instantáneamente)

### Requisito RF-05: Recibir Interpretación Personalizada

- [ ] Las interpretaciones son generadas por la API de Claude
- [ ] La generación de interpretación debe tener un indicador de carga visible
- [ ] Si falla la generación, mostrar mensaje de error y opción de reintentar
- [ ] La interpretación se genera después de que se revelan todas las cartas

### Requisito RF-06: Interpretación Individual de Cartas

- [ ] La interpretación individual explica cada carta considerando:
  - Su posición en la tirada (ej: "como pasado", "como obstáculo")
  - Su orientación (derecha o invertida)
  - Su relación con la pregunta formulada
- [ ] Cada carta tiene su propia sección en la interpretación
- [ ] El contenido es coherente y educativo

### Requisito RF-07: Interpretación General Holística

- [ ] La interpretación general sintetiza el mensaje de todas las cartas en conjunto
- [ ] Para la tirada "Sí o No", la interpretación debe:
  - Dar una respuesta clara (Sí/No/Indefinido)
  - Explicar por qué esa carta específica significa esa respuesta (objetivo educativo)
  - Relacionar el simbolismo de la carta con la interpretación binaria
- [ ] Relaciona el conjunto de cartas con la pregunta del usuario
- [ ] Proporciona una conclusión o reflexión final

### Requisito RF-08: Acceder a Enciclopedia de Cartas

- [ ] Todas las 78 cartas del Tarot de Marsella están disponibles:
  - 22 Arcanos Mayores (0-XXI)
  - 56 Arcanos Menores (Bastos, Copas, Espadas, Oros - del As al 10 + 4 figuras cada uno)
- [ ] La información está almacenada localmente (no requiere IA ni internet)
- [ ] La lista de cartas es navegable y se puede filtrar por tipo (Arcanos Mayores/Menores)
- [ ] Desde una tirada realizada, el usuario puede tocar una carta para ver su detalle completo
- [ ] Se puede acceder desde el menú principal

### Requisito RF-09: Información Detallada de Cartas

- [ ] Cada carta incluye:
  1. Nombre completo
  2. Imagen del Tarot de Marsella
  3. Significado General
  4. Significado en Posición Derecha
  5. Significado en Posición Invertida
  6. Simbolismo e Iconografía
  7. Palabras Clave (5-8 conceptos)
- [ ] Las imágenes de las cartas son de dominio público
- [ ] Toda la información es clara, comprensible y educativa
- [ ] La información es consistente entre todas las cartas

### Requisito RF-10: Cargar Imágenes de Cartas

- [ ] Las cartas se muestran con las imágenes reales del Tarot de Marsella
- [ ] Las imágenes cargan en menos de 2 segundos
- [ ] La interfaz es usable y clara en pantallas de diferentes tamaños
- [ ] Las imágenes se escalan proporcionadamente
- [ ] No hay distorsión visual en las imágenes

---

## Criterios No Funcionales

### Requisito RNF-01: Funcionalidad Sin Conexión a Internet

- [ ] La app funciona sin conexión para la enciclopedia de cartas
- [ ] Todas las imágenes se cargan desde almacenamiento local
- [ ] La información de cartas se accede sin API externas
- [ ] La enciclopedia es totalmente funcional sin conexión
- [ ] Se puede navegar y buscar cartas sin internet

### Requisito RNF-02: Tiempo de Respuesta de Interpretación por IA

- [ ] La generación de interpretación por IA toma menos de 30 segundos
- [ ] Se muestra indicador de carga visible mientras se espera
- [ ] Si la respuesta tarda más, se ofrece opción de reintentar
- [ ] Los tiempos cumplen incluso con conexiones 3G moderadas
- [ ] No hay bloqueo de interfaz durante la generación

### Requisito RNF-03: Tiempo de Carga de Imágenes

- [ ] Las imágenes de las cartas cargan en menos de 2 segundos
- [ ] No hay bloqueos en la interfaz durante carga de imágenes
- [ ] Las imágenes se cachean correctamente
- [ ] La interfaz responde rápidamente al seleccionar cartas

### Requisito RNF-04: Plataforma y Versión Mínima

- [ ] La app corre en Android 7.0 (API 24) o superior
- [ ] La interfaz es responsive en diferentes tamaños de pantalla
- [ ] La orientación está bloqueada en modo vertical
- [ ] No se requiere soporte para tablet o modo horizontal
- [ ] La app es compatible con versiones recientes de Android

### Requisito RNF-05: Rendimiento de Tirada

- [ ] El usuario puede completar una tirada desde la selección hasta ver la interpretación en menos de 2 minutos
- [ ] Las transiciones entre pantallas son suaves
- [ ] No hay bloqueos de interfaz durante procesos
- [ ] El tiempo total respeta la suma de tiempos individuales

---

## Criterios Visuales y de UI/UX

### Requisitos de Cartas

- [ ] Las cartas se muestran con las imágenes reales del Tarot de Marsella
- [ ] Existe animación al revelar las cartas (no aparecen instantáneamente)
- [ ] Las cartas invertidas se muestran claramente rotadas 180°
- [ ] La interfaz es usable y clara en pantallas de diferentes tamaños
- [ ] Las cartas son fáciles de distinguir entre sí

### Requisitos de Disposición

- [ ] La tirada "Cruz" se muestra con disposición exacta en forma de cruz:
  ```
          [3]
           ↑
         Ayuda

  [1] ← [5] → [2]
  De dónde  Conclusión  Hacia dónde
  vengo     (centro)    voy

           ↓
          [4]
        Obstáculo
  ```
- [ ] Otras tiradas se alinean horizontalmente
- [ ] El espaciado entre cartas es uniforme
- [ ] Los rótulos de posición son claros

### Requisitos de Animación

- [ ] Las animaciones de revelación de cartas son suaves
- [ ] Los indicadores de carga son visibles
- [ ] Las transiciones entre pantallas son coherentes
- [ ] No hay animaciones excesivas o distrayentes

### Requisitos de Accesibilidad

- [ ] Texto legible en diferentes tamaños de pantalla
- [ ] Suficiente contraste de colores
- [ ] Interacciones táctiles apropiadas para móvil
- [ ] Campos de entrada accesibles

---

## Criterios de Éxito Integrados

### Funcionales Globales

- ✅ El usuario puede completar una tirada desde la selección hasta ver la interpretación en menos de 2 minutos
- ✅ El usuario puede consultar información de cualquier carta sin realizar una tirada
- ✅ Las interpretaciones de la IA son coherentes con la pregunta formulada
- ✅ Los 5 tipos de tirada funcionan correctamente
- ✅ La enciclopedia de cartas es completa y accesible

### No Funcionales Globales

- ✅ La app funciona sin conexión para la enciclopedia de cartas
- ✅ La generación de interpretación por IA toma menos de 30 segundos (depende de red)
- ✅ Las imágenes de las cartas cargan en menos de 2 segundos
- ✅ Compatible con Android 7.0 (API 24) o superior
- ✅ La orientación está bloqueada en vertical

### Visuales y Experiencia

- ✅ Las cartas se muestran con imágenes reales del Tarot de Marsella
- ✅ La interfaz es mística pero moderna, no recargada
- ✅ Las animaciones son suaves y coherentes
- ✅ Todas las disposiciones de tiradas funcionan correctamente
- ✅ La aplicación es intuitiva para usuarios sin experiencia previa

---

## Checklist de Validación Final

### Validación Funcional

- [ ] Todos los 10 RF tienen sus criterios cumplidos
- [ ] Pruebas manuales de los 5 tipos de tirada
- [ ] Validación de selección aleatoria sin repetición
- [ ] Pruebas de interpretación IA en diferentes escenarios
- [ ] Enciclopedia completa y funcional
- [ ] Imágenes se cargan correctamente

### Validación No Funcional

- [ ] Pruebas sin conexión internet
- [ ] Medición de tiempos de respuesta IA
- [ ] Medición de tiempos de carga de imágenes
- [ ] Pruebas en múltiples dispositivos Android
- [ ] Validación de tiempos totales de tirada

### Validación de UI/UX

- [ ] Pruebas en diferentes tamaños de pantalla
- [ ] Validación de todas las animaciones
- [ ] Verificación de disposiciones de tiradas
- [ ] Pruebas de accesibilidad
- [ ] Feedback de usuarios

---

## Estado del Documento

- **Última actualización**: 2026-04-24
- **Estado de validación**: ⏳ Pendiente
- **Responsable**: Equipo de QA

---

*Documento generado automáticamente desde `requirements.md`*
