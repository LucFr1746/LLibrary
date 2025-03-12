package io.github.lucfr1746.llibrary.util.helper;

import io.github.lucfr1746.llibrary.LLibrary;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileAPI {

    private final Plugin plugin;
    private final Path pluginPath;

    private final boolean isLogger;

    public FileAPI(String pluginName, boolean enableLogger) {
        this.plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (this.plugin == null) {
            throw new IllegalArgumentException("No plugin found with name: " + pluginName);
        }
        this.pluginPath = plugin.getDataFolder().toPath();
        this.isLogger = enableLogger;
    }

    public FileAPI(Plugin plugin, boolean enableLogger) {
        this.plugin = plugin;
        this.pluginPath = plugin.getDataFolder().toPath();
        this.isLogger = enableLogger;
    }

    public FileAPI createFolder(String... folders) {
        Path folderPath = this.pluginPath.resolve(Paths.get("", folders));
        try {
            if (Files.notExists(folderPath)) {
                Files.createDirectories(folderPath);
                if (isLogger) LLibrary.getPluginLogger().info("Created folder: " + folderPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create folder at: " + folderPath + " - " + e.getMessage(), e);
        }
        return this;
    }

    public FileAPI createDefaultFile(String filename, String... folders) {
        Path filePath = this.pluginPath.resolve(Paths.get("", folders).resolve(filename));
        String resourcePath = String.join("/", folders) + "/" + filename;

        try (InputStream inputStream = this.plugin.getResource(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }

            Files.createDirectories(filePath.getParent());
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            if (isLogger) LLibrary.getPluginLogger().info("Created default file: " + filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create default file: " + filePath + " - " + e.getMessage(), e);
        }
        return this;
    }

    public FileAPI createFile(String filename, String... folders) {
        Path filePath = this.pluginPath.resolve(Paths.get("", folders).resolve(filename));
        try {
            Files.createDirectories(filePath.getParent());
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
                if (isLogger) LLibrary.getPluginLogger().info("Created file: " + filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create file: " + filePath + " - " + e.getMessage(), e);
        }
        return this;
    }

    public FileConfiguration getYAMLConfiguration(String filename, String... folders) {
        Path filePath = this.pluginPath.resolve(Paths.get("", folders).resolve(ensureYamlExtension(filename)));

        if (Files.notExists(filePath)) {
            throw new IllegalArgumentException("Configuration file not found: " + filePath);
        }

        return YamlConfiguration.loadConfiguration(filePath.toFile());
    }

    public FileConfiguration getOrCreateDefaultYAMLConfiguration(String filename, String... folders) {
        Path filePath = this.pluginPath.resolve(Paths.get("", folders).resolve(ensureYamlExtension(filename)));

        if (Files.notExists(filePath)) {
            createDefaultFile(filename, folders);
        }
        return getYAMLConfiguration(filename, folders);
    }

    public FileConfiguration getOrCreateYAMLConfiguration(String filename, String... folders) {
        Path filePath = this.pluginPath.resolve(Paths.get("", folders).resolve(ensureYamlExtension(filename)));

        if (Files.notExists(filePath)) {
            createFile(filename, folders);
        }
        return getYAMLConfiguration(filename, folders);
    }

    public List<File> getAllFiles(String... folders) {
        Path folderPath = this.pluginPath.resolve(Paths.get("", folders));
        if (!Files.exists(folderPath) || !Files.isDirectory(folderPath)) {
            throw new IllegalArgumentException("Folder not found: " + folderPath);
        }

        try (Stream<Path> paths = Files.list(folderPath)) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to list files in folder: " + folderPath + " - " + e.getMessage(), e);
        }
    }

    public FileConfiguration convertToYAMLConfiguration(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }

    private String ensureYamlExtension(String filename) {
        return filename.endsWith(".yml") ? filename : filename + ".yml";
    }
}