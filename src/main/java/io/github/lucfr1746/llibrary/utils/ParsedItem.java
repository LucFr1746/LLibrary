package io.github.lucfr1746.llibrary.utils;

import io.github.lucfr1746.llibrary.LLibrary;
import io.github.lucfr1746.llibrary.utils.APIs.LoggerAPI;
import org.bukkit.Bukkit;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code ParsedItem} class parses and represents custom item metadata from an {@link ItemStack}.
 * <p>
 * This class is responsible for reading and writing custom component data embedded in the item meta,
 * using a custom format.
 * It provides methods to load, read, and modify values in a structured manner.
 * </p>
 */
public class ParsedItem {

    private final String type;
    private final Map<String, Object> components = new LinkedHashMap<>();
    private int amount;

    /**
     * Constructs a {@code ParsedItem} from an {@link ItemStack}.
     *
     * @param itemStack the item stack to parse.
     * @throws NullPointerException if the itemStack or its meta is null.
     */
    public ParsedItem(ItemStack itemStack) {
        this(itemStack.getType(), Objects.requireNonNull(itemStack.getItemMeta()));
        amount = itemStack.getAmount();
    }

    /**
     * Private constructor that initializes a {@code ParsedItem} with a material and its meta.
     *
     * @param mat  the material of the item.
     * @param meta the item meta.
     */
    private ParsedItem(Material mat, ItemMeta meta) {
        this(mat, meta.getAsString());
    }

    /**
     * Private constructor that parses a raw string representation of the item meta.
     *
     * @param mat the material of the item.
     * @param raw the raw string from the item meta.
     */
    private ParsedItem(Material mat, String raw) {
        int index = raw.indexOf("{");
        if (index == -1) {
            type = mat.getKey().toString();
            return;
        }
        type = mat.getKey().toString();
        raw = raw.substring(index);
        components.putAll((Map<String, Object>) eatMap(raw, 0, 0).payload);
    }

    /**
     * Loads or creates a map at the specified path.
     *
     * @param data the data map.
     * @param path the key to retrieve.
     * @return the map at the given path.
     */
    public static Map<String, Object> loadMap(Map<String, Object> data, String path) {
        data.putIfAbsent(path, new LinkedHashMap<>());
        return (Map<String, Object>) data.get(path);
    }

    /**
     * Loads or creates a list at the specified path.
     *
     * @param data the data map.
     * @param path the key to retrieve.
     * @return the list at the given path.
     */
    public static List<Object> loadList(Map<String, Object> data, String path) {
        data.putIfAbsent(path, new ArrayList<>());
        return (List<Object>) data.get(path);
    }

    /**
     * Retrieves a map from the data at the specified path.
     *
     * @param data the data map.
     * @param path the key to retrieve.
     * @return the map if present, otherwise {@code null}.
     */
    public static Map<String, Object> getMap(Map<String, Object> data, String path) {
        return (Map<String, Object>) data.getOrDefault(path, null);
    }

    /**
     * Retrieves a list of raw objects from the data at the specified path.
     *
     * @param data the data map.
     * @param path the key to retrieve.
     * @return the list if present, otherwise {@code null}.
     */
    public static List<Object> getListOfRaw(Map<String, Object> data, String path) {
        return (List<Object>) data.getOrDefault(path, null);
    }

    /**
     * Retrieves a list of maps from the data at the specified path.
     *
     * @param data the data map.
     * @param path the key to retrieve.
     * @return the list of maps if present, otherwise {@code null}.
     */
    public static List<Map<String, Object>> getListOfMap(Map<String, Object> data, String path) {
        return (List<Map<String, Object>>) data.getOrDefault(path, null);
    }

    /**
     * Loads or creates a list of raw objects at the specified path.
     *
     * @param data the data map.
     * @param path the key to retrieve.
     * @return the list of objects.
     */
    public static List<Object> loadListOfRaw(Map<String, Object> data, String path) {
        data.putIfAbsent(path, new ArrayList<>());
        return (List<Object>) data.get(path);
    }

    /**
     * Loads or creates a list of maps at the specified path.
     *
     * @param data the data map.
     * @param path the key to retrieve.
     * @return the list of maps.
     */
    public static List<Map<String, Object>> loadListOfMap(Map<String, Object> data, String path) {
        data.putIfAbsent(path, new ArrayList<>());
        return (List<Map<String, Object>>) data.get(path);
    }

