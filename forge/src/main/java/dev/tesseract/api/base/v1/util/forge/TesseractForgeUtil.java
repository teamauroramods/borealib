package dev.tesseract.api.base.v1.util.forge;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;

public interface TesseractForgeUtil {

    /**
     * Gets the event bus for the specified mod id.
     *
     * @param modId The mod id
     * @return An event bus for the given mod id
     */
    static IEventBus getEventBus(String modId) {
        return ((FMLModContainer) ModList.get().getModContainerById(modId).orElseThrow(() -> new IllegalArgumentException("Invalid mod: " + modId))).getEventBus();
    }
}
