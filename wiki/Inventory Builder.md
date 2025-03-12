# InventoryBuilder

`InventoryBuilder` is a class for creating and managing custom inventories in a Bukkit/Spigot plugin. It allows defining buttons, handling events, and enforcing access requirements.

```java
package io.github.lucfr1746.llibrary.inventory;
```

## Features
- Supports custom inventory types (MenuType).
- Implement event handling for click, open, and close events.
- Use priority-based button management.
- Supports open requirements and actions.
- Provides serialization support for loading from config files.

## Enum: LockMode
Defines inventory interaction locking behavior.

| Mode   | Description                                           |
|--------|-------------------------------------------------------|
| ALL    | Blocks all interactions.                              |
| GUI    | Blocks GUI clicks but allows player inventory clicks. |
| PLAYER | Allows GUI clicks but blocks player inventory clicks. |
| NONE   | No restrictions.                                      |

## Fields

| Field             | Type                                   | Description                                       |
|-------------------|----------------------------------------|---------------------------------------------------|
| id                | String                                 | Unique identifier of the inventory.               |
| uuid	             | String                                 | Randomly generated UUID.                          |
| title             | String                                 | The inventory's display title.                    |
| menuType	         | MenuType	                              | The inventory's type (e.g., GENERIC_9X3).         |
| lockMode	         | LockMode	                              | Determines how interactions are handled.          |
| inventoryView     | InventoryView                          | The currently open inventory instance.            |
| buttonMap         | Map<Integer, TreeSet<InventoryButton>> | Mapping of slots to buttons (sorted by priority). |
| openRequirements	 | List<Requirement>                      | Conditions required to open the inventory.        |
| openActions       | List<Action>                           | Actions executed when the inventory is opened.    |
| openCommands	     | List<String>                           | Commands executed when the inventory is opened.   |

---

## Methods
### Setter Methods

| Method              | Parameters                                       | Return Type | Description                           |
|---------------------|--------------------------------------------------|-------------|---------------------------------------|
| setId               | String id                                        | void        | Sets the inventory's ID.              |
| setTitle            | String title                                     | void        | Sets the inventory title.             |
| setMenuType         | MenuType menuType                                | void        | Sets the inventory type.              |
| setLockMode         | LockMode lockMode                                | void        | Defines how interactions are handled. |
| setButtonMap        | Map<Integer, TreeSet<InventoryButton>> buttonMap | void        | Sets all buttons in the inventory.    |
| setOpenRequirements | List<Requirement> openRequirements               | void        | Sets opening conditions.              |
| setOpenActions      | List<Action> openActions                         | void        | Sets actions triggered on open.       |
| setOpenCommands     | List<String> openCommands                        | void        | Sets commands executed on open.       |

### Getter Methods

| Method              | Return Type                            | Description                            |
|---------------------|----------------------------------------|----------------------------------------|
| getId               | String                                 | Returns the inventory ID.              |
| getUUID             | String                                 | Returns the unique UUID.               |
| getTitle            | String                                 | Returns the inventory title.           |
| getMenuType         | MenuType                               | Returns the inventory type.            |
| getLockMode         | LockMode                               | Returns the interaction lock mode.     |
| getInventoryView    | InventoryView                          | Returns the currently open inventory.  |
| getButtonMap        | Map<Integer, TreeSet<InventoryButton>> | Returns the inventory's buttons.       |
| getOpenRequirements | List<Requirement>                      | Returns the list of open requirements. |
| getOpenActions      | List<Action>                           | Returns actions triggered on open.     |
| getOpenCommands     | List<String>                           | Returns commands executed on open.     |

### Helper Methods

| Method               | Parameters                                                           | Return Type        | Description                                                                                |
|----------------------|----------------------------------------------------------------------|--------------------|--------------------------------------------------------------------------------------------|
| `addButton`          | `int slot, InventoryButton button`                                   | `void`             | Adds a button to a specific slot, replacing any button with the same priority.             |
| `refreshButton`      | `Player player, int slot, InventoryButton button`                    | `void`             | Replaces a button in a slot and updates the inventory for the player.                      |
| `refreshButtons`     | `Player player, Map<Integer, TreeSet<InventoryButton>> newButtonMap` | `void`             | Updates multiple buttons at once and refreshes the inventory view.                         |
| `updateButtons`      | `Player player`                                                      | `void`             | Updates all buttons in the inventory for the player.                                       |
| `updateButton`       | `Player player, int slot`                                            | `void`             | Updates a specific button in a slot based on visibility requirements.                      |
| `decorate`           | `Player player`                                                      | `void`             | Initializes the inventory view and updates the buttons.                                    |
| `clone`              | `-`                                                                  | `InventoryBuilder` | Creates a deep copy of the `InventoryBuilder`, including button mappings and requirements. |
| `loadFromFile`       | `FileConfiguration fileConfiguration`                                | `void`             | Loads inventory data (buttons, actions, requirements) from a configuration file.           |
| `loadID`             | `FileConfiguration fileConfiguration`                                | `void`             | Loads the inventory ID from the configuration file.                                        |
| `loadOpenCommands`   | `FileConfiguration fileConfiguration`                                | `void`             | Loads open commands from the configuration file.                                           |
| `loadMenuProperties` | `FileConfiguration fileConfiguration`                                | `void`             | Loads the inventory title, type, and other properties.                                     |
| `loadItems`          | `FileConfiguration fileConfiguration`                                | `void`             | Parses and loads inventory buttons from the configuration file.                            |

---

## Usage Example

```java
InventoryBuilder builder = new InventoryBuilder();
builder.setTitle("Custom Menu");
builder.setMenuType(MenuType.GENERIC_9X6);
builder.addButton(10, new InventoryButton().id("test").priority(1));
```

---
**Author:** [lucfr1746](https://github.com/lucfr1746)




