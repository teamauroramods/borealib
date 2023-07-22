package com.teamaurora.borealib.api.registry.v1.extended;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.entity.schedule.ScheduleBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class EntityRegistryProvider extends ExtendedRegistryWrapperProvider<EntityType<?>> {

    private final RegistryWrapper.Provider<MemoryModuleType<?>> memoryModuleTypeRegistry;
    private final RegistryWrapper.Provider<SensorType<?>> sensorTypeRegistry;
    private final RegistryWrapper.Provider<Schedule> scheduleRegistry;
    private final RegistryWrapper.Provider<Activity> activityRegistry;

    private EntityRegistryProvider(RegistryWrapper.Provider<EntityType<?>> entityRegistry) {
        super(entityRegistry);
        this.memoryModuleTypeRegistry = RegistryWrapper.provider(Registries.MEMORY_MODULE_TYPE, entityRegistry.owner());
        this.sensorTypeRegistry = RegistryWrapper.provider(Registries.SENSOR_TYPE, entityRegistry.owner());
        this.scheduleRegistry = RegistryWrapper.provider(Registries.SCHEDULE, entityRegistry.owner());
        this.activityRegistry = RegistryWrapper.provider(Registries.ACTIVITY, entityRegistry.owner());
    }

    public static EntityRegistryProvider create(String owner) {
        return new EntityRegistryProvider(RegistryWrapper.provider(Registries.ENTITY_TYPE, owner));
    }

    public <R> RegistryReference<MemoryModuleType<R>> registerMemoryModuleType(String id) {
        return this.registerMemoryModuleType(id, null);
    }

    public <R> RegistryReference<MemoryModuleType<R>> registerMemoryModuleType(String id, @Nullable Codec<R> codec) {
        return this.memoryModuleTypeRegistry.register(id, () -> new MemoryModuleType<>(Optional.ofNullable(codec)));
    }

    public <R extends Sensor<?>> RegistryReference<SensorType<R>> registerSensorType(String id, Supplier<R> supplier) {
        return this.sensorTypeRegistry.register(id, () -> new SensorType<>(supplier));
    }

    public RegistryReference<Schedule> registerSchedule(String id, Consumer<ScheduleBuilder> builder) {
        return this.scheduleRegistry.register(id, () -> {
            Schedule schedule = new Schedule();
            builder.accept(new ScheduleBuilder(schedule));
            return schedule;
        });
    }

    public RegistryReference<Activity> registerActivity(ResourceLocation id) {
        return this.activityRegistry.register(id, () -> new Activity(id.getNamespace() + ":" + id));
    }

    public <R> RegistryReference<MemoryModuleType<R>> registerMemoryModuleType(ResourceLocation id) {
        return this.registerMemoryModuleType(id, null);
    }

    public <R> RegistryReference<MemoryModuleType<R>> registerMemoryModuleType(ResourceLocation id, @Nullable Codec<R> codec) {
        return this.memoryModuleTypeRegistry.register(id, () -> new MemoryModuleType<>(Optional.ofNullable(codec)));
    }

    public <R extends Sensor<?>> RegistryReference<SensorType<R>> registerSensorType(ResourceLocation id, Supplier<R> supplier) {
        return this.sensorTypeRegistry.register(id, () -> new SensorType<>(supplier));
    }

    public RegistryReference<Schedule> registerSchedule(ResourceLocation id, Consumer<ScheduleBuilder> builder) {
        return this.scheduleRegistry.register(id, () -> {
            Schedule schedule = new Schedule();
            builder.accept(new ScheduleBuilder(schedule));
            return schedule;
        });
    }

    public RegistryReference<Activity> registerActivity(String id) {
        return this.activityRegistry.register(id, () -> new Activity(this.activityRegistry.owner() + ":" + id));
    }

    public RegistryWrapper.Provider<MemoryModuleType<?>> getMemoryModuleTypeRegistry() {
        return memoryModuleTypeRegistry;
    }

    public RegistryWrapper.Provider<SensorType<?>> getSensorTypeRegistry() {
        return sensorTypeRegistry;
    }

    public RegistryWrapper.Provider<Schedule> getScheduleRegistry() {
        return scheduleRegistry;
    }

    public RegistryWrapper.Provider<Activity> getActivityRegistry() {
        return activityRegistry;
    }
}
