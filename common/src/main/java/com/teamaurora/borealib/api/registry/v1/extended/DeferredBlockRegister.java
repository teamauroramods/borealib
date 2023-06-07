package com.teamaurora.borealib.api.registry.v1.extended;

import com.teamaurora.borealib.api.registry.v1.DeferredRegister;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Function;
import java.util.function.Supplier;

public final class DeferredBlockRegister extends ExtendedDeferredRegister<Block> {

    private final DeferredRegister<Item> itemRegistry;

    private DeferredBlockRegister(DeferredRegister<Block> blockRegistry, DeferredRegister<Item> itemRegistry) {
        super(blockRegistry);
        this.itemRegistry = itemRegistry;
    }

    public static DeferredBlockRegister create(DeferredRegister<Item> itemRegistry) {
        return new DeferredBlockRegister(DeferredRegister.create(Registries.BLOCK, itemRegistry.id()), itemRegistry);
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

    public DeferredRegister<Item> getItemRegistry() {
        return this.itemRegistry;
    }
}
