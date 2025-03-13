## Creating custom Inventory

```yaml
menu-id: "CustomInventory"
open-command:
  - custom
  - command

open-requirement:
  permission:
    type: PERMISSION
    permission: permission
    deny-action:
      - "[message] <red>You don't have permission to do that!"
      - "[sound] ENTITY_ENDERMAN_TELEPORT"

open-action:
  - "[message] <yellow>Opening Inventory...</yellow>"
  - "[sound] UI_BUTTON_CLICK"

menu-title: "Custom Inventory"
menu-type: GENERIC_9X6

items:
  close:
    material: BARRIER
    slot: 49
    priority: 1
    display-name: "<red>Close</red>"
  filled-items:
    material: BLACK_STAINED_GLASS_PANE
    slot: [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53]
    priority: 0
```
In Inventory Builder, a default button is `close`.
Like in the example, which means it automatically has a function to close that inventory.

## Register the inventory
### Automatically
After you have the file, move it into `LLibrary/menus` and use command `/llib reload`.
It will register the inventory for you.
Now you can use command `/custom` or `/command` to open the inventory.

**Note**: If you are using this method, the inventory will not have any buttons with functionality, except `close` button. If you want to have functionality buttons, move to `Using code for customization`.

### Using code for customization
You may have to create a file similar to the example in your plugin, or put it somewhere in your plugin's folder.
Next, create a class that loads while the plugin is enabled.

```java
public final class YourPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        // call the class
    }
}
```

```java
import io.github.lucfr1746.llibrary.inventory.InventoryBuilder;
import io.github.lucfr1746.llibrary.util.helper.FileAPI;

public class CustomInventory extends InventoryBuilder {

    public CustomInventory() {
        FileAPI fileAPI = new FileAPI("YourPluginName or MainClassInstance", true);
        loadFromFile(fileAPI.getOrCreateDefaultYAMLConfiguration("menu_name", "folder"));
    }
}
```
Now, you can add buttons, or whatever you want. Like [Create Inventory using code](https://github.com/LucFr1746/LLibrary/wiki/Create-Inventory-Using-Code).
