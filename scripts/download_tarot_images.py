#!/usr/bin/env python3
"""
Script para descargar las 78 imágenes del Tarot de Marsella desde Wikimedia Commons.
Descarga en resolución 800px y las renombra automáticamente.

Uso:
    python3 download_tarot_images.py
"""

import os
import sys
import urllib.request
import time
from pathlib import Path

# Configuración
RESOLUTION = "800px"
OUTPUT_DIR = "../app/src/main/res/drawable"
BASE_URL = "https://upload.wikimedia.org/wikipedia/commons"

# Mapeo de cartas: (nombre_archivo_destino, URL_en_Wikimedia)
# URLs obtenidas de: https://commons.wikimedia.org/wiki/Category:Tarot_de_Marseille_-_Nicolas_Conver_1760
TAROT_CARDS = {
    # ARCANOS MAYORES (22 cartas) - Imágenes originales de alta calidad
    "card_major_00": "9/90/Jean_Dodal_Tarot_trump_00.jpg",
    "card_major_01": "d/de/Jean_Dodal_Tarot_trump_01.jpg",
    "card_major_02": "8/88/Jean_Dodal_Tarot_trump_02.jpg",
    "card_major_03": "d/d2/Jean_Dodal_Tarot_trump_03.jpg",
    "card_major_04": "c/c3/Jean_Dodal_Tarot_trump_04.jpg",
    "card_major_05": "e/e0/Jean_Dodal_Tarot_trump_05.jpg",
    "card_major_06": "3/3a/Jean_Dodal_Tarot_trump_06.jpg",
    "card_major_07": "b/b5/Jean_Dodal_Tarot_trump_07.jpg",
    "card_major_08": "f/f5/Jean_Dodal_Tarot_trump_08.jpg",
    "card_major_09": "4/4d/Jean_Dodal_Tarot_trump_09.jpg",
    "card_major_10": "3/3c/Jean_Dodal_Tarot_trump_10.jpg",
    "card_major_11": "e/e0/Jean_Dodal_Tarot_trump_11.jpg",
    "card_major_12": "b/b7/Jean_Dodal_Tarot_trump_12.jpg",
    "card_major_13": "d/d7/Jean_Dodal_Tarot_trump_13.jpg",
    "card_major_14": "3/3e/Jean_Dodal_Tarot_trump_14.jpg",
    "card_major_15": "5/53/Jean_Dodal_Tarot_trump_15.jpg",
    "card_major_16": "5/53/Jean_Dodal_Tarot_trump_16.jpg",
    "card_major_17": "d/db/Jean_Dodal_Tarot_trump_17.jpg",
    "card_major_18": "a/a5/Jean_Dodal_Tarot_trump_18.jpg",
    "card_major_19": "1/17/Jean_Dodal_Tarot_trump_19.jpg",
    "card_major_20": "d/d8/Jean_Dodal_Tarot_trump_20.jpg",
    "card_major_21": "f/ff/Jean_Dodal_Tarot_trump_21.jpg",

    # BASTOS (14 cartas) - SVG originales
    "card_wands_01": "1/1b/Cburnett_tarot_deck_wands_ace.svg",
    "card_wands_02": "c/c9/Cburnett_tarot_deck_wands_2.svg",
    "card_wands_03": "1/14/Cburnett_tarot_deck_wands_3.svg",
    "card_wands_04": "a/a7/Cburnett_tarot_deck_wands_4.svg",
    "card_wands_05": "9/9d/Cburnett_tarot_deck_wands_5.svg",
    "card_wands_06": "3/3a/Cburnett_tarot_deck_wands_6.svg",
    "card_wands_07": "e/e4/Cburnett_tarot_deck_wands_7.svg",
    "card_wands_08": "6/6b/Cburnett_tarot_deck_wands_8.svg",
    "card_wands_09": "4/4d/Cburnett_tarot_deck_wands_9.svg",
    "card_wands_10": "0/0b/Cburnett_tarot_deck_wands_10.svg",
    "card_wands_page": "6/6a/Cburnett_tarot_deck_wands_page.svg",
    "card_wands_knight": "1/16/Cburnett_tarot_deck_wands_knight.svg",
    "card_wands_queen": "0/0d/Cburnett_tarot_deck_wands_queen.svg",
    "card_wands_king": "c/ce/Cburnett_tarot_deck_wands_king.svg",

    # COPAS (14 cartas) - SVG originales
    "card_cups_01": "3/36/Cburnett_tarot_deck_cups_ace.svg",
    "card_cups_02": "f/f8/Cburnett_tarot_deck_cups_2.svg",
    "card_cups_03": "7/7a/Cburnett_tarot_deck_cups_3.svg",
    "card_cups_04": "3/35/Cburnett_tarot_deck_cups_4.svg",
    "card_cups_05": "d/d7/Cburnett_tarot_deck_cups_5.svg",
    "card_cups_06": "a/a9/Cburnett_tarot_deck_cups_6.svg",
    "card_cups_07": "3/32/Cburnett_tarot_deck_cups_7.svg",
    "card_cups_08": "f/f4/Cburnett_tarot_deck_cups_8.svg",
    "card_cups_09": "2/24/Cburnett_tarot_deck_cups_9.svg",
    "card_cups_10": "8/84/Cburnett_tarot_deck_cups_10.svg",
    "card_cups_page": "a/ad/Cburnett_tarot_deck_cups_page.svg",
    "card_cups_knight": "f/fa/Cburnett_tarot_deck_cups_knight.svg",
    "card_cups_queen": "6/62/Cburnett_tarot_deck_cups_queen.svg",
    "card_cups_king": "0/04/Cburnett_tarot_deck_cups_king.svg",

    # ESPADAS (14 cartas) - SVG originales
    "card_swords_01": "1/1a/Cburnett_tarot_deck_swords_ace.svg",
    "card_swords_02": "9/9e/Cburnett_tarot_deck_swords_2.svg",
    "card_swords_03": "0/02/Cburnett_tarot_deck_swords_3.svg",
    "card_swords_04": "b/bf/Cburnett_tarot_deck_swords_4.svg",
    "card_swords_05": "2/23/Cburnett_tarot_deck_swords_5.svg",
    "card_swords_06": "9/9a/Cburnett_tarot_deck_swords_6.svg",
    "card_swords_07": "3/34/Cburnett_tarot_deck_swords_7.svg",
    "card_swords_08": "a/af/Cburnett_tarot_deck_swords_8.svg",
    "card_swords_09": "2/2f/Cburnett_tarot_deck_swords_9.svg",
    "card_swords_10": "5/5b/Cburnett_tarot_deck_swords_10.svg",
    "card_swords_page": "4/4c/Cburnett_tarot_deck_swords_page.svg",
    "card_swords_knight": "b/b0/Cburnett_tarot_deck_swords_knight.svg",
    "card_swords_queen": "d/d4/Cburnett_tarot_deck_swords_queen.svg",
    "card_swords_king": "3/33/Cburnett_tarot_deck_swords_king.svg",

    # OROS/PENTÁCULOS (14 cartas) - SVG originales
    "card_pentacles_01": "f/fd/Cburnett_tarot_deck_pentacles_ace.svg",
    "card_pentacles_02": "9/9f/Cburnett_tarot_deck_pentacles_2.svg",
    "card_pentacles_03": "4/42/Cburnett_tarot_deck_pentacles_3.svg",
    "card_pentacles_04": "3/35/Cburnett_tarot_deck_pentacles_4.svg",
    "card_pentacles_05": "9/96/Cburnett_tarot_deck_pentacles_5.svg",
    "card_pentacles_06": "a/a6/Cburnett_tarot_deck_pentacles_6.svg",
    "card_pentacles_07": "6/6a/Cburnett_tarot_deck_pentacles_7.svg",
    "card_pentacles_08": "4/49/Cburnett_tarot_deck_pentacles_8.svg",
    "card_pentacles_09": "f/f0/Cburnett_tarot_deck_pentacles_9.svg",
    "card_pentacles_10": "4/42/Cburnett_tarot_deck_pentacles_10.svg",
    "card_pentacles_page": "e/ec/Cburnett_tarot_deck_pentacles_page.svg",
    "card_pentacles_knight": "d/d2/Cburnett_tarot_deck_pentacles_knight.svg",
    "card_pentacles_queen": "8/88/Cburnett_tarot_deck_pentacles_queen.svg",
    "card_pentacles_king": "1/1c/Cburnett_tarot_deck_pentacles_king.svg",
}


