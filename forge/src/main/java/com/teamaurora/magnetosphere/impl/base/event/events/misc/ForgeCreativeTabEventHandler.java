package com.teamaurora.magnetosphere.impl.base.event.events.misc;

import com.teamaurora.magnetosphere.api.base.v1.event.Event;
import com.teamaurora.magnetosphere.api.base.v1.event.events.misc.CreativeTabEvents;
import com.teamaurora.magnetosphere.core.Magnetosphere;
import com.teamaurora.magnetosphere.impl.base.event.events.misc.CreativeTabEventsImpl;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Magnetosphere.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeCreativeTabEventHandler {

    @SubscribeEvent
    public static void onEvent(CreativeModeTabEvent.BuildContents event) {
        CreativeTabEvents.Output output = new CreativeTabEvents.Output() {
            @Override
            public void acceptAfter(ItemStack after, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
                if (after.isEmpty()) {
                    event.getEntries().put(stack, visibility);
                } else {
                    event.getEntries().putAfter(after, stack, visibility);
                }
            }

            @Override
            public void acceptBefore(ItemStack before, ItemStack stack, CreativeModeTab.TabVisibility visibility) {
                if (before.isEmpty()) {
                    event.getEntries().put(stack, visibility);
                } else {
                    event.getEntries().putBefore(before, stack, visibility);
                }
            }
        };
        CreativeTabEvents.MODIFY_ENTRIES_ALL.invoker().modifyEntries(event.getTab(), event.getFlags(), event.getParameters(), output, event.hasPermissions());
        Event<CreativeTabEvents.ModifyEntries> event1 = CreativeTabEventsImpl.get(event.getTab());
        if (event1 != null) {
            event1.invoker().onModify(event.getFlags(), event.getParameters(), output, event.hasPermissions());
        }
    }
}
