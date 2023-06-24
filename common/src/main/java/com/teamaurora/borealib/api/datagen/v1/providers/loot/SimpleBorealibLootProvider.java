package com.teamaurora.borealib.api.datagen.v1.providers.loot;

import com.teamaurora.borealib.api.datagen.v1.BorealibPackOutput;
import com.teamaurora.borealib.impl.datagen.providers.loot.BorealibLootProviderImpl;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * A simple loot provider that works with any parameter set. Inheritor classes should override {@link #generate(BiConsumer)} to register their loot tables.
 *
 * @author ebo2022
 * @since 1.0
 */
public abstract class SimpleBorealibLootProvider implements BorealibLootProvider {

    protected final PackOutput.PathProvider pathProvider;
    protected final LootContextParamSet paramSet;

    protected SimpleBorealibLootProvider(BorealibPackOutput output, LootContextParamSet paramSet) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "loot_tables");
        this.paramSet = paramSet;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        return BorealibLootProviderImpl.run(cachedOutput, this, this.paramSet, this.pathProvider);
    }

    @Override
    public String getName() {
        return WordUtils.capitalizeFully(Objects.requireNonNull(LootContextParamSets.getKey(this.paramSet), "Unknown loot param set").getPath().replace("_", " ")) + " Loot Tables";
    }
}
