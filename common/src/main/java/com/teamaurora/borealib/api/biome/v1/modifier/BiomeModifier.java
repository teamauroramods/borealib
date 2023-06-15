package com.teamaurora.borealib.api.biome.v1.modifier;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamaurora.borealib.api.base.v1.util.CodecHelper;

import java.util.List;

/**
 * Used to modify attributes of a large group of biomes.
 * <p>All stages load after datapacks and maybe some other modifiers are refreshed, meaning existing features can be looked up.
 *
 * @author ebo2022
 * @since 1.0
 */
public final class BiomeModifier {

    private final BiomeSelector selector;
    private final List<BiomeModifierAction> actions;

    public static final Codec<BiomeModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BiomeSelector.CODEC.fieldOf("selector").forGetter(BiomeModifier::selector),
            CodecHelper.nonEmptyList(BiomeModifierAction.CODEC).fieldOf("actions").forGetter(BiomeModifier::actions)
    ).apply(instance, BiomeModifier::new));

    private BiomeModifier(BiomeSelector selector,  List<BiomeModifierAction> actions) {
        this.selector = selector;
        this.actions = ImmutableList.copyOf(actions);
    }

    public BiomeSelector selector() {
        return this.selector;
    }

    public List<BiomeModifierAction> actions() {
        return this.actions;
    }
}
