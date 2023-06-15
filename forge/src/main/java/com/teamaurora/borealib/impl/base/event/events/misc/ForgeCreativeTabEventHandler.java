package com.teamaurora.borealib.impl.base.event.events.misc;

import com.teamaurora.borealib.api.base.v1.event.Event;
import com.teamaurora.borealib.api.event.creativetabs.v1.CreativeTabEvents;
import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.impl.event.creativetabs.CreativeTabEventsImpl;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Borealib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeCreativeTabEventHandler {

    @SubscribeEvent
    public static void onEvent(BuildCreativeModeTabContentsEvent event) {
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
        CreativeTabEvents.MODIFY_ENTRIES_ALL.invoker().modifyEntries(event.getTabKey(), event.getTab(), event.getFlags(), event.getParameters(), output, event.hasPermissions());
        Event<CreativeTabEvents.ModifyEntries> event1 = CreativeTabEventsImpl.get(event.getTabKey());
        if (event1 != null) {
            event1.invoker().onModify(event.getFlags(), event.getParameters(), output, event.hasPermissions());
        }
    }
}
