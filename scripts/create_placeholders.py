#!/usr/bin/env python3
"""
Script para crear placeholders visuales para las cartas del tarot.
Crea imágenes PNG simples pero atractivas como placeholder.

Uso:
    python3 create_placeholders.py
"""

import os
from pathlib import Path

# Configuración
OUTPUT_DIR = "../app/src/main/res/drawable"

# Definición de todas las cartas
CARDS = {
    # Arcanos Mayores
    "card_major_00": "El Loco",
    "card_major_01": "El Mago",
    "card_major_02": "La Sacerdotisa",
    "card_major_03": "La Emperatriz",
    "card_major_04": "El Emperador",
    "card_major_05": "El Hierofante",
    "card_major_06": "Los Enamorados",
    "card_major_07": "El Carro",
    "card_major_08": "La Fuerza",
    "card_major_09": "El Ermitaño",
    "card_major_10": "La Rueda",
    "card_major_11": "La Justicia",
    "card_major_12": "El Colgado",
    "card_major_13": "La Muerte",
    "card_major_14": "La Templanza",
    "card_major_15": "El Diablo",
    "card_major_16": "La Torre",
    "card_major_17": "La Estrella",
    "card_major_18": "La Luna",
    "card_major_19": "El Sol",
    "card_major_20": "El Juicio",
    "card_major_21": "El Mundo",

    # Bastos
    "card_wands_01": "As de Bastos",
    "card_wands_02": "Dos de Bastos",
    "card_wands_03": "Tres de Bastos",
    "card_wands_04": "Cuatro de Bastos",
    "card_wands_05": "Cinco de Bastos",
    "card_wands_06": "Seis de Bastos",
    "card_wands_07": "Siete de Bastos",
    "card_wands_08": "Ocho de Bastos",
    "card_wands_09": "Nueve de Bastos",
    "card_wands_10": "Diez de Bastos",
    "card_wands_page": "Sota de Bastos",
    "card_wands_knight": "Caballero de Bastos",
    "card_wands_queen": "Reina de Bastos",
    "card_wands_king": "Rey de Bastos",

    # Copas
    "card_cups_01": "As de Copas",
    "card_cups_02": "Dos de Copas",
    "card_cups_03": "Tres de Copas",
    "card_cups_04": "Cuatro de Copas",
    "card_cups_05": "Cinco de Copas",
    "card_cups_06": "Seis de Copas",
    "card_cups_07": "Siete de Copas",
    "card_cups_08": "Ocho de Copas",
    "card_cups_09": "Nueve de Copas",
    "card_cups_10": "Diez de Copas",
    "card_cups_page": "Sota de Copas",
    "card_cups_knight": "Caballero de Copas",
    "card_cups_queen": "Reina de Copas",
    "card_cups_king": "Rey de Copas",

    # Espadas
    "card_swords_01": "As de Espadas",
    "card_swords_02": "Dos de Espadas",
    "card_swords_03": "Tres de Espadas",
    "card_swords_04": "Cuatro de Espadas",
    "card_swords_05": "Cinco de Espadas",
    "card_swords_06": "Seis de Espadas",
    "card_swords_07": "Siete de Espadas",
    "card_swords_08": "Ocho de Espadas",
    "card_swords_09": "Nueve de Espadas",
    "card_swords_10": "Diez de Espadas",
    "card_swords_page": "Sota de Espadas",
    "card_swords_knight": "Caballero de Espadas",
    "card_swords_queen": "Reina de Espadas",
    "card_swords_king": "Rey de Espadas",

    # Oros/Pentáculos
    "card_pentacles_01": "As de Oros",
    "card_pentacles_02": "Dos de Oros",
    "card_pentacles_03": "Tres de Oros",
    "card_pentacles_04": "Cuatro de Oros",
    "card_pentacles_05": "Cinco de Oros",
    "card_pentacles_06": "Seis de Oros",
    "card_pentacles_07": "Siete de Oros",
    "card_pentacles_08": "Ocho de Oros",
    "card_pentacles_09": "Nueve de Oros",
    "card_pentacles_10": "Diez de Oros",
    "card_pentacles_page": "Sota de Oros",
    "card_pentacles_knight": "Caballero de Oros",
    "card_pentacles_queen": "Reina de Oros",
    "card_pentacles_king": "Rey de Oros",
}


