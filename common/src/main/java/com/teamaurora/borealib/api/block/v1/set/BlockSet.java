package com.teamaurora.borealib.api.block.v1.set;

import com.teamaurora.borealib.api.base.v1.modloading.ModLoaderService;
import com.teamaurora.borealib.api.block.v1.set.variant.BlockVariant;
import com.teamaurora.borealib.api.block.v1.set.variant.ItemVariant;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.api.registry.v1.extended.DeferredBlockRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

/**
 * A group of blocks and items that share common properties (e.g. the Oak woodset or the Deepslate stone set).
 *
 * @param <T> The type of block set (should extend {@link BlockSet})
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
     * Add a block variant to be registered.
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
     * Add a block variant to be registered if the specified condition is true.
     *
     * @param condition The condition to check
     * @param variant   The variant to add
     */
    public T includeIf(boolean condition, BlockVariant<T> variant) {
        if (condition) this.include(variant);
        return this.getThis();
    }

    public T includeItem(ItemVariant<T> variant) {
        this.validateMutable();
        if (!this.itemVariants.add(variant))
            throw new IllegalStateException("Attempted to add duplicate block set item variant");
        return this.getThis();
    }

    public T includeItem(List<ItemVariant<T>> variants) {
        variants.forEach(this::includeItem);
        return this.getThis();
    }

    @SafeVarargs
    public final T includeItem(ItemVariant<T>... variants) {
        return this.includeItem(Arrays.asList(variants));
    }

    public Optional<RegistryReference<Block>> variant(BlockVariant<T> variant) {
        return Optional.ofNullable(this.blocksByVariant.get(variant));
    }

    public RegistryReference<Block> variantOrThrow(BlockVariant<T> variant) {
        return this.variant(variant).orElseThrow();
    }

    public Optional<RegistryReference<Item>> itemVariant(ItemVariant<T> variant) {
        return Optional.ofNullable(this.itemsByVariant.get(variant));
    }

    public RegistryReference<Item> itemVariantOrThrow(ItemVariant<T> variant) {
        return this.itemVariant(variant).orElseThrow();
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getBaseName() {
        return this.baseName;
    }

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
            if (variant.getOnRegister() != null)
                reference.listen(variant.getOnRegister());
        });
        this.itemVariants.forEach(variant -> {
            RegistryReference<Item> reference = register.getItemRegistry().register(variant.createName(this.namespace, this.baseName), variant.getFactory().create(this.getThis()));
            this.itemsByVariant.put(variant, reference);
            if (variant.getOnRegister() != null)
                reference.listen(variant.getOnRegister());
        });
        this.registered = true;
        return this.getThis();
    }

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

    protected void validateMutable() {
        if (this.registered)
            throw new IllegalStateException("Cannot change a block set after it has been registered");
    }

    protected abstract T getThis();

    @FunctionalInterface
    public interface ComponentFactory<T, R> {
        Supplier<T> create(R set);
    }
}
