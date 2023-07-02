package com.teamaurora.borealib.api.block.v1.set;

import com.teamaurora.borealib.api.base.v1.modloading.ModLoaderService;
import com.teamaurora.borealib.api.block.v1.set.variant.BlockVariant;
import com.teamaurora.borealib.api.block.v1.set.variant.ItemVariant;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.api.registry.v1.extended.DeferredBlockRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

/**
 * A group of interconnected blocks and items; these could be any sort of blocks, such as a stone palette or a new tree type.
 *
 * @param <T> The type of block set (should extend {@link BlockSet} to allow for use of chain methods)
 * @author ebo2022
 * @since 1.0.0
 */
public abstract class BlockSet<T> {

    // These maps and lists are linked as register order is VERY important
    // Some blocks may require others to be registered before them to function properly
    private final Set<BlockVariant<T>> blockVariants = new LinkedHashSet<>();
    private final Set<ItemVariant<T>> itemVariants = new LinkedHashSet<>();
    private final Map<BlockVariant<T>, RegistryReference<Block>> blocksByVariant = new LinkedHashMap<>();
    private final Map<ItemVariant<T>, RegistryReference<Item>> itemsByVariant = new LinkedHashMap<>();

    // No need for specific order here, just querying
    private final Map<RegistryReference<Block>, RegistryReference<Item>> blockItems = new HashMap<>();
    private final String namespace;
    private final String baseName;
    private boolean registered;

    protected BlockSet(@Nullable String namespace, String baseName) {
        this.namespace = namespace;
        this.baseName = baseName;
    }

    /**
     * Adds a block variant to be registered.
     *
     * @param variant The variant to add
     */
    public T include(BlockVariant<T> variant) {
        this.validateMutable();
        if (!this.blockVariants.add(variant))
            throw new IllegalStateException("Attempted to add duplicate block set variant");
        return this.getThis();
    }

    /**
     * Adds an item variant to be registered.
     *
     * @param variant The variant to add
     */
    public T includeItem(ItemVariant<T> variant) {
        this.validateMutable();
        if (!this.itemVariants.add(variant))
            throw new IllegalStateException("Attempted to add duplicate block set item variant");
        return this.getThis();
    }

    /**
     * Gets a block for the given variant if the set includes it.
     *
     * @param variant The variant to get the block for
     * @return The corresponding block if it exists, otherwise {@link Optional#empty()}
     */
    public Optional<RegistryReference<Block>> variant(BlockVariant<T> variant) {
        return Optional.ofNullable(this.blocksByVariant.get(variant));
    }

    /**
     * Gets a block for the given variant or throws an exception if it doesn't exist.
     *
     * @param variant The variant to get the block for
     * @return The corresponding block which is guaranteed to exist
     */
    public RegistryReference<Block> variantOrThrow(BlockVariant<T> variant) {
        return this.variant(variant).orElseThrow();
    }

    /**
     * Gets an item for the given variant if the set includes it.
     *
     * @param variant The variant to get the item for
     * @return The corresponding item if it exists, otherwise {@link Optional#empty()}
     */
    public Optional<RegistryReference<Item>> itemVariant(ItemVariant<T> variant) {
        return Optional.ofNullable(this.itemsByVariant.get(variant));
    }

    /**
     * Gets an item for the given variant or throws an exception if it doesn't exist.
     *
     * @param variant The variant to get the item for
     * @return The corresponding item which is guaranteed to exist
     */
    public RegistryReference<Item> itemVariantOrThrow(ItemVariant<T> variant) {
        return this.itemVariant(variant).orElseThrow();
    }

    /**
     * Directly gets the block for the specified variant and throws an exception if it doesn't exist.
     *
     * @param variant The variant to get a block for
     * @return The block for the given variant
     */
    public Block getBlock(BlockVariant<T> variant) {
        return this.variantOrThrow(variant).get();
    }

    /**
     * Directly gets the item for the specified variant and throws an exception if it doesn't exist.
     *
     * @param variant The variant to get an item for
     * @return The item for the given variant
     */
    public Item getItem(ItemVariant<T> variant) {
        return this.itemVariantOrThrow(variant).get();
    }

