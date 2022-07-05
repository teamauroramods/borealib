package dev.tesseract.api.platform.forge;

import dev.tesseract.api.platform.Mod;
import dev.tesseract.api.platform.Platform;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.forgespi.language.IModInfo;
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
        return FMLLoader.isProduction();
    }

    public static boolean isDevelopment() {
        return !FMLLoader.isProduction();
    }

    public static Path getGameDirectory() {
        return FMLPaths.GAMEDIR.get();
    }

    public static Path getConfigFolder() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static Path getModsFolder() {
        return FMLPaths.MODSDIR.get();
    }

    public static Platform.Environment getAgnosticEnvironment() {
        return Platform.Environment.fromPlatform(getEnvironment());
    }

    public static Dist getEnvironment() {
        return FMLEnvironment.dist;
    }

    public static boolean isOptifrickLoaded() {
        return isModLoaded("optifine");
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static Collection<String> getModIds() {
        return ModList.get().getMods().stream().map(IModInfo::getModId).collect(Collectors.toList());
    }

    public static Stream<Mod> getMods() {
        for (IModInfo mod : ModList.get().getMods()) {
            getMod(mod.getModId());
        }
        return mods.values().stream();
    }

    public static Mod getMod(String modId) {
        return mods.computeIfAbsent(modId, ModImpl::new);
    }
}
