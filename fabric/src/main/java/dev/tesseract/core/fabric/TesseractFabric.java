package dev.tesseract.core.fabric;

import dev.tesseract.core.Tesseract;
import net.fabricmc.api.ModInitializer;

public class TesseractFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Tesseract.INSTANCE.setup();
        TesseractFabricEvents.init();
    }
}
