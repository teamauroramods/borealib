package com.teamaurora.borealib.core.fabric;

import com.teamaurora.borealib.api.base.v1.modloading.fabric.DelegatedModInitializer;
import com.teamaurora.borealib.api.config.v1.ModConfig;
import com.teamaurora.borealib.api.event.creativetabs.v1.CreativeTabEvents;
import com.teamaurora.borealib.api.event.entity.v1.player.PlayerEvents;
import com.teamaurora.borealib.api.event.entity.v1.player.PlayerInteractionEvents;
import com.teamaurora.borealib.api.event.lifecycle.v1.LevelLifecycleEvents;
import com.teamaurora.borealib.api.event.lifecycle.v1.ServerLifecycleEvents;
import com.teamaurora.borealib.api.event.registry.v1.CommandRegistryEvent;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.impl.biome.modifier.fabric.FabricBiomeModifierLoader;
import com.teamaurora.borealib.impl.config.fabric.ConfigLoadingHelper;
import com.teamaurora.borealib.impl.config.fabric.ConfigTracker;
import com.teamaurora.borealib.impl.event.creativetabs.CreativeTabEventsImpl;
import com.teamaurora.borealib.impl.event.entity.fabric.BorealibTradesLoader;
import com.teamaurora.borealib.impl.resource_condition.fabric.DefaultResourceConditionsImplImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.LevelResource;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@ApiStatus.Internal
@SuppressWarnings("UnstableApiUsage")
public class BorealibFabric implements DelegatedModInitializer {

    static MinecraftServer server;
    private static final LevelResource SERVERCONFIG = new LevelResource("serverconfig");

    @Override
    public String id() {
        return Borealib.MOD_ID;
    }


    @Override
    public void onInitialize() {
        DelegatedModInitializer.super.onInitialize();
        DefaultResourceConditionsImplImpl.init();
        ConfigTracker.INSTANCE.loadConfigs(ModConfig.Type.COMMON, FabricLoader.getInstance().getConfigDir());
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            ConfigTracker.INSTANCE.loadConfigs(ModConfig.Type.CLIENT, FabricLoader.getInstance().getConfigDir());
        ServerLifecycleEvents.PRE_STARTING.register(server1 -> {
            com.teamaurora.borealib.core.fabric.BorealibFabric.server = server1;
            ConfigTracker.INSTANCE.loadConfigs(ModConfig.Type.SERVER, ConfigLoadingHelper.getServerConfigDirectory(server));
            BorealibTradesLoader.init();
            return true;
        });
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STARTING.register(server -> {
        });
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPING.register(server -> ServerLifecycleEvents.STOPPING.invoker().forServer(server));
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.SERVER_STOPPED.register(server1 -> {
            com.teamaurora.borealib.core.fabric.BorealibFabric.server = null;
            ConfigTracker.INSTANCE.unloadConfigs(ModConfig.Type.SERVER, ConfigLoadingHelper.getServerConfigDirectory(server1));
            ServerLifecycleEvents.STOPPED.invoker().forServer(server1);
            FabricBiomeModifierLoader.purgeOldModifiers();
        });
        CreativeTabEventsImpl.forEach((tab, event) -> {
            ItemGroupEvents.modifyEntriesEvent(tab).register(entries -> {
                event.invoker().onModify(entries.getEnabledFeatures(), entries.getContext(), wrapOutput(entries), entries.shouldShowOpRestrictedItems());
            });
        });
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entries) -> CreativeTabEvents.MODIFY_ENTRIES_ALL.invoker().modifyEntries(RegistryWrapper.CREATIVE_MODE_TABS.getResourceKey(group).orElseThrow(), group, entries.getEnabledFeatures(), entries.getContext(), wrapOutput(entries), entries.shouldShowOpRestrictedItems()));
        ServerWorldEvents.LOAD.register((server, world) -> LevelLifecycleEvents.LOAD.invoker().load(world));
        ServerWorldEvents.UNLOAD.register((server, world) -> LevelLifecycleEvents.UNLOAD.invoker().unload(world));
        EntitySleepEvents.ALLOW_SLEEPING.register(((player, sleepingPos) -> PlayerEvents.START_SLEEPING.invoker().startSleeping(player, sleepingPos)));
        UseItemCallback.EVENT.register((player, level, hand) -> PlayerInteractionEvents.RIGHT_CLICK_ITEM.invoker().interact(player, level, hand));
        UseBlockCallback.EVENT.register((player, level, hand, result) -> PlayerInteractionEvents.RIGHT_CLICK_BLOCK.invoker().interact(player, level, hand, result));
        AttackBlockCallback.EVENT.register((player, level, hand, pos, direction) -> PlayerInteractionEvents.LEFT_CLICK_BLOCK.invoker().interact(player, level, hand, pos, direction));
        UseEntityCallback.EVENT.register((player, world, hand, entity, entityHitResult) -> PlayerInteractionEvents.RIGHT_CLICK_ENTITY.invoker().interact(player, world, hand, entity));
        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, environment) -> CommandRegistryEvent.EVENT.invoker().registerCommands(dispatcher, buildContext, environment));
    }

    private static CreativeTabEvents.Output wrapOutput(FabricItemGroupEntries entries) {
        return new CreativeTabEvents.Output() {
            @Override
            public void acceptAfter(ItemStack after, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
                if (after.isEmpty()) {
                    entries.accept(stack, visibility);
                } else {
                    entries.addAfter(after, List.of(stack), visibility);
                }
            }

            @Override
            public void acceptBefore(ItemStack before, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
                if (before.isEmpty()) {
                    entries.accept(stack, visibility);
                } else {
                    entries.addBefore(before, List.of(stack), visibility);
                }
            }
        };
    }

    @Nullable
    public static MinecraftServer getServer() {
        return server;
    }
}
