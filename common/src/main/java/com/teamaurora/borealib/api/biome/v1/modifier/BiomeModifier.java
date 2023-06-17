package com.teamaurora.borealib.api.biome.v1.modifier;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamaurora.borealib.api.base.v1.util.CodecHelper;
import com.teamaurora.borealib.core.registry.BorealibRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.RegistryFileCodec;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to modify properties of a large group of biomes.
 * <p>All stages load after datapacks, meaning existing features can be looked up.
 *
 * @author ebo2022
 * @since 1.0
 */
public final class BiomeModifier {

    private final BiomeSelector selector;
    private final List<BiomeModifierAction> actions;

    public static final Codec<BiomeModifier> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BiomeSelector.CODEC.fieldOf("selector").forGetter(BiomeModifier::selector),
            CodecHelper.nonEmptyList(BiomeModifierAction.CODEC).fieldOf("actions").forGetter(BiomeModifier::actions)
    ).apply(instance, BiomeModifier::new));
    public static final Codec<Holder<BiomeModifier>> CODEC = RegistryFileCodec.create(BorealibRegistries.BIOME_MODIFIERS, DIRECT_CODEC);
    public static final Codec<HolderSet<BiomeModifier>> LIST_CODEC = RegistryCodecs.homogeneousList(BorealibRegistries.BIOME_MODIFIERS, DIRECT_CODEC);

    private BiomeModifier(BiomeSelector selector,  List<BiomeModifierAction> actions) {
        this.selector = selector;
        this.actions = ImmutableList.copyOf(actions);
    }

    /**
     * @return The selector used to determine what biomes this modifier applies to
     */
    public BiomeSelector selector() {
        return this.selector;
    }

    /**
     * @return An unmodifiable list of actions this modifier takes on its selected biomes
     */
    public List<BiomeModifierAction> actions() {
        return this.actions;
    }

    /**
     * A builder to create a new biome modifier during data generation.
     *
     * @author ebo2022
     * @since 1.0
     */
    public static final class Builder {

        private final BiomeSelector selector;
        private final List<BiomeModifierAction> actions = new ArrayList<>();

        private Builder(BiomeSelector selector) {
            this.selector = selector;
        }

        /**
         * Creates a new builder using the specified selector.
         *
         * @param selector The selector for the final biome modifier
         * @return A new builder
         */
        public static Builder selects(BiomeSelector selector) {
            return new Builder(selector);
        }

        /**
         * Adds an action for the finished modifier to run.
         *
         * @param action The action to add
         */
        public Builder withAction(BiomeModifierAction action) {
            this.actions.add(action);
            return this;
        }

        /**
         * Builds a new biome modifier with the given selector and actions.
         *
         * @return A new biome modifier
         */
        public BiomeModifier build() {
            return new BiomeModifier(this.selector, this.actions);
        }
    }
}
