package com.teamaurora.magnetosphere.api.content_registries.v1;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record FlammableBlockEntry(int encouragement, int flammability) {
    public static final Codec<FlammableBlockEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("encouragement").forGetter(FlammableBlockEntry::encouragement),
            Codec.intRange(0, 300).fieldOf("flammability").forGetter(FlammableBlockEntry::flammability)
    ).apply(instance, FlammableBlockEntry::new));
}
