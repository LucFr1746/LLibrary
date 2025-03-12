package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code BookMetaBuilder} class extends {@link WritableBookBuilder} to provide
 * additional functionality for modifying {@link BookMeta} of an {@link ItemStack}.
 * This class allows setting the title, author, and generation of a written book.
 */
public class BookBuilder extends WritableBookBuilder {

    private final BookMeta bookMeta;

    /**
     * Constructs a {@code BookMetaBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link BookMeta}
     */
    public BookBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        if (!(getItemMeta() instanceof BookMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of BookMeta");
        }
        this.bookMeta = meta;
    }

    /**
     * Constructs a {@code BookMetaBuilder} with the specified {@link Material}.
     *
     * @param material the material for the item stack
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link BookMeta}
     */
    public BookBuilder(@NotNull Material material) {
        super(material);
        if (!(getItemMeta() instanceof BookMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of BookMeta");
        }
        this.bookMeta = meta;
    }

    /**
     * Sets the title of the book.
     *
     * @param title the title to set
     * @return true if the title was successfully set, false otherwise
     */
    public boolean setTitle(@NotNull String title) {
        return bookMeta.setTitle(title);
    }

    /**
     * Gets the title of the book.
     *
     * @return the title of the book, or null if none is set
     */
    public String getTitle() {
        return bookMeta.getTitle();
    }

    /**
     * Checks if the book has a title.
     *
     * @return true if the book has a title, false otherwise
     */
    public boolean hasTitle() {
        return bookMeta.hasTitle();
    }

    /**
     * Sets the author of the book.
     *
     * @param author the author to set
     * @return this {@code BookMetaBuilder} instance for method chaining
     */
    public BookBuilder setAuthor(@NotNull String author) {
        bookMeta.setAuthor(author);
        return this;
    }

    /**
     * Gets the author of the book.
     *
     * @return the author of the book, or null if none is set
     */
    public String getAuthor() {
        return bookMeta.getAuthor();
    }

    /**
     * Checks if the book has an author.
     *
     * @return true if the book has an author, false otherwise
     */
    public boolean hasAuthor() {
        return bookMeta.hasAuthor();
    }

    /**
     * Sets the generation of the book.
     *
     * @param generation the generation to set
     * @return this {@code BookMetaBuilder} instance for method chaining
     */
    public BookBuilder setGeneration(@NotNull BookMeta.Generation generation) {
        bookMeta.setGeneration(generation);
        return this;
    }

    /**
     * Gets the generation of the book.
     *
     * @return the generation of the book, or null if none is set
     */
    public BookMeta.Generation getGeneration() {
        return bookMeta.getGeneration();
    }

    /**
     * Checks if the book has a generation set.
     *
     * @return true if the book has a generation, false otherwise
     */
    public boolean hasGeneration() {
        return bookMeta.hasGeneration();
    }
}