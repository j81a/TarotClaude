# Guía de Seguridad - TarotAI

## ⚠️ IMPORTANTE: API Key de Anthropic

### Configuración Inicial

Para ejecutar esta aplicación necesitas una API Key de Anthropic:

1. **Obtén tu API Key**:
   - Visita https://console.anthropic.com/settings/keys
   - Crea una nueva API Key

2. **Configura la API Key localmente**:
   ```bash
   cp gradle.properties.template gradle.properties
   ```

3. **Edita `gradle.properties`** y reemplaza:
   ```properties
   CLAUDE_API_KEY=YOUR_API_KEY_HERE
   ```
   Con tu API Key real:
   ```properties
   CLAUDE_API_KEY=sk-ant-api03-xxxxxxxxxxxxx
   ```

### 🔐 Seguridad

- ✅ `gradle.properties` está en `.gitignore` y **NO se subirá a Git**
- ✅ Solo existe `gradle.properties.template` en el repositorio (sin API Key real)
- ⚠️ **NUNCA** hagas commit de `gradle.properties` con tu API Key
- ⚠️ **NUNCA** compartas tu API Key públicamente

### 🚨 Si expusiste accidentalmente tu API Key

1. **Regenera inmediatamente tu API Key** en https://console.anthropic.com/settings/keys
2. **Revoca la API Key comprometida**
3. Actualiza tu `gradle.properties` local con la nueva Key
4. Si fue commiteada a Git, usa `git filter-branch` para limpiar el historial

## Otros Archivos Sensibles

Los siguientes archivos también están protegidos por `.gitignore`:

- `local.properties` - Configuraciones locales del SDK de Android
- `*.keystore` - Archivos de firma de la aplicación
- `.idea/` - Configuraciones del IDE

## Reportar Vulnerabilidades

Si encuentras una vulnerabilidad de seguridad, por favor **NO** abras un issue público.
Contacta directamente al mantenedor del proyecto.
