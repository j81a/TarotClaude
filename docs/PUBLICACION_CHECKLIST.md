# ✅ Checklist de Publicación - TarotAI

## 📅 ACCIONES INMEDIATAS (HOY)

### 1. Generar Keystore ⏳
```bash
keytool -genkey -v -keystore ~/tarotai-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias tarotai
```
- [ ] Keystore generado
- [ ] Contraseña guardada en lugar seguro
- [ ] Archivo `.tarotai_secrets` actualizado con tu contraseña real

### 2. Configurar Secretos ⏳
```bash
# Edita este archivo y reemplaza TU_CONTRASEÑA_AQUI con tu contraseña real
nano ~/.tarotai_secrets
```
- [ ] Contraseñas actualizadas en `~/.tarotai_secrets`
- [ ] Archivo probado: `source ~/.tarotai_secrets`

### 3. Compilar Release AAB ⏳
```bash
source ~/.tarotai_secrets
./gradlew bundleRelease
```
- [ ] AAB compilado exitosamente
- [ ] Archivo ubicado en: `app/build/outputs/bundle/release/app-release.aab`
- [ ] Tamaño verificado (< 150MB)

---

## 📸 ASSETS GRÁFICOS (ESTA SEMANA)

### 4. Crear Capturas de Pantalla
Necesitas 4-8 capturas (1080x1920 px):
- [ ] 1. HomeScreen (Pantalla principal)
- [ ] 2. SpreadTypeScreen (Selección de tirada)
- [ ] 3. ReadingScreen (Tirada con cartas)
- [ ] 4. InterpretationScreen (Interpretación IA)
- [ ] 5. EncyclopediaScreen (Enciclopedia)
- [ ] 6. CardDetailScreen (Detalle de carta)
- [ ] 7. HistoryScreen (Historial) - OPCIONAL
- [ ] 8. ManualLoadScreen (Carga manual) - OPCIONAL

**Ubicación**: `docs/play_store_assets/screenshots/`

**Cómo tomarlas**:
1. Abrir la app en emulador o dispositivo real
2. Usar ADB: `adb shell screencap -p /sdcard/screenshot.png`
3. O usar herramientas del emulador
4. Redimensionar a 1080x1920 si es necesario

### 5. Crear Icono 512x512
- [ ] Icono de alta resolución creado (512x512 px PNG)
- [ ] Sin transparencias
- [ ] Guardado en: `docs/play_store_assets/ic_launcher_512.png`

**Herramientas sugeridas**:
- Figma (gratis)
- Canva (gratis)
- GIMP (gratis)

### 6. Crear Feature Graphic
- [ ] Gráfico 1024x500 px creado
- [ ] Contiene: Logo + texto "Tarot con IA" + fondo místico
- [ ] Guardado en: `docs/play_store_assets/feature_graphic.png`

---

## 🌐 REQUISITOS LEGALES (ESTA SEMANA)

### 7. Política de Privacidad
- [ ] Crear política de privacidad (usa template de la guía)
- [ ] Publicar en sitio web accesible públicamente
- [ ] URL guardada para Play Console

**Opciones de hosting gratis**:
- GitHub Pages
- Google Sites
- Netlify

### 8. Cuenta Google Play Console
- [ ] Cuenta de desarrollador creada
- [ ] Tarifa de $25 USD pagada
- [ ] Perfil de desarrollador completado
- [ ] Email de contacto configurado

---

## 📝 PLAY STORE LISTING (ESTA SEMANA)

### 9. Crear App en Play Console
- [ ] Nueva app creada en https://play.google.com/console
- [ ] Nombre: "TarotAI - Tarot con IA"
- [ ] Idioma: Español
- [ ] Tipo: Aplicación
- [ ] Categoría: Estilo de vida

### 10. Completar Ficha de la App
- [ ] Descripción corta (80 caracteres)
- [ ] Descripción completa (usa template de la guía)
- [ ] Icono 512x512 subido
- [ ] Feature graphic 1024x500 subido
- [ ] Capturas de pantalla subidas (mínimo 2)
- [ ] Categoría: Estilo de vida
- [ ] Etiquetas: tarot, ia, cartas, esoterismo

### 11. Clasificación de Contenido
- [ ] Cuestionario completado
- [ ] Marcado: Contenido de ocultismo/esoterismo
- [ ] Clasificación obtenida (esperada: PEGI 3 / Everyone)

### 12. Información de Contacto
- [ ] Email de soporte configurado
- [ ] URL de política de privacidad agregada
- [ ] Sitio web (opcional) agregado

---

## 🚀 SUBIDA Y PUBLICACIÓN (PRÓXIMA SEMANA)

### 13. Crear Release de Producción
- [ ] AAB subido a Play Console
- [ ] Nombre de versión: "1.0.0"
- [ ] Notas de la versión escritas (usa template de la guía)
- [ ] Revisión iniciada

### 14. Esperar Aprobación
- [ ] Revisión completada (1-7 días)
- [ ] App aprobada
- [ ] Publicación confirmada

### 15. Publicación Gradual (Recomendado)
- [ ] Día 1-2: 10% de usuarios
- [ ] Día 3-4: 25% de usuarios
- [ ] Día 5-6: 50% de usuarios
- [ ] Día 7+: 100% de usuarios

---

## 📊 POST-LANZAMIENTO

### 16. Monitoreo
- [ ] Revisar estadísticas diariamente
- [ ] Revisar reseñas y responder
- [ ] Monitorear crashes (si existen)
- [ ] Analizar métricas de instalación

### 17. Marketing (Opcional)
- [ ] Compartir en redes sociales
- [ ] Crear landing page
- [ ] Video demo en YouTube
- [ ] Blog post de lanzamiento

---

## 🎯 TIMELINE SUGERIDO

| Día | Actividad |
|-----|-----------|
| **Hoy** | 1-3: Keystore + Compilar AAB |
| **Mañana** | 4-6: Capturas + Assets gráficos |
| **Día 3-4** | 7-8: Política de privacidad + Cuenta Play Console |
| **Día 5-6** | 9-12: Completar ficha en Play Console |
| **Día 7** | 13: Subir AAB y crear release |
| **Día 8-14** | 14: Esperar aprobación de Google |
| **Día 15+** | 15-17: Publicación y monitoreo |

---

## 📞 AYUDA RÁPIDA

### Comandos útiles:

```bash
# Cargar secretos
source ~/.tarotai_secrets

# Compilar AAB
./gradlew bundleRelease

# Ver tamaño del AAB
ls -lh app/build/outputs/bundle/release/app-release.aab

# Verificar firma
jarsigner -verify -verbose -certs app/build/outputs/bundle/release/app-release.aab
```

### Enlaces útiles:

- **Guía completa**: `docs/PLAY_STORE_GUIDE.md`
- **Play Console**: https://play.google.com/console
- **Soporte Google**: https://support.google.com/googleplay/android-developer

---

## ✅ ESTADO ACTUAL

**Progreso**: 0/17 tareas completadas (0%)

**Próximo paso**: Generar keystore (Tarea #1)

---

**Última actualización**: 2026-05-12
