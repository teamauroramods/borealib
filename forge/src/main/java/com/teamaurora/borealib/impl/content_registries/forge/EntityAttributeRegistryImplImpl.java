package com.teamaurora.borealib.impl.content_registries.forge;

import com.teamaurora.borealib.core.Borealib;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ApiStatus.Internal
@Mod.EventBusSubscriber(modid = Borealib.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityAttributeRegistryImplImpl {

    private static final Set<Consumer<EntityAttributeCreationEvent>> ATTRIBUTE_FACTORIES = ConcurrentHashMap.newKeySet();

    @SubscribeEvent
    public static void onEvent(EntityAttributeCreationEvent event) {
        ATTRIBUTE_FACTORIES.forEach(consumer -> consumer.accept(event));
    }

    public static <T extends LivingEntity> void register(Supplier<EntityType<T>> entity, Supplier<AttributeSupplier.Builder> attributeBuilder) {
        ATTRIBUTE_FACTORIES.add(event -> event.put(entity.get(), attributeBuilder.get().build()));
    }
}
