package com.teamaurora.borealib.api.convention_tags.v1;

import com.teamaurora.borealib.api.content_registries.v1.TagRegistry;
import com.teamaurora.borealib.core.Borealib;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public final class PlatformEntityTypeTags {

    public static final TagKey<EntityType<?>> BOSSES = tag("bosses");

    private PlatformEntityTypeTags() {
    }

    private static TagKey<EntityType<?>> tag(String path) {
        return TagRegistry.bindEntityType(Borealib.location(path));
    }
}