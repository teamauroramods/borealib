package com.teamaurora.borealib.api.event.registry.v1;

import com.mojang.brigadier.CommandDispatcher;
import com.teamaurora.borealib.api.base.v1.event.Event;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

/**
 * Wraps platform-specific code to register commands at the proper time.
 *
 * @author ebo2022
 * @since 1.0
 */
@FunctionalInterface
public interface CommandRegistryEvent {

    Event<CommandRegistryEvent> EVENT = Event.createLoop(CommandRegistryEvent.class);

    /**
     * Called to register the modded commands.
     *
     * @param dispatcher   The dispatcher instance ready to accept new commands
     * @param buildContext Context to provide access to registries
     * @param selection    The current command environment; register different sets of commands according to this
     */
    void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection selection);
}