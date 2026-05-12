# 📱 Play Store Assets para TarotAI

Esta carpeta contiene todos los recursos gráficos necesarios para la publicación en Google Play Store.

## 📂 Estructura

```
play_store_assets/
├── ic_launcher_512.png          # Icono de alta resolución (512x512 px)
├── feature_graphic.png           # Gráfico de funciones (1024x500 px)
├── screenshots/                  # Capturas de pantalla
│   ├── 01_home.png              # Pantalla principal
│   ├── 02_spread_selection.png  # Selección de tirada
│   ├── 03_reading.png           # Tirada de cartas
│   ├── 04_interpretation.png    # Interpretación IA
│   ├── 05_encyclopedia.png      # Enciclopedia
│   ├── 06_card_detail.png       # Detalle de carta
│   ├── 07_history.png           # Historial (opcional)
│   └── 08_manual_load.png       # Carga manual (opcional)
└── README.md                     # Este archivo
```

## 📐 Especificaciones

### Icono de Alta Resolución
- **Tamaño**: 512x512 px
- **Formato**: PNG
- **Fondo**: Sin transparencia
- **Contenido**: Logo de TarotAI

### Feature Graphic
- **Tamaño**: 1024x500 px
- **Formato**: PNG o JPEG
- **Contenido sugerido**:
  - Logo de TarotAI
  - Texto: "Tarot con IA"
  - Fondo místico/esotérico
  - Cartas del tarot de fondo

### Capturas de Pantalla
- **Cantidad**: Mínimo 2, recomendado 4-8
- **Tamaño**: 1080x1920 px (portrait)
- **Formato**: PNG o JPEG
- **Peso máximo**: 8MB por imagen

## 🎨 Cómo Crear los Assets

### Opción 1: Tomar Screenshots

```bash
# Con dispositivo conectado vía ADB
adb shell screencap -p /sdcard/screenshot.png
adb pull /sdcard/screenshot.png ./screenshots/01_home.png

# Con emulador Android Studio
# Click derecho en la pantalla > Save Screenshot
```

### Opción 2: Usar Herramientas de Diseño

**Para el icono y feature graphic:**
- **Figma** (gratis): https://figma.com
- **Canva** (gratis): https://canva.com
- **GIMP** (gratis): https://gimp.org

**Templates recomendados:**
- Buscar "app icon 512x512" en Canva
- Buscar "feature graphic 1024x500" en Canva

## ✅ Checklist de Assets

- [ ] ic_launcher_512.png creado
- [ ] feature_graphic.png creado
- [ ] screenshots/01_home.png
- [ ] screenshots/02_spread_selection.png
- [ ] screenshots/03_reading.png
- [ ] screenshots/04_interpretation.png
- [ ] screenshots/05_encyclopedia.png (opcional)
- [ ] screenshots/06_card_detail.png (opcional)
- [ ] screenshots/07_history.png (opcional)
- [ ] screenshots/08_manual_load.png (opcional)

## 🎯 Consejos de Diseño

### Icono 512x512
- Usar colores de la app (morado místico + dorado)
- Incluir símbolo reconocible (ej: carta de tarot estilizada)
- Legible incluso en tamaño pequeño
- Sin texto (el texto se vuelve ilegible)

### Feature Graphic 1024x500
- Debe verse bien en horizontal
- Incluir nombre de la app "TarotAI"
- Usar tipografía legible
- Puede incluir tagline: "Tarot con Inteligencia Artificial"
- Fondo atractivo pero no saturado

### Screenshots
- Mostrar las funcionalidades principales
- Usar pantallas con contenido real (no pantallas vacías)
- Orden lógico: Home → Tirada → Resultado → Extras
- Pueden incluir overlays con texto explicativo (opcional)

## 🚀 Próximos Pasos

1. **Crear assets gráficos** usando Figma/Canva
2. **Tomar screenshots** de la app funcionando
3. **Organizar archivos** en esta carpeta siguiendo la estructura
4. **Verificar dimensiones** con los comandos:

```bash
# Verificar tamaño de imagen
file ic_launcher_512.png
sips -g pixelWidth -g pixelHeight ic_launcher_512.png  # macOS
```

---

**Última actualización**: 2026-05-12
