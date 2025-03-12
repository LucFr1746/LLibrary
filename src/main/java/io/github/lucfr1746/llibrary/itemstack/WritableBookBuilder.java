package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.WritableBookMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The {@code WritableBookBuilder} class extends {@link ItemBuilder} to provide
 * additional functionality for modifying {@link WritableBookMeta} of an {@link ItemStack}.
 * This class allows adding, setting, and retrieving pages in writable books.
 */
public class WritableBookBuilder extends ItemBuilder {

    private final WritableBookMeta bookMeta;

    /**
     * Constructs a {@code WritableBookBuilder} with a new {@link Material#WRITABLE_BOOK}.
     */
    public WritableBookBuilder() {
        super(Material.WRITABLE_BOOK);
        this.bookMeta = validateWritableBookMeta();
    }

    /**
     * Constructs a {@code WritableBookBuilder} using an existing {@link ItemStack}.
     *
     * @param itemStack the item stack to modify
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link WritableBookMeta}
     */
    public WritableBookBuilder(@NotNull ItemStack itemStack) {
        super(itemStack);
        this.bookMeta = validateWritableBookMeta();
    }

    /**
     * Ensures the item meta is a valid {@link WritableBookMeta}.
     *
     * @return the valid {@link WritableBookMeta}
     * @throws IllegalArgumentException if the item's meta is not an instance of {@link WritableBookMeta}
     */
    private WritableBookMeta validateWritableBookMeta() {
        if (!(getItemMeta() instanceof WritableBookMeta meta)) {
            throw new IllegalArgumentException("ItemMeta is not an instance of WritableBookMeta");
        }
        return meta;
    }

    /**
     * Adds new pages to the end of the book.
     *
     * @param pages the pages to add
     * @return this {@code WritableBookBuilder} instance for method chaining
     */
    public WritableBookBuilder addPage(@NotNull String... pages) {
        bookMeta.addPage(pages);
        return this;
    }

    /**
     * Gets the specified page in the book.
     *
     * @param page the page number (1-based index)
     * @return the content of the specified page
     * @throws IndexOutOfBoundsException if the page number is invalid
     */
    public String getPage(int page) {
        return bookMeta.getPage(page);
    }

    /**
     * Gets the number of pages in the book.
     *
     * @return the number of pages
     */
    public int getPageCount() {
        return bookMeta.getPageCount();
    }

    /**
     * Gets all the pages in the book.
     *
     * @return a list of pages
     */
    public List<String> getPages() {
        return bookMeta.getPages();
    }

    /**
     * Checks if the book has any pages.
     *
     * @return true if the book has pages, false otherwise
     */
    public boolean hasPages() {
        return bookMeta.hasPages();
    }

    /**
     * Sets the content of a specific page.
     *
     * @param page the page number (1-based index)
     * @param content the content to set
     * @return this {@code WritableBookBuilder} instance for method chaining
     * @throws IndexOutOfBoundsException if the page number is invalid
     */
    public WritableBookBuilder setPage(int page, @NotNull String content) {
        if (page < 1 || page > getPageCount()) {
            throw new IndexOutOfBoundsException("Page index out of range: " + page);
        }
        bookMeta.setPage(page, content);
        return this;
    }

    /**
     * Sets the pages of the book, replacing any existing pages.
     *
     * @param pages the pages to set
     * @return this {@code WritableBookBuilder} instance for method chaining
     */
    public WritableBookBuilder setPages(@NotNull String... pages) {
        bookMeta.setPages(pages);
        return this;
    }

    /**
     * Sets the pages of the book using a list, replacing any existing pages.
     *
     * @param pages the list of pages to set
     * @return this {@code WritableBookBuilder} instance for method chaining
     */
    public WritableBookBuilder setPages(@NotNull List<String> pages) {
        bookMeta.setPages(pages);
        return this;
    }

    /**
     * Clears all pages in the book.
     *
     * @return this {@code WritableBookBuilder} instance for method chaining
     */
    public WritableBookBuilder clearPages() {
        bookMeta.setPages();
        return this;
    }
}