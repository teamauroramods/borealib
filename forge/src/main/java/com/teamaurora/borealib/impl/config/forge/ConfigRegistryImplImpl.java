package com.teamaurora.borealib.impl.config.forge;

import com.teamaurora.borealib.api.base.v1.util.forge.ForgeHelper;
import com.teamaurora.borealib.api.config.v1.ConfigBuilder;
import com.teamaurora.borealib.api.config.v1.ModConfig;
import com.teamaurora.borealib.api.event.config.v1.ConfigEvents;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.ApiStatus;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@ApiStatus.Internal
public class ConfigRegistryImplImpl {

    private static final Map<String, Map<ModConfig.Type, ModConfig>> CONFIGS = new ConcurrentHashMap<>();
    private static final Set<String> registeredEvents = ConcurrentHashMap.newKeySet();

    public static <T> T register(String modId, ModConfig.Type type, String fileName, Function<ConfigBuilder, T> consumer) {
        ModLoadingContext context = ModLoadingContext.get();
        IEventBus bus = ForgeHelper.getEventBus(modId);

        Pair<T, ForgeConfigSpec> pair = new ConfigBuilderImpl(new ForgeConfigSpec.Builder()).configure(consumer);
        net.minecraftforge.fml.config.ModConfig config = new net.minecraftforge.fml.config.ModConfig(convert(type), pair.getRight(), context.getActiveContainer(), fileName);
        context.getActiveContainer().addConfig(config);

        if (registeredEvents.add(modId)) {
            bus.<ModConfigEvent.Loading>addListener(event -> {
                net.minecraftforge.fml.config.ModConfig modConfig = event.getConfig();
                get(modConfig.getModId(), convert(modConfig.getType())).ifPresent(c -> {
                    ConfigEvents.LOADING.invoker().accept(c);
                });
            });
            bus.<ModConfigEvent.Reloading>addListener(event -> {
                net.minecraftforge.fml.config.ModConfig modConfig = event.getConfig();
                get(modConfig.getModId(), convert(modConfig.getType())).ifPresent(c -> {
                    ConfigEvents.RELOADING.invoker().accept(c);
                });
            });
        }

        CONFIGS.computeIfAbsent(modId, __ -> new EnumMap<>(ModConfig.Type.class)).put(type, new ModConfigImpl(config));
        return pair.getLeft();
    }

    public static Optional<ModConfig> get(String modId, ModConfig.Type type) {
        return !CONFIGS.containsKey(modId) ? Optional.empty() : Optional.ofNullable(CONFIGS.get(modId).get(type));
    }

    public static net.minecraftforge.fml.config.ModConfig.Type convert(ModConfig.Type type) {
        return switch (type) {
            case COMMON -> net.minecraftforge.fml.config.ModConfig.Type.COMMON;
            case CLIENT -> net.minecraftforge.fml.config.ModConfig.Type.CLIENT;
            case SERVER -> net.minecraftforge.fml.config.ModConfig.Type.SERVER;
        };
    }

    public static ModConfig.Type convert(net.minecraftforge.fml.config.ModConfig.Type type) {
        return switch (type) {
            case COMMON -> ModConfig.Type.COMMON;
            case CLIENT -> ModConfig.Type.CLIENT;
            case SERVER -> ModConfig.Type.SERVER;
        };
    }
}
