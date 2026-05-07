# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 🌍 Idioma / Language

**IMPORTANTE**: Este proyecto está en español. **SIEMPRE** debes comunicarte en español con el usuario.
- Todos los mensajes, explicaciones y documentación deben estar en español
- Los comentarios en el código pueden estar en español o inglés según contexto
- Los commits y documentación técnica deben estar en español
- Solo usa inglés si el usuario explícitamente lo solicita

**IMPORTANT**: This project is in Spanish. You must **ALWAYS** communicate in Spanish with the user.

## Project Overview

TarotAI is an Android application built with Kotlin and Gradle. The project uses modern Android development practices with:
- **Package name**: `com.waveapp.tarotai`
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36
- **Java version**: 11
- **Kotlin version**: 2.0.21

## Build System

This project uses Gradle with Kotlin DSL (`.gradle.kts` files) and centralized dependency management via `gradle/libs.versions.toml`.

### Common Build Commands

⚠️ **IMPORTANTE - NO EJECUTAR AUTOMÁTICAMENTE**:
- **NO ejecutes** `./gradlew assembleDebug`, `./gradlew installDebug` ni comandos de build **sin permiso explícito del usuario**
- **Razón**: Estos comandos consumen tokens y tiempo. El usuario prefiere ejecutarlos manualmente.
- **Excepción**: Solo ejecutar si el usuario lo pide explícitamente con frases como "compila", "instala", "build", etc.

```bash
# Build the project (debug and release variants) - ⚠️ NO EJECUTAR AUTOMÁTICAMENTE
./gradlew build

# Assemble debug APK - ⚠️ NO EJECUTAR AUTOMÁTICAMENTE
./gradlew assembleDebug

# Assemble release APK - ⚠️ NO EJECUTAR AUTOMÁTICAMENTE
./gradlew assembleRelease

# Clean build artifacts
./gradlew clean

# Install debug build on connected device/emulator - ⚠️ NO EJECUTAR AUTOMÁTICAMENTE
./gradlew installDebug

# Uninstall all variants
./gradlew uninstallAll
```

**Workflow recomendado**:
1. Claude hace cambios en el código
2. Claude informa al usuario qué cambió
3. **El usuario** ejecuta manualmente `./gradlew assembleDebug && ./gradlew installDebug`

### Testing Commands

```bash
# Run all unit tests
./gradlew test

# Run unit tests for debug variant
./gradlew testDebug

# Run instrumented tests on connected devices
./gradlew connectedAndroidTest

# Run all checks (lint + tests)
./gradlew check
```

### Useful Development Commands

```bash
# View project dependencies
./gradlew app:dependencies

# View Android dependencies specifically
./gradlew app:androidDependencies

# Check if Jetifier is needed
./gradlew app:checkJetifier

# Display available source sets
./gradlew app:sourceSets
```

## Project Structure

```
TarotAI/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/waveapp/tarotai/    # Main application code
│   │   │   ├── res/                          # Android resources (layouts, drawables, etc.)
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                             # Unit tests (JUnit)
│   │   └── androidTest/                      # Instrumented tests (Espresso)
│   ├── build.gradle.kts                      # App-level build configuration
│   └── proguard-rules.pro                    # ProGuard rules for release builds
├── gradle/
│   └── libs.versions.toml                    # Centralized dependency versions
├── build.gradle.kts                          # Root-level build configuration
├── settings.gradle.kts                       # Project settings and module includes
├── gradlew / gradlew.bat                     # Gradle wrapper scripts
└── local.properties                          # Local SDK paths (git-ignored)
```

## Code Architecture

The application follows standard Android project structure with:
- Source code in `app/src/main/java/com/waveapp/tarotai/`
- Unit tests in `app/src/test/java/com/waveapp/tarotai/`
- Instrumented tests in `app/src/androidTest/java/com/waveapp/tarotai/`

The project currently has a minimal structure as it appears to be in early development stages with no activities or main source files yet implemented.

## Dependencies

Key dependencies are managed through version catalogs in `gradle/libs.versions.toml`:
- **AndroidX Core KTX**: 1.17.0
- **AppCompat**: 1.7.1
- **Material Components**: 1.13.0
- **JUnit**: 4.13.2 (unit testing)
- **Espresso**: 3.7.0 (UI testing)

To add new dependencies, update `gradle/libs.versions.toml` first, then reference them in `app/build.gradle.kts`.

## Development Notes

- The project uses Kotlin as the primary language
- No build flavors or custom build types are currently configured (only debug/release)
- ProGuard is disabled for release builds (`isMinifyEnabled = false`)
- The app currently has no launcher activity defined in the manifest

## Metodología de desarrollo
Este proyecto usa Spec-Driven Development (SDD) estrictamente.

### Documentos de especificación (en orden)
1. **docs/requirements/** — qué construir (fuente de verdad, estructura fraccionada)
   - Lee `docs/requirements/index.md` primero para el overview
   - Luego consulta los archivos específicos según necesites
2. **docs/plan/** — arquitectura y decisiones técnicas (estructura fraccionada)
   - Lee `docs/plan/index.md` primero para el overview
   - Luego consulta los archivos específicos según necesites
3. **docs/tasks.md** — tareas atómicas con checklist (archivo único)

### Estructura fraccionada para optimizar tokens
⚠️ **IMPORTANTE**: Este proyecto usa estructura fraccionada en `requirements/` y `plan/`
- **NO leas** `docs/requirements.md` (archivo antiguo, desactualizado)
- **SÍ lee** `docs/requirements/index.md` y archivos específicos en esa carpeta
- **NO leas** `docs/plan.md` (archivo antiguo, desactualizado)
- **SÍ lee** `docs/plan/index.md` y archivos específicos en esa carpeta
- Esto ahorra tokens y mejora performance en sesiones largas

### Reglas obligatorias
- Nunca escribir código sin tener aprobados los 3 documentos anteriores
- No avanzar al siguiente documento sin aprobación explícita mía
- Si algo del requirements cambia, actualizar el doc antes de tocar código
- Cada tarea del tasks.md debe poder implementarse y testearse de forma aislada

### Stack definido
- Kotlin + Jetpack Compose
- Arquitectura MVVM + Clean Architecture básica
- Hilt para inyección de dependencias
- Room para persistencia local
- API de Claude para interpretación de tiradas

## Informe de Rate Limit de API (OBLIGATORIO)

**AL FINAL DE CADA RESPUESTA** debes incluir una estimación del estado del rate limit de Claude API:

```markdown
## ⚠️ Estimación de Bloqueo de API

**Uso en esta sesión**: ~XXXk tokens
**Riesgo de bloqueo**: Bajo/Moderado/Alto
**Ventana deslizante se reinicia aproximadamente**: ~HH:MM AM/PM
```

**Notas importantes:**
- Los tokens de las herramientas (como `assembleDebug`) también cuentan para el rate limit
- **NO ejecutes comandos de build (`./gradlew assembleDebug`, `installDebug`, etc.) automáticamente**
- El rate limit es de ~5 horas de ventana deslizante
- Si el uso supera ~150k tokens en esta sesión, el riesgo es Alto
- La hora de reinicio es aproximada (5 horas desde que el usuario empezó a usar Claude hoy)

Este informe es OBLIGATORIO y debe aparecer al final de CADA respuesta que generes.