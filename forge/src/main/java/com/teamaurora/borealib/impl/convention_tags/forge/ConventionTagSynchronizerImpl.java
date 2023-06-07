package com.teamaurora.borealib.impl.convention_tags.forge;

import com.teamaurora.borealib.api.content_registries.v1.TagRegistry;
import com.teamaurora.borealib.api.convention_tags.v1.PlatformBlockTags;
import com.teamaurora.borealib.api.convention_tags.v1.PlatformEntityTypeTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ConventionTagSynchronizerImpl {

    public static void init() {
        PlatformBlockTags.getAllTags().forEach(tagKey -> TagRegistry.syncTags(Registries.BLOCK, tagKey, TagRegistry.bindBlock(new ResourceLocation("forge", tagKey.location().getPath()))));

        TagRegistry.syncTags(Registries.ENTITY_TYPE, PlatformEntityTypeTags.BOSSES, Tags.EntityTypes.BOSSES);
    }
}