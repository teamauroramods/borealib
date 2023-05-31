package com.teamaurora.magnetosphere.api.base.v1.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;

public interface CodecHelper {

    static <T, R> Codec<Either<T, R>> either(Codec<T> codec1, Codec<R> codec2) {
        return new ExtraCodecs.EitherCodec<>(codec1, codec2);
    }
}
