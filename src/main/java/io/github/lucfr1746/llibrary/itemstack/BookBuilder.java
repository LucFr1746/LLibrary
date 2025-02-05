package io.github.lucfr1746.llibrary.itemstack;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A builder class for creating and managing various types of books in Minecraft.
 * <p>
 * This builder provides methods to obtain specific builders for different book types,
 * such as enchanted books, recipe books, writable books, and written books.
 * </p>
 * @param book the book {@link ItemStack}. Must not be {@code null}.
 */
public record BookBuilder(@NotNull ItemStack book) {

    /**
     * Constructs a new {@code EnchantedBookBuilder} for manipulating enchanted books.
     *
     * @return a new instance of {@link EnchantedBookBuilder} initialized with the current book.
     * @throws IllegalStateException if the book is not a valid enchanted book.
     */
    public EnchantedBookBuilder getEnchantedBookBuilder() {
        return new EnchantedBookBuilder(this.book);
    }

    /**
     * Constructs a new {@code RecipeBookBuilder} for manipulating recipe books.
     *
     * @return a new instance of {@link RecipeBookBuilder} initialized with the current book.
     * @throws IllegalStateException if the book is not a valid recipe book.
     */
    public RecipeBookBuilder getRecipeBookBuilder() {
        return new RecipeBookBuilder(this.book);
    }

    /**
     * Constructs a new {@code WritableBookBuilder} for manipulating writable books.
     *
     * @return a new instance of {@link WritableBookBuilder} initialized with the current book.
     * @throws IllegalStateException if the book is not a valid writable book.
     */
    public WritableBookBuilder getWritableBookBuilder() {
        return new WritableBookBuilder(this.book);
    }

    /**
     * Constructs a new {@code WrittenBookBuilder} for manipulating written books.
     *
     * @return a new instance of {@link WrittenBookBuilder} initialized with the current book.
     * @throws IllegalStateException if the book is not a valid written book.
     */
    public WrittenBookBuilder getWrittenBookBuilder() {
        return new WrittenBookBuilder(this.book);
    }

    /**
     * A builder class for creating and modifying enchanted books in Minecraft.
     * <p>
     * The builder allows the addition and removal of enchantments, setting stored enchantments, and cloning the resulting
     * enchanted book {@link ItemStack}. The builder ensures that the input {@link ItemStack} is valid and has the
     * {@link EnchantmentStorageMeta} applied.
     * </p>
     * @param enchantedBook the enchanted {@link ItemStack}. Must not be {@code null} and must have
     *                      {@link EnchantmentStorageMeta}.
     */
    public record EnchantedBookBuilder(@NotNull ItemStack enchantedBook) {

        /**
         * Constructs a new {@code EnchantedBookBuilder} from the specified enchanted book {@link ItemStack}.
         * <p>
         * The provided {@code ItemStack} must be a valid enchanted book with {@link EnchantmentStorageMeta}.
         * The original item is cloned to maintain immutability.
         * </p>
         *
         * @param enchantedBook the enchanted {@link ItemStack}. Must not be {@code null} and must have
         *                      {@link EnchantmentStorageMeta}.
         * @throws NullPointerException     if {@code enchantedBook} is {@code null}.
         * @throws IllegalArgumentException if {@code enchantedBook} is {@link Material#AIR}.
         * @throws IllegalStateException    if {@code enchantedBook} does not have {@link EnchantmentStorageMeta}.
         */
        public EnchantedBookBuilder(ItemStack enchantedBook) {
            if (enchantedBook.getType() == Material.AIR) {
                throw new IllegalArgumentException("The ItemStack cannot be AIR!");
            }
            if (!(enchantedBook.getItemMeta() instanceof EnchantmentStorageMeta)) {
                throw new IllegalStateException("Expected EnchantmentStorageMeta but found: " + enchantedBook.getItemMeta() + "\nTry to use Material.ENCHANTED_BOOK instead!");
            }
            this.enchantedBook = enchantedBook.clone();
        }

