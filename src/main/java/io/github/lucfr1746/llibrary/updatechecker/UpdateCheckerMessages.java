package io.github.lucfr1746.llibrary.updatechecker;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

class UpdateCheckerMessages {

    @NotNull
    private static TextComponent createLink(@NotNull final String text, @NotNull final String link) {
        final ComponentBuilder lore = new ComponentBuilder("Link: ")
                .bold(true)
                .append(link)
                .bold(false);
        final TextComponent component = new TextComponent(text);
        component.setBold(true);
        component.setColor(net.md_5.bungee.api.ChatColor.GOLD);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
        //noinspection deprecation
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, lore.create()));
        return component;
    }

    protected static void printCheckResultToConsole(UpdateCheckEvent event) {

        final UpdateChecker instance = event.getChecker();
        final Plugin plugin = instance.getPlugin();

        if (event.getSuccess() == UpdateCheckSuccess.FAIL || event.getResult() == UpdateCheckResult.UNKNOWN) {
            plugin.getLogger().warning("Could not check for updates.");
            return;
        }

        if (event.getResult() == UpdateCheckResult.RUNNING_LATEST_VERSION) {
            if (instance.isSuppressUpToDateMessage()) return;
            plugin.getLogger().info(String.format("You are using the latest version of %s.", plugin.getName()));
            return;
        }

        List<String> lines = new ArrayList<>();

        lines.add(String.format("There is a new version of %s available!", plugin.getName()));
        lines.add(" ");
        lines.add(String.format("Your version:   %s%s", instance.isColoredConsoleOutput() ? ChatColor.RED : "", event.getUsedVersion()));
        lines.add(String.format("Latest version: %s%s", instance.isColoredConsoleOutput() ? ChatColor.GREEN : "", event.getLatestVersion()));

        List<String> downloadLinks = instance.getAppropriateDownloadLinks();

        if (!downloadLinks.isEmpty()) {
            lines.add(" ");
            lines.add("Please update to the newest version.");
            lines.add(" ");
            if (downloadLinks.size() == 1) {
                lines.add("Download:");
                lines.add("  " + downloadLinks.getFirst());
            } else if (downloadLinks.size() == 2) {
                lines.add(String.format("Download (%s)", instance.getNamePaidVersion()));
                lines.add("  " + downloadLinks.getFirst());
                lines.add(" ");
                lines.add(String.format("Download (%s)", instance.getNameFreeVersion()));
                lines.add("  " + downloadLinks.get(1));
            }
        }

        if(instance.getSupportLink() != null) {
            lines.add(" ");
            lines.add("Support:");
            lines.add("  " + instance.getSupportLink());
        }

        if(instance.getDonationLink() != null) {
            lines.add(" ");
            lines.add("Donate:");
            lines.add("  " + instance.getDonationLink());
        }

        printNiceBoxToConsole(plugin.getLogger(), lines);
    }

    protected static void printCheckResultToPlayer(Player player, boolean showMessageWhenLatestVersion, UpdateChecker checker) {
        if (checker.getLastCheckResult() == UpdateCheckResult.NEW_VERSION_AVAILABLE) {
            player.sendMessage(ChatColor.GRAY + "There is a new version of " + ChatColor.GOLD + checker.getPlugin().getName() + ChatColor.GRAY + " available.");
            sendLinks(checker, player);
            player.sendMessage(ChatColor.DARK_GRAY + "Latest version: " + ChatColor.GREEN + checker.getLatestVersion() + ChatColor.DARK_GRAY + " | Your version: " + ChatColor.RED + checker.getUsedVersion());
            player.sendMessage("");
        } else if (checker.getLastCheckResult() == UpdateCheckResult.UNKNOWN) {
            player.sendMessage(ChatColor.GOLD + checker.getPlugin().getName() + ChatColor.RED + " could not check for updates.");
        } else {
            if (showMessageWhenLatestVersion) {
                player.sendMessage(ChatColor.GREEN + "You are running the latest version of " + ChatColor.GOLD + checker.getPlugin().getName());
            }
        }
    }

    private static void printNiceBoxToConsole(Logger logger, List<String> lines) {
        int longestLine = 0;
        for (String line : lines) {
            longestLine = Math.max(line.length(), longestLine);
        }
        longestLine += 2;
        if (longestLine > 120) longestLine = 120;
        longestLine += 2;
        StringBuilder dash = new StringBuilder(longestLine);
        Stream.generate(() -> "*").limit(longestLine).forEach(dash::append);

        logger.log(Level.WARNING, dash.toString());
        for (String line : lines) {
            logger.log(Level.WARNING, ("*" + " ") + line);
        }
        logger.log(Level.WARNING, dash.toString());
    }

    private static void sendLinks(UpdateChecker checker, @NotNull final Player... players) {

        List<TextComponent> links = new ArrayList<>();

        List<String> downloadLinks = checker.getAppropriateDownloadLinks();

        if (downloadLinks.size() == 2) {
            links.add(createLink(String.format("Download (%s)", checker.getNamePaidVersion()), downloadLinks.get(0)));
            links.add(createLink(String.format("Download (%s)", checker.getNameFreeVersion()), downloadLinks.get(1)));
        } else if (downloadLinks.size() == 1) {
            links.add(createLink("Download", downloadLinks.getFirst()));
        }
        if (checker.getDonationLink() != null) {
            links.add(createLink("Donate", checker.getDonationLink()));
        }
        if (checker.getChangelogLink() != null) {
            links.add(createLink("Changelog", checker.getChangelogLink()));
        }
        if (checker.getSupportLink() != null) {
            links.add(createLink("Support", checker.getSupportLink()));
        }

        final TextComponent placeholder = new TextComponent(" | ");
        placeholder.setColor(net.md_5.bungee.api.ChatColor.GRAY);

        TextComponent text = new TextComponent("");

        Iterator<TextComponent> iterator = links.iterator();
        while (iterator.hasNext()) {
            TextComponent next = iterator.next();
            text.addExtra(next);
            if (iterator.hasNext()) {
                text.addExtra(placeholder);
            }
        }

        for (Player player : players) {
            player.spigot().sendMessage(text);
        }
    }
}
