# Cómo Publicar la Política de Privacidad de ARCANA1

## Opción 1: GitHub Pages (GRATIS y RECOMENDADO)

GitHub Pages es un servicio gratuito de GitHub que te permite publicar páginas web estáticas.

### Pasos:

1. **Crear un repositorio público en GitHub** (si aún no lo tienes):
   - Ve a https://github.com/new
   - Nombre sugerido: `arcana1-privacy`
   - Marca como "Public"
   - Click en "Create repository"

2. **Subir el archivo HTML**:
   ```bash
   cd /Users/jorgeaguiar/SddProyect/TarotAI

   # Si el repo no existe, clónalo
   git clone https://github.com/TU_USUARIO/arcana1-privacy.git
   cd arcana1-privacy

   # Copiar el archivo HTML
   cp ../docs/privacy_policy.html index.html

   # Subir a GitHub
   git add index.html
   git commit -m "Add privacy policy"
   git push origin main
   ```

3. **Activar GitHub Pages**:
   - Ve a tu repositorio en GitHub
   - Click en "Settings" (Configuración)
   - En el menú lateral, click en "Pages"
   - En "Source", selecciona "main" branch
   - Click en "Save"

4. **Obtener la URL**:
   - La URL será: `https://TU_USUARIO.github.io/arcana1-privacy/`
   - Espera 1-2 minutos para que se publique
   - Verifica que funcione abriendo la URL en tu navegador

5. **Usar la URL en Play Store**:
   - Copia la URL completa
   - Pégala en el campo "Política de privacidad" de Play Console

---

## Opción 2: Google Sites (GRATIS)

1. Ve a https://sites.google.com/
2. Click en "Crear" (botón +)
3. Selecciona "Página en blanco"
4. Dale un nombre: "ARCANA1 Privacy Policy"
5. Haz click en "Insertar" > "HTML integrado"
6. Copia y pega el contenido de `privacy_policy.html`
7. Click en "Publicar"
8. Copia la URL generada
9. Úsala en Play Store

---

## Opción 3: Netlify (GRATIS)

1. Ve a https://www.netlify.com/
2. Regístrate o inicia sesión
3. Arrastra la carpeta que contiene `privacy_policy.html`
4. Netlify generará automáticamente una URL
5. Úsala en Play Store

---

## Opción 4: Hosting Propio

Si tienes un dominio y hosting web:

1. Sube `privacy_policy.html` a tu servidor
2. Accede vía: `https://tudominio.com/privacy_policy.html`
3. Úsala en Play Store

---

## ✅ Verificación

Antes de poner la URL en Play Store, verifica que:

- ✅ La página carga correctamente en navegadores móviles
- ✅ No hay errores 404
- ✅ El contenido es legible en pantallas pequeñas
- ✅ La URL es accesible públicamente (sin login)
- ✅ Usa HTTPS (candado verde en el navegador)

---

## 📝 Nota para Play Store

En Google Play Console, cuando te pidan la "Política de privacidad":

1. Ve a "Configuración de la app" > "Ficha de Play Store"
2. Busca el campo "Política de privacidad"
3. Pega la URL completa (ejemplo: `https://tuusuario.github.io/arcana1-privacy/`)
4. Click en "Guardar"

---

## 🔄 Actualizaciones Futuras

Si necesitas actualizar la política de privacidad:

1. Edita el archivo `docs/privacy_policy.html`
2. Actualiza la fecha en "Última actualización"
3. Sube el nuevo archivo al mismo lugar
4. NO necesitas cambiar la URL en Play Store

---

## 📧 Email de Contacto

Recuerda que el email de contacto en la política es: **jorgeaguiar.dev@gmail.com**

Si quieres cambiarlo, edita el archivo antes de publicarlo.