    /**
     * Reads a {@link NamespacedKey} from the data at the specified path.
     *
     * @param data     the data map.
     * @param path     the key to retrieve.
     * @param defValue the default value if the key is not found.
     * @return the {@link NamespacedKey} or the default value.
     */
    public static NamespacedKey readNamespacedKey(Map<String, Object> data, String path, NamespacedKey defValue) {
        String text = readString(data, path, null);
        if (text == null)
            return defValue;
        return new NamespacedKey(text.split(":")[0], text.split(":")[1]);
    }

    /**
     * Reads a {@link NamespacedKey} from the data at the specified path.
     *
     * @param data the data map.
     * @param path the key to retrieve.
     * @return the {@link NamespacedKey} or {@code null} if not found.
     */
    public static NamespacedKey readNamespacedKey(Map<String, Object> data, String path) {
        return readNamespacedKey(data, path, null);
    }

    /**
     * Reads a string from the data at the specified path.
     *
     * @param data     the data map.
     * @param path     the key to retrieve.
     * @param defValue the default value if the key is not found.
     * @return the string value or the default value.
     */
    public static String readString(Map<String, Object> data, String path, String defValue) {
        if (!data.containsKey(path))
            return defValue;
        return (String) data.get(path);
    }

    /**
     * Reads a string from the data at the specified path.
     *
     * @param data the data map.
     * @param path the key to retrieve.
     * @return the string value or {@code null} if not found.
     */
    public static String readString(Map<String, Object> data, String path) {
        return readString(data, path, null);
    }

    /**
     * Reads a boolean value from the data at the specified path.
     *
     * @param data     the data map.
     * @param path     the key to retrieve.
     * @param defValue the default value if the key is not found.
     * @return the boolean value.
     */
    public static Boolean readBoolean(Map<String, Object> data, String path, Boolean defValue) {
        Integer value = readInt(data, path);
        return value == null ? defValue : value != 0;
    }

    /**
     * Reads a boolean value from the data at the specified path.
     *
     * @param data the data map.
     * @param path the key to retrieve.
     * @return the boolean value or {@code null} if not found.
     */
    public static Boolean readBoolean(Map<String, Object> data, String path) {
        return readBoolean(data, path, null);
    }

    /**
     * Reads an integer value from the data at the specified path.
     *
     * @param data the data map.
     * @param path the key to retrieve.
     * @return the integer value or {@code null} if not found.
     */
    public static Integer readInt(Map<String, Object> data, String path) {
        return readInt(data, path, null);
    }

    /**
     * Reads an integer value from the data at the specified path.
     *
     * @param data     the data map.
     * @param path     the key to retrieve.
     * @param defValue the default value if the key is not found.
     * @return the integer value.
     */
    public static Integer readInt(Map<String, Object> data, String path, Integer defValue) {
        if (!data.containsKey(path))
            return defValue;
        String value = (String) data.get(path);
        if (value.endsWith("b"))
            value = value.substring(0, value.length() - 1);
        return Integer.parseInt(value);
    }

    /**
     * Reads a double value from the data at the specified path.
     *
     * @param data the data map.
     * @param path the key to retrieve.
     * @return the double value or {@code null} if not found.
     */
    public static Double readDouble(Map<String, Object> data, String path) {
        return readDouble(data, path, null);
    }

    /**
     * Reads a double value from the data at the specified path.
     *
     * @param data     the data map.
     * @param path     the key to retrieve.
     * @param defValue the default value if the key is not found.
     * @return the double value.
     */
    public static Double readDouble(Map<String, Object> data, String path, Double defValue) {
        if (!data.containsKey(path))
            return defValue;
        String value = (String) data.get(path);
        if (value.endsWith("f"))
            value = value.substring(0, value.length() - 1);
        return Double.parseDouble(value);
    }

    /**
     * Reads a float value from the data at the specified path.
     *
     * @param data the data map.
     * @param path the key to retrieve.
     * @return the float value or {@code null} if not found.
     */
    public static Float readFloat(Map<String, Object> data, String path) {
        return readFloat(data, path, null);
    }

