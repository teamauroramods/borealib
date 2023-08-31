package com.teamaurora.borealib.api.base.v1.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;

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
}
