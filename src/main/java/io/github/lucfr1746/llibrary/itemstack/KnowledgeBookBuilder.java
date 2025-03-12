package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.KnowledgeBookMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The {@code KnowledgeBookBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link KnowledgeBookMeta} of an {@link ItemStack}.
 * This class allows adding, setting, and retrieving recipes for the knowledge book.
 */
public class KnowledgeBookBuilder extends ItemBuilder {

    private final KnowledgeBookMeta knowledgeBookMeta;

    /**
     * Constructs a {@code KnowledgeBookBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link KnowledgeBookMeta}
     */
    public KnowledgeBookBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof KnowledgeBookMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of KnowledgeBookMeta");
        }
        this.knowledgeBookMeta = meta;
    }

    /**
     * Constructs a {@code KnowledgeBookBuilder} with the specified amount.
     *
     * @param amount the quantity of items in the stack
     */
    public KnowledgeBookBuilder(int amount) {
        super(Material.KNOWLEDGE_BOOK, amount);
        if (!(getItemMeta() instanceof KnowledgeBookMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of KnowledgeBookMeta");
        }
        this.knowledgeBookMeta = meta;
    }

    /**
     * Adds recipes to the knowledge book.
     *
     * @param recipes the recipes to add
     * @return this {@code KnowledgeBookBuilder} instance for method chaining
     */
    public KnowledgeBookBuilder addRecipes(@NotNull NamespacedKey... recipes) {
        knowledgeBookMeta.addRecipe(recipes);
        return this;
    }

    /**
     * Sets the recipes for the knowledge book, replacing any existing ones.
     *
     * @param recipes the list of recipes to set
     * @return this {@code KnowledgeBookBuilder} instance for method chaining
     */
    public KnowledgeBookBuilder setRecipes(@NotNull List<NamespacedKey> recipes) {
        knowledgeBookMeta.setRecipes(recipes);
        return this;
    }

    /**
     * Gets all recipes in the knowledge book.
     *
     * @return the list of recipes in the book
     */
    public List<NamespacedKey> getRecipes() {
        return knowledgeBookMeta.getRecipes();
    }

    /**
     * Checks if the knowledge book contains any recipes.
     *
     * @return true if there are recipes, false otherwise
     */
    public boolean hasRecipes() {
        return knowledgeBookMeta.hasRecipes();
    }
}