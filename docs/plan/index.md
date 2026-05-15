# Plan de Implementación - TarotAI

> **Documento de arquitectura y decisiones técnicas**
> Define CÓMO implementar los requisitos definidos en `requirements/`
> Última actualización: 2026-05-06

## Versiones

### v1.0.0 (Completada) ✅
- Tiradas automáticas
- Interpretación por IA
- Enciclopedia offline

### v1.1.0 (Completada) ✅
- Historial de lecturas guardadas
- Carga manual de tiradas
- Reutilización de pantallas (QuestionScreen compartida)

### v1.2.0 (En desarrollo) ⏳
- Guardado consistente con valor por defecto
- Consultante opcional en ambos modos
- Carga manual con visualización mejorada
- Imágenes en selector de cartas

---

## Introducción

Este documento proporciona una guía técnica completa para la implementación de TarotAI, una aplicación móvil Android que combina un sistema de tiradas de tarot con interpretaciones potenciadas por inteligencia artificial. La arquitectura está diseñada siguiendo patrones reconocidos como MVVM y Clean Architecture para garantizar mantenibilidad, testabilidad y escalabilidad.

**v1.1.0** agrega funcionalidades profesionales manteniendo coherencia UX mediante reutilización inteligente de componentes existentes.

El documento cubre decisiones tecnológicas fundamentales, patrones de arquitectura, estructura del código, gestión del estado, seguridad, y estrategias de testing. Cada sección proporciona justificaciones claras y ejemplos prácticos para facilitar la comprensión e implementación consistente.

Este es un documento vivo que sirve como referencia durante todo el ciclo de desarrollo y debe consultarse regularmente para asegurar que todas las decisiones técnicas se mantienen consistentes.

## Tabla de Contenidos

### Plan Base (v1.0.0) ✅

1. **[Arquitectura General](./01-arquitectura.md)** - MVVM, Clean Architecture, estructura modular del proyecto
2. **[Estándares de Código](./02-estandares.md)** - Documentación en español, convenciones de naming, formato
3. **[Stack Tecnológico](./03-stack.md)** - Librerías, versiones, justificaciones de cada tecnología
4. **[Modelo de Datos](./04-datos.md)** - Domain models, enums, estructuras de datos principales
5. **[Casos de Uso](./05-casos-uso.md)** - Use cases para tiradas y enciclopedia
6. **[Flujo de Navegación](./06-navegacion.md)** - Rutas, pantallas, estructura de navegación
7. **[Gestión de Estado](./07-estado.md)** - StateFlow, UiState pattern, ViewModels
8. **[Assets y Recursos](./08-assets.md)** - Imágenes, JSON data, ubicaciones de recursos
9. **[Configuración de Seguridad](./09-seguridad.md)** - API Key management, BuildConfig
10. **[Animaciones](./10-animaciones.md)** - Animate de cartas, secuencias de revelado
11. **[Manejo de Errores](./11-errores.md)** - Estrategias por capa, mensajes de usuario
12. **[Performance](./12-performance.md)** - Optimizaciones, carga de imágenes, caché
13. **[Testing Strategy](./13-testing.md)** - Pirámide de testing, estrategia de cobertura
14. **[Dependencias](./14-dependencias.md)** - build.gradle.kts, versiones, librerías
15. **[Fases de Implementación](./15-fases.md)** - Roadmap de desarrollo, milestones
16. **[Riesgos y Mitigaciones](./16-riesgos.md)** - Análisis de riesgos, planes de contingencia
17. **[Métricas de Éxito Técnico](./17-metricas.md)** - Criterios de aceptación técnicos, KPIs

### Cambios de Versión

18. **[Cambios de v1.1.0](./18-v1.1.0.md)** ✅ - Historial, Carga Manual, decisiones arquitecturales
19. **[Cambios de v1.2.0](./19-v1.2.0.md)** ✅ - Guardado consistente, consultante opcional, carga manual mejorada
20. **[Cambios de v1.3.0](./20-v1.3.0.md)** ✅ - Reconocimiento de voz (Speech-to-Text)
21. **[Cambios de v1.5.0](./21-v1.5.0.md)** 🆕 - Modernización de HomeScreen (UI/UX)
