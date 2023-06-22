package com.teamaurora.borealib.core.mixin;

import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ItemTagsProvider.class)
public interface ItemTagsProviderAccessor {

    @Accessor
    Map<TagKey<Block>, TagKey<Item>> getTagsToCopy();
}