    /**
     * Reads a float value from the data at the specified path.
     *
     * @param data     the data map.
     * @param path     the key to retrieve.
     * @param defValue the default value if the key is not found.
     * @return the float value.
     */
    public static Float readFloat(Map<String, Object> data, String path, Float defValue) {
        Double value = readDouble(data, path, null);
        return value == null ? defValue : value.floatValue();
    }

    /**
     * Sets a string value in the data at the specified path.
     *
     * @param data  the data map.
     * @param path  the key to set.
     * @param value the string value.
     */
    public static void setValue(Map<String, Object> data, String path, String value) {
        if (value == null)
            data.remove(path);
        else if (!(value.startsWith("\"") && value.endsWith("\"")))
            data.put(path, "\"" + value + "\"");
        else
            data.put(path, value);
    }

    /**
     * Sets a boolean value in the data at the specified path.
     *
     * @param data  the data map.
     * @param path  the key to set.
     * @param value the boolean value.
     */
    public static void setValue(Map<String, Object> data, String path, Boolean value) {
        if (value == null)
            data.remove(path);
        else
            data.put(path, String.valueOf(value ? 1 : 0));
    }

    /**
     * Sets an integer value in the data at the specified path.
     *
     * @param data  the data map.
     * @param path  the key to set.
     * @param value the integer value.
     */
    public static void setValue(Map<String, Object> data, String path, Integer value) {
        if (value == null)
            data.remove(path);
        else
            data.put(path, value.toString());
    }

    /**
     * Sets a double value in the data at the specified path.
     *
     * @param data  the data map.
     * @param path  the key to set.
     * @param value the double value.
     */
    public static void setValue(Map<String, Object> data, String path, Double value) {
        if (value == null)
            data.remove(path);
        else
            data.put(path, value.toString());
    }

    /**
     * Sets a float value in the data at the specified path.
     *
     * @param data  the data map.
     * @param path  the key to set.
     * @param value the float value.
     */
    public static void setValue(Map<String, Object> data, String path, Float value) {
        if (value == null)
            data.remove(path);
        else
            data.put(path, value.toString());
    }

    /**
     * Sets a {@link NamespacedKey} in the data at the specified path.
     *
     * @param data  the data map.
     * @param path  the key to set.
     * @param value the {@link NamespacedKey} value.
     */
    public static void setValue(Map<String, Object> data, String path, NamespacedKey value) {
        if (value == null)
            data.remove(path);
        else
            data.put(path, value.toString());
    }

    /**
     * Sets a {@link Keyed} value in the data at the specified path.
     *
     * @param data  the data map.
     * @param path  the key to set.
     * @param value the {@link Keyed} value.
     */
    public static void setValue(Map<String, Object> data, String path, Keyed value) {
        if (value == null)
            data.remove(path);
        else
            data.put(path, value.getKey().toString());
    }

    /**
     * Sets a string value at the specified nested path within the components.
     *
     * @param value the string value to set.
     * @param paths the nested keys defining the path.
     */
    public void set(@Nullable String value, String... paths) {
        Map<String, Object> map = components;
        for (int i = 0; i < paths.length - 1; i++) {
            if (!(map.containsKey(paths[i]) && map.get(paths[i]) instanceof Map)) {
                if (value == null)
                    return;
                map.put(paths[i], new LinkedHashMap<String, Object>());
            }
            map = (Map<String, Object>) map.get(paths[i]);
        }
        if (value == null)
            map.remove(paths[paths.length - 1]);
        else
            map.put(paths[paths.length - 1], value);
    }

    /**
     * Sets a map value at the specified nested path within the components.
     *
     * @param value the map value to set.
     * @param paths the nested keys defining the path.
     */
    public void set(@NotNull Map<String, Object> value, String... paths) {
        Map<String, Object> map = components;
        for (int i = 0; i < paths.length - 1; i++) {
            if (!(map.containsKey(paths[i]) && map.get(paths[i]) instanceof Map))
                map.put(paths[i], new LinkedHashMap<String, Object>());
            map = (Map<String, Object>) map.get(paths[i]);
        }
        fixValue(value);
        map.put(paths[paths.length - 1], value);
    }

