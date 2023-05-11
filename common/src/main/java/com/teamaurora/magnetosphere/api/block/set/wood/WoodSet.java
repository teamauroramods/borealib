package com.teamaurora.magnetosphere.api.block.set.wood;

import com.teamaurora.magnetosphere.api.block.set.BlockSet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.Function;
import java.util.function.Supplier;

public final class WoodSet extends BlockSet<WoodSet> {

    private final WoodType woodType;
    private final Supplier<BlockBehaviour.Properties> baseProperties;
    private MaterialColor barkColor = MaterialColor.WOOD;
    private MaterialColor woodColor = MaterialColor.PODZOL;

    private WoodSet(String namespace, String baseName, Sound sound) {
        super(namespace, baseName);
        this.woodType = sound.function.apply(baseName);
        WoodType.register(this.woodType);
        this.baseProperties = () -> BlockBehaviour.Properties.of(Material.WOOD).strength(2F, 3F).sound(this.woodType.soundType());
    }

    public WoodSet color(MaterialColor barkColor, MaterialColor woodColor) {
        this.barkColor = barkColor;
        this.woodColor = woodColor;
        return this;
    }

    public Supplier<BlockBehaviour.Properties> getBaseProperties() {
        return this.baseProperties;
    }

    public WoodType getWoodType() {
        return this.woodType;
    }

    public MaterialColor getBarkColor() {
        return this.barkColor;
    }

    public MaterialColor getWoodColor() {
        return this.woodColor;
    }

    @Override
    protected WoodSet getThis() {
        return this;
    }

    public enum Sound {
        DEFAULT(SoundType.CHERRY_WOOD,
                SoundEvents.CHERRY_WOOD_DOOR_CLOSE,
                SoundEvents.CHERRY_WOOD_DOOR_OPEN,
                SoundEvents.CHERRY_WOOD_TRAPDOOR_CLOSE,
                SoundEvents.CHERRY_WOOD_TRAPDOOR_OPEN,
                SoundEvents.CHERRY_WOOD_PRESSURE_PLATE_CLICK_OFF,
                SoundEvents.CHERRY_WOOD_PRESSURE_PLATE_CLICK_ON,
                SoundEvents.CHERRY_WOOD_BUTTON_CLICK_OFF,
                SoundEvents.CHERRY_WOOD_BUTTON_CLICK_ON,
                SoundType.CHERRY_WOOD_HANGING_SIGN,
                SoundEvents.CHERRY_WOOD_FENCE_GATE_CLOSE,
                SoundEvents.CHERRY_WOOD_FENCE_GATE_OPEN),
        LEGACY(SoundType.WOOD,
                SoundEvents.WOODEN_DOOR_CLOSE,
                SoundEvents.WOODEN_DOOR_OPEN,
                SoundEvents.WOODEN_TRAPDOOR_CLOSE,
                SoundEvents.WOODEN_TRAPDOOR_OPEN,
                SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF,
                SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON,
                SoundEvents.WOODEN_BUTTON_CLICK_OFF,
                SoundEvents.WOODEN_BUTTON_CLICK_ON,
                SoundType.HANGING_SIGN,
                SoundEvents.FENCE_GATE_CLOSE,
                SoundEvents.FENCE_GATE_OPEN),
        MUSHROOM_LIKE(SoundType.NETHER_WOOD,
                SoundEvents.NETHER_WOOD_DOOR_CLOSE,
                SoundEvents.NETHER_WOOD_DOOR_OPEN,
                SoundEvents.NETHER_WOOD_TRAPDOOR_CLOSE,
                SoundEvents.NETHER_WOOD_TRAPDOOR_OPEN,
                SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF,
                SoundEvents.NETHER_WOOD_PRESSURE_PLATE_CLICK_ON,
                SoundEvents.NETHER_WOOD_BUTTON_CLICK_OFF,
                SoundEvents.NETHER_WOOD_BUTTON_CLICK_ON,
                SoundType.NETHER_WOOD_HANGING_SIGN,
                SoundEvents.NETHER_WOOD_FENCE_GATE_CLOSE,
                SoundEvents.NETHER_WOOD_FENCE_GATE_OPEN),
        BAMBOO_LIKE(SoundType.BAMBOO_WOOD,
                SoundEvents.BAMBOO_WOOD_DOOR_CLOSE,
                SoundEvents.BAMBOO_WOOD_DOOR_OPEN,
                SoundEvents.BAMBOO_WOOD_TRAPDOOR_CLOSE,
                SoundEvents.BAMBOO_WOOD_TRAPDOOR_OPEN,
                SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_OFF,
                SoundEvents.BAMBOO_WOOD_PRESSURE_PLATE_CLICK_ON,
                SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_OFF,
                SoundEvents.BAMBOO_WOOD_BUTTON_CLICK_ON,
                SoundType.BAMBOO_WOOD_HANGING_SIGN,
                SoundEvents.BAMBOO_WOOD_FENCE_GATE_CLOSE,
                SoundEvents.BAMBOO_WOOD_FENCE_GATE_OPEN);

        private final Function<String, WoodType> function;

        Sound(SoundType soundType, SoundEvent doorClose, SoundEvent doorOpen, SoundEvent trapdoorClose, SoundEvent trapdoorOpen, SoundEvent pressurePlateClickOff, SoundEvent pressurePlateClickOn, SoundEvent buttonClickOff, SoundEvent buttonClickOn, SoundType hangingSignSoundType, SoundEvent fenceGateClose, SoundEvent fenceGateOpen) {
            this.function = s -> {
                BlockSetType blockSetType = new BlockSetType(s, soundType, doorClose, doorOpen, trapdoorClose, trapdoorOpen, pressurePlateClickOff, pressurePlateClickOn, buttonClickOff, buttonClickOn);
                BlockSetType.register(blockSetType);
                return new WoodType(s, blockSetType, soundType, hangingSignSoundType, fenceGateClose, fenceGateOpen);
            };
        }
    }
}
