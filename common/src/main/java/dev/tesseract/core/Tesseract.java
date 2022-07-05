package dev.tesseract.core;

import dev.tesseract.api.platform.Platform;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class Tesseract {
    public static final String MOD_ID = "tesseract";
    public static final Platform PLATFORM = Platform.builder(MOD_ID)
            .clientInit(() -> Tesseract::clientInit)
            .clientPostInit(() -> Tesseract::clientPostInit)
            .commonInit(Tesseract::commonInit)
            .commonPostInit(Tesseract::commonPostInit)
            .dataInit(Tesseract::dataInit)
            .build();

    private static void clientInit() {
    }

    private static void clientPostInit(Platform.SetupContext ctx) {
    }

    private static void commonInit() {
    }

    private static void commonPostInit(Platform.SetupContext ctx) {
    }

    private static void dataInit(Platform.DataSetupContext ctx) {
    }
}
