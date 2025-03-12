# InventoryButton

`InventoryButton` is a class that represents a button in a custom inventory system. It allows for dynamic icon creation, handling click events, and visibility requirements.

## Class Overview

```java
package io.github.lucfr1746.llibrary.inventory;
```
## Features
- Dynamic icon generation based on the player.
- Custom event handling for inventory interactions.
- Configurable priority for sorting buttons.
- Conditional visibility based on requirements.
- Supports cloning.
## Fields

| Field              | Type                            | Description                              |
|--------------------|---------------------------------|------------------------------------------|
| `iconCreator`      | `Function<Player, ItemStack>`   | Generates the button's icon dynamically. |
| `eventConsumer`    | `Consumer<InventoryClickEvent>` | Handles click events.                    |
| `id`               | `String`                        | Unique identifier for the button.        |
| `priority`         | `int`                           | Determines sorting order.                |
| `viewRequirements` | `List<Requirement>`             | Defines conditions for visibility.       |

## Methods

### Setter Methods (Fluent API)

| Method             | Parameters                                    | Return Type       | Description                          |
|--------------------|-----------------------------------------------|-------------------|--------------------------------------|
| `creator`          | `Function<Player, ItemStack> iconCreator`     | `InventoryButton` | Sets the icon generator.             |
| `consumer`         | `Consumer<InventoryClickEvent> eventConsumer` | `InventoryButton` | Sets the click event handler.        |
| `id`               | `String id`                                   | `InventoryButton` | Sets the button's unique identifier. |
| `priority`         | `int priority`                                | `InventoryButton` | Sets the button's priority.          |
| `viewRequirements` | `List<Requirement> requirements`              | `InventoryButton` | Sets visibility conditions.          |

### Getter Methods

| Method                | Return Type                     | Description                                        |
|-----------------------|---------------------------------|----------------------------------------------------|
| `getIconCreator`      | `Function<Player, ItemStack>`   | Returns the function generating the button's icon. |
| `getEventConsumer`    | `Consumer<InventoryClickEvent>` | Returns the event consumer handling clicks.        |
| `getId`               | `String`                        | Returns the button's identifier.                   |
| `getPriority`         | `int`                           | Returns the button's priority.                     |
| `getViewRequirements` | `List<Requirement>`             | Returns the list of visibility requirements.       |

### Cloning

```java
@Override
public InventoryButton clone() {
    try {
        InventoryButton clone = (InventoryButton) super.clone();
        clone.id = this.id;
        clone.priority = this.priority;
        clone.iconCreator = this.iconCreator;
        clone.eventConsumer = this.eventConsumer;
        clone.viewRequirements = this.viewRequirements != null ? List.copyOf(this.viewRequirements) : List.of();
        return clone;
    } catch (CloneNotSupportedException e) {
        throw new AssertionError("Cloning not supported", e);
    }
}
```

### Usage Example

```java
InventoryButton button = new InventoryButton()
    .id("example_button")
    .priority(1)
    .creator(player -> new ItemStack(Material.DIAMOND))
    .consumer(event -> event.getWhoClicked().sendMessage("You clicked the button!"));
```

### Notes
- The `clone` method ensures deep copying of `viewRequirements` to prevent modifications affecting original objects.
- This class follows the builder pattern for fluent configuration.

---
**Author:** [lucfr1746](https://github.com/lucfr1746)