    /**
     * Directly gets the item for the specified variant and throws an exception if it doesn't exist.
     *
     * @param variant The variant to get an item for
     * @return The item for the given variant
     */
    public Item getItem(BlockVariant<T> variant) {
        if (!variant.hasBlockItem()) throw new UnsupportedOperationException("Cannot get item for a block without once specified");
        return this.getBlock(variant).asItem();
    }

    /**
     * @return An unmodifiable view of corresponding variants and blocks
     */
    public Map<BlockVariant<T>, RegistryReference<Block>> getBlocksByVariant() {
        return Collections.unmodifiableMap(this.blocksByVariant);
    }

    /**
     * @return An unmodifiable view of corresponding variants and items
     */
    public Map<ItemVariant<T>, RegistryReference<Item>> getItemsByVariant() {
        return Collections.unmodifiableMap(this.itemsByVariant);
    }

    /**
     * @return The shared namespace of all blocks in this set
     */
    public String getNamespace() {
        return this.namespace;
    }

    /**
     * @return The "root" name of all blocks (e.g. "deepslate" shared between "deepslate_tiles" and "deepslate_stairs")
     */
    public String getBaseName() {
        return this.baseName;
    }

    /**
     * Registers the block set, runs the appropriate setup code, and finalizes it.
     * <p>New variants can no longer be added once this method is called; it is assumed the set is in its final state.
     *
     * @param register The {@link DeferredBlockRegister} to handle registration of blocks
     */
    public T registerTo(DeferredBlockRegister register) {
        this.validateMutable();
        this.blockVariants.forEach(variant -> {
            Supplier<Block> blockSupplier = variant.getFactory().create(this.getThis());
            ResourceLocation name = variant.createName(this.namespace, this.baseName);
            RegistryReference<Block> reference = register.register(name, blockSupplier);
            if (variant.hasBlockItem()) {
                RegistryReference<Item> itemReference = register.getItemRegistry().register(name, () -> variant.getBlockItemFactory().apply(this.getThis(), reference.get()));
                this.blockItems.put(reference, itemReference);
            }
            this.blocksByVariant.put(variant, reference);
        });
        this.itemVariants.forEach(variant -> {
            RegistryReference<Item> reference = register.getItemRegistry().register(variant.createName(this.namespace, this.baseName), variant.getFactory().create(this.getThis()));
            this.itemsByVariant.put(variant, reference);
        });
        this.registered = true;
        return this.getThis();
    }

    /**
     * Runs client init code for any variants that have it.
     */
    public void clientInit() {
        this.blocksByVariant.forEach((v, reference) -> {
            if (v.getClientInit() != null)
                v.getClientInit().get().accept(this.getThis(), reference);
            if (v.hasBlockItem() && v.getItemClientPostInit() != null)
                v.getItemClientInit().get().accept(this.getThis(), this.blockItems.get(reference));
        });
        this.itemsByVariant.forEach((v, reference) -> {
            if (v.getClientInit() != null)
                v.getClientInit().get().accept(this.getThis(), reference);
        });
    }

    /**
     * Runs client post-init code for any variants that have it.
     */
    public void clientPostInit(ModLoaderService.ParallelDispatcher dispatcher) {
        this.blocksByVariant.forEach((v, reference) -> {
            if (v.getClientPostInit() != null)
                v.getClientPostInit().get().accept(dispatcher, this.getThis(), reference);
            if (v.hasBlockItem() && v.getItemClientPostInit() != null)
                v.getItemClientPostInit().get().accept(dispatcher, this.getThis(), this.blockItems.get(reference));
        });
        this.itemsByVariant.forEach((v, reference) -> {
            if (v.getClientPostInit() != null)
                v.getClientPostInit().get().accept(dispatcher, this.getThis(), reference);
        });
    }

    /**
     * Asserts that the block set is unregistered before continuing with any mutability operations.
     */
    protected void validateMutable() {
        if (this.registered)
            throw new IllegalStateException("Cannot change a block set after it has been registered");
    }

    /**
     * @return This block set (as its subtype)
     */
    protected abstract T getThis();

    /**
     * Generates a principal object in a block or item variant.
     *
     * @param <T> The block set type
     * @param <R> The object type
     */
    @FunctionalInterface
    public interface ComponentFactory<T, R> {

        /**
         * Creates a factory for an object as part of the given block set.
         *
         * @param set The block set to generate the factory for
         * @return A factory to generate the object
         */
        Supplier<T> create(R set);
    }
}
