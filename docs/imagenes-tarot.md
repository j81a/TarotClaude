# Imágenes del Tarot de Marsella - Guía de Descarga

## 📋 Resumen

La aplicación TarotAI requiere **78 imágenes** del Tarot de Marsella (22 Arcanos Mayores + 56 Arcanos Menores).

Hemos identificado fuentes de **dominio público** en Wikimedia Commons para garantizar el uso legal y gratuito.

---

## 🎨 Fuentes de Imágenes de Dominio Público

### **Opción Recomendada: Wikimedia Commons**

Wikimedia Commons tiene colecciones completas del Tarot de Marsella de dominio público:

#### **1. Tarot de Jean Dodal (ca. 1701)**
- **URL:** https://commons.wikimedia.org/wiki/Category:Tarot_de_Marseille_-_Jean_Dodal
- **Archivos disponibles:** 29 cartas
- **Estado:** Dominio público (más de 300 años)
- **Calidad:** Alta resolución, escaneos de cartas históricas

#### **2. Tarot de Nicolas Conver (1760)**
- **URL:** https://commons.wikimedia.org/wiki/Category:Tarot_de_Marseille_-_Nicolas_Conver_1760
- **Archivos disponibles:** 26 cartas
- **Estado:** Dominio público
- **Calidad:** Excelente, deck completo disponible

#### **3. Colección General - Single Cards**
- **URL:** https://commons.wikimedia.org/wiki/Category:Tarot_de_Marseille_(Single_Cards)
- **Archivos disponibles:** 164 archivos
- **Estado:** Dominio público
- **Calidad:** Variada, múltiples versiones disponibles

---

## 📥 Cómo Descargar las Imágenes

### **Paso 1: Acceder a Wikimedia Commons**

Visita la categoría principal:
```
https://commons.wikimedia.org/wiki/Category:Tarot_de_Marseille
```

### **Paso 2: Seleccionar el Deck**

Recomendamos el **Tarot de Nicolas Conver (1760)** por estar completo y tener excelente calidad:
```
https://commons.wikimedia.org/wiki/Category:Tarot_de_Marseille_-_Nicolas_Conver_1760
```

### **Paso 3: Descargar Cada Carta**

1. Haz clic en la imagen de la carta
2. En la página de detalles, haz clic en "More details" o el botón de descarga
3. Selecciona la resolución deseada (recomendado: 1200px o mayor)
4. Guarda el archivo

### **Paso 4: Renombrar los Archivos**

Renombra cada archivo según esta convención:

#### **Arcanos Mayores (0-21):**
```
card_major_00.png  → El Loco (Le Mat)
card_major_01.png  → El Mago (Le Bateleur)
card_major_02.png  → La Sacerdotisa (La Papesse)
card_major_03.png  → La Emperatriz (L'Imperatrice)
card_major_04.png  → El Emperador (L'Empereur)
card_major_05.png  → El Hierofante (Le Pape)
card_major_06.png  → Los Enamorados (L'Amoureux)
card_major_07.png  → El Carro (Le Chariot)
card_major_08.png  → La Fuerza (La Force)
card_major_09.png  → El Ermitaño (L'Hermite)
card_major_10.png  → La Rueda de la Fortuna (La Roue de Fortune)
card_major_11.png  → La Justicia (La Justice)
card_major_12.png  → El Colgado (Le Pendu)
card_major_13.png  → La Muerte (L'Arcane sans nom)
card_major_14.png  → La Templanza (Temperance)
card_major_15.png  → El Diablo (Le Diable)
card_major_16.png  → La Torre (La Maison Dieu)
card_major_17.png  → La Estrella (L'Etoile)
card_major_18.png  → La Luna (La Lune)
card_major_19.png  → El Sol (Le Soleil)
card_major_20.png  → El Juicio (Le Jugement)
card_major_21.png  → El Mundo (Le Monde)
```

