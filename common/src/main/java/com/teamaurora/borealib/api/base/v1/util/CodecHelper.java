package com.teamaurora.borealib.api.base.v1.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import com.teamaurora.borealib.api.registry.v1.RegistryView;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.List;
import java.util.function.Function;

public interface CodecHelper {

    /**
     * Creates a codec that accepts two different types of objects.
     *
     * @param codec1 The first codec
     * @param codec2 The second codec
     * @return A new either codec
     */
    static <T, R> Codec<Either<T, R>> either(Codec<T> codec1, Codec<R> codec2) {
        return new ExtraCodecs.EitherCodec<>(codec1, codec2);
    }

    /**
     * Creates an enhanced list codec that also accepts a singleton entry.
     *
     * @param elementCodec The base codec
     * @param <T> The element type
     * @return A new enhanced list codec
     */
    static <T> Codec<List<T>> list(Codec<T> elementCodec) {
        return either(elementCodec.listOf(), elementCodec).xmap(
                either -> either.map(Function.identity(), List::of), // convert list/singleton to list when decoding
                list -> list.size() == 1 ? Either.right(list.get(0)) : Either.left(list) // convert list to singleton/list when encoding
        );
    }

    /**
     * Creates an enhanced list codec that also accepts a singleton entry. If the input is a list, it will be validated to have at least 1 element.
     *
     * @param elementCodec The base codec
     * @param <T> The element type
     * @return A new enhanced list codec
     */
    static <T> Codec<List<T>> nonEmptyList(Codec<T> elementCodec) {
        return ExtraCodecs.nonEmptyList(list(elementCodec));
    }

    /**
     * Creates a codec for any block that has the given block state property.
     *
     * @param property The block state property to check for
     * @return A new block with property codec
     */
    static Codec<Block> blockWithProperty(Property<?> property) {
       return ExtraCodecs.validate(RegistryView.BLOCK.byNameCodec(), block -> {
           if (!block.defaultBlockState().hasProperty(property))
               return DataResult.error(() -> "Block does not support property " + property);
           return DataResult.success(block);
       });
    }
}
