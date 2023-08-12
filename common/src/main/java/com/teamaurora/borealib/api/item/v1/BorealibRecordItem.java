package com.teamaurora.borealib.api.item.v1;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.RecordItem;

import java.util.function.Supplier;

public class BorealibRecordItem extends RecordItem {

    private final Supplier<SoundEvent> soundSupplier;

    public BorealibRecordItem(int analogOutput, Supplier<SoundEvent> soundEvent, Properties properties, int lengthInSeconds) {
        super(analogOutput, null, properties, lengthInSeconds);
        this.soundSupplier = soundEvent;
    }

    @Override
    public SoundEvent getSound() {
        return this.soundSupplier.get();
    }
}
