package com.teamaurora.borealib.impl.convention_tags.forge;

import com.teamaurora.borealib.api.content_registries.v1.TagRegistry;
import com.teamaurora.borealib.api.convention_tags.v1.MagnetosphereBlockTags;
import com.teamaurora.borealib.api.convention_tags.v1.MagnetosphereEntityTypeTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ConventionTagSynchronizerImpl {

    public static void init() {
        MagnetosphereBlockTags.getAllTags().forEach(tagKey -> TagRegistry.syncTags(Registries.BLOCK, tagKey, TagRegistry.bindBlock(new ResourceLocation("forge", tagKey.location().getPath()))));

        TagRegistry.syncTags(Registries.ENTITY_TYPE, MagnetosphereEntityTypeTags.BOSSES, Tags.EntityTypes.