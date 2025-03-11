package io.github.lucfr1746.llibrary.itemstack.component;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Represents a custom implementation of the {@link ToolComponent} interface.
 * This class allows configuring custom tool properties like mining speed,
 * block damage, and specific rules for block interactions.
 */
public class CustomToolComponent implements ToolComponent {

    private float defaultMiningSpeed;
    private int damagePerBlock;
    private final List<ToolRule> rules;

    /**
     * Constructs a new CustomToolComponent with the given default mining speed and block damage.
     *
     * @param defaultMiningSpeed the default mining speed of the tool
     * @param damagePerBlock the amount of damage the tool takes per block broken
     */
    public CustomToolComponent(float defaultMiningSpeed, int damagePerBlock) {
        this.defaultMiningSpeed = defaultMiningSpeed;
        this.damagePerBlock = damagePerBlock;
        this.rules = new ArrayList<>();
    }

    /**
     * Gets the default mining speed of the tool.
     *
     * @return the default mining speed
     */
    @Override
    public float getDefaultMiningSpeed() {
        return this.defaultMiningSpeed;
    }

    /**
     * Sets the default mining speed of the tool.
     *
     * @param speed the new mining speed
     */
    @Override
    public void setDefaultMiningSpeed(float speed) {
        this.defaultMiningSpeed = speed;
    }

    /**
     * Gets the amount of damage the tool takes per block broken.
     *
     * @return the damage per block
     */
    @Override
    public int getDamagePerBlock() {
        return this.damagePerBlock;
    }

    /**
     * Sets the amount of damage the tool takes per block broken.
     *
     * @param damage the new damage per block
     */
    @Override
    public void setDamagePerBlock(int damage) {
        this.damagePerBlock = damage;
    }

    /**
     * Gets all custom rules for block interactions.
     *
     * @return a list of tool rules
     */
    @NotNull
    @Override
    public List<ToolRule> getRules() {
        return Collections.unmodifiableList(this.rules);
    }

    /**
     * Sets the list of custom tool rules.
     *
     * @param rules the list of tool rules
     */
    @Override
    public void setRules(@NotNull List<ToolRule> rules) {
        this.rules.clear();
        this.rules.addAll(rules);
    }

    /**
     * Adds a custom rule for a specific block.
     *
     * @param block           the material of the block
     * @param speed           the custom mining speed, or null to use the default
     * @param correctForDrops whether the rule affects drops or null for default behavior
     * @return the created ToolRule
     */
    @NotNull
    @Override
    public ToolRule addRule(@NotNull Material block, @Nullable Float speed, @Nullable Boolean correctForDrops) {
        ToolRule rule = new CustomToolRule(Set.of(block), speed, correctForDrops);
        this.rules.add(rule);
        return rule;
    }

    /**
     * Adds a custom rule for multiple blocks.
     *
     * @param blocks          a collection of block materials
     * @param speed           the custom mining speed, or null to use the default
     * @param correctForDrops whether the rule affects drops or null for default behavior
     * @return the created ToolRule
     */
    @NotNull
    @Override
    public ToolRule addRule(@NotNull Collection<Material> blocks, @Nullable Float speed, @Nullable Boolean correctForDrops) {
        ToolRule rule = new CustomToolRule(new HashSet<>(blocks), speed, correctForDrops);
        this.rules.add(rule);
        return rule;
    }

    /**
     * Adds a custom rule for a tag of blocks.
     *
     * @param tag             the tag representing multiple block types
     * @param speed           the custom mining speed, or null to use the default
     * @param correctForDrops whether the rule affects drops or null for default behavior
     * @return the created ToolRule
     */
    @NotNull
    @Override
    public ToolRule addRule(@NotNull Tag<Material> tag, @Nullable Float speed, @Nullable Boolean correctForDrops) {
        ToolRule rule = new CustomToolRule(tag.getValues(), speed, correctForDrops);
        this.rules.add(rule);
        return rule;
    }

