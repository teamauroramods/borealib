package com.teamaurora.magnetosphere.api.block.v1.set;

import com.teamaurora.magnetosphere.api.block.v1.set.variant.BlockVariant;
import com.teamaurora.magnetosphere.api.block.v1.set.variant.ItemVariant;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryReference;
import com.teamaurora.magnetosphere.api.registry.v1.extended.DeferredBlockRegister;
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
 *
 * @since 1.0.0
 */
public abstract class BlockSet<T> {

    private final Set<BlockVariant<T>> blockVariants = new HashSet<>();
    private final Set<ItemVariant<T>> itemVariants = new HashSet<>();
    private final Map<BlockVariant<T>, RegistryReference<Block>> blocksByVariant = new HashMap<>();
    private final Map<ItemVariant<T>, RegistryReference<Item>> itemsByVariant = new HashMap<>();
    private final String namespace;
    private final String baseName;
    private boolean registered;

    protected BlockSet(@Nullable String namespace, String baseName) {
        this.namespace = namespace;
        this.baseName = baseName;
    }

    public T include(BlockVariant<T> variant) {
        this.validateMutable();
        if (!this.blockVariants.add(variant))
            throw new IllegalStateException("Attempted to add duplicate block set variant");
        return this.getThis();
    }

    public T include(List<BlockVariant<T>> variants) {
        variants.forEach(this::include);
        return this.getThis();
    }

    @SafeVarargs
    public final T include(BlockVariant<T>... variants) {
        return this.include(Arrays.asList(variants));
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
            RegistryReference<Block> reference;
            if (variant.hasBlockItem()) {
                reference = register.registerWithItem(name, blockSupplier, new Item.Properties());
            } else {
                reference = register.register(name, blockSupplier);
            }
            if (variant.getOnRegister() != null)
                reference.listen(variant.getOnRegister());
        });
        this.itemVariants.forEach(variant -> register.getItemRegistry().register(variant.createName(this.namespace, this.baseName), variant.getFactory().create(this.getThis())));
        this.registered = true;
        return this.getThis();
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
