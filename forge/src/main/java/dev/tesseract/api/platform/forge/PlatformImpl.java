package dev.tesseract.api.platform.forge;

import dev.tesseract.api.platform.ModContainer;
import dev.tesseract.api.platform.Platform;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.util.thread.EffectiveSide;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public class PlatformImpl {

    public static boolean isDevelopment() {
        return !FMLLoader.isProduction();
    }

    public static boolean isProduction() {
        return FMLLoader.isProduction();
    }

    public static Platform.Env getEnv() {
        return Platform.Env.fromPlatform(FMLLoader.getDist());
    }

    public static boolean isOptifrickLoaded() {
        return isModLoaded("optifine");
    }

    public static BlockableEventLoop<?> getGameExecutor() {
        return LogicalSidedProvider.WORKQUEUE.get(EffectiveSide.get());
    }

    public static Path getGameDir() {
        return FMLPaths.GAMEDIR.get();
    }

    public static Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static Path getModsDir() {
        return FMLPaths.MODSDIR.get();
    }

    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    public static Stream<ModContainer> getMods() {
        return ModList.get().applyForEachModContainer(ModContainerImpl::new);
    }

    public static Optional<ModContainer> getMod(String modId) {
        return ModList.get().getModContainerById(modId).map(ModContainerImpl::new);
    }
}