def create_xml_placeholder(card_name, card_title, output_path):
    """Crea un archivo XML drawable como placeholder."""

    # Determinar color según el tipo de carta
    if card_name.startswith("card_major"):
        color = "#9C27B0"  # Púrpura para Arcanos Mayores
        symbol = "★"
    elif "wands" in card_name:
        color = "#FF5722"  # Naranja para Bastos
        symbol = "🔥"
    elif "cups" in card_name:
        color = "#2196F3"  # Azul para Copas
        symbol = "💧"
    elif "swords" in card_name:
        color = "#9E9E9E"  # Gris para Espadas
        symbol = "⚔"
    elif "pentacles" in card_name:
        color = "#4CAF50"  # Verde para Oros
        symbol = "💰"
    else:
        color = "#9C27B0"
        symbol = "★"

    # Crear XML drawable
    xml_content = f'''<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <!-- Fondo de la carta -->
    <item>
        <shape android:shape="rectangle">
            <solid android:color="{color}"/>
            <corners android:radius="8dp"/>
        </shape>
    </item>

    <!-- Borde -->
    <item android:top="4dp" android:bottom="4dp" android:left="4dp" android:right="4dp">
        <shape android:shape="rectangle">
            <solid android:color="#FFFFFF"/>
            <corners android:radius="6dp"/>
        </shape>
    </item>

    <!-- Color interno -->
    <item android:top="8dp" android:bottom="8dp" android:left="8dp" android:right="8dp">
        <shape android:shape="rectangle">
            <solid android:color="{color}"/>
            <corners android:radius="4dp"/>
        </shape>
    </item>
</layer-list>
'''

    output_file = output_path / f"{card_name}.xml"

    try:
        with open(output_file, 'w', encoding='utf-8') as f:
            f.write(xml_content)
        return True
    except Exception as e:
        print(f"   ❌ Error creando {card_name}: {e}")
        return False


def main():
    """Función principal."""
    print("=" * 70)
    print("🎨 GENERADOR DE PLACEHOLDERS PARA CARTAS DEL TAROT")
    print("=" * 70)
    print()
    print(f"📁 Carpeta de destino: {OUTPUT_DIR}")
    print(f"🎴 Total de cartas: {len(CARDS)}")
    print()

    # Crear directorio si no existe
    script_dir = Path(__file__).parent
    output_path = (script_dir / OUTPUT_DIR).resolve()

    print(f"📂 Ruta completa: {output_path}")
    print()

    if not output_path.exists():
        print(f"⚠️  La carpeta {output_path} no existe.")
        print("   Creando carpeta...")
        output_path.mkdir(parents=True, exist_ok=True)
        print("   ✅ Carpeta creada.")
        print()

    print("⚡ Generando placeholders...")
    print()

    # Crear todos los placeholders
    successful = 0
    failed = 0

    for i, (card_name, card_title) in enumerate(CARDS.items(), 1):
        print(f"[{i}/{len(CARDS)}] 🎨 Creando: {card_name}.xml ({card_title})...")

        if create_xml_placeholder(card_name, card_title, output_path):
            print(f"   ✅ Creado")
            successful += 1
        else:
            failed += 1
        print()

    # Resumen final
    print("=" * 70)
    print("📊 RESUMEN")
    print("=" * 70)
    print(f"✅ Exitosas: {successful}")
    print(f"❌ Fallidas:  {failed}")
    print(f"📁 Ubicación: {output_path}")
    print()

    if failed == 0:
        print("🎉 ¡Todos los placeholders se crearon correctamente!")
        print()
        print("💡 Estos son placeholders con colores distintivos:")
        print("   🟣 Púrpura - Arcanos Mayores")
        print("   🟠 Naranja - Bastos (Wands)")
        print("   🔵 Azul - Copas (Cups)")
        print("   ⚫ Gris - Espadas (Swords)")
        print("   🟢 Verde - Oros/Pentáculos (Pentacles)")
        print()
        print("   Ahora puedes ejecutar la app y verás los placeholders.")
        print("   Más tarde puedes reemplazarlos con las imágenes reales.")
    else:
        print("⚠️  Algunos placeholders no se pudieron crear.")

    print()
    print("=" * 70)


if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("\n\n⚠️  Generación cancelada por el usuario.")
        exit(1)
    except Exception as e:
        print(f"\n\n❌ Error inesperado: {e}")
        exit(1)
