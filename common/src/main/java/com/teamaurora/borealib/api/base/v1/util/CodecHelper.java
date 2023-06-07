package com.teamaurora.borealib.api.base.v1.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.teamaurora.borealib.api.registry.v1.RegistryView;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;

public interface CodecHelper {

    static <T, R> Codec<Either<T, R>> either(Codec<T> codec1, Codec<R> codec2) {
        return new ExtraCodecs.EitherCodec<>(codec1, codec2);
    }

    static Codec<Block> blockWithProperty(Property<?> property) {
       return ExtraCodecs.validate(RegistryView.BLOCK.byNameCodec(), block -> {
           if (!block.defaultBlockState().hasProperty(property))
               return DataResult.error(() -> "Block does not support property " + property);
           return DataResult.success(block);
       });
    }

    static <T extends Block> Codec<Block> blockOfType(Class<T> type) {
        return ExtraCodecs.validate(RegistryView.BLOCK.byNameCodec(), block -> {
            if (!(block.getClass().equals(type)))
                return DataResult.error(() -> "Block is not a valid " + type.getSimpleName());
            return DataResult.success(block);
        });
    }
}
