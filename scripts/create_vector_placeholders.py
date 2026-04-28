#!/usr/bin/env python3
"""
Script para crear VectorDrawables simples para las cartas del tarot.
Compose requiere VectorDrawables o imágenes rasterizadas.

Uso:
    python3 create_vector_placeholders.py
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


def create_vector_placeholder(card_name, card_title, output_path):
    """Crea un VectorDrawable simple como placeholder."""

    # Determinar color según el tipo de carta
    if card_name.startswith("card_major"):
        color = "#9C27B0"  # Púrpura para Arcanos Mayores
    elif "wands" in card_name:
        color = "#FF5722"  # Naranja para Bastos
    elif "cups" in card_name:
        color = "#2196F3"  # Azul para Copas
    elif "swords" in card_name:
        color = "#9E9E9E"  # Gris para Espadas
    elif "pentacles" in card_name:
        color = "#4CAF50"  # Verde para Oros
    else:
        color = "#9C27B0"

    # Crear VectorDrawable simple (rectángulo con borde)
    xml_content = f'''<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="200dp"
    android:height="300dp"
    android:viewportWidth="200"
    android:viewportHeight="300">

    <!-- Fondo de la carta -->
    <path
        android:fillColor="{color}"
        android:pathData="M10,0 L190,0 Q200,0 200,10 L200,290 Q200,300 190,300 L10,300 Q0,300 0,290 L0,10 Q0,0 10,0 Z"/>

    <!-- Borde blanco interno -->
    <path
        android:fillColor="#FFFFFF"
        android:pathData="M15,5 L185,5 Q195,5 195,15 L195,285 Q195,295 185,295 L15,295 Q5,295 5,285 L5,15 Q5,5 15,5 Z"/>

    <!-- Área interna con color -->
    <path
        android:fillColor="{color}"
        android:pathData="M20,10 L180,10 Q190,10 190,20 L190,280 Q190,290 180,290 L20,290 Q10,290 10,280 L10,20 Q10,10 20,10 Z"/>
</vector>
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
    print("🎨 GENERADOR DE VECTORDRAWABLES PARA CARTAS DEL TAROT")
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

    # Eliminar archivos antiguos primero
    print("🗑️  Eliminando placeholders antiguos...")
    for old_file in output_path.glob("card_*.xml"):
        try:
            old_file.unlink()
            print(f"   Eliminado: {old_file.name}")
        except Exception as e:
            print(f"   ⚠️  No se pudo eliminar {old_file.name}: {e}")
    print()

    print("⚡ Generando nuevos VectorDrawables...")
    print()

    # Crear todos los placeholders
    successful = 0
    failed = 0

    for i, (card_name, card_title) in enumerate(CARDS.items(), 1):
        print(f"[{i}/{len(CARDS)}] 🎨 Creando: {card_name}.xml ({card_title})...", end=" ")

        if create_vector_placeholder(card_name, card_title, output_path):
            print("✅")
            successful += 1
        else:
            print("❌")
            failed += 1

    # Resumen final
    print()
    print("=" * 70)
    print("📊 RESUMEN")
    print("=" * 70)
    print(f"✅ Exitosas: {successful}")
    print(f"❌ Fallidas:  {failed}")
    print(f"📁 Ubicación: {output_path}")
    print()

    if failed == 0:
        print("🎉 ¡Todos los VectorDrawables se crearon correctamente!")
        print()
        print("💡 Estos son VectorDrawables válidos con colores distintivos:")
        print("   🟣 Púrpura - Arcanos Mayores")
        print("   🟠 Naranja - Bastos (Wands)")
        print("   🔵 Azul - Copas (Cups)")
        print("   ⚫ Gris - Espadas (Swords)")
        print("   🟢 Verde - Oros/Pentáculos (Pentacles)")
        print()
        print("   Ahora la app debería funcionar correctamente.")
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
