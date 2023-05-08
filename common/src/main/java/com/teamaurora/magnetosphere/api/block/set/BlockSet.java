package com.teamaurora.magnetosphere.api.block.set;

import com.teamaurora.magnetosphere.api.block.set.variant.BlockVariant;
import com.teamaurora.magnetosphere.api.block.set.variant.ItemVariant;
import com.teamaurora.magnetosphere.api.registry.v1.RegistryReference;
import com.teamaurora.magnetosphere.api.registry.v1.extended.DeferredBlockRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.function.Supplier;

/**
 * A set of blocks and items that share common properties (e.g. the Oak woodset or the Deepslate stone set).
 * <p>Block Sets are comprised of block and item variants which provide a template for the set to register objects.
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

    protected BlockSet(String namespace, String baseName) {
        this.namespace = namespace;
        this.baseName = baseName;
    }

    public BlockSet<T> include(BlockVariant<T> variant) {
        if (!this.blockVariants.add(variant))
            throw new IllegalStateException("Attempted to add duplicate block set variant");
        return this;
    }

    public BlockSet<T> include(ItemVariant<T> variant) {
        if (!this.itemVariants.add(variant))
            throw new IllegalStateException("Attempted to add duplicate block set variant");
        return this;
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

    public BlockSet<T> register(DeferredBlockRegister register) {
        T context = this.makeContext();
        this.blockVariants.forEach(variant -> {
            Supplier<Block> blockSupplier = variant.getFactory().create(context, this);
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
        this.itemVariants.forEach(variant -> register.getItemRegistry().register(variant.createName(this.namespace, this.baseName), variant.getFactory().create(context, this)));
        return this;
    }

    public abstract T makeContext();

    @FunctionalInterface
    public interface ComponentFactory<T, R> {
        Supplier<T> create(R context, BlockSet<R> set);
    }
}