    /**
     * Removes a value at the specified nested path within the components.
     *
     * @param paths the nested keys defining the path.
     */
    public void remove(String... paths) {
        Map<String, Object> map = components;
        for (int i = 0; i < paths.length - 1; i++) {
            if (map.containsKey(paths[i]) && map.get(paths[i]) instanceof Map)
                map = (Map<String, Object>) map.get(paths[i]);
            else
                return;
        }
        map.remove(paths[paths.length - 1], new LinkedHashMap<>());
    }

    /**
     * Loads an empty map at the specified nested path if it does not exist.
     *
     * @param paths the nested keys defining the path.
     */
    public void loadEmptyMap(String... paths) {
        Map<String, Object> map = components;
        for (int i = 0; i < paths.length - 1; i++) {
            if ((!map.containsKey(paths[i]) && map.get(paths[i]) instanceof Map))
                map.put(paths[i], new LinkedHashMap<String, Object>());
            map = (Map<String, Object>) map.get(paths[i]);
        }
        if (!map.containsKey(paths[paths.length - 1]))
            map.put(paths[paths.length - 1], new LinkedHashMap<>());
    }

    /**
     * Sets a list of maps at the specified nested path within the components.
     *
     * @param value the list of maps to set.
     * @param paths the nested keys defining the path.
     */
    public void set(@NotNull List<Map<String, Object>> value, String... paths) {
        Map<String, Object> map = components;
        for (int i = 0; i < paths.length - 1; i++) {
            if (!(map.containsKey(paths[i]) && map.get(paths[i]) instanceof Map))
                map.put(paths[i], new LinkedHashMap<String, Object>());
            map = (Map<String, Object>) map.get(paths[i]);
        }
        fixValue(value);
        map.put(paths[paths.length - 1], value);
    }

    /**
     * Recursively fixes values in a map by converting numbers, keys, and booleans to string representations.
     *
     * @param value the map to fix.
     */
    private void fixValue(Map<String, Object> value) {
        value.forEach((k, v) -> {
            if (v instanceof Map) {
                fixValue((Map<String, Object>) v);
            } else if (v instanceof Number) {
                value.put(k, v.toString());
            } else if (v instanceof NamespacedKey) {
                value.put(k, v.toString());
            } else if (v instanceof Boolean) {
                value.put(k, ((boolean) v) ? "1b" : "0b");
            } else if (v instanceof List) {
                fixValue((List<Map<String, Object>>) v);
            }
        });
    }

    /**
     * Recursively fixes values in a list of maps.
     *
     * @param value the list of maps to fix.
     */
    private void fixValue(List<Map<String, Object>> value) {
        value.forEach(this::fixValue);
    }

    /**
     * Loads a string value from the components. If not found, set the default value.
     *
     * @param defValue the default string value.
     * @param paths    the nested keys defining the path.
     * @return the loaded string value.
     */
    public String load(@Nullable String defValue, String... paths) {
        String raw = read(paths);
        if (raw == null && defValue != null) {
            set(defValue, paths);
            return defValue;
        }
        return raw;
    }

    /**
     * Loads a double value from the components. If not found, set the default value.
     *
     * @param defValue the default double value.
     * @param paths    the nested keys defining the path.
     * @return the loaded double value.
     */
    public Double load(Double defValue, String... paths) {
        Double raw = readDouble(null, paths);
        if (raw == null && defValue != null) {
            set(defValue, paths);
            return defValue;
        }
        return raw;
    }

    /**
     * Loads a float value from the components. If not found, set the default value.
     *
     * @param defValue the default float value.
     * @param paths    the nested keys defining the path.
     * @return the loaded float value.
     */
    public Float load(Float defValue, String... paths) {
        Float raw = readFloat(null, paths);
        if (raw == null && defValue != null) {
            set(defValue, paths);
            return defValue;
        }
        return raw;
    }

    /**
     * Loads a long value from the components. If not found, set the default value.
     *
     * @param defValue the default long value.
     * @param paths    the nested keys defining the path.
     * @return the loaded long value.
     */
    public Long load(Long defValue, String... paths) {
        Long raw = readLong(null, paths);
        if (raw == null && defValue != null) {
            set(defValue, paths);
            return defValue;
        }
        return raw;
    }

