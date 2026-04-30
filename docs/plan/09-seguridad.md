# 9. Configuración de Seguridad

## 9.1 API Key de Claude

**Opción elegida: BuildConfig**

```kotlin
// build.gradle.kts (app)
android {
    buildTypes {
        debug {
            buildConfigField("String", "CLAUDE_API_KEY", "\"${project.findProperty("CLAUDE_API_KEY")}\"")
        }
        release {
            buildConfigField("String", "CLAUDE_API_KEY", "\"${project.findProperty("CLAUDE_API_KEY")}\"")
        }
    }
}

// local.properties (git-ignored)
CLAUDE_API_KEY=sk-ant-api03-xxxxx
```

**Justificación:**
- ✅ No expuesta en código fuente
- ✅ Diferente por entorno (debug/release)
- ⚠️ Limitación: La key está en el APK (aceptable para MVP sin backend)

**Mejora futura:** Implementar backend proxy para ocultar completamente la API key.