        /**
         * Adds a map of enchantments to the book.
         * <p>
         * This method adds each enchantment from the provided map to the book with the specified level.
         * If {@code ignoreLevelRestriction} is true, level restrictions are ignored for each enchantment.
         * </p>
         *
         * @param enchants               the map of enchantments to add to the book.
         * @param ignoreLevelRestriction whether to ignore level restrictions for the enchantments.
         * @return the current {@code EnchantedBookBuilder} instance.
         */
        public EnchantedBookBuilder addEnchants(Map<Enchantment, Integer> enchants, boolean ignoreLevelRestriction) {
            EnchantmentStorageMeta enchantmentStorageMeta = getEnchantmentStorageMeta();
            enchants.forEach(((enchantment, integer) -> {
                enchantmentStorageMeta.addEnchant(enchantment, integer, ignoreLevelRestriction);
            }));
            this.enchantedBook.setItemMeta(enchantmentStorageMeta);
            return this;
        }

        /**
         * Adds a map of enchantments to the book with level restrictions enabled.
         *
         * @param enchants the map of enchantments to add to the book.
         * @return the current {@code EnchantedBookBuilder} instance.
         */
        public EnchantedBookBuilder addEnchants(Map<Enchantment, Integer> enchants) {
            return addEnchants(enchants, false);
        }

        /**
         * Adds a stored enchantment to the book.
         * <p>
         * This method adds a single enchantment to the book's stored enchantments with the specified level.
         * If {@code ignoreLevelRestriction} is true, the level restriction is ignored.
         * </p>
         *
         * @param enchantment            the enchantment to add.
         * @param level                  the level of the enchantment.
         * @param ignoreLevelRestriction whether to ignore the level restriction.
         * @return the current {@code EnchantedBookBuilder} instance.
         */
        public EnchantedBookBuilder addStoredEnchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
            EnchantmentStorageMeta enchantmentStorageMeta = getEnchantmentStorageMeta();
            enchantmentStorageMeta.addStoredEnchant(enchantment, level, ignoreLevelRestriction);
            this.enchantedBook.setItemMeta(enchantmentStorageMeta);
            return this;
        }

        /**
         * Adds a stored enchantment to the book with level restrictions enabled.
         *
         * @param enchantment the enchantment to add.
         * @param level       the level of the enchantment.
         * @return the current {@code EnchantedBookBuilder} instance.
         */
        public EnchantedBookBuilder addStoredEnchant(Enchantment enchantment, int level) {
            return addStoredEnchant(enchantment, level, false);
        }

        /**
         * Adds a stored enchantment to the book with level 1 and level restrictions enabled.
         *
         * @param enchantment the enchantment to add.
         * @return the current {@code EnchantedBookBuilder} instance.
         */
        public EnchantedBookBuilder addStoredEnchant(Enchantment enchantment) {
            return addStoredEnchant(enchantment, 1, false);
        }

        /**
         * Builds and returns a new {@link ItemStack} based on the current state of this {@code EnchantedBookBuilder}.
         *
         * @return a new cloned {@link ItemStack} representing the enchanted book.
         */
        public ItemStack build() {
            return this.enchantedBook.clone();
        }

        /**
         * Clears all stored enchantments from the enchanted book.
         *
         * @return the current {@code EnchantedBookBuilder} instance.
         */
        public EnchantedBookBuilder clearEnchants() {
            EnchantmentStorageMeta enchantmentStorageMeta = getEnchantmentStorageMeta();
            enchantmentStorageMeta.getStoredEnchants().forEach((enchantment, integer) -> enchantmentStorageMeta.removeStoredEnchant(enchantment));
            this.enchantedBook.setItemMeta(enchantmentStorageMeta);
            return this;
        }

        /**
         * Retrieves the level of a specific stored enchantment.
         *
         * @param enchantment the enchantment to check.
         * @return the level of the enchantment.
         */
        public int getStoredEnchantLevel(Enchantment enchantment) {
            return getEnchantmentStorageMeta().getStoredEnchantLevel(enchantment);
        }

        /**
         * Retrieves all stored enchantments for the enchanted book.
         *
         * @return a map of stored enchantments with their levels.
         */
        public Map<Enchantment,Integer> getStoredEnchants() {
            return getEnchantmentStorageMeta().getEnchants();
        }

        /**
         * Checks if a specific stored enchantment is conflicting with any existing stored enchantment.
         *
         * @param enchantment the enchantment to check.
         * @return {@code true} if the enchantment conflicts with another stored enchantment, otherwise {@code false}.
         */
        public boolean hasConflictingStoredEnchant(Enchantment enchantment) {
            return getEnchantmentStorageMeta().hasConflictingStoredEnchant(enchantment);
        }

