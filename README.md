![Banner](https://cdn.modrinth.com/data/cached_images/71df530b843cf6fa0ce475f79bc09e065ac94097.png)

## Getting Started
- Make sure your server is running Java 21 or later​.
- Download & Install the LLibrary jar file into the plugins folder​.
- Start the server​. (Make sure to close and start it, do not reload the server)

Below is the basic information about the API to get going.\
For more detailed information, tutorials, and information about advanced features, visit the [wiki on Github](https://github.com/LucFr1746/LLibrary/wiki).
## Features:
- **Inventory Builder**: Create a custom inventory, custom types, custom buttons with different functions per button, animated GUI, and refresh GUI anytime-sync with server threads, etc.

- **Item Builder**: Customize item stack, modifier item stack meta-data, modules id,... All of you can think of. **Custom GUI Item Editor**, etc.

- **Update Checker**: Fully customize the update checker and visualize output, console, and admin's chat announcements, etc.

- **Recipe API**: Get the final result item stack from a 3x3 list of ingredients, and get all craftable items from a list of ingredients, etc.

- **Enchantment API**: Get all enchantments suitable for the items, etc.

## Question

<details>
<summary>Why 1.21.2 - 1.21.x ?</summary>

- Due to Spigot 1.21.1-R0.1-SNAPSHOT having an API named **"Menu Type"**, we decided to use that to create the **Inventory Builder API**. This makes the builder more flexible and able to create multiple types of inventories.

- Due to Spigot 1.21.2-R0.1-SNAPSHOT having an API named **"Damage Resistant"**, ItemStack will be able to have damage-resistant flags. Ex: "DamageTypeTags.IS_FIRE" makes the item stack immune to fire.

</details>

<details>
<summary>Has this added any features to the server ?</summary>

This added some in-game quality of life, such as:
- A GUI that helps you modify item stack in-game, you can customize all of the GUI like text, and toggle the functions of the GUI or disable this GUI in the config file. Of course, the GUI has permission to open and per-function permission if you want to customize more. Who knows!

</details>


## bStats
[![bStats Stats](https://bstats.org/signatures/bukkit/LucFrLib.svg)](https://bstats.org/plugin/bukkit/LucFrLib)