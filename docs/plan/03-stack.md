# 3. Stack Tecnológico

## 3.1 UI y Navegación

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **Jetpack Compose** | 1.7.x | UI declarativa moderna |
| **Compose Navigation** | 2.8.x | Navegación entre pantallas |
| **Material 3** | 1.3.x | Diseño y componentes UI |
| **Coil** | 2.7.x | Carga de imágenes |

**Decisión: Jetpack Compose**
- ✅ Estándar moderno de Android (vs XML)
- ✅ Menos código boilerplate
- ✅ Animaciones más fáciles de implementar
- ✅ Mejor integración con MVVM

**Internacionalización (i18n):**
- Todos los textos de UI deben estar en `res/values/strings.xml`
- NO hardcodear strings en código Kotlin
- Usar `stringResource(R.string.key)` en Composables
- Preparado para soportar múltiples idiomas (español, inglés, etc.)
- Los datos de las cartas (nombres, significados) también irán en recursos localizables

## 3.2 Inyección de Dependencias

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **Hilt** | 2.52 | DI basado en Dagger |

**Decisión: Hilt**
- ✅ Oficial de Google para Android
- ✅ Menos configuración que Dagger puro
- ✅ Integración perfecta con ViewModels

## 3.3 Persistencia Local

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **Room** | 2.6.x | Base de datos SQLite |
| **Kotlin Serialization** | 1.7.x | Serialización JSON |

**Decisión: Room**
- ✅ ORM oficial de Android
- ✅ Verificación en tiempo de compilación
- ✅ Integración con Kotlin Coroutines
- ✅ Ideal para almacenar enciclopedia de cartas

**Esquema de Base de Datos:**

```sql
-- Tabla: Cartas del Tarot
CREATE TABLE tarot_cards (
    id INTEGER PRIMARY KEY,          -- 0-77 (0-21 Mayores, 22-77 Menores)
    name TEXT NOT NULL,              -- "El Loco", "As de Copas"
    arcana_type TEXT NOT NULL,       -- "MAJOR" o "MINOR"
    suit TEXT,                       -- "WANDS", "CUPS", "SWORDS", "PENTACLES" (null para Mayores)
    image_path TEXT NOT NULL,        -- "arcana_major_0.jpg"
    general_meaning TEXT NOT NULL,
    upright_meaning TEXT NOT NULL,
    reversed_meaning TEXT NOT NULL,
    symbolism TEXT NOT NULL,
    keywords TEXT NOT NULL           -- JSON array: ["inicio", "locura", "libertad"]
);

-- Índices para búsqueda rápida
CREATE INDEX idx_arcana_type ON tarot_cards(arcana_type);
CREATE INDEX idx_suit ON tarot_cards(suit);
```

## 3.4 Networking

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **Retrofit** | 2.11.x | Cliente HTTP |
| **OkHttp** | 4.12.x | Cliente HTTP subyacente |
| **Kotlin Coroutines** | 1.9.x | Asincronía |

**API de Claude (Anthropic):**
- Endpoint: `https://api.anthropic.com/v1/messages`
- Autenticación: API Key en header `x-api-key`
- Modelo: `claude-3-5-sonnet-20241022` (o versión disponible)

**Estructura de Request para Interpretación:**

```kotlin
data class ClaudeRequest(
    val model: String,
    val max_tokens: Int,
    val messages: List<Message>
)

data class Message(
    val role: String,  // "user"
    val content: String
)
```

**Prompt Template para Interpretación:**

```
Eres un experto en Tarot de Marsella. Interpreta la siguiente tirada:

TIPO DE TIRADA: {spreadType}
PREGUNTA DEL USUARIO: {question}

CARTAS:
{forEach carta}
- Posición {position}: {cardName} ({orientation})
{end}

Proporciona:
1. INTERPRETACIÓN INDIVIDUAL de cada carta considerando su posición y orientación
2. INTERPRETACIÓN GENERAL de toda la tirada en conjunto

{if spreadType == "Sí o No"}
3. RESPUESTA CLARA: Sí / No / Indefinido
4. JUSTIFICACIÓN EDUCATIVA: Explica por qué esta carta específica significa esa respuesta
{end}

Formato de respuesta en JSON:
{
  "individual_interpretations": [
    {
      "card": "nombre",
      "position": "posición",
      "interpretation": "texto"
    }
  ],
  "general_interpretation": "texto",
  "yes_no_answer": "Sí|No|Indefinido" // solo para tirada Sí o No
  "yes_no_justification": "texto" // solo para tirada Sí o No
}
```

## 3.5 Testing

| Tecnología | Propósito |
|-----------|-----------|
| **JUnit 5** | Tests unitarios |
| **Mockk** | Mocking para Kotlin |
| **Turbine** | Testing de Flows |
| **Compose UI Test** | Testing de UI |
