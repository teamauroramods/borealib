package com.teamaurora.magnetosphere.impl.base.util.tabs.forge;

import com.teamaurora.magnetosphere.api.base.v1.event.Event;
import com.teamaurora.magnetosphere.api.base.v1.util.tabs.ModifyCreativeTabEvent;
import com.teamaurora.magnetosphere.core.Magnetosphere;
import com.teamaurora.magnetosphere.impl.base.util.tabs.ModifyCreativeTabEventImpl;
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
        Event<ModifyCreativeTabEvent> event1 = ModifyCreativeTabEventImpl.get(event.getTab());
        if (event1 != null) {
            event1.invoker().onModify(event.getFlags(), event.getParameters(), new ModifyCreativeTabEvent.Output() {
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
            }, event.hasPermissions());
        }
    }
}
