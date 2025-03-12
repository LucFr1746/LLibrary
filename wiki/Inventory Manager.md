# InventoryManager Wiki

## Overview
`InventoryManager` is responsible for managing custom inventories in your plugin.  
It allows you to open, register, and handle GUI interactions seamlessly.

## Features
- Loads and manages custom inventories.
- Supports dynamic inventory opening through commands.
- Allows registration of inventory builders from YAML files.

---

## Methods

| Method                   | Parameters                          | Return Type | Description                                             |
|--------------------------|-------------------------------------|-------------|---------------------------------------------------------|
| openGUI                  | String menuID, Player player        | void        | Opens a custom GUI for a player.                        |
| registerInventoryBuilder | InventoryBuilder inventoryBuilder   | void        | Registers a new inventory builder.                      |
| registerInventoryBuilder | FileConfiguration fileConfiguration | void        | Loads and registers an inventory from a YAML file.      |

## Usage Example
### Opening a Custom GUI

```java
import io.github.lucfr1746.llibrary.LLibrary;

LLibrary.getInventoryManager().openGUI("menu_id", player);
```

### Registering a New Custom Inventory Builder
```java
InventoryBuilder menu = new InventoryBuilder();
menu.setId("menu_id");

LLibrary.getInventoryManager().registerInventoryBuilder(menu);
```

---
**Author:** [lucfr1746](https://github.com/lucfr1746)