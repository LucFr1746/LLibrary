package io.github.lucfr1746.llibrary.inventory;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.action.Action;
import io.github.lucfr1746.llibrary.action.ActionLoader;
import io.github.lucfr1746.llibrary.requirement.Requirement;
import io.github.lucfr1746.llibrary.requirement.RequirementLoader;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
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

public class InventoryBuilder implements InventoryHandler {

    public enum LockMode {
        ALL,
        GUI,
        PLAYER,
        NONE
    }

    private String id = UUID.randomUUID().toString();

    private String title = "";

    private MenuType menuType = MenuType.GENERIC_9X3;

    private LockMode lockMode = LockMode.ALL;

    private InventoryView inventoryView;

    private final Map<Integer, TreeSet<InventoryButton>> buttonMap = new HashMap<>();

    private final List<Requirement> openRequirements = new ArrayList<>();

    private final List<Action> openActions = new ArrayList<>();

    private final List<String> openCommands = new ArrayList<>();

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
        if (!getOpenCommands().isEmpty()) {
            Bukkit.getScheduler().runTask(LLibrary.getInstance(), () -> {
                for (String cmd : getOpenCommands()) {
                    CommandAPI.unregister(cmd);
                }
            });
        }
        this.openCommands.clear();
        this.openCommands.addAll(openCommands);
        if (this.openCommands.isEmpty()) return;
        Bukkit.getScheduler().runTask(LLibrary.getInstance(),
                () -> new CommandAPICommand(openCommands.getFirst())
                        .withAliases(openCommands.stream().skip(1).toArray(String[]::new))
                        .executesPlayer((player, args) -> {
                            if (openRequirements.stream().allMatch(req -> req.evaluate(player))) {
                                new InventoryManager().openGUI(this, player);
                            } else {
                                openRequirements.stream().filter(req -> !req.evaluate(player))
                                        .forEach(req -> req.getDenyHandler().forEach(handler -> handler.execute(player)));
                            }
                        }).register());
    }

    public String getId() {
        return this.id;
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

    public @Nullable InventoryView getInventoryView() {
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

    public void decorate(Player player) {
        this.inventoryView = menuType.typed().create(player, getTitle());
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
                .ifPresent(button -> {
                    ItemStack icon = button.getIconCreator().apply(player);
                    this.inventoryView.getTopInventory().setItem(slot, icon);
                });
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

    public void loadFromFile(@Nullable FileConfiguration fileConfiguration) {
        if (fileConfiguration == null)
            throw new IllegalArgumentException("The file must no be null!");
        loadID(fileConfiguration);

        setOpenRequirements(new RequirementLoader().getRequirements(fileConfiguration.getConfigurationSection("open-requirement")));
        setOpenActions(new ActionLoader().getActions(fileConfiguration.getStringList("open-action")));

        loadOpenCommands(fileConfiguration);
        loadMenuProperties(fileConfiguration);
    }

    private void loadID(@NotNull FileConfiguration fileConfiguration) {
        String id = fileConfiguration.getString("menu-id");
        if (id == null) {
            LLibrary.getPluginLogger().error("Missing menu-id in " + fileConfiguration.getName() + ". Skipping...");
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
        loadItems();
    }

    private void loadItems() {

    }
}
