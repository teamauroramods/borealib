package com.teamaurora.magnetosphere.impl.content_registries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;

import java.util.List;
import java.util.stream.Collectors;

public record ContentRegistryFile<T>(List<RegistryEntry<T>> values, boolean replace) {

    public static <T> Codec<ContentRegistryFile<T>> codec(Codec<T> elementCodec) {
        Codec<List<RegistryEntry<T>>> valuesCodec = ExtraCodecs.nonEmptyList(Codec.unboundedMap(KeyEntry.CODEC, elementCodec).xmap(map -> {
            return map.entrySet().stream().map(e -> new RegistryEntry<>(e.getKey(), e.getValue())).toList();
        }, list1 -> {
            return list1.stream().collect(Collectors.toMap(RegistryEntry::key, RegistryEntry::object));
        }));
        return RecordCodecBuilder.create(instance -> instance.group(
                valuesCodec.fieldOf("values").forGetter(ContentRegistryFile::values),
                Codec.BOOL.optionalFieldOf("replace", false).forGetter(ContentRegistryFile::replace)
        ).apply(instance, ContentRegistryFile::new));
    }

    public record KeyEntry(ResourceLocation id, boolean tag, boolean required) {
        static final Codec<KeyEntry> CODEC = Codec.STRING.comapFlatMap(s -> {
            boolean required = s.endsWith("?");
            if (required) s = s.substring(0, s.length() - 1);
            if (s.startsWith("#")) {
                return ResourceLocation.read(s.substring(1)).map(location -> new KeyEntry(location, true, required));
            } else {
                return ResourceLocation.read(s).map(location -> new KeyEntry(location, false, required));
            }
        }, entry -> {
            String string = entry.tag ? "#" + entry.id : entry.id.toString();
            return entry.required ? string + "?" : string;
        });
    }

    public record RegistryEntry<T>(KeyEntry key, T object) {
    }
}
