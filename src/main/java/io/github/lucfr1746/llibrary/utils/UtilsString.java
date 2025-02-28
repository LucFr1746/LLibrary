package io.github.lucfr1746.llibrary.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling strings,
 * particularly for manipulating item names and lore in Minecraft with support for PlaceholderAPI and color codes.
 */
public class UtilsString {

    /**
     * Private constructor to prevent instantiation.
     */
    UtilsString() {
        throw new UnsupportedOperationException();
    }

    /**
     * Updates the display name and lore of an ItemStack.
     *
     * @param item    The item to update.
     * @param desc    The raw text for display name and lore.
     * @param p       The player for PlaceholderAPI, or null if not used.
     * @param color   Whether to translate color codes.
     * @param holders Additional placeholders for text replacement must be in "toReplace", "replacement" pairs.
     */
    public static void updateDescription(@Nullable ItemStack item, @Nullable List<String> desc, @Nullable Player p, boolean color,
                                         String... holders) {
        if (item == null)
            return;

        String title;
        ArrayList<String> lore;
        if (desc == null || desc.isEmpty()) {
            title = " ";
            lore = null;
        } else if (desc.size() == 1) {
            title = desc.getFirst() != null ? (desc.getFirst().startsWith(ChatColor.RESET + "") ? desc.getFirst() : ChatColor.RESET + desc.getFirst()) : null;
            lore = null;
        } else {
            title = desc.getFirst().startsWith(ChatColor.RESET + "") ? desc.getFirst() : ChatColor.RESET + desc.getFirst();
            lore = new ArrayList<>();
            for (int i = 1; i < desc.size(); i++)
                lore.add(desc.get(i) != null ? (desc.get(i).startsWith(ChatColor.RESET + "") ? desc.get(i) : ChatColor.RESET + desc.get(i)) : "");
        }

        title = fix(title, p, color, holders);
        lore = fix(lore, p, color, holders);

        ItemMeta meta = item.getItemMeta();
        if (meta ==  null) return;
        meta.setDisplayName(title);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    /**
     * Processes a list of strings by applying placeholders, MiniMessage formatting, and color codes.
     *
     * @param list    The raw text list.
     * @param player  The player for PlaceholderAPI, or null if not used.
     * @param color   Whether to translate color codes.
     * @param holders Additional placeholders for text replacement.
     * @return A new list with formatted text, or null if the input list was null.
     */
    @Contract("!null, _, _, _ -> !null")
    public static @Nullable ArrayList<String> fix(@Nullable List<String> list, @Nullable Player player, boolean color, String... holders) {
        if (list == null)
            return null;
        ArrayList<String> newList = new ArrayList<>();
        for (String line : list)
            newList.add(fix(line, player, color, holders));
        return newList;
    }

    /**
     * Creates a clone of an ItemStack and updates its description.
     *
     * @param item        The item to clone and update.
     * @param description The raw text for display name and lore.
     * @param player      The player for PlaceholderAPI, or null if not used.
     * @param color       Whether to translate color codes.
     * @param holders     Additional placeholders for text replacement.
     * @return A new ItemStack with the updated display name and lore, or null if the input item is null or air.
     */
    public static ItemStack setDescription(@Nullable ItemStack item, @Nullable List<String> description, @Nullable Player player, boolean color,
                                           String... holders) {
        if (item == null || item.getType() == Material.AIR)
            return null;

        ItemStack itemCopy = new ItemStack(item);
        updateDescription(itemCopy, description, player, color, holders);
        return itemCopy;
    }

    /**
     * Processes a single string by applying placeholders, MiniMessage formatting, and color codes.
     *
     * @param text    The raw text.
     * @param player  The player for PlaceholderAPI, or null if not used.
     * @param color   Whether to translate color codes.
     * @param holders Additional placeholders for text replacement.
     * @return The formatted text.
     */
    @Contract("!null, _, _, _ -> !null")
    public static String fix(@Nullable String text, @Nullable Player player, boolean color, String... holders) {
        if (text == null)
            return null;

        if (holders != null && holders.length % 2 != 0)
            throw new IllegalArgumentException("holder without replacer");
        if (holders != null && holders.length > 0)
            for (int i = 0; i < holders.length; i += 2)
                text = text.replace(holders[i], holders[i + 1]);

        if (player != null && Hooks.isPAPIEnabled())
            text = PlaceholderAPI.setPlaceholders(player, text);

        text = MiniMessage.miniMessage().deserialize(text).toString();

        if (color)
            text = ChatColor.translateAlternateColorCodes('&', text);

        return text;
    }

    /**
     * Reverts color codes.
     *
     * @param text The text to process.
     * @return The text with color codes reverted.
     */
    @Contract("!null -> !null")
    public static @Nullable String revertColors(@Nullable String text) {
        if (text == null)
            return null;
        return text.replace("§", "&");
    }

    /**
     * Removes all color codes and formatting from the text.
     *
     * @param text The text to clear.
     * @return The plain text without colors or formatting.
     */
    @Contract("!null -> !null")
    public static @Nullable String clearColors(@Nullable String text) {
        if (text == null)
            return null;
        return ChatColor.stripColor(text);
    }

    /**
     * Formats a number to a string with a given number of decimal places.
     *
     * @param value    The number to format.
     * @param decimals The number of decimal places.
     * @param optional Whether trailing zeros should be removed.
     * @return The formatted number as a string.
     */
    public static @NotNull String formatNumber(double value, int decimals, boolean optional) {
        DecimalFormat df = new DecimalFormat("0");
        df.setMaximumFractionDigits(decimals);
        df.setMinimumFractionDigits(optional ? 0 : decimals);
        return df.format(value);
    }
}
