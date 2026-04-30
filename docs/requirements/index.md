# Requisitos del Proyecto - TarotAI

## Introducción

TarotAI es una aplicación Android que permite realizar tiradas de Tarot de Marsella con interpretaciones generadas por IA, combinando reflexión personal con aprendizaje sobre simbología del tarot.

### Propósito
Ofrecer dos experiencias complementarias:
1. **Reflexión personal**: Responder preguntas mediante tiradas interpretadas por IA
2. **Aprendizaje**: Estudiar el significado de cada carta del Tarot de Marsella

### Usuarios Objetivo
- Público general interesado en tarot
- Estudiantes de tarot que desean aprender
- Usuarios que buscan reflexión personal mediante las cartas

---

## Tabla de Contenidos

1. [Requisitos Funcionales](./funcionales.md) - 10 requisitos
2. [Requisitos No Funcionales](./no-funcionales.md) - 5 requisitos
3. [UI/UX](./ui-ux.md) - Especificaciones visuales
4. [Criterios de Aceptación](./criterios-aceptacion.md) - Validación de requisitos

---

## Resumen Ejecutivo

### Cuantitativo de Requisitos

| Categoría | Cantidad |
|-----------|----------|
| **Requisitos Funcionales** | 10 |
| **Requisitos No Funcionales** | 5 |
| **Total** | 15 |

### Funcionalidades Core

**3 pilares principales:**

1. **Realización de Tiradas** (5 tipos disponibles)
   - Carta Simple (1 carta)
   - Sí o No (1 carta)
   - Presente (3 cartas)
   - Tendencia (3 cartas)
   - Cruz (5 cartas)

2. **Interpretación por IA**
   - Interpretación individual de cada carta
   - Interpretación general holística
   - Generadas por API de Claude

3. **Enciclopedia de Cartas**
   - 78 cartas (22 Arcanos Mayores + 56 Arcanos Menores)
   - Disponible sin conexión a internet
   - Información completa de cada carta

### Restricciones Clave

**Plataforma**
- Exclusivamente Android (SDK mínimo 24 - Android 7.0)
- Orientación vertical (portrait)

**Fuera del Alcance (MVP)**
- Sistema de usuarios y autenticación
- Historial de tiradas guardadas
- Backend propio
- Compartir en redes sociales
- Sistema de favoritos

### Criterios de Éxito

**Funcionales**
- Usuario completa tirada en menos de 2 minutos
- Usuario consulta información de cartas sin tirada
- Interpretaciones coherentes con la pregunta

**No Funcionales**
- App funciona sin conexión para enciclopedia
- Generación de IA en menos de 30 segundos
- Imágenes cargan en menos de 2 segundos

---

## Información Adicional

- **Fuente de verdad**: Este documento define QUÉ construir y POR QUÉ, no el CÓMO
- **Última actualización**: 2026-04-24
- **Estado**: ⏳ Pendiente de revisión

---

*Documento generado automáticamente desde `requirements.md`*