    /**
     * Loads an integer value from the components. If not found, set the default value.
     *
     * @param defValue the default integer value.
     * @param paths    the nested keys defining the path.
     * @return the loaded integer value.
     */
    public Integer load(Integer defValue, String... paths) {
        Integer raw = readInteger(null, paths);
        if (raw == null && defValue != null) {
            set(defValue, paths);
            return defValue;
        }
        return raw;
    }

    /**
     * Loads a byte value from the components. If not found, set the default value.
     *
     * @param defValue the default byte value.
     * @param paths    the nested keys defining the path.
     * @return the loaded byte value.
     */
    public Byte load(Byte defValue, String... paths) {
        Byte raw = readByte(null, paths);
        if (raw == null && defValue != null) {
            set(defValue, paths);
            return defValue;
        }
        return raw;
    }

    /**
     * Loads a boolean value from the components. If not found, set the default value.
     *
     * @param defValue the default boolean value.
     * @param paths    the nested keys defining the path.
     * @return the loaded boolean value.
     */
    public Boolean load(Boolean defValue, String... paths) {
        Boolean raw = readBoolean(null, paths);
        if (raw == null && defValue != null) {
            set(defValue, paths);
            return defValue;
        }
        return raw;
    }

    /**
     * Loads a {@link NamespacedKey} from the components. If not found, set the default value.
     *
     * @param defValue the default {@link NamespacedKey} value.
     * @param paths    the nested keys defining the path.
     * @return the loaded {@link NamespacedKey} value.
     */
    public NamespacedKey load(NamespacedKey defValue, String... paths) {
        NamespacedKey raw = readNamespacedKey(null, paths);
        if (raw == null && defValue != null) {
            set(defValue, paths);
            return defValue;
        }
        return raw;
    }

    /**
     * Sets a double value at the specified nested path.
     *
     * @param value the double value to set.
     * @param paths the nested keys defining the path.
     */
    public void set(Double value, String... paths) {
        set(value == null ? null : String.valueOf(value), paths);
    }

    /**
     * Sets a float value at the specified nested path.
     *
     * @param value the float value to set.
     * @param paths the nested keys defining the path.
     */
    public void set(Float value, String... paths) {
        set(value == null ? null : value + "f", paths);
    }

    /**
     * Sets a long value at the specified nested path.
     *
     * @param value the long value to set.
     * @param paths the nested keys defining the path.
     */
    public void set(Long value, String... paths) {
        set(value == null ? null : String.valueOf(value), paths);
    }

    /**
     * Sets an integer value at the specified nested path.
     *
     * @param value the integer value to set.
     * @param paths the nested keys defining the path.
     */
    public void set(Integer value, String... paths) {
        set(value == null ? null : String.valueOf(value), paths);
    }

    /**
     * Sets a byte value at the specified nested path.
     *
     * @param value the byte value to set.
     * @param paths the nested keys defining the path.
     */
    public void set(Byte value, String... paths) {
        set(value == null ? null : (value + "b"), paths);
    }

    /**
     * Sets a boolean value at the specified nested path.
     *
     * @param value the boolean value to set.
     * @param paths the nested keys defining the path.
     */
    public void set(Boolean value, String... paths) {
        set(value == null ? null : (value ? "1b" : "0b"), paths);
    }

    /**
     * Sets a {@link NamespacedKey} at the specified nested path.
     *
     * @param value the {@link NamespacedKey} to set.
     * @param paths the nested keys defining the path.
     */
    public void set(NamespacedKey value, String... paths) {
        set(value == null ? null : value.toString(), paths);
    }

    /**
     * Reads a value from the components at the specified nested path.
     *
     * @param paths the nested keys defining the path.
     * @return the string representation of the value or {@code null} if not found.
     */
    private String read(String[] paths) {
        Map<String, Object> map = components;
        for (int i = 0; i < paths.length - 1; i++) {
            if (map.containsKey(paths[i]) && map.get(paths[i]) instanceof Map)
                map = (Map<String, Object>) map.get(paths[i]);
            else
                return null;
        }
        return map.get(paths[paths.length - 1]) instanceof String ? (String) map.get(paths[paths.length - 1]) : null;
    }

