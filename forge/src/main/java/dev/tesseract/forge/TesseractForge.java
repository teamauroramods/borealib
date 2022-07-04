package dev.tesseract.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.tesseract.Tesseract;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Tesseract.MOD_ID)
public class TesseractForge {
    public TesseractForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(Tesseract.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Tesseract.init();
    }
}