#### **Bastos (Bâtons):**
```
card_wands_01.png     → As de Bastos
card_wands_02.png     → Dos de Bastos
...
card_wands_10.png     → Diez de Bastos
card_wands_page.png   → Sota de Bastos (Valet)
card_wands_knight.png → Caballero de Bastos (Cavalier)
card_wands_queen.png  → Reina de Bastos (Reine)
card_wands_king.png   → Rey de Bastos (Roi)
```

#### **Copas (Coupes):**
```
card_cups_01.png     → As de Copas
card_cups_02.png     → Dos de Copas
...
card_cups_10.png     → Diez de Copas
card_cups_page.png   → Sota de Copas (Valet)
card_cups_knight.png → Caballero de Copas (Cavalier)
card_cups_queen.png  → Reina de Copas (Reine)
card_cups_king.png   → Rey de Copas (Roi)
```

#### **Espadas (Épées):**
```
card_swords_01.png     → As de Espadas
card_swords_02.png     → Dos de Espadas
...
card_swords_10.png     → Diez de Espadas
card_swords_page.png   → Sota de Espadas (Valet)
card_swords_knight.png → Caballero de Espadas (Cavalier)
card_swords_queen.png  → Reina de Espadas (Reine)
card_swords_king.png   → Rey de Espadas (Roi)
```

#### **Oros/Pentáculos (Deniers):**
```
card_pentacles_01.png     → As de Oros
card_pentacles_02.png     → Dos de Oros
...
card_pentacles_10.png     → Diez de Oros
card_pentacles_page.png   → Sota de Oros (Valet)
card_pentacles_knight.png → Caballero de Oros (Cavalier)
card_pentacles_queen.png  → Reina de Oros (Reine)
card_pentacles_king.png   → Rey de Oros (Roi)
```

### **Paso 5: Colocar en el Proyecto**

Copia todos los archivos renombrados a:
```
/app/src/main/res/drawable/
```

---

## 🔄 Alternativa: Script de Descarga Automática

Si prefieres automatizar el proceso, puedes usar un script Python para descargar todas las imágenes:

```python
# TODO: Implementar script de descarga automática desde Wikimedia Commons
# Este script se puede crear en una fase futura si es necesario
```

---

## 📝 Licencia y Atribución

### **Tarot de Marsella - Dominio Público**

Las imágenes del Tarot de Marsella de Jean Dodal (1701) y Nicolas Conver (1760) están en **dominio público** porque:

1. Fueron creadas hace más de 300 y 260 años respectivamente
2. Los derechos de autor han expirado
3. No requieren atribución

### **Recomendación de Atribución (Opcional)**

Aunque no es obligatorio, es una buena práctica incluir en la app:

```
Imágenes del Tarot de Marsella basadas en el deck de Nicolas Conver (1760)
Fuente: Wikimedia Commons (Dominio Público)
```

---

## 🖼️ Imágenes Placeholder Temporales

Mientras descargas las imágenes reales, la app incluye **placeholders** (imágenes temporales) para todas las cartas.

Estas imágenes placeholder son simples diseños con el nombre de cada carta para que puedas:
- Probar la funcionalidad de la app
- Ver el diseño y navegación
- Desarrollar sin esperar las imágenes finales

**Ubicación:** `/app/src/main/res/drawable/`

---

## ✅ Checklist de Implementación

- [x] Identificar fuentes de dominio público
- [x] Documentar proceso de descarga
- [x] Crear convención de nombres
- [ ] Descargar las 78 imágenes
- [ ] Renombrar según convención
- [ ] Colocar en `/drawable/`
- [ ] Verificar que todas las imágenes cargan correctamente

---

## 🆘 Soporte

Si tienes problemas descargando las imágenes:

1. **Opción 1:** Usa las imágenes placeholder temporales incluidas
2. **Opción 2:** Busca "Tarot de Marseille public domain" en Google Images con filtro de licencia
3. **Opción 3:** Considera comprar un deck digital con licencia comercial si prefieres imágenes modernas

---

**Fecha de creación:** 2026-04-27
**Última actualización:** 2026-04-27