def download_image(card_name, url_path, output_path):
    """Descarga una imagen desde Wikimedia Commons."""
    full_url = f"{BASE_URL}/{url_path}"

    # Determinar extensión según el archivo original
    if url_path.endswith('.svg'):
        ext = '.svg'
    elif url_path.endswith('.png'):
        ext = '.png'
    else:
        ext = '.jpg'

    output_file = output_path / f"{card_name}{ext}"

    try:
        print(f"📥 Descargando: {card_name}{ext}...")

        # Agregar User-Agent para evitar bloqueos
        req = urllib.request.Request(
            full_url,
            headers={'User-Agent': 'TarotAI-App/1.0 (Educational Project)'}
        )

        with urllib.request.urlopen(req, timeout=30) as response:
            data = response.read()

        with open(output_file, 'wb') as f:
            f.write(data)

        print(f"   ✅ Descargada: {output_file.name}")
        return True

    except Exception as e:
        print(f"   ❌ Error descargando {card_name}: {e}")
        return False


def main():
    """Función principal."""
    print("=" * 70)
    print("🃏 DESCARGADOR DE IMÁGENES DEL TAROT DE MARSELLA")
    print("=" * 70)
    print()
    print(f"📁 Carpeta de destino: {OUTPUT_DIR}")
    print(f"📐 Resolución: {RESOLUTION}")
    print(f"🎴 Total de cartas: {len(TAROT_CARDS)}")
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

    # Comenzar descarga automáticamente
    print("⚡ Comenzando descarga...")
    print()

    # Descargar todas las imágenes
    successful = 0
    failed = 0

    for i, (card_name, url_path) in enumerate(TAROT_CARDS.items(), 1):
        print(f"[{i}/{len(TAROT_CARDS)}] ", end="")

        if download_image(card_name, url_path, output_path):
            successful += 1
        else:
            failed += 1

        # Pausa breve para no sobrecargar el servidor
        time.sleep(0.5)
        print()

    # Resumen final
    print("=" * 70)
    print("📊 RESUMEN DE DESCARGA")
    print("=" * 70)
    print(f"✅ Exitosas: {successful}")
    print(f"❌ Fallidas:  {failed}")
    print(f"📁 Ubicación: {output_path}")
    print()

    if failed == 0:
        print("🎉 ¡Todas las imágenes se descargaron correctamente!")
        print("   Ahora puedes ejecutar la app y verás las imágenes reales.")
    else:
        print("⚠️  Algunas imágenes no se pudieron descargar.")
        print("   Revisa los errores arriba y vuelve a ejecutar el script.")

    print()
    print("=" * 70)


if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("\n\n⚠️  Descarga cancelada por el usuario.")
        sys.exit(1)
    except Exception as e:
        print(f"\n\n❌ Error inesperado: {e}")
        sys.exit(1)
