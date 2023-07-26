package com.teamaurora.borealib.api.datagen.v1.providers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamaurora.borealib.api.base.v1.platform.ModContainer;
import com.teamaurora.borealib.api.registry.v1.RegistryReference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Generates translation keys for language files.
 *
 * @author ebo2022
 * @since 1.0
 */
public abstract class BorealibLanguageProvider implements DataProvider {

    protected final PackOutput.PathProvider pathProvider;
    private final String domain;
    private final String locale;

    /**
     * Generates a language provider with the <code>en_us</code> locale.
     *
     * @param output The data output generator
     */
    protected BorealibLanguageProvider(PackOutput output, ModContainer modContainer) {
        this(output, modContainer, "en_us");
    }

    /**
     * Generates a language provider for the given locale.
     *
     * @param output The data output generator
     * @param locale The language code
     */
    protected BorealibLanguageProvider(PackOutput output, ModContainer container, String locale) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "lang");
        this.domain = container.getId();
        this.locale = locale;
    }

    /**
     * Generates all translation keys for the generator.
     *
     * @param registry The registry to add translation keys to
     */
    public abstract void generateLanguage(TranslationRegistry registry);

    @Override
    public CompletableFuture<?> run(CachedOutput writer) {
        TreeMap<String, String> translationEntries = new TreeMap<>();
        this.generateLanguage((key, value) -> {
            if (translationEntries.containsKey(key))
                throw new RuntimeException("Existing translation key found - " + key + " - Duplicate will be ignored.");
            translationEntries.put(key, value);
        });
        JsonObject langEntryJson = new JsonObject();
        for (Map.Entry<String, String> entry : translationEntries.entrySet())
            langEntryJson.addProperty(entry.getKey(), entry.getValue());
        return DataProvider.saveStable(writer, langEntryJson, this.pathProvider.json(new ResourceLocation(this.domain, this.locale)));
    }

    @Override
    public String getName() {
        return "Language (" + this.locale + ")";
    }

    /**
     * Used to register language keys.
     *
     * @since 1.0.0
     */
    @ApiStatus.NonExtendable
    @FunctionalInterface
    public interface TranslationRegistry {

        /**
         * Adds a translation.
         *
         * @param translationKey The key of the translation
         * @param value          The value of the entry
         */
        void add(String translationKey, String value);

        /**
         * Adds a translation for an {@link Item}.
         *
         * @param item  The {@link Item} to get the translation key from
         * @param value The value of the entry
         */
        default void add(Item item, String value) {
            this.add(item.getDescriptionId(), value);
        }

        /**
         * Adds a translation for a {@link Block}.
         *
         * @param block The {@link Block} to get the translation key from
         * @param value The value of the entry
         */
        default void add(Block block, String value) {
            this.add(block.getDescriptionId(), value);
        }

        /**
         * Adds a translation for an {@link CreativeModeTab}.
         *
         * @param registryKey The {@link ResourceKey} to get the translation key from
         * @param value The value of the entry
         */
        default void add(ResourceKey<CreativeModeTab> registryKey, String value) {
            final CreativeModeTab group = BuiltInRegistries.CREATIVE_MODE_TAB.getOrThrow(registryKey);
            final ComponentContents content = group.getDisplayName().getContents();
            if (content instanceof TranslatableContents translatableTextContent) {
                this.add(translatableTextContent.getKey(), value);
                return;
            }
            throw new UnsupportedOperationException("Cannot add language entry for ItemGroup (%s) as the display name is not translatable.".formatted(group.getDisplayName().getString()));
        }

        /**
         * Adds a translation for an {@link EntityType}.
         *
         * @param entityType The {@link EntityType} to get the translation key from
         * @param value      The value of the entry
         */
        default void add(EntityType<?> entityType, String value) {
            this.add(entityType.getDescriptionId(), value);
        }

        /**
         * Adds a translation for an {@link Enchantment}.
         *
         * @param enchantment The {@link Enchantment} to get the translation key from
         * @param value       The value of the entry
         */
        default void add(Enchantment enchantment, String value) {
            this.add(enchantment.getDescriptionId(), value);
        }

        /**
         * Adds a translation for an {@link Attribute}.
         *
         * @param entityAttribute The {@link Attribute} to get the translation key from
         * @param value           The value of the entry
         */
        default void add(Attribute entityAttribute, String value) {
            this.add(entityAttribute.getDescriptionId(), value);
        }

        /**
         * Adds a translation for a {@link StatType}.
         *
         * @param statType The {@link StatType} to get the translation key from
         * @param value    The value of the entry
         */
        default void add(StatType<?> statType, String value) {
            this.add(statType.getTranslationKey(), value);
        }

        /**
         * Adds a translation for a {@link MobEffect}.
         *
         * @param statusEffect The {@link MobEffect} to get the translation key from
         * @param value        The value of the entry
         */
        default void add(MobEffect statusEffect, String value) {
            this.add(statusEffect.getDescriptionId(), value);
        }

        /**
         * Adds a translation for an {@link ResourceLocation}.
         *
         * @param identifier The {@link ResourceLocation} to get the translation key from.
         * @param value      The value of the entry.
         */
        default void add(ResourceLocation identifier, String value) {
            this.add(identifier.toLanguageKey(), value);
        }

        /**
         * Merges an existing language file into the generated language file.
         *
         * @param existingLanguageFile The path to the existing language file
         * @throws IOException if loading the language file failed
         */
        default void add(Path existingLanguageFile) throws IOException {
            try (Reader reader = Files.newBufferedReader(existingLanguageFile)) {
                JsonObject translations = JsonParser.parseReader(reader).getAsJsonObject();

                for (String key : translations.keySet()) {
                    add(key, translations.get(key).getAsString());
                }
            }
        }
    }
}