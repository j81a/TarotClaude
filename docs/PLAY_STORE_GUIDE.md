# 🚀 Guía Completa para Publicar TarotAI en Google Play Store

**Versión**: 1.0.0
**Fecha**: 2026-05-12
**App**: TarotAI - Tarot con IA

---

## 📋 TABLA DE CONTENIDOS

1. [Preparación de la App](#fase-1-preparación-de-la-app)
2. [Creación de Cuenta Google Play Console](#fase-2-crear-cuenta-en-google-play-console)
3. [Preparación de Assets](#fase-3-preparar-assets-gráficos)
4. [Configuración del Proyecto](#fase-4-configurar-proyecto-en-play-console)
5. [Subida del APK/AAB](#fase-5-subir-app-bundle)
6. [Revisión y Publicación](#fase-6-revisión-y-publicación)

---

## FASE 1: Preparación de la App

### 1.1. Generar Keystore (Clave de Firma)

**IMPORTANTE**: Guarda esta clave en un lugar seguro. Si la pierdes, NO podrás actualizar tu app.

```bash
# Ejecutar en terminal (requiere interacción manual)
keytool -genkey -v -keystore ~/tarotai-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 -alias tarotai
```

**Información a proporcionar:**
- **Contraseña del keystore**: Mínimo 6 caracteres (¡GUÁRDALA!)
- **Nombre y apellido**: Tu nombre o el de tu empresa
- **Unidad organizativa**: (Opcional) Ej: "Development"
- **Organización**: (Opcional) Ej: "WaveApp"
- **Ciudad**: Tu ciudad
- **Estado/Provincia**: Tu estado
- **Código de país**: Ej: "ES", "MX", "AR"

**IMPORTANTE**:
```
⚠️ GUARDAR EN LUGAR SEGURO:
   - Archivo: ~/tarotai-release-key.jks
   - Contraseña del keystore
   - Contraseña del alias (puede ser la misma)
   - Alias: tarotai
```

---

### 1.2. Configurar Signing en build.gradle.kts

Edita el archivo `app/build.gradle.kts`:

```kotlin
android {
    // ... configuración existente ...

    signingConfigs {
        create("release") {
            storeFile = file(System.getenv("TAROT_KEYSTORE_PATH")
                ?: "${System.getProperty("user.home")}/tarotai-release-key.jks")
            storePassword = System.getenv("TAROT_KEYSTORE_PASSWORD")
            keyAlias = "tarotai"
            keyPassword = System.getenv("TAROT_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

---

### 1.3. Configurar Variables de Entorno

Crea archivo `~/.tarotai_secrets` (NO subir a git):

```bash
export TAROT_KEYSTORE_PATH="/Users/jorgeaguiar/tarotai-release-key.jks"
export TAROT_KEYSTORE_PASSWORD="tu_contraseña_keystore"
export TAROT_KEY_PASSWORD="tu_contraseña_clave"
```

Carga las variables antes de compilar:

```bash
source ~/.tarotai_secrets
```

---

### 1.4. Actualizar versionCode y versionName

En `app/build.gradle.kts`:

```kotlin
android {
    defaultConfig {
        applicationId = "com.waveapp.tarotai"
        minSdk = 24
        targetSdk = 36
        versionCode = 1        // Incrementar con cada release
        versionName = "1.0.0"  // Versión visible para usuarios

        // ... resto de configuración ...
    }
}
```

---

### 1.5. Compilar Release APK/AAB

**Opción A: APK (más simple)**
```bash
./gradlew assembleRelease
```
El APK estará en: `app/build/outputs/apk/release/app-release.apk`

**Opción B: AAB - App Bundle (RECOMENDADO por Google)**
```bash
./gradlew bundleRelease
```
El AAB estará en: `app/build/outputs/bundle/release/app-release.aab`

**IMPORTANTE**: Google recomienda AAB porque optimiza la descarga para cada dispositivo.

---

### 1.6. Verificar tamaño del APK/AAB

```bash
# Ver tamaño del AAB
ls -lh app/build/outputs/bundle/release/app-release.aab

# Ver tamaño del APK
ls -lh app/build/outputs/apk/release/app-release.apk
```

**Límite**: 150MB para AAB, 100MB para APK (sin expansion files)

---

## FASE 2: Crear Cuenta en Google Play Console

### 2.1. Requisitos Previos

- ✅ Cuenta de Google (Gmail)
- ✅ Tarjeta de crédito/débito válida
- ✅ $25 USD (pago único de registro)

### 2.2. Registro

1. Ve a: https://play.google.com/console/signup
2. Acepta el acuerdo de desarrollador
3. Paga la tarifa de registro ($25 USD)
4. Completa tu perfil:
   - Nombre del desarrollador (visible públicamente)
   - Email de contacto
   - Sitio web (opcional)
   - Dirección física

**IMPORTANTE**: El nombre del desarrollador será visible en Play Store.

---

## FASE 3: Preparar Assets Gráficos

### 3.1. Icono de la App

**Ya tienes**: `app/src/main/res/mipmap-*/ic_launcher.png`

**Requisitos Google Play**:
- **Icono de alta resolución**: 512x512 px PNG (sin transparencias)
- Ubicación sugerida: `docs/play_store_assets/ic_launcher_512.png`

### 3.2. Capturas de Pantalla

**OBLIGATORIO**:
- **Mínimo**: 2 capturas
- **Recomendado**: 4-8 capturas
- **Formato**: PNG o JPEG
- **Tamaño**:
  - Teléfono: 1080x1920 hasta 7680x4320
  - Tablet 7": 1200x1920 hasta 7680x4320
  - Tablet 10": 1800x2560 hasta 7680x4320

**Capturas sugeridas para TarotAI**:
1. Pantalla principal (HomeScreen)
2. Selección de tipo de tirada (SpreadTypeScreen)
3. Tirada de cartas (ReadingScreen)
4. Interpretación con IA (InterpretationScreen)
5. Enciclopedia de cartas (EncyclopediaScreen)
6. Detalle de carta (CardDetailScreen)
7. Historial de lecturas (HistoryScreen)
8. Carga manual de tirada (ManualLoadScreen)

**Ubicación sugerida**: `docs/play_store_assets/screenshots/`

### 3.3. Gráfico de Funciones (Feature Graphic)

**Requisitos**:
- **Tamaño**: 1024x500 px
- **Formato**: PNG o JPEG
- **Ubicación**: `docs/play_store_assets/feature_graphic.png`

**Contenido sugerido**: Logo + texto "Tarot con IA" + fondo místico

### 3.4. Video Promocional (Opcional)

- **Plataforma**: YouTube
- **Duración**: 30 segundos - 2 minutos
- **Contenido**: Demo de la app funcionando

---

## FASE 4: Configurar Proyecto en Play Console

### 4.1. Crear Nueva App

1. Ir a https://play.google.com/console
2. Click en "Crear app"
3. Completar formulario:
   - **Nombre de la app**: "TarotAI - Tarot con IA"
   - **Idioma predeterminado**: Español (España) o tu idioma
   - **Tipo de app**: App
   - **Gratis o de pago**: Gratis (o De pago si vas a cobrar)
   - **Categoría**: Estilo de vida > Misticismo y esoterismo

### 4.2. Configurar Ficha de Play Store

#### Descripción Corta (hasta 80 caracteres):
```
Consulta el Tarot de Marsella con interpretaciones personalizadas de IA
```

#### Descripción Completa (hasta 4000 caracteres):

```markdown
🔮 TarotAI - Tarot de Marsella con Inteligencia Artificial

Descubre el poder del Tarot de Marsella con interpretaciones personalizadas generadas por inteligencia artificial. TarotAI combina la sabiduría ancestral del tarot con tecnología de última generación para ofrecerte lecturas profundas y significativas.

✨ CARACTERÍSTICAS PRINCIPALES

📚 Enciclopedia Completa
• 78 cartas del Tarot de Marsella con imágenes de alta calidad
• Significados detallados de cada carta (derecha e invertida)
• Simbolismo y palabras clave
• Consulta offline, disponible sin conexión

🎴 5 Tipos de Tiradas
• Sí o No: Respuestas directas con justificación educativa
• Tirada Simple: Carta única para consultas rápidas
• Presente: Análisis de tu situación actual (3 cartas)
• Tendencia: Pasado, Presente y Futuro (3 cartas)
• Cruz Celta: Tirada completa para situaciones complejas (10 cartas)

🤖 Interpretación con IA
• Interpretaciones personalizadas basadas en tu pregunta
• Análisis contextual de cada posición
• Explicaciones educativas y profundas
• Powered by Claude AI (Anthropic)

🎙️ Reconocimiento de Voz
• Dicta tu pregunta con voz
• Manos libres mientras consultas
• Reconocimiento en español

📖 Historial de Lecturas
• Guarda tus lecturas importantes
• Agrega notas personales
• Consulta tus lecturas pasadas
• Organiza por fecha y consultante

🎨 Carga Manual de Tiradas
• Registra tiradas realizadas con cartas físicas
• Selector visual de cartas con imágenes
• Recibe interpretación de IA de tus tiradas físicas
• Ideal para tarotistas profesionales

🔒 PRIVACIDAD Y SEGURIDAD
• Sin registro ni cuenta requerida
• Tus datos se almacenan solo en tu dispositivo
• No compartimos información personal
• Conexión segura con API de interpretación

💜 IDEAL PARA
• Principiantes en el tarot
• Tarotistas experimentados
• Personas en búsqueda de orientación espiritual
• Curiosos del esoterismo
• Profesionales que quieren digitalizar sus lecturas

📱 CARACTERÍSTICAS TÉCNICAS
• Diseño Material 3 moderno
• Modo oscuro místico
• Animaciones fluidas
• Optimizada para todo tipo de dispositivos
• Funcionamiento offline (excepto interpretación IA)

⚠️ NOTA IMPORTANTE
TarotAI es una herramienta de autoconocimiento y reflexión personal. Las interpretaciones son generadas por IA basándose en significados tradicionales del Tarot de Marsella y no deben sustituir el consejo profesional en áreas de salud, finanzas o legal.

🌟 ¿Por qué elegir TarotAI?
• Gratis y sin anuncios intrusivos
• Actualizaciones constantes
• Soporte en español
• Comunidad de usuarios activa

Descarga TarotAI ahora y comienza tu viaje de autodescubrimiento con el Tarot de Marsella.

---

Desarrollado por WaveApp
¿Preguntas o sugerencias? Contáctanos: support@waveapp.com
```

#### Categorías y etiquetas:
- **Categoría principal**: Estilo de vida
- **Categoría secundaria**: Entretenimiento
- **Etiquetas**: tarot, tarot marsella, tirada tarot, ia, inteligencia artificial, cartas, lectura cartas, esoterismo, misticismo

---

### 4.3. Información de Contacto

- **Sitio web**: (Opcional) Si tienes un sitio web
- **Email**: Tu email de soporte (será público)
- **Teléfono**: (Opcional)
- **Política de privacidad**: URL requerida (ver sección 4.5)

---

### 4.4. Clasificación de Contenido

Completa el cuestionario de contenido:
- **Violencia**: No
- **Contenido sexual**: No
- **Lenguaje**: No
- **Drogas**: No
- **Contenido generado por usuarios**: No (a menos que agregues foros/chat)
- **Temas sensibles**: Sí - **Ocultismo/Esoterismo**

**Clasificación esperada**: PEGI 3 o Everyone

---

### 4.5. Política de Privacidad (OBLIGATORIA)

Debes crear una página web con tu política de privacidad. Ejemplo básico:

```markdown
# Política de Privacidad - TarotAI

**Última actualización**: 12 de mayo de 2026

## Información que recopilamos

TarotAI NO recopila información personal identificable de sus usuarios.

## Datos almacenados localmente

La aplicación almacena localmente en tu dispositivo:
- Historial de lecturas guardadas
- Notas personales
- Preferencias de la app

Estos datos NO se envían a servidores externos.

## Servicios de terceros

Para generar interpretaciones personalizadas, enviamos a la API de Anthropic Claude:
- Tu pregunta
- Las cartas seleccionadas
- El tipo de tirada

Esta información se procesa de forma anónima y NO se vincula a tu identidad.

## Permisos de la app

- **INTERNET**: Para generar interpretaciones con IA
- **RECORD_AUDIO**: Para reconocimiento de voz (opcional)

## Contacto

Para preguntas sobre privacidad: privacy@waveapp.com
```

**Hosting sugerido**: GitHub Pages (gratis)

---

### 4.6. Público Objetivo

- **Público objetivo**: 18+
- **Contenido**: Ocultismo/Esoterismo/Tarot

---

## FASE 5: Subir App Bundle (AAB)

### 5.1. Crear Nueva Release

1. En Play Console, ir a **Producción > Crear nueva versión**
2. Subir el archivo `app-release.aab`
3. Completar:
   - **Nombre de la versión**: "1.0.0"
   - **Notas de la versión** (en español):

```
🎉 Primera versión de TarotAI

✨ Características incluidas:
• 78 cartas del Tarot de Marsella con enciclopedia completa
• 5 tipos de tiradas (Sí/No, Simple, Presente, Tendencia, Cruz)
• Interpretaciones personalizadas con IA
• Reconocimiento de voz para preguntas
• Historial de lecturas con notas
• Carga manual de tiradas físicas
• Modo offline para enciclopedia

¡Disfruta de tus lecturas de tarot con tecnología IA!
```

---

### 5.2. Revisión de Google

- **Tiempo estimado**: 1-7 días
- **Estado**: Pendiente de revisión
- **Notificación**: Por email

**Posibles razones de rechazo**:
- Iconos de baja calidad
- Descripción engañosa
- Política de privacidad faltante
- Violación de políticas de contenido

---

## FASE 6: Después de la Aprobación

### 6.1. Publicación Gradual (Recomendado)

- **10%** de usuarios (Día 1-2)
- **25%** de usuarios (Día 3-4)
- **50%** de usuarios (Día 5-6)
- **100%** de usuarios (Día 7+)

**Ventaja**: Detectar problemas antes de afectar a todos

---

### 6.2. Monitoreo Post-Lanzamiento

**Revisar cada día**:
- ⭐ Calificaciones y reseñas
- 📊 Estadísticas de instalaciones
- 💥 Informes de fallos (Crashlytics)
- 📈 Métricas de rendimiento

**Herramientas**:
- Google Play Console > Estadísticas
- Google Play Console > Informes de calidad
- Firebase Crashlytics (si lo integras)

---

### 6.3. Responder a Reseñas

**Buenas prácticas**:
- Responder en menos de 24 horas
- Agradecer reseñas positivas
- Resolver problemas de reseñas negativas
- Ser profesional y cortés

---

## 📝 CHECKLIST PRE-PUBLICACIÓN

Antes de subir, verifica:

### Técnico
- [ ] versionCode incrementado
- [ ] versionName actualizado (ej: "1.0.0")
- [ ] Keystore generado y guardado de forma segura
- [ ] Signing configurado en build.gradle.kts
- [ ] AAB compilado exitosamente
- [ ] APK/AAB firmado correctamente
- [ ] Tamaño < 150MB
- [ ] Probado en dispositivos reales
- [ ] Sin errores de compilación
- [ ] ProGuard configurado

### Assets
- [ ] Icono 512x512 creado
- [ ] 4-8 capturas de pantalla
- [ ] Feature graphic 1024x500
- [ ] Video promocional (opcional)

### Play Console
- [ ] Cuenta creada y pagada ($25)
- [ ] App creada en Play Console
- [ ] Descripción corta y larga completadas
- [ ] Categorías seleccionadas
- [ ] Clasificación de contenido completada
- [ ] Política de privacidad publicada (URL)
- [ ] Información de contacto completa
- [ ] Capturas subidas
- [ ] Icono 512x512 subido
- [ ] Feature graphic subido

### Legal
- [ ] Política de privacidad creada y accesible
- [ ] Términos de servicio (opcional pero recomendado)
- [ ] Derechos de imágenes verificados

---

## 🚨 ERRORES COMUNES Y SOLUCIONES

### Error: "keystore password was incorrect"

**Solución**: Verifica las variables de entorno `TAROT_KEYSTORE_PASSWORD`

### Error: "Unsigned APK"

**Solución**: Asegúrate de ejecutar `bundleRelease` no `bundleDebug`

### Error: "App bundle contains forbidden permissions"

**Solución**: Revisa los permisos en `AndroidManifest.xml`

### Error: "Target SDK version must be at least 33"

**Solución**: Actualiza `targetSdk = 36` en `build.gradle.kts`

---

## 📞 SOPORTE

- **Google Play Console Help**: https://support.google.com/googleplay/android-developer
- **Comunidad de desarrolladores**: https://www.reddit.com/r/androiddev/
- **Stack Overflow**: https://stackoverflow.com/questions/tagged/google-play

---

## 🎯 PRÓXIMOS PASOS DESPUÉS DE PUBLICAR

1. **Marketing**:
   - Compartir en redes sociales
   - Crear landing page
   - Blog post de lanzamiento
   - Video demo en YouTube

2. **Monitoreo**:
   - Configurar Firebase Analytics
   - Configurar Crashlytics
   - Revisar métricas diariamente

3. **Mejora continua**:
   - Responder reseñas
   - Corregir bugs reportados
   - Planificar v1.1.0 con nuevas funcionalidades

---

## ✅ ¡Listo!

Sigue esta guía paso a paso y tendrás TarotAI publicada en Google Play Store.

**¡Mucha suerte con tu lanzamiento! 🚀**
