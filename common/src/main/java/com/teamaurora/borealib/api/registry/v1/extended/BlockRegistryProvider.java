package com.teamaurora.borealib.api.registry.v1.extended;

import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;
import java.util.function.Supplier;

public final class BlockRegistryProvider extends ExtendedRegistryWrapperProvider<Block> {

    private final RegistryWrapper.Provider<Item> itemRegistry;

    private BlockRegistryProvider(RegistryWrapper.Provider<Block> blockRegistry, RegistryWrapper.Provider<Item> itemRegistry) {
        super(blockRegistry);
        this.itemRegistry = itemRegistry;
    }

    public static BlockRegistryProvider create(String owner) {
        return new BlockRegistryProvider(RegistryWrapper.provider(Registries.BLOCK, owner), RegistryWrapper.provider(Registries.ITEM, owner));
    }

    public <R extends Block> RegistryReference<R> registerWithItem(String id, Supplier<? extends R> block, Item.Properties properties) {
        return this.registerWithItem(id, block, object -> new BlockItem(object, properties));
    }

    public <R extends Block> RegistryReference<R> registerWithItem(String id, Supplier<? extends R> block, Function<R, Item> itemFactory) {
        RegistryReference<R> register = this.register(id, block);
        this.itemRegistry.register(id, () -> itemFactory.apply(register.get()));
        return register;
    }

    public <R extends Block> RegistryReference<R> registerWithItem(ResourceLocation id, Supplier<? extends R> block, Item.Properties properties) {
        return this.registerWithItem(id, block, object -> new BlockItem(object, properties));
    }

    public <R extends Block> RegistryReference<R> registerWithItem(ResourceLocation id, Supplier<? extends R> block, Function<R, Item> itemFactory) {
        RegistryReference<R> register = this.register(id, block);
        this.itemRegistry.register(id, () -> itemFactory.apply(register.get()));
        return register;
    }

    public RegistryWrapper.Provider<Item> getItemRegistry() {
        return this.itemRegistry;
    }
}
