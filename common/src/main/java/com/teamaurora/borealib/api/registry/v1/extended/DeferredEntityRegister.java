package com.teamaurora.borealib.api.registry.v1.extended;

import com.mojang.serialization.Codec;
import com.teamaurora.borealib.api.registry.v1.DeferredRegister;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
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

public final class DeferredEntityRegister extends ExtendedDeferredRegister<EntityType<?>> {

    private final DeferredRegister<MemoryModuleType<?>> memoryModuleTypeRegistry;
    private final DeferredRegister<SensorType<?>> sensorTypeRegistry;
    private final DeferredRegister<Schedule> scheduleRegistry;
    private final DeferredRegister<Activity> activityRegistry;

    private DeferredEntityRegister(DeferredRegister<EntityType<?>> entityRegistry) {
        super(entityRegistry);
        this.memoryModuleTypeRegistry = DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, entityRegistry.id());
        this.sensorTypeRegistry = DeferredRegister.create(Registries.SENSOR_TYPE, entityRegistry.id());
        this.scheduleRegistry = DeferredRegister.create(Registries.SCHEDULE, entityRegistry.id());
        this.activityRegistry = DeferredRegister.create(Registries.ACTIVITY, entityRegistry.id());
    }

    public static DeferredEntityRegister create(String modId) {
        return new DeferredEntityRegister(DeferredRegister.create(Registries.ENTITY_TYPE, modId));
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
        return this.activityRegistry.register(id, () -> new Activity(this.activityRegistry.id() + ":" + id));
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
        return this.activityRegistry.register(id, () -> new Activity(this.activityRegistry.id() + ":" + id));
    }

    public DeferredRegister<MemoryModuleType<?>> getMemoryModuleTypeRegistry() {
        return memoryModuleTypeRegistry;
    }

    public DeferredRegister<SensorType<?>> getSensorTypeRegistry() {
        return sensorTypeRegistry;
    }

    public DeferredRegister<Schedule> getScheduleRegistry() {
        return scheduleRegistry;
    }

    public DeferredRegister<Activity> getActivityRegistry() {
        return activityRegistry;
    }
}
