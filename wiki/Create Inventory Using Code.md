## Creating custom inventory

```java
import io.github.lucfr1746.llibrary.inventory.InventoryBuilder;
import org.bukkit.inventory.MenuType;

public class CustomInventory extends InventoryBuilder {

    public CustomInventory() {
        setId("custom_inventory");         // default is random uuid
        setLockMode(LockMode.ALL);         // default is LockMode.ALL
        setMenuType(MenuType.GENERIC_9X6); // default is MenuType.GENERIC_9x3
        setTitle("Custom Inventory");      // default is ""
    }
}
```
Now an inventory named `"Custom Inventory"` has been created!

## Adding open commands

```java
import io.github.lucfr1746.llibrary.inventory.InventoryBuilder;
import org.bukkit.inventory.MenuType;

import java.util.ArrayList;

public class CustomInventory extends InventoryBuilder {

    public CustomInventory() {
        setId("custom_inventory");         // default is random uuid
        setLockMode(LockMode.ALL);         // default is LockMode.ALL
        setMenuType(MenuType.GENERIC_9X6); // default is MenuType.GENERIC_9x3
        setTitle("Custom Inventory");      // default is ""

        registerCommands();
    }

    private void registerCommands() {
        setOpenCommands(new ArrayList<String>("custom", "command"));
    }
}
```
The inventory already has command. Now before player can use command `/custom` or `/inventory` to open, we have to register the inventory through `Inventory Manager`.

```java
import io.github.lucfr1746.llibrary.LLibrary;

public CustomInventory() {
    setId("custom_inventory");         // default is random uuid
    setLockMode(LockMode.ALL);         // default is LockMode.ALL
    setMenuType(MenuType.GENERIC_9X6); // default is MenuType.GENERIC_9x3
    setTitle("Custom Inventory");      // default is ""

    registerCommands();

    LLibrary.getInventoryManager().registerInventoryBuilder(this);
}
```

You are good to go now, remember to call `new CustomInventory()` somewhere in your plugin before it actually works.

**Now, you want to add some features such as open requirements, open actions, and button with functionality? Let's straight into it!**

## Adding open requirements

```java
import io.github.lucfr1746.llibrary.inventory.InventoryBuilder;
import io.github.lucfr1746.llibrary.requirement.Requirement;
import io.github.lucfr1746.llibrary.requirement.list.HasLevelRequirement;
import io.github.lucfr1746.llibrary.requirement.list.HasPermissionRequirement;
import org.bukkit.inventory.MenuType;

import java.util.ArrayList;

public class CustomInventory extends InventoryBuilder {

    public CustomInventory() {
        setId("custom_inventory");         // default is random uuid
        setLockMode(LockMode.ALL);         // default is LockMode.ALL
        setMenuType(MenuType.GENERIC_9X6); // default is MenuType.GENERIC_9x3
        setTitle("Custom Inventory");      // default is ""

        registerRequirements();
    }

    // After this, player must have permission of "permission"
    // and level higher or equal to 10 to open the inventory.
    private void registerRequirements() {
        List<Requirement> requirementList = new ArrayList<>();
        requirementList.add(new HasPermissionRequirement("permission"));
        requirementList.add(new HasLevelRequirement(10));
        setOpenRequirements(requirementList);
    }

    // Or create a requirement with deny actions
    // Player without permission "permission" will be sent a message "Deny"
    // when trying to open the inventory.
    private void registerRequirements() {
        HasPermissionRequirement permissionRequirement = new HasPermissionRequirement("permission");
        requirement.setDenyHandler(new ArrayList<Action>(new MessageAction("&cDeny!")));
        
        setOpenRequirements(new ArrayList<Requirement>(permissionRequirement));
    }
}
```

## Adding open actions

```java
import io.github.lucfr1746.llibrary.action.Action;
import io.github.lucfr1746.llibrary.action.list.GiveMoneyAction;
import io.github.lucfr1746.llibrary.action.list.MessageAction;
import io.github.lucfr1746.llibrary.inventory.InventoryBuilder;
import org.bukkit.inventory.MenuType;

import java.util.ArrayList;

public class CustomInventory extends InventoryBuilder {

    public CustomInventory() {
        setId("custom_inventory");         // default is random uuid
        setLockMode(LockMode.ALL);         // default is LockMode.ALL
        setMenuType(MenuType.GENERIC_9X6); // default is MenuType.GENERIC_9x3
        setTitle("Custom Inventory");      // default is ""

        registerOpenActions();
    }

    // The Player will receive a message "You opened the inventory!"
    // and $100 money (Required VaultAPI) after the inventory has been opened.
    private void registerOpenActions() {
        List<Action> actionList = new ArrayList<>();
        actionList.add(new MessageAction("&aYou opened the inventory!"));
        actionList.add(new GiveMoneyAction(100));
        setOpenActions(actionList);
    }
}
```

## Adding a button with functionality to the inventory

```java
import io.github.lucfr1746.llibrary.inventory.InventoryBuilder;
import io.github.lucfr1746.llibrary.inventory.InventoryButton;
import io.github.lucfr1746.llibrary.itemstack.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.MenuType;

import java.util.ArrayList;

public class CustomInventory extends InventoryBuilder {

    public CustomInventory() {
        setId("custom_inventory");         // default is random uuid
        setLockMode(LockMode.ALL);         // default is LockMode.ALL
        setMenuType(MenuType.GENERIC_9X6); // default is MenuType.GENERIC_9x3
        setTitle("Custom Inventory");      // default is ""

        addButton();
    }

    private void addButton() {
        addButton(49, closeButton());
    }

    // Create a button with id "close", priority of "1" and required permission "permission" to view.
    // The creator is the interface of the button, in this it is a barrier named "Close"
    // The consumer is the function of the button, in this, it closes the inventory when the player clicked.
    private InventoryButton closeButton() {
        return new InventoryButton()
                .id("close")
                .priority(0)
                .viewRequirements(new ArrayList<Requirement>(new HasPermissionRequirement("permission")))
                .creator(player -> {
                    new ItemBuilder(Material.BARRIER)
                            .setDisplayName("&cClose")
                            .build();
                })
                .consumer(event -> {
                    Player player = (Player) event.getWhoClicked(); 
                    player.closeInventory();
                    player.sendMessage("Your closed the inventory!");
                });
    }
}
```

---
**Author:** [lucfr1746](https://github.com/lucfr1746)