# Requirements - TarotAI

> **Fuente de verdad del proyecto**
> Este documento define QUÉ construir y POR QUÉ, no el CÓMO.
> Última actualización: 2026-04-24

---

## 1. Visión del Producto

### 1.1 Propósito
TarotAI es una aplicación Android que permite realizar tiradas de Tarot de Marsella con interpretaciones generadas por IA, combinando reflexión personal con aprendizaje sobre simbología del tarot.

### 1.2 Usuarios Objetivo
- Público general interesado en tarot
- Estudiantes de tarot que desean aprender
- Usuarios que buscan reflexión personal mediante las cartas

### 1.3 Valor Principal
La app ofrece dos experiencias complementarias:
1. **Reflexión personal**: Responder preguntas mediante tiradas interpretadas por IA
2. **Aprendizaje**: Estudiar el significado de cada carta del Tarot de Marsella

---

## 2. Funcionalidades Core

### 2.1 Realización de Tiradas

#### Contexto
El usuario necesita hacer una consulta al tarot y obtener una interpretación contextualizada a su pregunta.

#### Comportamiento Esperado

**Dado** que el usuario quiere realizar una consulta
**Cuando** accede a la función de nueva tirada
**Entonces** debe poder:
1. Seleccionar uno de los 5 tipos de tirada disponibles
2. Formular su pregunta en texto libre
3. Iniciar el proceso de selección de cartas
4. Ver las cartas seleccionadas en una interfaz gráfica realista
5. Recibir una interpretación personalizada basada en su pregunta

#### Tipos de Tirada

La app debe soportar exactamente 5 tipos de tiradas:

| Tipo | Cantidad de Cartas | Propósito | Posiciones |
|------|-------------------|-----------|------------|
| **Carta Simple** | 1 | Orientación rápida sobre cualquier tema | - Respuesta |
| **Sí o No** | 1 | Respuesta binaria explicada a pregunta específica | - Respuesta (con justificación) |
| **Presente** | 3 | Análisis de la situación actual | 1. Presente<br>2. Obstáculo<br>3. Ayuda |
| **Tendencia** | 3 | Análisis temporal de evolución | 1. De dónde vengo (Pasado)<br>2. Dónde estoy (Presente)<br>3. A dónde voy (Tendencia) |
| **Cruz** | 5 | Análisis profundo en forma de cruz | 1. De dónde vengo (izquierda)<br>2. Hacia dónde voy (derecha)<br>3. Ayuda (arriba)<br>4. Obstáculo (abajo)<br>5. Conclusión (centro) |

#### Diseño Visual de las Tiradas

**Tirada Cruz (5 cartas)**: Disposición espacial en forma de cruz
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

**Otras tiradas**: Disposición horizontal de izquierda a derecha

#### Criterios de Aceptación
- [ ] El usuario puede elegir entre los 5 tipos de tirada
- [ ] El usuario puede ingresar una pregunta de texto (mínimo 10 caracteres)
- [ ] La pregunta es obligatoria para todas las tiradas excepto "Carta Simple"
- [ ] Las cartas se seleccionan aleatoriamente del mazo de 78 cartas (22 Arcanos Mayores + 56 Arcanos Menores)
- [ ] Las cartas pueden aparecer en posición normal o invertida (50% probabilidad cada una)
  - **Justificación**: Estudios estadísticos de lecturas reales muestran distribución 50/50
- [ ] No se pueden repetir cartas en una misma tirada
- [ ] La interfaz muestra visualmente las cartas en su posición correspondiente
- [ ] La tirada "Cruz" se muestra con disposición en forma de cruz (no lineal)

### 2.2 Interpretación por IA

#### Contexto
El usuario necesita comprender qué significan las cartas en el contexto de su pregunta específica.

#### Comportamiento Esperado

**Dado** que se ha completado una tirada
**Cuando** el sistema genera la interpretación
**Entonces** debe proporcionar:
1. **Interpretación individual**: Explicación de cada carta según su posición y orientación (derecha/invertida)
2. **Interpretación general**: Análisis holístico de todas las cartas en conjunto, relacionándolas con la pregunta del usuario

#### Criterios de Aceptación
- [ ] La interpretación individual explica cada carta considerando:
  - Su posición en la tirada (ej: "como pasado", "como obstáculo")
  - Su orientación (derecha o invertida)
  - Su relación con la pregunta formulada
- [ ] La interpretación general sintetiza el mensaje de todas las cartas en conjunto
- [ ] Para la tirada "Sí o No", la interpretación debe:
  - Dar una respuesta clara (Sí/No/Indefinido)
  - **Explicar por qué esa carta específica significa esa respuesta** (objetivo educativo)
  - Relacionar el simbolismo de la carta con la interpretación binaria
- [ ] Las interpretaciones son generadas por la API de Claude
- [ ] La generación de interpretación debe tener un indicador de carga visible
- [ ] Si falla la generación, mostrar mensaje de error y opción de reintentar

### 2.3 Enciclopedia de Cartas

#### Contexto
El usuario quiere aprender sobre el significado de las cartas sin necesidad de realizar una tirada. Esta información debe estar disponible sin conexión a internet.

#### Comportamiento Esperado

