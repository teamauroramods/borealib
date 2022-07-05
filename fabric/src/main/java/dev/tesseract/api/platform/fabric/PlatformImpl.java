package dev.tesseract.api.platform.fabric;

import dev.tesseract.api.platform.Mod;
import dev.tesseract.api.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.jetbrains.annotations.ApiStatus;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApiStatus.Internal
public class PlatformImpl {
    private static final Map<String, Mod> mods = new ConcurrentHashMap<>();

    public static boolean isProduction() {
        return !FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static boolean isDevelopment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static Path getGameDirectory() {
        return FabricLoader.getInstance().getGameDir().toAbsolutePath().normalize();
    }

    public static Path getConfigFolder() {
        return FabricLoader.getInstance().getConfigDir().toAbsolutePath().normalize();
    }

    public static Path getModsFolder() {
        return getGameDirectory().resolve("mods");
    }

    public static Platform.Environment getAgnosticEnvironment() {
        return Platform.Environment.fromPlatform(getEnvironment());
    }

    public static EnvType getEnvironment() {
        return FabricLoader.getInstance().getEnvironmentType();
    }

    public static boolean isOptifrickLoaded() {
        return isModLoaded("optifabric");
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    public static Collection<String> getModIds() {
        return FabricLoader.getInstance().getAllMods().stream().map(ModContainer::getMetadata).map(ModMetadata::getId).collect(Collectors.toList());
    }

    public static Stream<Mod> getMods() {
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            getMod(mod.getMetadata().getId());
        }
        return mods.values().stream();
    }

    public static Mod getMod(String id) {
        return mods.computeIfAbsent(id, ModImpl::new);
    }
}