    /**
     * Reads a map from the components at the specified nested path.
     *
     * @param paths the nested keys defining the path.
     * @return the map if present, otherwise {@code null}.
     */
    public Map<String, Object> readMap(String... paths) {
        Map<String, Object> map = components;
        for (int i = 0; i < paths.length - 1; i++) {
            if (map.containsKey(paths[i]) && map.get(paths[i]) instanceof Map)
                map = (Map<String, Object>) map.get(paths[i]);
            else
                return null;
        }
        return map.get(paths[paths.length - 1]) instanceof Map ? (Map<String, Object>) map.get(paths[paths.length - 1]) : null;
    }

    /**
     * Reads a list of maps from the components at the specified nested path.
     *
     * @param paths the nested keys defining the path.
     * @return the list of maps if present, otherwise {@code null}.
     */
    public List<Map<String, Object>> readList(String... paths) {
        Map<String, Object> map = components;
        for (int i = 0; i < paths.length - 1; i++) {
            if (map.containsKey(paths[i]) && map.get(paths[i]) instanceof Map)
                map = (Map<String, Object>) map.get(paths[i]);
            else
                return null;
        }
        return map.get(paths[paths.length - 1]) instanceof List ? (List<Map<String, Object>>) map.get(paths[paths.length - 1]) : null;
    }

    /**
     * Reads an integer value from the components at the specified nested path.
     *
     * @param defValue the default integer value.
     * @param paths    the nested keys defining the path.
     * @return the integer value.
     */
    public Integer readInteger(Integer defValue, String... paths) {
        String value = read(paths);
        if (value == null)
            return defValue;
        if (value.endsWith("b") || value.endsWith("f"))
            value = value.substring(0, value.length() - 1);
        return Integer.valueOf(value);
    }

    /**
     * Reads a string from the components at the specified nested path.
     *
     * @param defValue the default string value.
     * @param paths    the nested keys defining the path.
     * @return the string value.
     */
    public String readString(String defValue, String... paths) {
        String value = read(paths);
        if (value == null)
            return defValue;
        return value;
    }

    /**
     * Reads a double value from the components at the specified nested path.
     *
     * @param defValue the default double value.
     * @param paths    the nested keys defining the path.
     * @return the double value.
     */
    public Double readDouble(Double defValue, String... paths) {
        String value = read(paths);
        if (value == null)
            return defValue;
        if (value.endsWith("b") || value.endsWith("f"))
            value = value.substring(0, value.length() - 1);
        return Double.valueOf(value);
    }

    /**
     * Reads a float value from the components at the specified nested path.
     *
     * @param defValue the default float value.
     * @param paths    the nested keys defining the path.
     * @return the float value.
     */
    public Float readFloat(Float defValue, String... paths) {
        String value = read(paths);
        if (value == null)
            return defValue;
        if (value.endsWith("b") || value.endsWith("f"))
            value = value.substring(0, value.length() - 1);
        return Float.valueOf(value);
    }

    /**
     * Reads a long value from the components at the specified nested path.
     *
     * @param defValue the default long value.
     * @param paths    the nested keys defining the path.
     * @return the long value.
     */
    public Long readLong(Long defValue, String... paths) {
        String value = read(paths);
        if (value == null)
            return defValue;
        if (value.endsWith("b") || value.endsWith("f"))
            value = value.substring(0, value.length() - 1);
        return Long.valueOf(value);
    }

    /**
     * Reads a byte value from the components at the specified nested path.
     *
     * @param defValue the default byte value.
     * @param paths    the nested keys defining the path.
     * @return the byte value.
     */
    public Byte readByte(Byte defValue, String... paths) {
        String value = read(paths);
        if (value == null)
            return defValue;
        if (value.endsWith("b") || value.endsWith("f"))
            value = value.substring(0, value.length() - 1);
        return Byte.valueOf(value);
    }

    /**
     * Reads a {@link NamespacedKey} from the components at the specified nested path.
     *
     * @param defValue the default {@link NamespacedKey} value.
     * @param paths    the nested keys defining the path.
     * @return the {@link NamespacedKey} value.
     */
    public NamespacedKey readNamespacedKey(NamespacedKey defValue, String... paths) {
        String value = read(paths);
        if (value == null)
            return defValue;
        String[] split = value.split(":");
        if (split.length != 2)
            return defValue;
        return new NamespacedKey(split[0], split[1]);
    }