        /**
         * Checks if a specific enchantment is stored in the enchanted book.
         *
         * @param enchantment the enchantment to check.
         * @return {@code true} if the enchantment is stored, otherwise {@code false}.
         */
        public boolean hasStoredEnchant(Enchantment enchantment) {
            return getEnchantmentStorageMeta().hasStoredEnchant(enchantment);
        }

        /**
         * Checks if the enchanted book has any stored enchantments.
         *
         * @return {@code true} if there are any stored enchantments, otherwise {@code false}.
         */
        public boolean hasStoredEnchants() {
            return getEnchantmentStorageMeta().hasStoredEnchants();
        }

        /**
         * Removes a specific stored enchantment from the enchanted book.
         *
         * @param enchantment the enchantment to remove.
         * @return the current {@code EnchantedBookBuilder} instance.
         */
        public EnchantedBookBuilder removeStoredEnchant(Enchantment enchantment) {
            EnchantmentStorageMeta enchantmentStorageMeta = getEnchantmentStorageMeta();
            enchantmentStorageMeta.removeStoredEnchant(enchantment);
            this.enchantedBook.setItemMeta(enchantmentStorageMeta);
            return this;
        }

        /**
         * Sets a map of enchantments to the book, replacing any existing enchantments.
         *
         * @param enchants               the map of enchantments to set to the book.
         * @param ignoreLevelRestriction whether to ignore level restrictions for the enchantments.
         * @return the current {@code EnchantedBookBuilder} instance.
         */
        public EnchantedBookBuilder setEnchants(Map<Enchantment, Integer> enchants, boolean ignoreLevelRestriction) {
            clearEnchants();
            return addEnchants(enchants, ignoreLevelRestriction);
        }

        /**
         * Sets a map of enchantments to the book with level restrictions enabled.
         *
         * @param enchants the map of enchantments to set to the book.
         * @return the current {@code EnchantedBookBuilder} instance.
         */
        public EnchantedBookBuilder setEnchants(Map<Enchantment, Integer> enchants) {
            return setEnchants(enchants, false);
        }

        /**
         * Converts the current {@code EnchantedBookBuilder} to an {@link ItemBuilder} for further manipulation.
         *
         * @return a new {@link ItemBuilder} initialized with the current enchanted book.
         */
        public ItemBuilder toItemBuilder() {
            return new ItemBuilder(this.enchantedBook.clone());
        }

