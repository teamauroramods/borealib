package com.teamaurora.borealib.api.registry.v1.extended;

import com.google.common.base.Suppliers;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * A registry provider that allows for the creation of block entities with an indefinite amount of blocks.
 *
 * @author ebo2022
 * @since 1.0
 */
public final class BlockEntityRegistryProvider extends ExtendedRegistryWrapperProvider<BlockEntityType<?>> {

    private BlockEntityRegistryProvider(RegistryWrapper.Provider<BlockEntityType<?>> parent) {
        super(parent);
    }

    public static BlockEntityRegistryProvider create(String owner) {
        return new BlockEntityRegistryProvider(RegistryWrapper.provider(Registries.BLOCK_ENTITY_TYPE, owner));
    }

    private static Supplier<Set<Block>> collectBlocks(Class<?> clazz) {
        return Suppliers.memoize(() -> RegistryWrapper.BLOCK.stream().filter(clazz::isInstance).collect(Collectors.toSet()));
    }

    /**
     * Registers a new dynamic block entity.
     *
     * @param name         The name of the block entity
     * @param factory      The factory to generate the block entity
     * @param lazyBlockSet A supplier for the blocks that this entity should support
     * @param <T>          The block entity type
     * @return A reference to the registered block entity
     */
    public <T extends BlockEntity> RegistryReference<BlockEntityType<T>> registerDynamic(ResourceLocation name, BlockEntityType.BlockEntitySupplier<? extends T> factory, Supplier<Set<Block>> lazyBlockSet) {
        return this.register(name, () -> new BlockEntityType<>(factory, Collections.emptySet(), null) {

            @Override
            public boolean isValid(BlockState blockState) {
                return lazyBlockSet.get().contains(blockState.getBlock());
            }
        });
    }

    /**
     * Registers a new dynamic block entity.
     *
     * @param name    The name of the block entity
     * @param factory The factory to generate the block entity
     * @param clazz   A marker class to determine what blocks are valid
     * @param <T>     The block entity type
     * @return A reference to the registered block entity
     */
    public <T extends BlockEntity> RegistryReference<BlockEntityType<T>> registerDynamic(ResourceLocation name, BlockEntityType.BlockEntitySupplier<? extends T> factory, Class<?> clazz) {
        return this.registerDynamic(name, factory, collectBlocks(clazz));
    }

    /**
     * Registers a new dynamic block entity.
     *
     * @param name         The name of the block entity
     * @param factory      The factory to generate the block entity
     * @param lazyBlockSet A supplier for the blocks that this entity should support
     * @param <T>          The block entity type
     * @return A reference to the registered block entity
     */
    public <T extends BlockEntity> RegistryReference<BlockEntityType<T>> registerDynamic(String name, BlockEntityType.BlockEntitySupplier<? extends T> factory, Supplier<Set<Block>> lazyBlockSet) {
        return this.registerDynamic(new ResourceLocation(this.owner(), name), factory, lazyBlockSet);
    }

    /**
     * Registers a new dynamic block entity.
     *
     * @param name    The name of the block entity
     * @param factory The factory to generate the block entity
     * @param clazz   A marker class to determine what blocks are valid
     * @param <T>     The block entity type
     * @return A reference to the registered block entity
     */
    public <T extends BlockEntity> RegistryReference<BlockEntityType<T>> registerDynamic(String name, BlockEntityType.BlockEntitySupplier<? extends T> factory, Class<? extends Block> clazz) {
        return this.registerDynamic(name, factory, collectBlocks(clazz));
    }
}