    /**
     * Removes a specific rule from the tool.
     *
     * @param rule the rule to remove
     * @return true if the rule was removed, false otherwise
     */
    @Override
    public boolean removeRule(@NotNull ToolRule rule) {
        return this.rules.remove(rule);
    }

    /**
     * Serializes this tool component into a map for data storage.
     *
     * @return a map containing the tool properties and rules
     */
    @NotNull
    @Override
    public Map<String, Object> serialize() {
        return Map.of(
                "defaultMiningSpeed", this.defaultMiningSpeed,
                "damagePerBlock", this.damagePerBlock,
                "rules", this.rules.stream().map(rule -> Map.of(
                        "blocks", rule.getBlocks(),
                        "speed", rule.getSpeed(),
                        "correctForDrops", rule.isCorrectForDrops()
                )).toList()
        );
    }

    /**
     * Represents a custom implementation of the {@link ToolComponent.ToolRule} interface.
     * This class defines rules for how a tool interacts with specific blocks,
     * including custom mining speeds and drop behavior.
     */
    public static class CustomToolRule implements ToolComponent.ToolRule {

        private Collection<Material> blocks;
        private Float speed;
        private Boolean correctForDrops;

        /**
         * Constructs a new CustomToolRule.
         *
         * @param blocks          the collection of block materials this rule applies to
         * @param speed           the custom mining speed, or null to use the default
         * @param correctForDrops whether the rule affects drops or null for default behavior
         */
        public CustomToolRule(@NotNull Collection<Material> blocks, @Nullable Float speed, @Nullable Boolean correctForDrops) {
            this.blocks = new HashSet<>(blocks);
            this.speed = speed;
            this.correctForDrops = correctForDrops;
        }

        /**
         * Gets the collection of blocks this rule applies to.
         *
         * @return an unmodifiable collection of block materials
         */
        @NotNull
        @Override
        public Collection<Material> getBlocks() {
            return Collections.unmodifiableCollection(blocks);
        }

        /**
         * Sets the rule to apply to a single block material.
         *
         * @param block the material to set
         */
        @Override
        public void setBlocks(@NotNull Material block) {
            this.blocks = Set.of(block);
        }

        /**
         * Sets the rule to apply to a collection of block materials.
         *
         * @param blocks the collection of materials to set
         */
        @Override
        public void setBlocks(@NotNull Collection<Material> blocks) {
            this.blocks = new HashSet<>(blocks);
        }

        /**
         * Sets the rule to apply to blocks within a tag.
         *
         * @param tag the tag containing the blocks this rule should apply to
         */
        @Override
        public void setBlocks(@NotNull Tag<Material> tag) {
            this.blocks = new HashSet<>(tag.getValues());
        }

        /**
         * Gets the mining speed for this rule.
         *
         * @return the mining speed, or null if using the default speed
         */
        @Nullable
        @Override
        public Float getSpeed() {
            return this.speed;
        }

        /**
         * Sets the mining speed for this rule.
         *
         * @param speed the new mining speed, or null to reset to default
         */
        @Override
        public void setSpeed(@Nullable Float speed) {
            this.speed = speed;
        }

        /**
         * Get whether this rule is considered the optimal tool for the blocks listed by this rule and will drop items.
         *
         * @return true if drop correction is enabled, false otherwise, or null for default behavior
         */
        @Nullable
        @Override
        public Boolean isCorrectForDrops() {
            return this.correctForDrops;
        }

        /**
         * Set whether this rule is considered the optimal tool for the blocks listed by this rule and will drop items.
         *
         * @param correct true to enable drop correction, false to disable, or null for default
         */
        @Override
        public void setCorrectForDrops(@Nullable Boolean correct) {
            this.correctForDrops = correct;
        }

        /**
         * Serializes this tool rule into a map for data storage.
         *
         * @return a map containing the blocks, mining speed, and drop correction settings
         */
        @NotNull
        @Override
        public Map<String, Object> serialize() {
            return Map.of(
                    "blocks", blocks.stream().map(Material::toString).toList(),
                    "speed", speed,
                    "correctForDrops", correctForDrops
            );
        }
    }
}
