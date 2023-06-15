package com.teamaurora.borealib.api.event.creativetabs.v1;

import com.teamaurora.borealib.api.base.v1.event.Event;
import com.teamaurora.borealib.impl.event.creativetabs.CreativeTabEventsImpl;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Collection;

public final class CreativeTabEvents {

    public static final Event<ModifyEntriesAll> MODIFY_ENTRIES_ALL = Event.createLoop(ModifyEntriesAll.class);

    @FunctionalInterface
    public interface ModifyEntries {
        void onModify(FeatureFlagSet flags, CreativeModeTab.ItemDisplayParameters parameters, Output output, boolean canUseGameMasterBlocks);
    }

    @FunctionalInterface
    public interface ModifyEntriesAll {
        void modifyEntries(ResourceKey<CreativeModeTab> tabKey, CreativeModeTab tab, FeatureFlagSet flags, CreativeModeTab.ItemDisplayParameters parameters, Output output, boolean canUseGameMasterBlocks);
    }

    public static Event<CreativeTabEvents.ModifyEntries> modifyEntriesEvent(ResourceKey<CreativeModeTab> tab) {
        return CreativeTabEventsImpl.event(tab);
    }

    private CreativeTabEvents() {
    }

    public interface Output extends CreativeModeTab.Output {
        void acceptAfter(ItemStack after, ItemStack stack, CreativeModeTab.TabVisibility visibility);

        void acceptBefore(ItemStack before, ItemStack stack, CreativeModeTab.TabVisibility visibility);

        @Override
        default void accept(ItemStack stack, CreativeModeTab.TabVisibility visibility) {
            acceptAfter(ItemStack.EMPTY, stack, visibility);
        }

        default void acceptAfter(ItemStack after, ItemStack stack) {
            this.acceptAfter(after, stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        default void acceptAfter(ItemStack after, ItemLike item, CreativeModeTab.TabVisibility visibility) {
            this.acceptAfter(after, new ItemStack(item), visibility);
        }

        default void acceptAfter(ItemStack after, ItemLike item) {
            this.acceptAfter(after, new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        default void acceptAllAfter(ItemStack after, Collection<ItemStack> stacks, CreativeModeTab.TabVisibility visibility) {
            stacks.forEach((stack) -> acceptAfter(after, stack, visibility));
        }

        default void acceptAllAfter(ItemStack after, Collection<ItemStack> stacks) {
            this.acceptAllAfter(after, stacks, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        default void acceptAllItemsAfter(ItemLike after, Collection<ItemLike> stacks) {
            this.acceptAllAfter(after, stacks.stream().map(ItemStack::new).toList());
        }

        default void acceptAfter(ItemLike after, ItemStack stack) {
            this.acceptAfter(new ItemStack(after), stack);
        }

        default void acceptAfter(ItemLike after, ItemLike item, CreativeModeTab.TabVisibility visibility) {
            this.acceptAfter(new ItemStack(after), item, visibility);
        }

        default void acceptAfter(ItemLike after, ItemLike item) {
            this.acceptAfter(new ItemStack(after), item);
        }

        default void acceptAllAfter(ItemLike after, Collection<ItemStack> stacks, CreativeModeTab.TabVisibility visibility) {
            acceptAllAfter(new ItemStack(after), stacks, visibility);
        }

        default void acceptAllAfter(ItemLike after, Collection<ItemStack> stacks) {
            acceptAllAfter(new ItemStack(after), stacks);
        }

        default void acceptBefore(ItemStack before, ItemStack stack) {
            this.acceptBefore(before, stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        default void acceptBefore(ItemStack before, ItemLike item, CreativeModeTab.TabVisibility visibility) {
            this.acceptBefore(before, new ItemStack(item), visibility);
        }

        default void acceptBefore(ItemStack before, ItemLike item) {
            this.acceptBefore(before, new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        default void acceptAllBefore(ItemStack before, Collection<ItemStack> stacks, CreativeModeTab.TabVisibility visibility) {
            stacks.forEach((stack) -> acceptBefore(before, stack, visibility));
        }

        default void acceptAllBefore(ItemStack before, Collection<ItemStack> stacks) {
            this.acceptAllBefore(before, stacks, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        default void acceptBefore(ItemLike before, ItemStack stack) {
            this.acceptBefore(new ItemStack(before), stack);
        }

        default void acceptBefore(ItemLike before, ItemLike item, CreativeModeTab.TabVisibility visibility) {
            this.acceptBefore(new ItemStack(before), item, visibility);
        }

        default void acceptBefore(ItemLike before, ItemLike item) {
            this.acceptBefore(new ItemStack(before), item);
        }

        default void acceptAllBefore(ItemLike before, Collection<ItemStack> stacks, CreativeModeTab.TabVisibility visibility) {
            acceptAllBefore(new ItemStack(before), stacks, visibility);
        }

        default void acceptAllBefore(ItemLike before, Collection<ItemStack> stacks) {
            acceptAllBefore(new ItemStack(before), stacks);
        }

        default void acceptAllItemsBefore(ItemLike before, Collection<ItemLike> stacks) {
            this.acceptAllBefore(before, stacks.stream().map(ItemStack::new).toList());
        }
    }
}