    /**
     * Reads a boolean value from the components at the specified nested path.
     *
     * @param defValue the default boolean value.
     * @param paths    the nested keys defining the path.
     * @return the boolean value.
     */
    public Boolean readBoolean(Boolean defValue, String... paths) {
        return readInteger((!defValue) ? 0 : 1, paths) != 0;
    }

    /**
     * Retrieves the underlying components map.
     *
     * @return the component map.
     */
    public Map<String, Object> getMap() {
        return components;
    }

    /**
     * Converts this {@code ParsedItem} back to an {@link ItemStack} using the Bukkit item factory.
     *
     * @return the resulting {@link ItemStack}.
     */
    public ItemStack toItemStack() {
        ItemStack item = Bukkit.getItemFactory().createItemStack(toString());
        item.setAmount(amount);
        return item;
    }

    /**
     * Converts this {@code ParsedItem} to an {@link ItemMeta} using the Bukkit item factory.
     *
     * @return the resulting {@link ItemMeta}.
     */
    public ItemMeta toItemMeta() {
        return Bukkit.getItemFactory().createItemStack(toString()).getItemMeta();
    }

    /**
     * Converts this {@code ParsedItem} to its string representation.
     *
     * @return the string representation.
     */
    public String toString() {
        StringBuilder text = new StringBuilder(type);
        if (components.isEmpty())
            return text.toString();
        text.append("[");
        components.forEach((key, value) -> text.append(key)
                .append("=").append(writeComponent(value)).append(","));
        return text.substring(0, text.length() - 1) + "]";
    }

    /**
     * Determines if the given text requires quoting.
     *
     * @param text the text to check.
     * @return {@code true} if brackets are needed, {@code false} otherwise.
     */
    private boolean needBrackets(String text) {
        Pattern pattern = Pattern.compile("^[-_.0-9a-zA-Z]+$");
        Matcher matcher = pattern.matcher(text);
        return !matcher.matches();
    }

    /**
     * Writes a component value to its string representation.
     *
     * @param value the value to write.
     * @return the string representation.
     */
    private String writeComponent(Object value) {
        if (value instanceof String text) {
            if (text.startsWith("'")) {
                return text;
            }
            if (needBrackets(text)) {
                return "\"" + text + "\"";
            }
            return text;
        }
        if (value instanceof List) {
            List<Object> list = (List<Object>) value;
            if (list.isEmpty())
                return "[]";
            StringBuilder text = new StringBuilder("[");
            list.forEach((el) -> text.append(writeComponent(el)).append(","));
            return text.substring(0, text.length() - 1) + "]";
        }
        if (value instanceof Map) {
            Map<String, Object> map = ((Map<String, Object>) value);
            if (map.isEmpty())
                return "{}";
            StringBuilder text = new StringBuilder("{");
            map.forEach((key, val) -> text.append(needBrackets(key) ? "\"" + key + "\"" : key)
                    .append(":").append(writeComponent(val)).append(","));
            return text.substring(0, text.length() - 1) + "}";
        }
        new LoggerAPI(LLibrary.getInstance()).info(value.getClass().getSimpleName() + " " + value);
        throw new RuntimeException();
    }

    /**
     * Parses a list from the raw string.
     *
     * @param raw   the raw string to parse.
     * @param depth the current depth of parsing.
     * @param index the current index in the string.
     * @return an {@code EatResult} containing the new index and the parsed list.
     */
    private EatResult eatList(String raw, int depth, int index) {
        List<Object> value = new ArrayList<>();
        index++;
        while (raw.charAt(index) != ']') {
            EatResult tmp = switch (raw.charAt(index)) {
                case '[' -> eatList(raw, depth + 1, index);
                case '{' -> eatMap(raw, depth + 1, index);
                case '"' -> eatString(raw, depth + 1, index);
                case '\'' -> eatTextComponent(raw, depth + 1, index);
                default -> eatRawValue(raw, 1, index);
            };
            value.add(tmp.payload);
            index = tmp.newIndex;
            if (raw.charAt(index) == ',')
                index++;
        }

        return new EatResult(index + 1, value);
    }

