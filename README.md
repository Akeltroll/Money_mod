# Money Mod — NeoForge 1.21.1

A Minecraft mod that adds a configurable coin economy. Mobs drop coins on death, coins can be exchanged via the Wandering Trader, and stored in tiered money pouches.

## Features

- **7 coin tiers** — Copper, Iron, Gold, Emerald, Diamond, Netherite, Nether Star
- **Mob drops** — each mob has a configurable drop chance and coin type (fully editable in the config file)
- **Wandering Trader exchanges** — trade lower-tier coins for higher-tier ones (random chance per trader)
- **Money pouches** — 3 tiers (Basic: 9 slots, Advanced: 18 slots, Final: 27 slots) to store coins
- **Curios support** (optional) — equip a pouch in the belt slot via Shift + Right Click
- **Per-coin toggle** — each coin type can be individually disabled in the config

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.73+
- [Curios API](https://www.curseforge.com/minecraft/mc-mods/curios) *(optional)*

## Configuration

The config file is located at `config/moneymod-common.toml`.

You can configure:
- Enable/disable each coin type
- Number of slots per pouch tier (1–54)
- Coin drops per mob using the format:
  - `"namespace:entity:coin_type:chance"` — e.g. `"minecraft:zombie:copper_coin:0.08"`
  - `"namespace:entity:coin_type:chance:count"` — e.g. `"minecraft:wither:iron_coin:1.0:3"`

Available coin types: `copper_coin`, `iron_coin`, `gold_coin`, `emerald_coin`, `diamond_coin`, `netherite_coin`, `nether_star_coin`

## License

All Rights Reserved — Copyright (c) 2025 Akeltroll.
You may not use, copy, modify, or distribute this mod without explicit written permission from the author.
