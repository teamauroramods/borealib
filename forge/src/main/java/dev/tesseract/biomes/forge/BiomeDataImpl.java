package dev.tesseract.biomes.forge;

import dev.tesseract.biomes.BiomeModifierRegistry;
import dev.tesseract.biomes.BiomeProperties;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

import java.util.Optional;

public class BiomeDataImpl implements BiomeModifierRegistry.BiomeData {

    private final Optional<ResourceKey<Biome>> key;
    private final ModifiableBiomeInfo.BiomeInfo.Builder parent;
    private final BiomeProperties properties;

    public BiomeDataImpl(Optional<ResourceKey<Biome>> key, ModifiableBiomeInfo.BiomeInfo.Builder parent){
        this.key = key;
        this.parent = parent;
        this.properties = new BiomePropertiesImpl(parent);
    }

    @Override
    public Optional<ResourceLocation> getKey() {
        return this.key.map(ResourceKey::location);
    }

    @Override
    public BiomeProperties getProperties() {
        return this.properties;
    }

    @Override
    public boolean hasTag(TagKey<Biome> tag) {
        MinecraftServer server = Pollen.getRunningServer();
        if (server != null) {
            Optional<? extends Registry<Biome>> registry = server.registryAccess().registry(Registry.BIOME_REGISTRY);
            if (registry.isPresent()) {
                Optional<Holder<Biome>> holder = registry.get().getHolder(this.key.get());
                if (holder.isPresent()) {
                    return holder.get().is(tag);
                }
            }
        }
        return false;
    }
}
