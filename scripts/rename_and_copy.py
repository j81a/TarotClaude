#!/usr/bin/env python3
"""
Script para renombrar y copiar las cartas del tarot al proyecto.
Convierte nombres como "00-TheFool.jpg" a "card_major_00.jpg"
"""

import shutil
from pathlib import Path

SOURCE_DIR = "Cards-jpg"
DEST_DIR = "../app/src/main/res/drawable"

# Mapeo completo
RENAME_MAP = {
    # Arcanos Mayores
    "00-TheFool.jpg": "card_major_00.jpg",
    "01-TheMagician.jpg": "card_major_01.jpg",
    "02-TheHighPriestess.jpg": "card_major_02.jpg",
    "03-TheEmpress.jpg": "card_major_03.jpg",
    "04-TheEmperor.jpg": "card_major_04.jpg",
    "05-TheHierophant.jpg": "card_major_05.jpg",
    "06-TheLovers.jpg": "card_major_06.jpg",
    "07-TheChariot.jpg": "card_major_07.jpg",
    "08-Strength.jpg": "card_major_08.jpg",
    "09-TheHermit.jpg": "card_major_09.jpg",
    "10-WheelOfFortune.jpg": "card_major_10.jpg",
    "11-Justice.jpg": "card_major_11.jpg",
    "12-TheHangedMan.jpg": "card_major_12.jpg",
    "13-Death.jpg": "card_major_13.jpg",
    "14-Temperance.jpg": "card_major_14.jpg",
    "15-TheDevil.jpg": "card_major_15.jpg",
    "16-TheTower.jpg": "card_major_16.jpg",
    "17-TheStar.jpg": "card_major_17.jpg",
    "18-TheMoon.jpg": "card_major_18.jpg",
    "19-TheSun.jpg": "card_major_19.jpg",
    "20-Judgement.jpg": "card_major_20.jpg",
    "21-TheWorld.jpg": "card_major_21.jpg",

    # Bastos
    "Wands01.jpg": "card_wands_01.jpg",
    "Wands02.jpg": "card_wands_02.jpg",
    "Wands03.jpg": "card_wands_03.jpg",
    "Wands04.jpg": "card_wands_04.jpg",
    "Wands05.jpg": "card_wands_05.jpg",
    "Wands06.jpg": "card_wands_06.jpg",
    "Wands07.jpg": "card_wands_07.jpg",
    "Wands08.jpg": "card_wands_08.jpg",
    "Wands09.jpg": "card_wands_09.jpg",
    "Wands10.jpg": "card_wands_10.jpg",
    "Wands11.jpg": "card_wands_page.jpg",
    "Wands12.jpg": "card_wands_knight.jpg",
    "Wands13.jpg": "card_wands_queen.jpg",
    "Wands14.jpg": "card_wands_king.jpg",

    # Copas
    "Cups01.jpg": "card_cups_01.jpg",
    "Cups02.jpg": "card_cups_02.jpg",
    "Cups03.jpg": "card_cups_03.jpg",
    "Cups04.jpg": "card_cups_04.jpg",
    "Cups05.jpg": "card_cups_05.jpg",
    "Cups06.jpg": "card_cups_06.jpg",
    "Cups07.jpg": "card_cups_07.jpg",
    "Cups08.jpg": "card_cups_08.jpg",
    "Cups09.jpg": "card_cups_09.jpg",
    "Cups10.jpg": "card_cups_10.jpg",
    "Cups11.jpg": "card_cups_page.jpg",
    "Cups12.jpg": "card_cups_knight.jpg",
    "Cups13.jpg": "card_cups_queen.jpg",
    "Cups14.jpg": "card_cups_king.jpg",

    # Espadas
    "Swords01.jpg": "card_swords_01.jpg",
    "Swords02.jpg": "card_swords_02.jpg",
    "Swords03.jpg": "card_swords_03.jpg",
    "Swords04.jpg": "card_swords_04.jpg",
    "Swords05.jpg": "card_swords_05.jpg",
    "Swords06.jpg": "card_swords_06.jpg",
    "Swords07.jpg": "card_swords_07.jpg",
    "Swords08.jpg": "card_swords_08.jpg",
    "Swords09.jpg": "card_swords_09.jpg",
    "Swords10.jpg": "card_swords_10.jpg",
    "Swords11.jpg": "card_swords_page.jpg",
    "Swords12.jpg": "card_swords_knight.jpg",
    "Swords13.jpg": "card_swords_queen.jpg",
    "Swords14.jpg": "card_swords_king.jpg",

    # Pentáculos
    "Pentacles01.jpg": "card_pentacles_01.jpg",
    "Pentacles02.jpg": "card_pentacles_02.jpg",
    "Pentacles03.jpg": "card_pentacles_03.jpg",
    "Pentacles04.jpg": "card_pentacles_04.jpg",
    "Pentacles05.jpg": "card_pentacles_05.jpg",
    "Pentacles06.jpg": "card_pentacles_06.jpg",
    "Pentacles07.jpg": "card_pentacles_07.jpg",
    "Pentacles08.jpg": "card_pentacles_08.jpg",
    "Pentacles09.jpg": "card_pentacles_09.jpg",
    "Pentacles10.jpg": "card_pentacles_10.jpg",
    "Pentacles11.jpg": "card_pentacles_page.jpg",
    "Pentacles12.jpg": "card_pentacles_knight.jpg",
    "Pentacles13.jpg": "card_pentacles_queen.jpg",
    "Pentacles14.jpg": "card_pentacles_king.jpg",
}


def main():
    script_dir = Path(__file__).parent
    source_path = script_dir / SOURCE_DIR
    dest_path = (script_dir / DEST_DIR).resolve()

    print("=" * 70)
    print("🎴 RENOMBRADO Y COPIA DE CARTAS")
    print("=" * 70)
    print(f"Origen: {source_path}")
    print(f"Destino: {dest_path}")
    print()

    # Primero eliminar XMLs viejos
    print("🗑️  Eliminando placeholders XML...")
    for xml_file in dest_path.glob("card_*.xml"):
        xml_file.unlink()
        print(f"   ✓ {xml_file.name}")
    print()

    # Copiar y renombrar
    print("📋 Copiando y renombrando...")
    copied = 0

    for source_name, dest_name in RENAME_MAP.items():
        source_file = source_path / source_name
        dest_file = dest_path / dest_name

        if source_file.exists():
            shutil.copy2(source_file, dest_file)
            print(f"   ✓ {source_name} → {dest_name}")
            copied += 1
        else:
            print(f"   ✗ No encontrado: {source_name}")

    print()
    print("=" * 70)
    print(f"✅ {copied}/78 cartas copiadas")
    print("=" * 70)


if __name__ == "__main__":
    main()
