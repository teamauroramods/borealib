package com.teamaurora.borealib.api.registry.v1.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Consumer;

/**
 * A helper class to generate {@link BlockBehaviour.Properties} for wood blocks.
 *
 * @author ebo2022
 * @author rose_
 * @since 1.0
 */
public record WoodProperties(MapColor woodColor, MapColor logColor, MapColor leavesColor, Consumer<BlockBehaviour.Properties> sharedProperties, SoundType sound, SoundType logSound, SoundType leavesSound, SoundType saplingSound) {

    public BlockBehaviour.Properties planks() {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();
        this.sharedProperties.accept(properties);
        return properties.mapColor(this.woodColor).strength(2.0F, 3.0F).sound(this.sound);
    }

    public BlockBehaviour.Properties log() {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();
        this.sharedProperties.accept(properties);
        return properties.mapColor(state -> state.getValue(BlockStateProperties.AXIS) == Direction.Axis.Y ? this.woodColor : this.logColor).strength(2.0F).sound(this.logSound);
    }

    public BlockBehaviour.Properties wood() {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();
        this.sharedProperties.accept(properties);
        return properties.mapColor(this.logColor).strength(2.0F).sound(this.logSound);
    }

    public BlockBehaviour.Properties strippedLogOrWood() {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();
        this.sharedProperties.accept(properties);
        return properties.mapColor(this.woodColor).strength(2.0F).sound(this.logSound);
    }

    public BlockBehaviour.Properties leaves() {
        return BlockBehaviour.Properties.of().mapColor(this.leavesColor).strength(0.2F).randomTicks().sound(this.leavesSound).noOcclusion().isValidSpawn(PropertiesHelper::ocelotOrParrot).isSuffocating(PropertiesHelper::never).isViewBlocking(PropertiesHelper::never).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor(PropertiesHelper::never);
    }

    public BlockBehaviour.Properties pressurePlate() {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();
        this.sharedProperties.accept(properties);
        return properties.mapColor(this.woodColor).noCollission().strength(0.5F).sound(this.sound);
    }

    public BlockBehaviour.Properties trapdoor() {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();
        this.sharedProperties.accept(properties);
        return properties.mapColor(this.woodColor).strength(3.0F).sound(this.sound).noOcclusion().isValidSpawn(PropertiesHelper::never);
    }

    public BlockBehaviour.Properties button() {
        return BlockBehaviour.Properties.of().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY).sound(this.sound);
    }

    public BlockBehaviour.Properties door() {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();
        this.sharedProperties.accept(properties);
        return properties.mapColor(this.woodColor).strength(3.0F).sound(this.sound).noOcclusion().pushReaction(PushReaction.DESTROY);
    }

    public BlockBehaviour.Properties sign() {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();
        this.sharedProperties.accept(properties);
        return properties.mapColor(this.woodColor).forceSolidOn().instrument(NoteBlockInstrument.BASS).ignitedByLava().noCollission().strength(1.0F).sound(this.sound);
    }

    public BlockBehaviour.Properties beehive() {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();
        this.sharedProperties.accept(properties);
        return properties.mapColor(this.woodColor).strength(0.6F).sound(this.sound);
    }

    public BlockBehaviour.Properties bookshelf() {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();
        this.sharedProperties.accept(properties);
        return properties.mapColor(this.woodColor).strength(1.5F).sound(this.sound);
    }

    public BlockBehaviour.Properties ladder() {
        return PropertiesHelper.ladder();
    }

    public BlockBehaviour.Properties cabinet() {
        return BlockBehaviour.Properties.of().mapColor(this.woodColor).strength(2.5f).sound(this.sound);
    }

    public BlockBehaviour.Properties sapling() {
        return BlockBehaviour.Properties.of().mapColor(this.leavesColor).noCollission().randomTicks().instabreak().sound(this.saplingSound).pushReaction(PushReaction.DESTROY);
    }

    public BlockBehaviour.Properties chest() {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();
        this.sharedProperties.accept(properties);
        return properties.mapColor(this.woodColor).strength(2.5F).sound(this.sound);
    }

    public BlockBehaviour.Properties leafPile() {
        return BlockBehaviour.Properties.of().mapColor(this.leavesColor).replaceable().noCollission().ignitedByLava().pushReaction(PushReaction.DESTROY).strength(0.2F).sound(this.leavesSound);
    }

    public BlockBehaviour.Properties leafCarpet() {
        return BlockBehaviour.Properties.of().mapColor(this.leavesColor).noCollission().ignitedByLava().strength(0.0F).sound(this.leavesSound).noOcclusion();
    }

    public BlockBehaviour.Properties post() {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.of();
        this.sharedProperties.accept(properties);
        return properties.mapColor(this.woodColor).strength(2.0F, 3.0F).sound(this.logSound);
    }

    /**
     * Creates a builder to generate properties with the given wood and log colors.
     *
     * @param woodColor The wood color to use
     * @param logColor  The log color to use
     * @return A new {@link Builder}
     */
    public static Builder builder(MapColor woodColor, MapColor logColor) {
        return new Builder(woodColor, logColor);
    }

    /**
     * A builder class to assist in creating {@link WoodProperties}.
     *
     * @since 1.0
     */
    public static final class Builder {

        private final MapColor woodColor;
        private final MapColor logColor;
        private MapColor leavesColor = MapColor.PLANT;
        private final Consumer<BlockBehaviour.Properties> sharedProperties = properties -> {
          properties.ignitedByLava();
          properties.instrument(NoteBlockInstrument.BASS);
        };
        private SoundType sound = SoundType.WOOD;
        private SoundType logSound = SoundType.WOOD;
        private SoundType leavesSound = SoundType.GRASS;
        private SoundType saplingSound = SoundType.GRASS;

        private Builder(MapColor woodColor, MapColor logColor) {
            this.woodColor = woodColor;
            this.logColor = logColor;
        }

        /**
         * Sets the color for leaves to be displayed as on the map.
         *
         * @param color The color to display
         */
        public Builder leavesColor(MapColor color) {
            this.leavesColor = color;
            return this;
        }

        /**
         * Sets the sound to use for wood blocks.
         *
         * @param soundType The sound type to use
         */
        public Builder sound(SoundType soundType) {
            this.sound = soundType;
            return this;
        }

        /**
         * Sets the sound to use for log blocks.
         *
         * @param soundType The sound type to use
         */
        public Builder logSound(SoundType soundType) {
            this.logSound = soundType;
            return this;
        }

        /**
         * Sets the sound to use for leaves blocks.
         *
         * @param soundType The sound type to use
         */
        public Builder leavesSound(SoundType soundType) {
            this.leavesSound = soundType;
            return this;
        }

        /**
         * Sets the sound to use for sapling blocks.
         *
         * @param soundType The sound type to use
         */
        public Builder saplingSound(SoundType soundType) {
            this.saplingSound = soundType;
            return this;
        }

        public WoodProperties build() {
            return new WoodProperties(this.logColor, this.woodColor, this.leavesColor, this.sharedProperties, this.sound, this.logSound, this.leavesSound, this.saplingSound);
        }
    }
}