**Dado** que el usuario quiere estudiar una carta específica
**Cuando** accede a la enciclopedia de cartas
**Entonces** debe poder:
1. Ver la lista completa de las 78 cartas del Tarot de Marsella
2. Seleccionar cualquier carta para ver su información detallada
3. Acceder a esta información incluso sin conexión a internet

#### Información de Cada Carta

Cada carta debe incluir (almacenado localmente):

1. **Nombre**: Nombre completo de la carta (ej: "El Loco", "As de Copas")
2. **Imagen**: Representación visual del Tarot de Marsella
3. **Significado General**: Descripción del arquetipo o concepto que representa
4. **Significado en Posición Derecha**: Interpretación cuando aparece normal
5. **Significado en Posición Invertida**: Interpretación cuando aparece al revés
6. **Simbolismo e Iconografía**: Explicación de los símbolos visuales en la carta
7. **Palabras Clave**: Lista de 5-8 conceptos principales asociados a la carta

#### Criterios de Aceptación
- [ ] Todas las 78 cartas del Tarot de Marsella están disponibles:
  - 22 Arcanos Mayores (0-XXI)
  - 56 Arcanos Menores (Bastos, Copas, Espadas, Oros - del As al 10 + 4 figuras cada uno)
- [ ] La información está almacenada localmente (no requiere IA ni internet)
- [ ] Las imágenes de las cartas son de dominio público
- [ ] La lista de cartas es navegable y se puede filtrar por tipo (Arcanos Mayores/Menores)
- [ ] Desde una tirada realizada, el usuario puede tocar una carta para ver su detalle completo

---

## 3. Interfaz Gráfica

### 3.1 Diseño Visual

#### Contexto
La experiencia debe ser inmersiva y visualmente atractiva, emulando la experiencia de una tirada real de cartas.

#### Requisitos Visuales
- Las cartas deben mostrarse como imágenes del Tarot de Marsella tradicional
- Durante la tirada, debe haber una animación de "robar" o "revelar" cartas
- Las cartas invertidas deben mostrarse visualmente rotadas 180°
- El diseño debe evocar una atmósfera mística pero moderna (no recargada)

#### Criterios de Aceptación
- [ ] Las cartas se muestran con las imágenes reales del Tarot de Marsella
- [ ] Existe alguna animación al revelar las cartas (no aparecen instantáneamente)
- [ ] Las cartas invertidas se muestran claramente rotadas
- [ ] La interfaz es usable y clara en pantallas de diferentes tamaños

---

## 4. Restricciones y Alcance

### 4.1 Fuera del Alcance (MVP)
Las siguientes funcionalidades **NO** forman parte de la versión inicial:
- Sistema de usuarios y autenticación
- Historial de tiradas guardadas
- Backend propio
- Compartir resultados en redes sociales
- Tiradas colaborativas
- Sistema de favoritos
- Personalización de mazos

### 4.2 Dependencias Externas
- **API de Claude**: Requerida para generar interpretaciones contextualizadas
- **Imágenes de dominio público**: Necesarias las 78 cartas del Tarot de Marsella

### 4.3 Plataforma
- **Exclusivamente Android** (SDK mínimo 24 - Android 7.0)
- Orientación vertical (portrait)

---

## 5. Criterios de Éxito

### 5.1 Funcionales
- ✅ El usuario puede completar una tirada desde la selección hasta ver la interpretación en menos de 2 minutos
- ✅ El usuario puede consultar información de cualquier carta sin realizar una tirada
- ✅ Las interpretaciones de la IA son coherentes con la pregunta formulada

### 5.2 No Funcionales
- ✅ La app funciona sin conexión para la enciclopedia de cartas
- ✅ La generación de interpretación por IA toma menos de 30 segundos (depende de red)
- ✅ Las imágenes de las cartas cargan en menos de 2 segundos

---

## 6. Preguntas Abiertas y Decisiones Pendientes

### 6.1 Imágenes del Tarot
- **Estado**: Por conseguir
- **Requisito**: 78 imágenes de dominio público del Tarot de Marsella
- **Fuentes potenciales**: Wikimedia Commons, proyectos de tarot libres
- **Decisión pendiente**: Seleccionar el conjunto específico de imágenes a utilizar

### 6.2 Datos de la Enciclopedia
- **Estado**: Por definir fuente
- **Opciones**:
  - Redactar manualmente la información de las 78 cartas
  - Utilizar contenido de dominio público existente
  - Generar con IA y validar manualmente
- **Decisión pendiente**: Definir estrategia de contenido

---

## 7. Glosario

| Término | Definición |
|---------|------------|
| **Arcano Mayor** | Una de las 22 cartas numeradas del 0 (El Loco) al XXI (El Mundo) |
| **Arcano Menor** | Una de las 56 cartas divididas en 4 palos (Bastos, Copas, Espadas, Oros) |
| **Tirada** | Acción de seleccionar y colocar cartas para realizar una consulta |
| **Posición** | Ubicación específica de una carta en una tirada (ej: pasado, presente) |
| **Carta Invertida** | Carta que aparece rotada 180°, con significado diferente al normal |
| **Tarot de Marsella** | Una de las barajas de tarot más antiguas y tradicionales |

---

## 8. Aprobación

Este documento requiere aprobación explícita antes de proceder con `plan.md`.

**Estado**: ⏳ Pendiente de revisión

---

**Fin del documento de requisitos**