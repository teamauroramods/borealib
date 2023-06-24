package com.teamaurora.borealib.api.datagen.v1.providers;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.teamaurora.borealib.api.datagen.v1.BorealibPackOutput;
import com.teamaurora.borealib.api.datagen.v1.SimpleConditionalDataProvider;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * @author ebo2022
 * @since 1.0
 */
public abstract class BorealibAdvancementProvider extends SimpleConditionalDataProvider {

    private final PackOutput.PathProvider pathProvider;

    protected BorealibAdvancementProvider(BorealibPackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
    }

    /**
     * Registers all advancements to be generated.
     *
     * @param exporter An exporter to handle advancements to be registered
     */
    public abstract void registerAdvancements(Consumer<Advancement> exporter);

    /**
     * Creates an exporter that adds the specified conditions to all advancements registered by it.
     *
     * @param exporter   The original advancement exporter
     * @param conditions The resource condition to add
     * @return An exporter that adds the conditions to all advancements registered by it
     */
    protected Consumer<Advancement> withConditions(Consumer<Advancement> exporter, ResourceConditionProvider... conditions) {
        Preconditions.checkArgument(conditions.length > 0, "At least one resource condition required");
        return advancement -> {
            this.addConditions(advancement.getId(), conditions);
            exporter.accept(advancement);
        };
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        Set<ResourceLocation> ids = new HashSet<>();
        Set<Advancement> advancements = new HashSet<>();

        this.registerAdvancements(advancements::add);

        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (Advancement advancement : advancements) {
            if (!ids.add(advancement.getId()))
                throw new IllegalStateException("Duplicate advancement " + advancement.getId());
            JsonObject advancementJson = advancement.deconstruct().serializeToJson();
            this.injectConditions(advancement.getId(), advancementJson);
            futures.add(DataProvider.saveStable(output, advancementJson, this.pathProvider.json(advancement.getId())));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Advancements";
    }
}
