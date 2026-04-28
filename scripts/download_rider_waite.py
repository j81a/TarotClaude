#!/usr/bin/env python3
"""
Script para descargar las 78 cartas del Rider-Waite-Smith Tarot desde Wikimedia Commons.
Estas son las ilustraciones clásicas y más reconocibles del tarot.

Uso:
    python3 download_rider_waite.py
"""

import os
import sys
import urllib.request
import urllib.parse
import json
import time
from pathlib import Path

# Configuración
OUTPUT_DIR = "../app/src/main/res/drawable"
API_URL = "https://commons.wikimedia.org/w/api.php"

# Mapeo completo de las 78 cartas Rider-Waite-Smith
# Nombres exactos de archivos en Wikimedia Commons
TAROT_CARDS = {
    # ARCANOS MAYORES (22 cartas)
    "card_major_00": "RWS Tarot 00 Fool.jpg",
    "card_major_01": "RWS Tarot 01 Magician.jpg",
    "card_major_02": "RWS Tarot 02 High Priestess.jpg",
    "card_major_03": "RWS Tarot 03 Empress.jpg",
    "card_major_04": "RWS Tarot 04 Emperor.jpg",
    "card_major_05": "RWS Tarot 05 Hierophant.jpg",
    "card_major_06": "RWS Tarot 06 Lovers.jpg",
    "card_major_07": "RWS Tarot 07 Chariot.jpg",
    "card_major_08": "RWS Tarot 08 Strength.jpg",
    "card_major_09": "RWS Tarot 09 Hermit.jpg",
    "card_major_10": "RWS Tarot 10 Wheel of Fortune.jpg",
    "card_major_11": "RWS Tarot 11 Justice.jpg",
    "card_major_12": "RWS Tarot 12 Hanged Man.jpg",
    "card_major_13": "RWS Tarot 13 Death.jpg",
    "card_major_14": "RWS Tarot 14 Temperance.jpg",
    "card_major_15": "RWS Tarot 15 Devil.jpg",
    "card_major_16": "RWS Tarot 16 Tower.jpg",
    "card_major_17": "RWS Tarot 17 Star.jpg",
    "card_major_18": "RWS Tarot 18 Moon.jpg",
    "card_major_19": "RWS Tarot 19 Sun.jpg",
    "card_major_20": "RWS Tarot 20 Judgement.jpg",
    "card_major_21": "RWS Tarot 21 World.jpg",

    # BASTOS/WANDS (14 cartas)
    "card_wands_01": "Wands01.jpg",
    "card_wands_02": "Wands02.jpg",
    "card_wands_03": "Wands03.jpg",
    "card_wands_04": "Wands04.jpg",
    "card_wands_05": "Wands05.jpg",
    "card_wands_06": "Wands06.jpg",
    "card_wands_07": "Wands07.jpg",
    "card_wands_08": "Wands08.jpg",
    "card_wands_09": "Wands09.jpg",
    "card_wands_10": "Wands10.jpg",
    "card_wands_page": "Wands11.jpg",
    "card_wands_knight": "Wands12.jpg",
    "card_wands_queen": "Wands13.jpg",
    "card_wands_king": "Wands14.jpg",

    # COPAS/CUPS (14 cartas)
    "card_cups_01": "Cups01.jpg",
    "card_cups_02": "Cups02.jpg",
    "card_cups_03": "Cups03.jpg",
    "card_cups_04": "Cups04.jpg",
    "card_cups_05": "Cups05.jpg",
    "card_cups_06": "Cups06.jpg",
    "card_cups_07": "Cups07.jpg",
    "card_cups_08": "Cups08.jpg",
    "card_cups_09": "Cups09.jpg",
    "card_cups_10": "Cups10.jpg",
    "card_cups_page": "Cups11.jpg",
    "card_cups_knight": "Cups12.jpg",
    "card_cups_queen": "Cups13.jpg",
    "card_cups_king": "Cups14.jpg",

    # ESPADAS/SWORDS (14 cartas)
    "card_swords_01": "Swords01.jpg",
    "card_swords_02": "Swords02.jpg",
    "card_swords_03": "Swords03.jpg",
    "card_swords_04": "Swords04.jpg",
    "card_swords_05": "Swords05.jpg",
    "card_swords_06": "Swords06.jpg",
    "card_swords_07": "Swords07.jpg",
    "card_swords_08": "Swords08.jpg",
    "card_swords_09": "Swords09.jpg",
    "card_swords_10": "Swords10.jpg",
    "card_swords_page": "Swords11.jpg",
    "card_swords_knight": "Swords12.jpg",
    "card_swords_queen": "Swords13.jpg",
    "card_swords_king": "Swords14.jpg",

    # OROS/PENTACLES (14 cartas)
    "card_pentacles_01": "Pents01.jpg",
    "card_pentacles_02": "Pents02.jpg",
    "card_pentacles_03": "Pents03.jpg",
    "card_pentacles_04": "Pents04.jpg",
    "card_pentacles_05": "Pents05.jpg",
    "card_pentacles_06": "Pents06.jpg",
    "card_pentacles_07": "Pents07.jpg",
    "card_pentacles_08": "Pents08.jpg",
    "card_pentacles_09": "Pents09.jpg",
    "card_pentacles_10": "Pents10.jpg",
    "card_pentacles_page": "Pents11.jpg",
    "card_pentacles_knight": "Pents12.jpg",
    "card_pentacles_queen": "Pents13.jpg",
    "card_pentacles_king": "Pents14.jpg",
}


