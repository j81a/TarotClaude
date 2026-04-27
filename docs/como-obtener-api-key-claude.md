# Cómo Obtener tu API Key de Claude (Anthropic)

> **Guía paso a paso para conseguir acceso a la API de Claude**

---

## 🔑 Paso 1: Verifica si ya tienes una API Key

Antes de crear una nueva, verifica si ya tienes una guardada:

### Opción A: Buscar en tu computadora
```bash
# En tu terminal, busca archivos con "claude" o "anthropic"
cd ~
grep -r "sk-ant-api" . 2>/dev/null

# Busca en archivos de configuración comunes
cat ~/.bashrc | grep CLAUDE
cat ~/.zshrc | grep CLAUDE
cat ~/.env | grep CLAUDE
```

### Opción B: Revisar si ya tienes cuenta
1. Ve a: https://console.anthropic.com/
2. Intenta iniciar sesión con tu email
3. Si ya tienes cuenta, ve directamente al **Paso 3**

---

## 🆕 Paso 2: Crear Cuenta en Anthropic (si no tienes)

### 2.1 Registrarse
1. Ve a: **https://console.anthropic.com/signup**
2. Regístrate con:
   - Email (recomendado)
   - Google
   - GitHub

### 2.2 Verificar Email
- Revisa tu bandeja de entrada
- Haz click en el link de verificación

### 2.3 Configurar Cuenta
- Completa tu perfil básico
- Acepta los términos de servicio

---

## 🔐 Paso 3: Generar tu API Key

### 3.1 Acceder a la Consola
1. Inicia sesión en: **https://console.anthropic.com/**
2. Verás el dashboard principal

### 3.2 Navegar a API Keys
1. En el menú lateral izquierdo, busca **"API Keys"**
2. O ve directamente a: **https://console.anthropic.com/settings/keys**

### 3.3 Crear Nueva Key
1. Click en **"Create Key"** o **"+ New Key"**
2. Dale un nombre descriptivo: `TarotAI-Dev` o `TarotAI-Local`
3. Click en **"Create"** o **"Generate"**

### 3.4 Copiar la Key
⚠️ **MUY IMPORTANTE**:
- La key se mostrará **UNA SOLA VEZ**
- Cópiala inmediatamente
- Se ve así: `sk-ant-api03-xxxxxxxxxxxxxxxxxxxxxxxxxxxxx`
- Si la pierdes, deberás crear una nueva

```
Ejemplo de API Key:
sk-ant-api03-AbCdEfGhIjKlMnOpQrStUvWxYz1234567890AbCdEfGhIjKlMnOpQrStUvWx_yz123456
```

---

## 💳 Paso 4: Configurar Método de Pago (Requerido)

**Anthropic requiere un método de pago** para usar la API (aunque el uso sea mínimo).

### 4.1 Añadir Tarjeta
1. En la consola, ve a **"Billing"** o **"Settings > Billing"**
2. Click en **"Add Payment Method"**
3. Ingresa los datos de tu tarjeta de crédito/débito

### 4.2 Configurar Límites (Recomendado)
Para evitar sorpresas:

1. Ve a **"Settings > Usage Limits"**
2. Configura un **límite mensual**:
   - Para desarrollo/pruebas: **$5-10 USD/mes**
   - Para producción inicial: **$20-50 USD/mes**

**Ejemplo de límites seguros**:
```
- Monthly spending limit: $10 USD
- Email alert at: $5 USD (50%)
- Stop API calls at: $10 USD (100%)
```

### 4.3 Costos Estimados para TarotAI

**Modelo recomendado**: `claude-3-5-sonnet-20241022`

| Acción | Tokens aprox. | Costo por llamada | 100 llamadas |
|--------|--------------|-------------------|--------------|
| Tirada de 1 carta | ~500 tokens | $0.002 | $0.20 |
| Tirada de 3 cartas | ~1000 tokens | $0.004 | $0.40 |
| Tirada de 5 cartas | ~1500 tokens | $0.006 | $0.60 |

**Estimación mensual para desarrollo**:
- 50 tiradas de prueba/mes = ~$0.20
- 100 tiradas de prueba/mes = ~$0.40
- 500 tiradas de prueba/mes = ~$2.00

💡 **Conclusión**: Con $5-10 USD puedes hacer cientos de pruebas sin problema.

---

## 📁 Paso 5: Guardar la API Key en el Proyecto

### 5.1 Abrir/Crear el archivo `local.properties`

En la raíz de tu proyecto TarotAI:

```bash
cd /Users/jorgeaguiar/SddProyect/TarotAI
```

Verificar si existe:
```bash
ls -la local.properties
```

Si NO existe, créalo:
```bash
touch local.properties
```

### 5.2 Añadir la API Key

Abre el archivo con tu editor favorito:
```bash
# Con nano
nano local.properties

# Con VS Code
code local.properties

# Con cualquier editor de texto
open -a TextEdit local.properties
```

