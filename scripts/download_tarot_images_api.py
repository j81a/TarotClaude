#!/usr/bin/env python3
"""
Script mejorado para descargar imágenes del tarot usando la API de Wikimedia Commons.
Usa la API oficial para obtener las URLs correctas de descarga.

Uso:
    python3 download_tarot_images_api.py
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

# Definición de todas las cartas del tarot con sus nombres en Wikimedia Commons
TAROT_CARDS = {
    # ARCANOS MAYORES (22 cartas) - Jean Dodal Tarot
    "card_major_00": "Jean Dodal Tarot trump 00.jpg",
    "card_major_01": "Jean Dodal Tarot trump 01.jpg",
    "card_major_02": "Jean Dodal Tarot trump 02.jpg",
    "card_major_03": "Jean Dodal Tarot trump 03.jpg",
    "card_major_04": "Jean Dodal Tarot trump 04.jpg",
    "card_major_05": "Jean Dodal Tarot trump 05.jpg",
    "card_major_06": "Jean Dodal Tarot trump 06.jpg",
    "card_major_07": "Jean Dodal Tarot trump 07.jpg",
    "card_major_08": "Jean Dodal Tarot trump 08.jpg",
    "card_major_09": "Jean Dodal Tarot trump 09.jpg",
    "card_major_10": "Jean Dodal Tarot trump 10.jpg",
    "card_major_11": "Jean Dodal Tarot trump 11.jpg",
    "card_major_12": "Jean Dodal Tarot trump 12.jpg",
    "card_major_13": "Jean Dodal Tarot trump 13.jpg",
    "card_major_14": "Jean Dodal Tarot trump 14.jpg",
    "card_major_15": "Jean Dodal Tarot trump 15.jpg",
    "card_major_16": "Jean Dodal Tarot trump 16.jpg",
    "card_major_17": "Jean Dodal Tarot trump 17.jpg",
    "card_major_18": "Jean Dodal Tarot trump 18.jpg",
    "card_major_19": "Jean Dodal Tarot trump 19.jpg",
    "card_major_20": "Jean Dodal Tarot trump 20.jpg",
    "card_major_21": "Jean Dodal Tarot trump 21.jpg",

    # BASTOS (14 cartas) - Cburnett tarot deck
    "card_wands_01": "Cburnett tarot deck wands ace.svg",
    "card_wands_02": "Cburnett tarot deck wands 2.svg",
    "card_wands_03": "Cburnett tarot deck wands 3.svg",
    "card_wands_04": "Cburnett tarot deck wands 4.svg",
    "card_wands_05": "Cburnett tarot deck wands 5.svg",
    "card_wands_06": "Cburnett tarot deck wands 6.svg",
    "card_wands_07": "Cburnett tarot deck wands 7.svg",
    "card_wands_08": "Cburnett tarot deck wands 8.svg",
    "card_wands_09": "Cburnett tarot deck wands 9.svg",
    "card_wands_10": "Cburnett tarot deck wands 10.svg",
    "card_wands_page": "Cburnett tarot deck wands page.svg",
    "card_wands_knight": "Cburnett tarot deck wands knight.svg",
    "card_wands_queen": "Cburnett tarot deck wands queen.svg",
    "card_wands_king": "Cburnett tarot deck wands king.svg",

    # COPAS (14 cartas) - Cburnett tarot deck
    "card_cups_01": "Cburnett tarot deck cups ace.svg",
    "card_cups_02": "Cburnett tarot deck cups 2.svg",
    "card_cups_03": "Cburnett tarot deck cups 3.svg",
    "card_cups_04": "Cburnett tarot deck cups 4.svg",
    "card_cups_05": "Cburnett tarot deck cups 5.svg",
    "card_cups_06": "Cburnett tarot deck cups 6.svg",
    "card_cups_07": "Cburnett tarot deck cups 7.svg",
    "card_cups_08": "Cburnett tarot deck cups 8.svg",
    "card_cups_09": "Cburnett tarot deck cups 9.svg",
    "card_cups_10": "Cburnett tarot deck cups 10.svg",
    "card_cups_page": "Cburnett tarot deck cups page.svg",
    "card_cups_knight": "Cburnett tarot deck cups knight.svg",
    "card_cups_queen": "Cburnett tarot deck cups queen.svg",
    "card_cups_king": "Cburnett tarot deck cups king.svg",

    # ESPADAS (14 cartas) - Cburnett tarot deck
    "card_swords_01": "Cburnett tarot deck swords ace.svg",
    "card_swords_02": "Cburnett tarot deck swords 2.svg",
    "card_swords_03": "Cburnett tarot deck swords 3.svg",
    "card_swords_04": "Cburnett tarot deck swords 4.svg",
    "card_swords_05": "Cburnett tarot deck swords 5.svg",
    "card_swords_06": "Cburnett tarot deck swords 6.svg",
    "card_swords_07": "Cburnett tarot deck swords 7.svg",
    "card_swords_08": "Cburnett tarot deck swords 8.svg",
    "card_swords_09": "Cburnett tarot deck swords 9.svg",
    "card_swords_10": "Cburnett tarot deck swords 10.svg",
    "card_swords_page": "Cburnett tarot deck swords page.svg",
    "card_swords_knight": "Cburnett tarot deck swords knight.svg",
    "card_swords_queen": "Cburnett tarot deck swords queen.svg",
    "card_swords_king": "Cburnett tarot deck swords king.svg",

    # OROS/PENTÁCULOS (14 cartas) - Cburnett tarot deck
    "card_pentacles_01": "Cburnett tarot deck pentacles ace.svg",
    "card_pentacles_02": "Cburnett tarot deck pentacles 2.svg",
    "card_pentacles_03": "Cburnett tarot deck pentacles 3.svg",
    "card_pentacles_04": "Cburnett tarot deck pentacles 4.svg",
    "card_pentacles_05": "Cburnett tarot deck pentacles 5.svg",
    "card_pentacles_06": "Cburnett tarot deck pentacles 6.svg",
    "card_pentacles_07": "Cburnett tarot deck pentacles 7.svg",
    "card_pentacles_08": "Cburnett tarot deck pentacles 8.svg",
    "card_pentacles_09": "Cburnett tarot deck pentacles 9.svg",
    "card_pentacles_10": "Cburnett tarot deck pentacles 10.svg",
    "card_pentacles_page": "Cburnett tarot deck pentacles page.svg",
    "card_pentacles_knight": "Cburnett tarot deck pentacles knight.svg",
    "card_pentacles_queen": "Cburnett tarot deck pentacles queen.svg",
    "card_pentacles_king": "Cburnett tarot deck pentacles king.svg",
}


def get_image_url_from_api(filename):
    """Obtiene la URL de descarga directa usando la API de Wikimedia Commons."""
    # Construir la query de la API
    params = {
        'action': 'query',
        'titles': f'File:{filename}',
        'prop': 'imageinfo',
        'iiprop': 'url',
        'format': 'json'
    }

    # Construir URL completa
    query_url = f"{API_URL}?{urllib.parse.urlencode(params)}"

    try:
        # Hacer request a la API
        req = urllib.request.Request(
            query_url,
            headers={'User-Agent': 'TarotAI-App/1.0 (Educational Project)'}
        )

        with urllib.request.urlopen(req, timeout=30) as response:
            data = json.loads(response.read().decode('utf-8'))

        # Extraer la URL de descarga del JSON
        pages = data.get('query', {}).get('pages', {})
        for page_id, page_data in pages.items():
            if page_id == '-1':
                # Imagen no encontrada
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

    # Primero obtener la URL real desde la API
    print(f"🔍 Consultando API para: {wikimedia_filename}...")
    download_url = get_image_url_from_api(wikimedia_filename)

    if not download_url:
        print(f"   ❌ No se pudo obtener URL de descarga")
        return False

    # Determinar extensión según el archivo original
    if wikimedia_filename.endswith('.svg'):
        ext = '.svg'
    elif wikimedia_filename.endswith('.png'):
        ext = '.png'
    else:
        ext = '.jpg'

    output_file = output_path / f"{card_name}{ext}"

    try:
        print(f"📥 Descargando: {card_name}{ext}...")
        print(f"   URL: {download_url}")

        # Descargar la imagen
        req = urllib.request.Request(
            download_url,
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
    print("🃏 DESCARGADOR DE IMÁGENES DEL TAROT (API)")
    print("=" * 70)
    print()
    print(f"📁 Carpeta de destino: {OUTPUT_DIR}")
    print(f"🎴 Total de cartas: {len(TAROT_CARDS)}")
    print(f"🔌 Usando API de Wikimedia Commons")
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

    # Comenzar descarga
    print("⚡ Comenzando descarga...")
    print()

    # Descargar todas las imágenes
    successful = 0
    failed = 0
    failed_cards = []

    for i, (card_name, wikimedia_filename) in enumerate(TAROT_CARDS.items(), 1):
        print(f"[{i}/{len(TAROT_CARDS)}] {card_name}")

        if download_image(card_name, wikimedia_filename, output_path):
            successful += 1
        else:
            failed += 1
            failed_cards.append((card_name, wikimedia_filename))

        # Pausa de 2 segundos para respetar rate limits
        print()
        if i < len(TAROT_CARDS):
            time.sleep(2)

    # Resumen final
    print("=" * 70)
    print("📊 RESUMEN DE DESCARGA")
    print("=" * 70)
    print(f"✅ Exitosas: {successful}")
    print(f"❌ Fallidas:  {failed}")
    print(f"📁 Ubicación: {output_path}")
    print()

    if failed > 0:
        print("⚠️  Cartas que fallaron:")
        for card_name, filename in failed_cards:
            print(f"   - {card_name}: {filename}")
        print()

    if failed == 0:
        print("🎉 ¡Todas las imágenes se descargaron correctamente!")
        print("   Ahora puedes ejecutar la app y verás las imágenes reales.")
        print()
        print("💡 Siguiente paso:")
        print("   ./gradlew clean build")
        print("   Luego ejecuta la app desde Android Studio")
    else:
        print("⚠️  Algunas imágenes no se pudieron descargar.")
        print("   Revisa los errores arriba.")
        if successful > 0:
            print(f"   {successful} imágenes se descargaron correctamente.")

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
        import traceback
        traceback.print_exc()
        sys.exit(1)