def get_image_url_from_api(filename):
    """Obtiene la URL de descarga directa usando la API de Wikimedia Commons."""
    params = {
        'action': 'query',
        'titles': f'File:{filename}',
        'prop': 'imageinfo',
        'iiprop': 'url',
        'format': 'json'
    }

    query_url = f"{API_URL}?{urllib.parse.urlencode(params)}"

    try:
        req = urllib.request.Request(
            query_url,
            headers={'User-Agent': 'TarotAI-App/1.0 (Educational Project)'}
        )

        with urllib.request.urlopen(req, timeout=30) as response:
            data = json.loads(response.read().decode('utf-8'))

        pages = data.get('query', {}).get('pages', {})
        for page_id, page_data in pages.items():
            if page_id == '-1':
                return None

            imageinfo = page_data.get('imageinfo', [])
            if imageinfo and len(imageinfo) > 0:
                return imageinfo[0].get('url')

        return None

    except Exception as e:
        print(f"   ⚠️  Error consultando API: {e}")
        return None


def download_image(card_name, wikimedia_filename, output_path):
    """Descarga una imagen usando la API de Wikimedia Commons."""

    print(f"🔍 {card_name}: {wikimedia_filename}")
    download_url = get_image_url_from_api(wikimedia_filename)

    if not download_url:
        print(f"   ❌ No se encontró")
        return False

    # Determinar extensión
    ext = '.jpg' if wikimedia_filename.endswith('.jpg') else '.png'
    output_file = output_path / f"{card_name}{ext}"

    try:
        req = urllib.request.Request(
            download_url,
            headers={'User-Agent': 'TarotAI-App/1.0 (Educational Project)'}
        )

        with urllib.request.urlopen(req, timeout=30) as response:
            data = response.read()

        with open(output_file, 'wb') as f:
            f.write(data)

        print(f"   ✅ Descargada")
        return True

    except Exception as e:
        print(f"   ❌ Error: {e}")
        return False


def main():
    """Función principal."""
    print("=" * 70)
    print("🃏 DESCARGADOR RIDER-WAITE-SMITH TAROT")
    print("=" * 70)
    print()
    print(f"📁 Destino: {OUTPUT_DIR}")
    print(f"🎴 Total: {len(TAROT_CARDS)} cartas")
    print()

    script_dir = Path(__file__).parent
    output_path = (script_dir / OUTPUT_DIR).resolve()

    if not output_path.exists():
        output_path.mkdir(parents=True, exist_ok=True)

    print("⚡ Descargando...")
    print()

    successful = 0
    failed = 0
    failed_cards = []

    for i, (card_name, wikimedia_filename) in enumerate(TAROT_CARDS.items(), 1):
        print(f"[{i}/{len(TAROT_CARDS)}] ", end="")

        if download_image(card_name, wikimedia_filename, output_path):
            successful += 1
        else:
            failed += 1
            failed_cards.append((card_name, wikimedia_filename))

        # Pausa de 2 segundos
        if i < len(TAROT_CARDS):
            time.sleep(2)

    print()
    print("=" * 70)
    print("📊 RESUMEN")
    print("=" * 70)
    print(f"✅ Exitosas: {successful}")
    print(f"❌ Fallidas:  {failed}")
    print()

    if failed > 0:
        print("Fallidas:")
        for card_name, filename in failed_cards:
            print(f"  - {card_name}: {filename}")
        print()

    if failed == 0:
        print("🎉 ¡Listo! Ahora ejecuta:")
        print("   ./gradlew clean build")
    else:
        print(f"⚠️  {failed} fallidas, {successful} exitosas")

    print("=" * 70)


if __name__ == "__main__":
    try:
        main()
    except KeyboardInterrupt:
        print("\n\n⚠️  Cancelado")
        sys.exit(1)
    except Exception as e:
        print(f"\n\n❌ Error: {e}")
        sys.exit(1)