        /**
         * Retrieves the {@link EnchantmentStorageMeta} from the enchanted book.
         *
         * @return the {@link EnchantmentStorageMeta} associated with the enchanted book.
         */
        private @NotNull EnchantmentStorageMeta getEnchantmentStorageMeta() {
            return (EnchantmentStorageMeta) Objects.requireNonNull(this.enchantedBook.getItemMeta());
        }
    }

    /**
     * A builder class for creating and modifying knowledge books in Minecraft.
     * <p>
     * The builder allows the addition, retrieval, and setting of recipes in a knowledge book {@link ItemStack}.
     * The input {@link ItemStack} is cloned to maintain immutability.
     * </p>
     * @param recipeBook the knowledge book {@link ItemStack}. Must not be {@code null} and must have
     *                   {@link KnowledgeBookMeta}.
     */
    public record RecipeBookBuilder(@NotNull ItemStack recipeBook) {

        /**
         * Constructs a new {@code RecipeBookBuilder} from the specified knowledge book {@link ItemStack}.
         * <p>
         * The provided {@code ItemStack} must be a valid knowledge book with {@link KnowledgeBookMeta}.
         * The original item is cloned to maintain immutability.
         * </p>
         *
         * @param recipeBook the knowledge book {@link ItemStack}. Must not be {@code null} and must have
         *                   {@link KnowledgeBookMeta}.
         * @throws NullPointerException     if {@code recipeBook} is {@code null}.
         * @throws IllegalArgumentException if {@code recipeBook} is {@link Material#AIR}.
         * @throws IllegalStateException    if {@code recipeBook} does not have {@link KnowledgeBookMeta}.
         */
        public RecipeBookBuilder(ItemStack recipeBook) {
            if (recipeBook.getType() == Material.AIR) {
                throw new IllegalArgumentException("The ItemStack cannot be AIR!");
            }
            if (!(recipeBook.getItemMeta() instanceof KnowledgeBookMeta)) {
                throw new IllegalStateException("Expected KnowledgeBookMeta but found: " + recipeBook.getItemMeta() + "\nTry to use Material.KNOWLEDGE_BOOK instead!");
            }
            this.recipeBook = recipeBook.clone();
        }

        /**
         * Adds one or more recipes to the knowledge book.
         *
         * @param recipes the recipes to add, represented by {@link NamespacedKey} instances.
         * @return the current {@code RecipeBookBuilder} instance.
         */
        public RecipeBookBuilder addRecipe(NamespacedKey... recipes) {
            KnowledgeBookMeta knowledgeBookMeta = getKnowledgeBookMeta();
            knowledgeBookMeta.addRecipe(recipes);
            this.recipeBook.setItemMeta(knowledgeBookMeta);
            return this;
        }

        /**
         * Builds and returns a new {@link ItemStack} based on the current state of this {@code RecipeBookBuilder}.
         *
         * @return a new cloned {@link ItemStack} representing the knowledge book.
         */
        public ItemStack build() {
            return this.recipeBook.clone();
        }

        /**
         * Retrieves the list of recipes currently stored in the knowledge book.
         *
         * @return a list of {@link NamespacedKey} representing the stored recipes.
         */
        public List<NamespacedKey> getRecipes() {
            return getKnowledgeBookMeta().getRecipes();
        }

        /**
         * Checks whether the knowledge book contains any recipes.
         *
         * @return {@code true} if the knowledge book contains recipes, otherwise {@code false}.
         */
        public boolean hasRecipes() {
            return getKnowledgeBookMeta().hasRecipes();
        }

        /**
         * Sets the list of recipes for the knowledge book, replacing any existing recipes.
         *
         * @param recipes the list of {@link NamespacedKey} representing the new recipes.
         * @return the current {@code RecipeBookBuilder} instance.
         */
        public RecipeBookBuilder setRecipes(List<NamespacedKey> recipes) {
            KnowledgeBookMeta knowledgeBookMeta = getKnowledgeBookMeta();
            knowledgeBookMeta.setRecipes(recipes);
            this.recipeBook.setItemMeta(knowledgeBookMeta);
            return this;
        }

        /**
         * Converts the current {@code RecipeBookBuilder} to an {@link ItemBuilder} for further manipulation.
         *
         * @return a new {@link ItemBuilder} initialized with the current knowledge book.
         */
        public ItemBuilder toItemBuilder() {
            return new ItemBuilder(this.recipeBook.clone());
        }

        /**
         * Retrieves the {@link KnowledgeBookMeta} from the knowledge book.
         *
         * @return the {@link KnowledgeBookMeta} associated with the knowledge book.
         * @throws NullPointerException if the meta-data is {@code null}.
         */
        private @NotNull KnowledgeBookMeta getKnowledgeBookMeta() {
            return (KnowledgeBookMeta) Objects.requireNonNull(this.recipeBook.getItemMeta());
        }
    }

    /**
     * A builder class for creating and modifying writable books in Minecraft.
     * <p>
     * This builder provides methods to manage book pages and their contents in a writable book {@link ItemStack}.
     * The input {@link ItemStack} is cloned to maintain immutability.
     * </p>
     * @param writableBook the writable book {@link ItemStack}. Must not be {@code null} and must have {@link WritableBookMeta}.
     */
    public record WritableBookBuilder(@NotNull ItemStack writableBook) {

        /**
         * Constructs a new {@code WritableBookBuilder} from the specified writable book {@link ItemStack}.
         * <p>
         * The provided {@code ItemStack} must be a valid writable book with {@link WritableBookMeta}.
         * The original item is cloned to maintain immutability.
         * </p>
         *
         * @param writableBook the writable book {@link ItemStack}. Must not be {@code null} and must have {@link WritableBookMeta}.
         * @throws NullPointerException     if {@code writableBook} is {@code null}.
         * @throws IllegalArgumentException if {@code writableBook} is {@link Material#AIR}.
         * @throws IllegalStateException    if {@code writableBook} does not have {@link WritableBookMeta}.
         */
        public WritableBookBuilder(ItemStack writableBook) {
            if (writableBook.getType() == Material.AIR) {
                throw new IllegalArgumentException("The ItemStack cannot be AIR!");
            }
            if (!(writableBook.getItemMeta() instanceof WritableBookMeta)) {
                throw new IllegalStateException("Expected WritableBookMeta but found: " + writableBook.getItemMeta() + "\nTry to use Material.KNOWLEDGE_BOOK instead!");
            }
            this.writableBook = writableBook.clone();
        }

        /**
         * Adds one or more pages to the writable book.
         *
         * @param pages the content for each page to add.
         * @return the current {@code WritableBookBuilder} instance.
         */
        public WritableBookBuilder addPage(String... pages) {
            WritableBookMeta writableBookMeta = getWritableBookMeta();
            writableBookMeta.addPage(pages);
            this.writableBook.setItemMeta(writableBookMeta);
            return this;
        }

        /**
         * Builds and returns a new {@link ItemStack} based on the current state of this {@code WritableBookBuilder}.
         *
         * @return a new cloned {@link ItemStack} representing the writable book.
         */
        public ItemStack build() {
            return this.writableBook.clone();
        }

        /**
         * Retrieves the content of the specified page.
         *
         * @param page the page number to retrieve (1-based index).
         * @return the content of the specified page.
         */
        public String getPage(int page) {
            return getWritableBookMeta().getPage(page);
        }

        /**
         * Retrieves the total number of pages in the writable book.
         *
         * @return the number of pages.
         */
        public int getPageCount() {
            return getWritableBookMeta().getPageCount();
        }

        /**
         * Retrieves the content of all pages in the writable book.
         *
         * @return a list of strings representing the pages.
         */
        public List<String> getPages() {
            return getWritableBookMeta().getPages();
        }

        /**
         * Checks whether the writable book contains any pages.
         *
         * @return {@code true} if the writable book has pages, otherwise {@code false}.
         */
        public boolean hasPages() {
            return getWritableBookMeta().hasPages();
        }

        /**
         * Sets the content of a specific page in the writable book.
         *
         * @param page the page number to set (1-based index).
         * @param data the content for the page.
         * @return the current {@code WritableBookBuilder} instance.
         */
        public WritableBookBuilder setPage(int page, String data) {
            WritableBookMeta writableBookMeta = getWritableBookMeta();
            writableBookMeta.setPage(page, data);
            this.writableBook.setItemMeta(writableBookMeta);
            return this;
        }

        /**
         * Sets the content for all pages in the writable book, replacing existing pages.
         *
         * @param pages a list of strings representing the new pages.
         * @return the current {@code WritableBookBuilder} instance.
         */
        public WritableBookBuilder setPages(List<String> pages) {
            WritableBookMeta writableBookMeta = getWritableBookMeta();
            writableBookMeta.setPages(pages);
            this.writableBook.setItemMeta(writableBookMeta);
            return this;
        }

        /**
         * Sets the content for all pages in the writable book, replacing existing pages.
         *
         * @param pages the new page content to set.
         * @return the current {@code WritableBookBuilder} instance.
         */
        public WritableBookBuilder setPages(String... pages) {
            WritableBookMeta writableBookMeta = getWritableBookMeta();
            writableBookMeta.setPages(pages);
            this.writableBook.setItemMeta(writableBookMeta);
            return this;
        }

        /**
         * Converts the current {@code WritableBookBuilder} to an {@link ItemBuilder} for further manipulation.
         *
         * @return a new {@link ItemBuilder} initialized with the current writable book.
         */
        public ItemBuilder toItemBuilder() {
            return new ItemBuilder(this.writableBook.clone());
        }

        /**
         * Retrieves the {@link WritableBookMeta} from the writable book.
         *
         * @return the {@link WritableBookMeta} associated with the writable book.
         * @throws NullPointerException if the meta-data is {@code null}.
         */
        private @NotNull WritableBookMeta getWritableBookMeta() {
            return (WritableBookMeta) Objects.requireNonNull(this.writableBook.getItemMeta());
        }
    }

    /**
     * A builder class for creating and modifying written books in Minecraft.
     * <p>
     * This builder provides methods to manage metadata such as title, author, and generation for a written book {@link ItemStack}.
     * The input {@link ItemStack} is cloned to maintain immutability.
     * </p>
     * @param writtenBook the written book {@link ItemStack}. Must not be {@code null} and must have {@link BookMeta}.
     */
    public record WrittenBookBuilder(@NotNull ItemStack writtenBook) {

        /**
         * Constructs a new {@code WrittenBookBuilder} from the specified written book {@link ItemStack}.
         * <p>
         * The provided {@code ItemStack} must be a valid written book with {@link BookMeta}.
         * The original item is cloned to maintain immutability.
         * </p>
         *
         * @param writtenBook the written book {@link ItemStack}. Must not be {@code null} and must have {@link BookMeta}.
         * @throws NullPointerException     if {@code writtenBook} is {@code null}.
         * @throws IllegalArgumentException if {@code writtenBook} is {@link Material#AIR}.
         * @throws IllegalStateException    if {@code writtenBook} does not have {@link BookMeta}.
         */
        public WrittenBookBuilder(ItemStack writtenBook) {
            if (writtenBook.getType() == Material.AIR) {
                throw new IllegalArgumentException("The ItemStack cannot be AIR!");
            }
            if (!(writtenBook.getItemMeta() instanceof BookMeta)) {
                throw new IllegalStateException("Expected WritableBookMeta but found: " + writtenBook.getItemMeta() + "\nTry to use Material.KNOWLEDGE_BOOK instead!");
            }
            this.writtenBook = writtenBook.clone();
        }

        /**
         * Builds and returns a new {@link ItemStack} based on the current state of this {@code WrittenBookBuilder}.
         *
         * @return a new cloned {@link ItemStack} representing the written book.
         */
        public ItemStack build() {
            return this.writtenBook.clone();
        }

        /**
         * Retrieves the author of the written book.
         *
         * @return the author's name, or {@code null} if not set.
         */
        public String getAuthor() {
            return getWrittenBookMeta().getAuthor();
        }

        /**
         * Retrieves the generation of the written book.
         *
         * @return the {@link BookMeta.Generation} of the book, or {@code null} if not set.
         */
        public BookMeta.Generation getGeneration() {
            return getWrittenBookMeta().getGeneration();
        }

        /**
         * Retrieves the title of the written book.
         *
         * @return the title of the book, or {@code null} if not set.
         */
        public String getTitle() {
            return getWrittenBookMeta().getTitle();
        }

        /**
         * Checks whether the book has an author set.
         *
         * @return {@code true} if the book has an author, otherwise {@code false}.
         */
        public boolean hasAuthor() {
            return getWrittenBookMeta().hasAuthor();
        }

        /**
         * Checks whether the book has a generation set.
         *
         * @return {@code true} if the book has a generation, otherwise {@code false}.
         */
        public boolean hasGeneration() {
            return getWrittenBookMeta().hasGeneration();
        }

        /**
         * Checks whether the book has a title set.
         *
         * @return {@code true} if the book has a title, otherwise {@code false}.
         */
        public boolean hasTitle() {
            return getWrittenBookMeta().hasTitle();
        }

        /**
         * Sets the author of the written book.
         *
         * @param author the author's name.
         * @return the current {@code WrittenBookBuilder} instance.
         */
        public WrittenBookBuilder setAuthor(String author) {
            BookMeta bookMeta = getWrittenBookMeta();
            bookMeta.setAuthor(author);
            this.writtenBook.setItemMeta(bookMeta);
            return this;
        }

        /**
         * Sets the generation of the written book.
         *
         * @param generation the {@link BookMeta.Generation} to set.
         * @return the current {@code WrittenBookBuilder} instance.
         */
        public WrittenBookBuilder setGeneration(BookMeta.Generation generation) {
            BookMeta bookMeta = getWrittenBookMeta();
            bookMeta.setGeneration(generation);
            this.writtenBook.setItemMeta(bookMeta);
            return this;
        }

        /**
         * Sets the title of the written book.
         *
         * @param title the title of the book.
         * @return the current {@code WrittenBookBuilder} instance.
         */
        public WrittenBookBuilder setTitle(String title) {
            BookMeta bookMeta = getWrittenBookMeta();
            bookMeta.setTitle(title);
            this.writtenBook.setItemMeta(bookMeta);
            return this;
        }

        /**
         * Converts the current {@code WrittenBookBuilder} to an {@link ItemBuilder} for further manipulation.
         *
         * @return a new {@link ItemBuilder} initialized with the current written book.
         */
        public ItemBuilder toItemBuilder() {
            return new ItemBuilder(this.writtenBook.clone());
        }

        /**
         * Retrieves the {@link BookMeta} from the written book.
         *
         * @return the {@link BookMeta} associated with the written book.
         * @throws NullPointerException if the meta data is {@code null}.
         */
        private @NotNull BookMeta getWrittenBookMeta() {
            return (BookMeta) Objects.requireNonNull(this.writtenBook.getItemMeta());
        }
    }
}
