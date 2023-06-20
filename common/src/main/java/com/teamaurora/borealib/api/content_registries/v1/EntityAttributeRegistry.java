package com.teamaurora.borealib.api.content_registries.v1;

import com.teamaurora.borealib.impl.content_registries.EntityAttributeRegistryImpl;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.Supplier;

/**
 * Used to register default attributes for living entities.
 *
 * @author ebo2022
 * @since 1.0
 */
public interface EntityAttributeRegistry {

    /**
     * Registers attributes for the specified entity.
     *
     * @param entity           The entity to add attributes for
     * @param attributeBuilder The attributes to add when the game is ready
     * @param <T>              The entity type
     */
    static <T extends LivingEntity> void register(Supplier<EntityType<T>> entity, Supplier<AttributeSupplier.Builder> attributeBuilder) {
        EntityAttributeRegistryImpl.register(entity, attributeBuilder);
    }
}
