package io.github.lucfr1746.llibrary.inventory;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
import io.github.lucfr1746.llibrary.action.ActionLoader;
import io.github.lucfr1746.llibrary.itemstack.ItemBuilder;
import io.github.lucfr1746.llibrary.requirement.Requirement;
import io.github.lucfr1746.llibrary.requirement.RequirementLoader;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class InventoryBuilder implements InventoryHandler, Cloneable {

    public enum LockMode {
        ALL,
        GUI,
        PLAYER,
        NONE
    }

    private String id = "InventoryBuilder";

    private String uuid = UUID.randomUUID().toString();

    private String title = "";

    private MenuType menuType = MenuType.GENERIC_9X3;

    private LockMode lockMode = LockMode.ALL;

    private InventoryView inventoryView;

    private Map<Integer, TreeSet<InventoryButton>> buttonMap = new HashMap<>();

    private List<Requirement> openRequirements = new ArrayList<>();

    private List<Action> openActions = new ArrayList<>();

    private List<String> openCommands = new ArrayList<>();

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMenuType(MenuType menuType) {
        this.menuType = menuType;
    }

    public void setLockMode(LockMode lockMode) {
        this.lockMode = lockMode;
    }

    public void setButtonMap(Map<Integer, TreeSet<InventoryButton>> buttonMap) {
        this.buttonMap.clear();
        this.buttonMap.putAll(buttonMap);
    }

    public void setOpenRequirements(List<Requirement> openRequirements) {
        this.openRequirements.clear();
        this.openRequirements.addAll(openRequirements);
    }

    public void setOpenActions(List<Action> openActions) {
        this.openActions.clear();
        this.openActions.addAll(openActions);
    }

    public void setOpenCommands(List<String> openCommands) {
        this.openCommands.clear();
        this.openCommands.addAll(openCommands);
    }

    public String getId() {
        return this.id;
    }

    public String getUUID() {
        return this.uuid;
    }

    public String getTitle() {
        return this.title;
    }

    public MenuType getMenuType() {
        return this.menuType;
    }

    public LockMode getLockMode() {
        return this.lockMode;
    }

    public @NotNull InventoryView getInventoryView() {
        return this.inventoryView;
    }

    public Map<Integer, TreeSet<InventoryButton>> getButtonMap() {
        return Collections.unmodifiableMap(this.buttonMap);
    }

    public TreeSet<InventoryButton> getSlotButtons(int slot) {
        return this.buttonMap.get(slot);
    }

    public List<Requirement> getOpenRequirements() {
        return Collections.unmodifiableList(this.openRequirements);
    }

    public List<Action> getOpenActions() {
        return Collections.unmodifiableList(this.openActions);
    }

    public List<String> getOpenCommands() {
        return Collections.unmodifiableList(this.openCommands);
    }

    public void addButton(int slot, InventoryButton button) {
        buttonMap.computeIfAbsent(slot,
                        k -> new TreeSet<>(Comparator.comparing(InventoryButton::getPriority).reversed()))
                .removeIf(b -> b.getPriority() == button.getPriority());
        buttonMap.get(slot).add(button);
    }

    public void refreshButton(Player player, int slot, InventoryButton button) {
        addButton(slot, button);
        updateButton(player, slot);
    }

    public void refreshButtons(Player player, Map<Integer, TreeSet<InventoryButton>> newButtonMap) {
        newButtonMap.forEach((slot, buttons) ->
                buttonMap.computeIfAbsent(slot,
                                k -> new TreeSet<>(Comparator.comparing(InventoryButton::getPriority).reversed()))
                        .addAll(buttons)
        );
        updateButtons(player);
    }

    private void updateButtons(Player player) {
        buttonMap.forEach((slot, buttons) -> updateButton(player, slot));
    }

    private void updateButton(Player player, int slot) {
        buttonMap.getOrDefault(slot, new TreeSet<>(Comparator.comparing(InventoryButton::getPriority).reversed()))
                .stream()
                .filter(button -> button.getViewRequirements().stream().allMatch(req -> req.evaluate(player)))
                .findFirst()
                .ifPresentOrElse(button -> {
                    if (slot >= this.inventoryView.getTopInventory().getSize()) {
                        LLibrary.getPluginLogger().warning("The slot -> " + slot + " is out of of inventory size. Skipping...");
                    } else {
                        ItemStack icon = button.getIconCreator().apply(player);
                        this.inventoryView.getTopInventory().setItem(slot, icon);
                    }
                }, () -> this.inventoryView.getTopInventory().clear(slot));
    }

    public void decorate(Player player) {
        this.inventoryView = menuType.typed().create(player, getTitle());
        updateButtons(player);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;

        boolean isPlayerInventory = event.getClickedInventory() instanceof PlayerInventory;

        switch (this.lockMode) {
            case GUI -> event.setCancelled(!isPlayerInventory);
            case PLAYER -> event.setCancelled(isPlayerInventory);
            case NONE -> event.setCancelled(false);
            default -> event.setCancelled(true);
        }

        int slot = event.getRawSlot();
        TreeSet<InventoryButton> buttons = buttonMap.get(slot);
        if (buttons == null || buttons.isEmpty()) return;

        buttons.stream()
                .filter(button -> button.getViewRequirements().stream()
                        .allMatch(req -> req.evaluate((Player) event.getWhoClicked())))
                .findFirst()
                .ifPresent(button -> {
                    if (button.getEventConsumer() != null) {
                        button.getEventConsumer().accept(event);
                    }
                });
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        for (Action action : getOpenActions()) {
            action.execute((Player) event.getPlayer());
        }
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }

    @Override
    public InventoryBuilder clone() {
        try {
            InventoryBuilder clone = (InventoryBuilder) super.clone();
            clone.uuid = UUID.randomUUID().toString();

            clone.openRequirements = new ArrayList<>(this.openRequirements);
            clone.openActions = new ArrayList<>(this.openActions);
            clone.openCommands = new ArrayList<>(this.openCommands);

            clone.buttonMap = new HashMap<>();
            for (Map.Entry<Integer, TreeSet<InventoryButton>> entry : this.buttonMap.entrySet()) {
                TreeSet<InventoryButton> clonedButtons = new TreeSet<>(Comparator.comparing(InventoryButton::getPriority).reversed());
                for (InventoryButton button : entry.getValue()) {
                    clonedButtons.add(button.clone());
                }
                clone.buttonMap.put(entry.getKey(), clonedButtons);
            }

            clone.inventoryView = null;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }

    public void loadFromFile(@Nullable FileConfiguration fileConfiguration) {
        if (fileConfiguration == null)
            throw new IllegalArgumentException("The file must no be null!");
        loadID(fileConfiguration);

        List<Requirement> requirements;
        if (fileConfiguration.getConfigurationSection("open-requirement") == null) {
            requirements = new ArrayList<>();
        } else {
            requirements = new RequirementLoader().getRequirements(fileConfiguration.getConfigurationSection("open-requirement"));
        }
        setOpenRequirements(requirements);

        List<Action> actions;
        if (!fileConfiguration.contains("open-action")) {
            actions = new ArrayList<>();
        } else {
            actions = new ActionLoader().getActions(fileConfiguration.getStringList("open-action"));
        }
        setOpenActions(actions);

        loadOpenCommands(fileConfiguration);
        loadMenuProperties(fileConfiguration);
    }

    private void loadID(@NotNull FileConfiguration fileConfiguration) {
        String id = fileConfiguration.getString("menu-id");
        if (id == null) {
            LLibrary.getPluginLogger().error("Missing menu-id which is required. Skipping...");
        }
        setId(id);
    }

    private void loadOpenCommands(@NotNull FileConfiguration fileConfiguration) {
        List<String> commands = new ArrayList<>();
        Optional.ofNullable(fileConfiguration.get("open-command")).ifPresent(cmd -> {
            if (cmd instanceof String) commands.add((String) cmd);
            else commands.addAll(fileConfiguration.getStringList("open-command"));
        });

        setOpenCommands(commands);
    }

    private void loadMenuProperties(@NotNull FileConfiguration fileConfiguration) {
        Optional.ofNullable(fileConfiguration.getString("menu-title")).ifPresent(this::setTitle);
        Optional.ofNullable(fileConfiguration.getString("menu-type")).ifPresent(type ->
                setMenuType(Registry.MENU.get(new NamespacedKey(NamespacedKey.MINECRAFT, type.toLowerCase())))
        );
        loadItems(fileConfiguration);
    }

    private void loadItems(@NotNull FileConfiguration fileConfiguration) {
        ConfigurationSection items = fileConfiguration.getConfigurationSection("items");
        if (items == null) return;

        for (String key : items.getKeys(false)) {
            ConfigurationSection item = Objects.requireNonNull(items.getConfigurationSection(key));

            Material material;
            try {
                material = Material.valueOf(item.getString("material"));
            } catch (IllegalArgumentException e) {
                LLibrary.getPluginLogger().error("There is no material named: " + item.getString("material") + ". Skipping this item...");
                continue;
            } catch (NullPointerException e) {
                LLibrary.getPluginLogger().error("Missing material for item: " + key + ". Skipping this item...");
                continue;
            }

            List<Integer> slots;
            if (item.getIntegerList("slot").isEmpty() && item.getInt("slot", -1) == -1) {
                LLibrary.getPluginLogger().error("Missing slot for item: " + key + ". Skipping this item...");
                continue;
            } else {
                if (item.getIntegerList("slot").isEmpty()) {
                    slots = Collections.singletonList(item.getInt("slot"));
                } else {
                    slots = new ArrayList<>(item.getIntegerList("slot"));
                }
            }

            List<Requirement> requirements;
            if (item.getConfigurationSection("requirements") == null) {
                requirements = new ArrayList<>();
            } else {
                requirements = new RequirementLoader().getRequirements(item.getConfigurationSection("requirements"));
            }

            String displayName = item.getString("display-name", "");
            List<String> lores = new ArrayList<>();
            Optional.ofNullable(item.get("lore")).ifPresent(lore -> {
                if (lore instanceof String) lores.add((String) lore);
                else lores.addAll(item.getStringList("lore"));
            });

            InventoryButton button = new InventoryButton()
                    .id(key)
                    .priority(item.getInt("priority", 0))
                    .viewRequirements(requirements)
                    .creator(player -> {
                        ItemBuilder itemBuilder = new ItemBuilder(material);
                        if (displayName.isBlank() && lores.isEmpty()) itemBuilder.setHideTooltip(true);
                        else {
                            itemBuilder.setDisplayName(displayName);
                            if (!lores.isEmpty()) itemBuilder.setLores(lores);
                        }
                        return itemBuilder.build();
                    })
                    .consumer(event -> {
                        if (key.equals("close")) event.getWhoClicked().closeInventory();
                    });
            slots.forEach(slot -> addButton(slot, button));
        }
    }
}