    /**
     * Parses a map from the raw string.
     *
     * @param raw   the raw string to parse.
     * @param depth the current depth of parsing.
     * @param index the current index in the string.
     * @return an {@code EatResult} containing the new index and the parsed map.
     */
    private EatResult eatMap(String raw, int depth, int index) {
        Map<String, Object> map = new LinkedHashMap<>();
        index++;
        while (raw.charAt(index) != '}') {
            String key;
            EatResult res;
            if (raw.charAt(index) == '\"') {
                res = eatString(raw, depth + 1, index);
            } else {
                res = eatRawValue(raw, depth + 1, index);
            }
            key = (String) res.payload;
            index = res.newIndex;
            index++; // skip the ':' or '='
            Object value;
            EatResult tmp = switch (raw.charAt(index)) {
                case '[' -> eatList(raw, depth + 1, index);
                case '{' -> eatMap(raw, depth + 1, index);
                case '"' -> eatString(raw, depth + 1, index);
                case '\'' -> eatTextComponent(raw, depth + 1, index);
                default -> eatRawValue(raw, 1, index);
            };
            value = tmp.payload;
            index = tmp.newIndex;
            map.put(key, value);
            if (raw.charAt(index) == ',') {
                index++;
            }
        }
        return new EatResult(index + 1, map);
    }

    /**
     * Parses a quoted string from the raw string.
     *
     * @param raw   the raw string to parse.
     * @param depth the current depth of parsing.
     * @param index the current index in the string.
     * @return an {@code EatResult} containing the new index and the parsed string.
     */
    private EatResult eatString(String raw, int depth, int index) {
        StringBuilder value = new StringBuilder();
        index++;
        while (raw.charAt(index) != '\"') {
            if (raw.charAt(index) == '\\') {
                value.append("\\");
                index++;
            }
            value.append(raw.charAt(index));
            index++;
        }
        return new EatResult(index + 1, value.toString());
    }

    /**
     * Parses a text component enclosed in single quotes from the raw string.
     *
     * @param raw   the raw string to parse.
     * @param depth the current depth of parsing.
     * @param index the current index in the string.
     * @return an {@code EatResult} containing the new index and the parsed text component.
     */
    private EatResult eatTextComponent(String raw, int depth, int index) {
        StringBuilder value = new StringBuilder("'");
        index++;
        while (raw.charAt(index) != '\'') {
            if (raw.charAt(index) == '\\') {
                value.append("\\");
                index++;
            }
            value.append(raw.charAt(index));
            index++;
        }
        value.append('\'');
        return new EatResult(index + 1, value.toString());
    }

    /**
     * Parses a raw value (number, unquoted string, etc.) from the raw string.
     *
     * @param raw   the raw string to parse.
     * @param depth the current depth of parsing.
     * @param index the current index in the string.
     * @return an {@code EatResult} containing the new index and the parsed raw value.
     */
    private EatResult eatRawValue(String raw, int depth, int index) {
        StringBuilder value = new StringBuilder();
        while (raw.charAt(index) != ',' && raw.charAt(index) != ']' && raw.charAt(index) != '}'
                && raw.charAt(index) != ':' && raw.charAt(index) != '=') {
            value.append(raw.charAt(index));
            index++;
        }
        return new EatResult(index, value.toString());
    }

    /**
     * Loads an empty list of maps at the specified nested path.
     *
     * @param paths the nested keys defining the path.
     * @return the empty list of maps.
     */
    public List<Map<String, Object>> loadEmptyList(String... paths) {
        Map<String, Object> map = components;
        for (int i = 0; i < paths.length - 1; i++) {
            if ((!map.containsKey(paths[i]) && map.get(paths[i]) instanceof Map))
                map.put(paths[i], new LinkedHashMap<String, Object>());
            map = (Map<String, Object>) map.get(paths[i]);
        }
        if (!map.containsKey(paths[paths.length - 1]))
            map.put(paths[paths.length - 1], new ArrayList<Map<String, Object>>());
        return (List<Map<String, Object>>) map.get(paths[paths.length - 1]);
    }

    /**
     * A helper record to represent the result of parsing a section of the raw string.
     *
     * @param newIndex the index in the raw string after parsing.
     * @param payload  the parsed object.
     */
    private record EatResult(int newIndex, Object payload) { }
}
