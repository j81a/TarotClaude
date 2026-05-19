# Requisitos del Proyecto - TarotAI

## Versiones

### v1.0.0 (Lanzamiento Inicial) ✅
- Realización de tiradas automáticas
- Interpretación por IA
- Enciclopedia de cartas offline

### v1.1.0 ✅
- **Historial de lecturas guardadas**
- **Carga manual de tiradas** (para tarotistas profesionales)

### v1.2.0 (En desarrollo) ⏳
- **Mejoras de UX** - Corrección de problemas de usabilidad identificados en testing

### v1.3.0 (Completada) ✅
- **Reconocimiento de Voz** - Entrada por voz en QuestionScreen

### v1.4.0 (Completada) ✅
- **Splash Screen Personalizado** - Pantalla de carga con branding

### v1.5.0 (Completada) ✅
- **Modernización de HomeScreen** - Rediseño visual de pantalla principal

### v1.5.1 (Completada) ✅
- **Mejoras Visuales Globales** - Márgenes consistentes + modernización de pantallas clave

### v1.6.0 (Completada) ✅
- **Campo Reflexiones en Cartas** - Preguntas de reflexión para cada carta del tarot

---

## Introducción

TarotAI es una aplicación Android que permite realizar tiradas de Tarot Rider-Waite-Smith con interpretaciones generadas por IA, combinando reflexión personal con aprendizaje sobre simbología del tarot.

### Propósito
Ofrecer dos experiencias complementarias:
1. **Reflexión personal**: Responder preguntas mediante tiradas interpretadas por IA
2. **Aprendizaje**: Estudiar el significado de cada carta del Tarot Rider-Waite-Smith

### Usuarios Objetivo
- Público general interesado en tarot
- Estudiantes de tarot que desean aprender
- Usuarios que buscan reflexión personal mediante las cartas
- **🆕 Tarotistas profesionales** que necesitan herramientas de gestión de consultas

---

## Tabla de Contenidos

### Requisitos v1.0.0 ✅
1. [Requisitos Funcionales](./funcionales.md) - RF-01 a RF-10
2. [Requisitos No Funcionales](./no-funcionales.md) - 5 requisitos
3. [UI/UX](./ui-ux.md) - Especificaciones visuales
4. [Criterios de Aceptación](./criterios-aceptacion.md) - Validación de requisitos

### Requisitos v1.1.0 ✅
5. [Historial de Lecturas Guardadas](./historial.md) - RF-11
6. [Carga Manual de Tiradas](./carga-manual.md) - RF-12

### Requisitos v1.2.0 ⏳
7. [Mejoras de UX](./mejoras-ux.md) - RF-13 a RF-20

### Requisitos v1.3.0 ✅
8. [Reconocimiento de Voz](./mejoras-ux.md#rf-21) - RF-21

### Requisitos v1.4.0 ✅
9. Splash Screen Personalizado (ver docs/fases/)

### Requisitos v1.5.0 ✅
10. [Modernización de HomeScreen](./mejoras-ux.md#rf-22) - RF-22

### Requisitos v1.5.1 ✅
11. [Mejoras Visuales Globales + Modernización de Pantallas](./mejoras-ux.md#rf-23) - RF-23

### Requisitos v1.6.0 ✅
12. [Campo Reflexiones en Cartas](./mejoras-ux.md#rf-24) - RF-24

---

## Resumen Ejecutivo

### Cuantitativo de Requisitos

| Categoría | v1.0.0 | v1.1.0 | Total |
|-----------|--------|--------|-------|
| **Requisitos Funcionales** | 10 | +2 | **12** |
| **Requisitos No Funcionales** | 5 | - | **5** |
| **Total** | 15 | +2 | **17** |

### Funcionalidades Core

**v1.0.0 - 3 pilares principales:**

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

**v1.1.0 - 2 nuevas funcionalidades profesionales:**

4. **Historial de Lecturas Guardadas** 🆕
   - Guardar lecturas con nombre de consultante
   - Agregar y editar notas personales
   - Revisión de interpretaciones pasadas
   - Persistencia en base de datos local

5. **Carga Manual de Tiradas** 🆕
   - Cargar cartas de tiradas físicas
   - Selección manual de cada carta y orientación
   - Interpretación por IA de cartas físicas
   - Ideal para tarotistas profesionales

### Restricciones Clave

**Plataforma**
- Exclusivamente Android (SDK mínimo 24 - Android 7.0)
- Orientación vertical (portrait)

**Fuera del Alcance (v1.1.0)**
- Sistema de usuarios y autenticación
- Backend propio (sincronización en la nube)
- Compartir en redes sociales
- Sistema de favoritos
- Exportar historial en PDF/imagen

### Criterios de Éxito

**Funcionales (v1.0.0)**
- ✅ Usuario completa tirada en menos de 2 minutos
- ✅ Usuario consulta información de cartas sin tirada
- ✅ Interpretaciones coherentes con la pregunta

**Funcionales (v1.1.0)**
- ⏳ Usuario puede guardar y revisar lecturas pasadas
- ⏳ Usuario puede cargar tiradas físicas para interpretación
- ⏳ Tarotistas profesionales adoptan la app como herramienta de trabajo

**No Funcionales**
- ✅ App funciona sin conexión para enciclopedia
- ✅ Generación de IA en menos de 30 segundos
- ✅ Imágenes cargan en menos de 2 segundos

---

## Información Adicional

- **Fuente de verdad**: Este documento define QUÉ construir y POR QUÉ, no el CÓMO
- **Última actualización**: 2026-05-06
- **Versión actual en desarrollo**: v1.1.0
- **Estado**: ⏳ Pendiente de revisión (v1.1.0)

---

*Documento actualizado con estructura fraccionada para optimización de tokens*
