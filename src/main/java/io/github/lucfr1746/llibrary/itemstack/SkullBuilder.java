package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Base64;
import java.util.Objects;

/**
 * The {@code SkullBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link SkullMeta} of an {@link ItemStack}.
 * This includes setting player owners and custom textures.
 */
public class SkullBuilder extends ItemBuilder {

    private final SkullMeta skullMeta;

    /**
     * Constructs a {@code SkullBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item is not a PLAYER_HEAD
     */
    public SkullBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof SkullMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of SkullMeta");
        }
        this.skullMeta = meta;
    }

    /**
     * Constructs a {@code SkullBuilder} with a PLAYER_HEAD item.
     *
     * @param amount the quantity of items in the stack
     */
    public SkullBuilder(int amount) {
        super(Material.PLAYER_HEAD, amount);
        if (!(getItemMeta() instanceof SkullMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of SkullMeta");
        }
        this.skullMeta = meta;
    }

    /**
     * Sets the owner of the skull.
     *
     * @param player the offline player whose head will be used
     * @return this {@code SkullBuilder} instance for chaining
     */
    public SkullBuilder setOwner(@NotNull OfflinePlayer player) {
        skullMeta.setOwningPlayer(player);
        return this;
    }

    /**
     * Sets the custom texture of the skull using a Base64 string.
     *
     * @param base64Texture the Base64-encoded texture string
     * @return this {@code SkullBuilder} instance for chaining
     */
    public SkullBuilder setSkullTexture(@NotNull String base64Texture) {
        PlayerProfile profile = skullMeta.getOwnerProfile();
        if (profile == null) {
            profile = createPlayerProfile();
        }

        try {
            URL skinUrl = base64ToUrl(base64Texture);
            PlayerTextures textures = profile.getTextures();
            textures.setSkin(skinUrl);
            profile.setTextures(textures);
            skullMeta.setOwnerProfile(profile);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid Base64 texture format", e);
        }

        return this;
    }

    /**
     * Gets the custom texture of the skull as a Base64 string.
     *
     * @return the Base64-encoded texture string, or null if not set
     */
    @Nullable
    public String getSkullTexture() {
        PlayerProfile profile = skullMeta.getOwnerProfile();
        if (profile == null || profile.getTextures().getSkin() == null) {
            return null;
        }
        return urlToBase64(profile.getTextures().getSkin().toString());
    }

    /**
     * Removes the custom texture from the skull.
     *
     * @return this {@code SkullBuilder} instance for chaining
     */
    public SkullBuilder removeSkullTexture() {
        PlayerProfile profile = createPlayerProfile();
        skullMeta.setOwnerProfile(profile);
        return this;
    }

    /**
     * Creates a new {@link PlayerProfile} with a random UUID.
     *
     * @return a new player profile
     */
    private PlayerProfile createPlayerProfile() {
        return Objects.requireNonNull(skullMeta.getOwnerProfile(), "Failed to create PlayerProfile");
    }

    /**
     * Decodes a Base64 texture string into a skin URL.
     *
     * @param base64 the Base64-encoded texture string
     * @return the URL of the player's skin
     * @throws IOException if decoding fails or URL is invalid
     */
    private URL base64ToUrl(String base64) throws IOException {
        byte[] decodedBytes = Base64.getDecoder().decode(base64);
        String json = new String(decodedBytes);

        String urlString = json.split("\"url\":\"")[1].split("\"")[0];

        return URI.create(urlString).toURL();
    }

    /**
     * Converts a skin URL to a Base64 string.
     *
     * @param url the skin URL
     * @return the encoded Base64 string
     */
    private String urlToBase64(String url) {
        return Base64.getEncoder().encodeToString(url.getBytes());
    }
}