# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

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

```bash
# Build the project (debug and release variants)
./gradlew build

# Assemble debug APK
./gradlew assembleDebug

# Assemble release APK
./gradlew assembleRelease

# Clean build artifacts
./gradlew clean

# Install debug build on connected device/emulator
./gradlew installDebug

# Uninstall all variants
./gradlew uninstallAll
```

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
1. docs/requirements.md — qué construir (fuente de verdad)
2. docs/plan.md — arquitectura y decisiones técnicas
3. docs/tasks.md — tareas atómicas con checklist

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