Agrega esta línea (reemplaza con tu key real):
```properties
## This file must *NOT* be checked into Version Control Systems,
# as it contains information specific to your local configuration.

sdk.dir=/Users/jorgeaguiar/Library/Android/sdk

# Claude API Key para TarotAI
CLAUDE_API_KEY=sk-ant-api03-xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

Guarda y cierra el archivo.

### 5.3 Verificar que `.gitignore` incluye `local.properties`

Abre el archivo `.gitignore` en la raíz del proyecto:
```bash
cat .gitignore | grep local.properties
```

Si NO aparece `local.properties`, agrégalo:
```bash
echo "local.properties" >> .gitignore
```

---

## ✅ Paso 6: Verificar la Configuración

### 6.1 Verificar que el archivo existe
```bash
cd /Users/jorgeaguiar/SddProyect/TarotAI
cat local.properties | grep CLAUDE_API_KEY
```

**Deberías ver**:
```
CLAUDE_API_KEY=sk-ant-api03-xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

### 6.2 Verificar que NO está en Git
```bash
git status | grep local.properties
```

**NO debería aparecer** en la lista de archivos para commit.

---

## 🧪 Paso 7: Probar la API Key (Opcional)

Puedes probar tu key con `curl` antes de usarla en la app:

```bash
# Reemplaza TU_API_KEY con tu key real
curl https://api.anthropic.com/v1/messages \
  -H "content-type: application/json" \
  -H "x-api-key: TU_API_KEY" \
  -H "anthropic-version: 2023-06-01" \
  -d '{
    "model": "claude-3-5-sonnet-20241022",
    "max_tokens": 100,
    "messages": [
      {"role": "user", "content": "Di hola en español"}
    ]
  }'
```

**Respuesta esperada**:
```json
{
  "content": [
    {
      "text": "¡Hola! ¿Cómo estás?",
      "type": "text"
    }
  ],
  "id": "msg_...",
  "model": "claude-3-5-sonnet-20241022",
  ...
}
```

Si ves esto, **¡tu API key funciona!** ✅

---

## ⚠️ Seguridad: Mejores Prácticas

### ✅ DO (Hacer)
- ✅ Guardar la key en `local.properties` (git-ignored)
- ✅ Usar variables de entorno en producción
- ✅ Configurar límites de gasto
- ✅ Rotar keys periódicamente (cada 3-6 meses)
- ✅ Usar keys diferentes para dev/prod

### ❌ DON'T (No Hacer)
- ❌ **NUNCA** subir la key a GitHub/GitLab
- ❌ **NUNCA** hardcodearla en el código
- ❌ **NUNCA** compartirla públicamente
- ❌ **NUNCA** incluirla en logs
- ❌ **NUNCA** dejar keys sin límites de gasto

---

## 🆘 Solución de Problemas

### Problema 1: "Invalid API Key"
**Causa**: Key incorrecta o mal formateada
**Solución**:
- Verifica que copiaste la key completa
- No debe tener espacios ni saltos de línea
- Debe empezar con `sk-ant-api`

### Problema 2: "Insufficient credits"
**Causa**: No has añadido método de pago o llegaste al límite
**Solución**:
- Ve a Billing y añade/verifica tu tarjeta
- Verifica los límites configurados

### Problema 3: "Rate limit exceeded"
**Causa**: Hiciste demasiadas llamadas muy rápido
**Solución**:
- Espera 1 minuto
- Implementa rate limiting en tu código

### Problema 4: "File not found: local.properties"
**Causa**: El archivo no existe o está en el lugar equivocado
**Solución**:
```bash
cd /Users/jorgeaguiar/SddProyect/TarotAI
touch local.properties
echo "CLAUDE_API_KEY=tu-key-aqui" >> local.properties
```

---

## 📊 Monitoreo de Uso

### Ver tus Estadísticas
1. Ve a: **https://console.anthropic.com/settings/usage**
2. Verás:
   - Requests realizados
   - Tokens consumidos
   - Costo total
   - Gráficas por día/semana/mes

### Alertas por Email
1. Ve a: **Settings > Notifications**
2. Activa alertas para:
   - 50% del límite alcanzado
   - 80% del límite alcanzado
   - Límite excedido

---

## 🎯 Resumen: Checklist Completo

```
☐ Crear cuenta en Anthropic Console
☐ Verificar email
☐ Generar API Key
☐ Copiar la key (¡solo se muestra una vez!)
☐ Añadir método de pago
☐ Configurar límites de gasto ($5-10 para desarrollo)
☐ Guardar key en local.properties
☐ Verificar que local.properties está en .gitignore
☐ (Opcional) Probar key con curl
☐ Monitorear uso en la consola
```

---

## 📞 Contacto y Recursos

- **Documentación oficial**: https://docs.anthropic.com/
- **Pricing**: https://www.anthropic.com/pricing
- **API Reference**: https://docs.anthropic.com/en/api/messages
- **Support**: support@anthropic.com
- **Status page**: https://status.anthropic.com/

---

**¡Listo!** Ya tienes todo configurado para usar Claude en TarotAI. 🎉

Ahora puedes continuar con la implementación del proyecto.
