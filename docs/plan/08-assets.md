# 8. Assets y Recursos

## 8.1 Imágenes de Cartas

**Ubicación:** `app/src/main/res/drawable/`

**Nomenclatura:**
- Arcanos Mayores: `card_major_00.jpg` hasta `card_major_21.jpg`
- Arcanos Menores:
  - `card_wands_01.jpg` hasta `card_wands_14.jpg`
  - `card_cups_01.jpg` hasta `card_cups_14.jpg`
  - `card_swords_01.jpg` hasta `card_swords_14.jpg`
  - `card_pentacles_01.jpg` hasta `card_pentacles_14.jpg`

**Tamaño recomendado:** 600x1000px (ratio 3:5)

**Fuente:** Wikimedia Commons - Tarot de Marsella de dominio público

## 8.2 Contenido de Enciclopedia

**Ubicación:** `app/src/main/assets/tarot_data.json`

**Estructura JSON:**

```json
{
  "cards": [
    {
      "id": 0,
      "name": "El Loco",
      "arcana_type": "MAJOR",
      "suit": null,
      "image_path": "card_major_00.jpg",
      "general_meaning": "Representa el inicio...",
      "upright_meaning": "En posición derecha...",
      "reversed_meaning": "En posición invertida...",
      "symbolism": "El Loco lleva un zurrón...",
      "keywords": ["inicio", "locura", "libertad", "espontaneidad", "riesgo"]
    }
  ]
}
```

**Estrategia de contenido:**
1. Generar contenido inicial con IA (Claude)
2. Validar manualmente para asegurar precisión
3. Almacenar en archivo JSON
4. Importar a Room DB en primera ejecución
