package dev.tesseract.impl;

import dev.tesseract.api.base.v1.modloading.ModLoaderService;
import org.jetbrains.annotations.ApiStatus;

import java.util.ServiceLoader;

@ApiStatus.Internal
public class Tesseract {

    public static final String MOD_ID = "tesseract";

    public static ModLoaderService findMod(String id) {
        return ServiceLoader.load(ModLoaderService.class)
                .stream()
                .filter(p -> p.get().id().equals(id))
                .findFirst()
                .map(ServiceLoader.Provider::get)
                .orElseThrow(() -> new IllegalStateException("Couldn't find mod with the id" + id));
    }
}
