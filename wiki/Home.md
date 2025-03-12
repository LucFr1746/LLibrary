![Banner](https://cdn.modrinth.com/data/cached_images/71df530b843cf6fa0ce475f79bc09e065ac94097.png)

## Getting Started
- Make sure your server is running Java 21 or later.
- Download & Install the LLibrary jar file into the plugins' folder.
- Start the server. (Make sure to close and start it; Do not reload the server)

Below is the basic information about the API to get going.\
For more detailed information, tutorials, and information about advanced features,
visit the [wiki on GitHub](https://github.com/LucFr1746/LLibrary/wiki).
## Features:
- **Inventory Builder**: Create a custom inventory, custom types, custom buttons with different functions per button, per player GUI, refresh GUI anytime-sync, etc.

- **Item Builder**: Customize item stack, modifier item stack meta-data, modules id,... All of you can think of. **Custom GUI Item Editor**, etc.

## Question
<details>
<summary>Why 1.21.2 — 1.21.x ?</summary>

- Due to Spigot 1.21.1-R0.1-SNAPSHOT having an API named **"Menu Type"**, we decided to use that to create the **Inventory Builder API**. This makes the builder more flexible and able to create multiple types of inventories.

- Due to Spigot 1.21.2-R0.1-SNAPSHOT having an API named **"Damage Resistant"**, ItemStack will be able to have damage-resistant flags. Ex: "DamageTypeTags.IS_FIRE" makes the item stack immune to fire. And an API named **"setItemModel(key)"** allows you to set an item's custom model based on a custom key or Minecraft key.

</details>

## bStats
[![bStats Stats](https://bstats.org/signatures/bukkit/LucFrLib.svg)](https://bstats.org/plugin/bukkit/LucFrLib)