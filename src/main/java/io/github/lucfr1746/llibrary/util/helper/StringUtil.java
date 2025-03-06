package io.github.lucfr1746.llibrary.util.helper;

import io.github.lucfr1746.llibrary.LLibrary;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;

public class StringUtil {

    @Contract("!null, _, _ -> !null")
    public static @Nullable String format(@Nullable String text, @Nullable Player player, String... holders) {
        if (text == null) return null;

        if (holders != null && holders.length % 2 != 0)
            throw new IllegalArgumentException("holder without replacer");
        if (holders != null && holders.length > 0)
            for (int i = 0; i < holders.length; i += 2)
                text = text.replace(holders[i], holders[i + 1]);

        if (player != null && LLibrary.isPlaceholderAPIEnabled())
            text = PlaceholderAPI.setPlaceholders(player, text);

        text = LegacyComponentSerializer
                .legacySection()
                .toBuilder()
                .hexColors()
                .useUnusualXRepeatedCharacterHexFormat()
                .build()
                .serialize(MiniMessage.miniMessage().deserialize(text.replace("§", "&")));
        text = ChatColor.translateAlternateColorCodes('&', text);

        return text;
    }

    @Contract("!null -> !null")
    public static @Nullable String revertColors(@Nullable String text) {
        if (text == null)
            return null;
        return text.replace("§", "&");
    }

    @Contract("!null -> !null")
    public static @Nullable String clearColors(@Nullable String text) {
        if (text == null)
            return null;
        return ChatColor.stripColor(text);
    }

    public static @NotNull String formatNumber(double value, int decimals, boolean optional) {
        DecimalFormat df = new DecimalFormat("0");
        df.setMaximumFractionDigits(decimals);
        df.setMinimumFractionDigits(optional ? 0 : decimals);
        return df.format(value);
    }
}
