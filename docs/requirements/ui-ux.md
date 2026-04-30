# Especificaciones de UI/UX - TarotAI

## Diseño Visual General

### Contexto
La experiencia debe ser inmersiva y visualmente atractiva, emulando la experiencia de una tirada real de cartas.

### Principios de Diseño
- Evocar una atmósfera mística pero moderna (no recargada)
- Enfoque en la claridad y usabilidad
- Animations suaves para una experiencia premium
- Respeto por diferentes tamaños de pantalla

---

## Requisitos Visuales

### Cartas

**Representación Visual**
- [ ] Las cartas deben mostrarse como imágenes del Tarot de Marsella tradicional
- [ ] Usar imágenes de dominio público de alta calidad
- [ ] Las cartas deben ser reconocibles y claras en diferentes tamaños de pantalla

**Orientación de Cartas**
- [ ] Las cartas invertidas deben mostrarse visualmente rotadas 180°
- [ ] La rotación debe ser clara e inmediata para el usuario
- [ ] Considerar indicadores visuales adicionales si es necesario

**Animaciones al Revelar**
- [ ] Existe alguna animación al revelar las cartas (no aparecen instantáneamente)
- [ ] Las cartas se muestran de manera progresiva
- [ ] Las animaciones deben ser suaves y coherentes

### Disposición Espacial de Tiradas

#### Tirada Cruz (5 cartas)

**Disposición en forma de cruz:**
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

**Requisitos**
- [ ] Las cartas se distribuyen en posición de cruz
- [ ] La carta central [5] es visualmente prominente
- [ ] Los rótulos de posición son claros
- [ ] Se adapta al tamaño de pantalla disponible

#### Otras Tiradas (1, 3 cartas)

**Disposición horizontal:**
- [ ] Las cartas se alinean horizontalmente de izquierda a derecha
- [ ] Espaciado uniforme entre cartas
- [ ] Rótulos de posición debajo de cada carta

### Interfaz General

**Usabilidad**
- [ ] La interfaz es usable y clara en pantallas de diferentes tamaños
- [ ] Controles intuitivos y fáciles de usar
- [ ] Navegación clara entre secciones

**Accesibilidad**
- [ ] Texto legible en diferentes tamaños de pantalla
- [ ] Suficiente contraste de colores
- [ ] Interacciones táctiles apropiadas para móvil

### Flujo de Interfaz

#### Pantalla Principal
- Acceso a "Nueva Tirada"
- Acceso a "Enciclopedia de Cartas"
- Información sobre la app

#### Nueva Tirada
1. Selección de tipo de tirada
2. Campo de entrada de pregunta (si aplica)
3. Botón para iniciar tirada
4. Pantalla de revelación de cartas con animaciones
5. Pantalla de interpretación

#### Enciclopedia
1. Lista navegable de 78 cartas
2. Filtros por tipo (Arcanos Mayores/Menores)
3. Búsqueda o scroll
4. Vista detallada de cada carta
5. Información completa visible en pantalla

---

## Indicadores Visuales

### Durante la Tirada
- [ ] Indicador de carga mientras se generan cartas
- [ ] Animación de "revelación" de cartas
- [ ] Confirmación visual cuando la tirada se completa

### Durante la Generación de IA
- [ ] Indicador de carga visible mientras se genera interpretación
- [ ] Mensaje indicando que se está procesando
- [ ] Opción para reintentar si falla (con mensaje de error claro)

### Estados de Error
- [ ] Mensajes de error claros y comprensibles
- [ ] Opciones para reintentar
- [ ] No mostrar errores técnicos complejos al usuario

---

## Consideraciones de Plataforma

### Android Específico

**Versión Mínima**: Android 7.0 (API 24)

**Orientación**: Vertical (portrait) obligatoria

**Tamaños de Pantalla**:
- [ ] Optimizado para teléfonos móviles estándar
- [ ] Funcional en pantallas desde 4.7" hasta 6.5"+
- [ ] Adaptación automática de layouts
- [ ] Escalado proporcionado de elementos

### Rendimiento Visual
- [ ] Animaciones suaves sin lag
- [ ] Carga de imágenes sin bloqueos
- [ ] Transiciones rápidas entre pantallas

---

## Componentes Clave

### Selector de Tipo de Tirada
- Mostrar 5 opciones claramente
- Cada opción debe indicar:
  - Nombre del tipo
  - Número de cartas
  - Descripción breve del propósito
  - Icono visual si es posible

### Campo de Pregunta
- Placeholder indicativo del formato esperado
- Contador de caracteres
- Validación en tiempo real (mínimo 10 caracteres)
- Mensaje de error si no cumple requisitos

### Contenedor de Cartas
- Adaptación dinámica según tipo de tirada
- Manejo de espacios apropiados
- Centering automático en pantalla

### Información de Carta
- Nombre prominente
- Imagen clara y escalable
- Significados bien organizados
- Palabras clave resaltadas
- Scroll vertical si contenido es extenso

---

## Especificaciones de Estilo

### Tipografía
- Fuente clara y legible en pequeños tamaños
- Jerarquía clara entre títulos y contenido
- Tamaños adaptables para diferentes pantallas

### Colores
- Paleta que sugiera misticismo sin ser excesiva
- Alto contraste para legibilidad
- Colores consistentes en toda la app

### Espaciado
- Márgenes y paddings apropiados
- Distribución equilibrada del contenido
- Suficiente espacio para interacción táctil

---

## Validación de UI/UX

**Criterios de Validación**
- [ ] Las cartas se muestran con imágenes reales del Tarot de Marsella
- [ ] Existe animación al revelar las cartas
- [ ] Las cartas invertidas están claramente rotadas
- [ ] La tirada Cruz tiene disposición correcta
- [ ] La interfaz es usable en diferentes tamaños de pantalla
- [ ] Los textos son legibles
- [ ] Las interacciones son intuitivas

---

*Documento generado automáticamente desde `requirements.md`*
