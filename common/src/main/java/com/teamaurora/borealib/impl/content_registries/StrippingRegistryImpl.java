package com.teamaurora.borealib.impl.content_registries;

import com.teamaurora.borealib.api.content_registries.v1.StrippingRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApiStatus.Internal
public class StrippingRegistryImpl {

    private static final Map<Block, Block> REGISTRY = new ConcurrentHashMap<>();

    public static void register(Block from, Block to) {
        REGISTRY.put(from, to);
    }

    @Nullable
    public static BlockState getStrippedState(BlockState from) {
        Block stripped = REGISTRY.get(from.getBlock());
        if (stripped == null)
            return null;

        BlockState strippedState = stripped.defaultBlockState();
        if (stripped instanceof StrippingRegistry.StrippedStateProvider provider)
            return provider.copyStrippedPropertiesFrom(from);
        for (Property<?> property : from.getProperties())
            if (strippedState.hasProperty(property))
                strippedState = setProperty(from, strippedState, property);
        return strippedState;
    }

    private static <T extends Comparable<T>> BlockState setProperty(BlockState from, BlockState to, Property<T> property) {
        return to.setValue(property, from.getValue(property));
    }
}