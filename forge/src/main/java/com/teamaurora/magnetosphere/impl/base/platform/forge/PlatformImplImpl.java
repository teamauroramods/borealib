package com.teamaurora.magnetosphere.impl.base.platform.forge;

import com.teamaurora.magnetosphere.api.base.v1.platform.ModContainer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.ApiStatus;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApiStatus.Internal
public class PlatformImplImpl {

    private static final Map<String, ModContainer> MOD_CONTAINERS = ModList.get().applyForEachModContainer(ModContainerImpl::new).collect(Collectors.toConcurrentMap(ModContainerImpl::getId, Function.identity()));

    public static boolean isForge() {
        return true;
    }

    public static boolean isFabric() {
        return false;
    }

    public static boolean isDevelopment() {
        return !FMLLoader.isProduction();
    }

    public static boolean isProduction() {
        return FMLLoader.isProduction();
    }

    public static boolean isClient() {
        return FMLLoader.getDist().isClient();
    }

    public static boolean isServer() {
        return FMLLoader.getDist().isDedicatedServer();
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
        return MOD_CONTAINERS.values().stream();
    }

    public static Optional<ModContainer> getMod(String modId) {
        return Optional.ofNullable(MOD_CONTAINERS.get(modId));
    }

    public static Optional<MinecraftServer> getRunningServer() {
        return Optional.ofNullable(ServerLifecycleHooks.getCurrentServer());
    }
}
