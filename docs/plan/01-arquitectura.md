# 1. Arquitectura General

## 1.1 Patrón Arquitectónico

**MVVM (Model-View-ViewModel) + Clean Architecture Básica**

```
┌─────────────────────────────────────────────────┐
│                 Presentation                     │
│  (Jetpack Compose UI + ViewModels)              │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│                  Domain                          │
│  (Use Cases + Domain Models + Repositories)     │
└─────────────────┬───────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────┐
│                   Data                           │
│  (Room DB + API Client + Data Models)           │
└─────────────────────────────────────────────────┘
```

**Justificación:**
- MVVM es el estándar recomendado por Google para Android
- Clean Architecture garantiza separación de responsabilidades
- Facilita testing y mantenibilidad
- Jetpack Compose requiere ViewModels para gestión de estado

## 1.2 Módulos de la Aplicación

El proyecto tendrá un **único módulo** (`app`) con paquetes organizados por feature:

```
com.waveapp.tarotai/
├── core/                    # Código compartido
│   ├── di/                 # Dependency Injection (Hilt)
│   ├── navigation/         # Navegación de Compose
│   └── ui/                 # Componentes UI reutilizables
│
├── data/                   # Capa de datos
│   ├── local/             # Room Database
│   │   ├── entities/      # Entidades de Room
│   │   ├── dao/           # Data Access Objects
│   │   └── database/      # Database singleton
│   ├── remote/            # API de Claude
│   │   ├── api/           # Interface del API
│   │   ├── dto/           # Data Transfer Objects
│   │   └── client/        # Cliente HTTP (Retrofit)
│   └── repository/        # Implementación de repositorios
│
├── domain/                # Capa de dominio
│   ├── model/            # Modelos de dominio
│   ├── repository/       # Interfaces de repositorios
│   └── usecase/          # Casos de uso
│
└── presentation/         # Capa de presentación
    ├── reading/         # Feature: Realizar tiradas
    │   ├── ui/         # Screens y componentes
    │   └── viewmodel/  # ViewModels
    ├── encyclopedia/    # Feature: Enciclopedia de cartas
    │   ├── ui/
    │   └── viewmodel/
    └── main/           # Pantalla principal y navegación
        ├── ui/
        └── viewmodel/
```

**Justificación de estructura modular por feature:**
- Cada feature (tirada, enciclopedia) es independiente
- Facilita encontrar código relacionado
- Preparado para escalar a multi-módulo si crece el proyecto
