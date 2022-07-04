package dev.tesseract.fabric;

import net.fabricmc.api.ModInitializer;

public class TesseractFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        dev.tesseract.Tesseract.init();
    }
}
