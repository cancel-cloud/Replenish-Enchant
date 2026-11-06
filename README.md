# ReplenishEnchant

**ReplenishEnchant** is a lightweight hoe enchantment for Paper and Purpur. Harvest a full crop and the same spot is replanted.

## Features

- Replants fully grown crops harvested with a Replenish hoe.
- Crops: wheat, carrots, potatoes, beetroots, nether wart, sweet berry bushes.
- Happy Villager particles on a successful replant.
- Creative mode does not replant.
- The hoe takes durability. Unbreaking and Mending still apply.
- Combines with Mending, Efficiency, Unbreaking, and Fortune.

## Installation

Requires **Java 25** and **Paper or Purpur 26.3**.

1. Install [MCKotlin-Paper](https://modrinth.com/plugin/mckotlin) `1.5.1-k2.4.20` or newer.
2. Download `ReplenishEnchant-1.0.jar`.
3. Put both jars in `plugins/`.
4. Restart the server.

## Where to get it

Replenish is level 1, hoes only, main hand. It is not crafted, and it does not show up in chests or on mob gear.

**Enchanting table.** Weight 5, the same as Unbreaking. Efficiency is 10, Fortune is 2, Silk Touch is 1. The cost window is 5–55, so a few bookshelves is enough and a full table still rolls it. On a stocked table, when those hoe enchantments are all eligible, Replenish is about **1 in 5** of the offered enchantment.

**Librarians.** It is in the normal tradeable book pool, not the treasure pool, so it does not get Mending's doubled price. Each enchanted-book offer is **1 in 41** (~2.4%). A master librarian has about a **6%** chance that one of their offers is Replenish. The book costs **5–19 emeralds** plus one book. Villagers who already rolled their trades keep the old offers. Employ a new librarian after installing.

**Anvil.** Anvil cost is 1, so an existing book or hoe can be combined onto another hoe.

## Usage

1. Get a hoe with Replenish.
2. Harvest a fully grown supported crop.
3. The crop drops, and the same spot is replanted.

Immature crops are not broken.

## Technical details

- **Id**: `replenishplugin:replenish`
- **Max level**: 1
- **Weight**: 5
- **Enchanting cost**: 5–55
- **Anvil cost**: 1
- **Tags**: `#minecraft:in_enchanting_table`, `#minecraft:tradeable`

Built for Paper and Purpur 26.3. License: MIT.
