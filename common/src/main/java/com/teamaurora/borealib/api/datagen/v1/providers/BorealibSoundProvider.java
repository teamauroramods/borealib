package com.teamaurora.borealib.api.datagen.v1.providers;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.teamaurora.borealib.api.datagen.v1.BorealibPackOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * @author ebo2022
 * @since 1.0
 */
public abstract class BorealibSoundProvider implements DataProvider {

    private final Path soundPath;

    protected BorealibSoundProvider(BorealibPackOutput output) {
        this.soundPath = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK).resolve( output.getModId() + "/sounds.json");
    }

    /**
     * Override to register custom sound definitions.
     *
     * @param registry A consumer to register sound definitions to
     */
    protected abstract void registerSoundDefinitions(Consumer<SoundDefinition> registry);

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        Set<SoundDefinition> sounds = new HashSet<>();
        Consumer<SoundDefinition> registry = sound -> {
            if (!sounds.add(sound))
                throw new IllegalStateException("Duplicate sound " + sound.soundId);
        };
        this.registerSoundDefinitions(registry);
        JsonObject json = new JsonObject();
        sounds.stream().sorted(Comparator.comparing(def -> def.soundId)).forEachOrdered(definition -> json.add(definition.soundId, definition.toJson()));
        return DataProvider.saveStable(cachedOutput, json, this.soundPath);
    }

    @Override
    public String getName() {
        return "Sound Definitions";
    }

    /**
     * Represents a single definition for a sound event in <code>sounds.json</code>.
     * <p>Sound definitions can contain one or several {@link SoundInstance}s which point to files to play and are picked randomly.
     *
     * @since 1.0
     */
    public static class SoundDefinition {

        private final String soundId;
        private final List<SoundInstance> sounds;
        private String subtitle;

        private SoundDefinition(String soundId) {
            this.soundId = soundId;
            this.sounds = new ArrayList<>();
        }

        private JsonObject toJson() {
            Preconditions.checkArgument(!this.sounds.isEmpty(), "At least one sound file must be defined");

            JsonObject json = new JsonObject();
            if (this.subtitle != null)
                json.addProperty("subtitle", this.subtitle);

            JsonArray soundsJson = new JsonArray();
            this.sounds.forEach(sound -> soundsJson.add(sound.toJson()));
            json.add("sounds", soundsJson);

            return json;
        }

        /**
         * Adds a new sound instance to the definition.
         *
         * @param sound The sound to add to the definition
         */
        public SoundDefinition addSound(SoundInstance sound) {
            this.sounds.add(sound);
            return this;
        }

        /**
         * Sets the translation key to use for the sound subtitle.
         *
         * @param subtitle The translation key for the sound in language files
         */
        public SoundDefinition subtitle(@Nullable String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        /**
         * Creates a sound definition with one unconfigured sound instance with the same file name as the sound event.
         *
         * @param sound The sound event to create a definition for
         * @return A new sound definition
         */
        public static SoundDefinition singleSound(Supplier<SoundEvent> sound) {
            return definition(sound).addSound(sound(sound));
        }

        /**
         * Creates a configurable definition for the given sound event.
         *
         * @param sound The sound to define
         * @return A new sound definition
         */
        public static SoundDefinition definition(Supplier<SoundEvent> sound) {
            return new SoundDefinition(sound.get().getLocation().getPath());
        }

        /**
         * Creates a configurable definition for the given sound name.
         *
         * @param sound The sound name to define
         * @return A new sound definition
         */
        public static SoundDefinition definition(String sound) {
            return new SoundDefinition(sound);
        }

        /**
         * Creates a sound instance with the same file path as that of the given sound event.
         *
         * @param sound The sound to create an instance of
         * @return A new sound instance
         */
        public static SoundInstance sound(Supplier<SoundEvent> sound) {
            return new SoundInstance(new ResourceLocation(sound.get().getLocation().getNamespace(), sound.get().getLocation().getPath().replaceAll("\\.", "/")));
        }

        /**
         * Creates a sound instance with the given file path.
         *
         * @param path The file path of the sound file
         * @return A new sound instance
         */
        public static SoundInstance sound(ResourceLocation path) {
            return new SoundInstance(path);
        }
    }

    /**
     * Represents a member sound file within a sound definition. These can be individually configured to have different properties.
     *
     * @since 1.0
     */
    public static class SoundInstance {

        private final ResourceLocation path;
        private float volume;
        private float pitch;
        private int weight;
        private SoundType type;
        private boolean preload;
        private boolean stream;
        private int attenuationDistance;

        private SoundInstance(ResourceLocation path) {
            this.path = path;
            this.volume = 1.0F;
            this.pitch = 1.0F;
            this.weight = 1;
            this.type = SoundType.FILE;
            this.preload = false;
            this.stream = false;
            this.attenuationDistance = 16;
        }

        private JsonElement toJson() {
            Preconditions.checkArgument(this.volume > 0.0F, "Invalid volume");
            Preconditions.checkArgument(this.pitch > 0.0F, "Invalid pitch");
            Preconditions.checkArgument(this.weight > 0, "Invalid weight");

            if (this.volume == 1.0F && this.pitch == 1.0F && this.weight == 1 && this.type == SoundType.FILE && !this.preload && !this.stream && this.attenuationDistance == 16)
                return new JsonPrimitive(this.path.toString());

            JsonObject json = new JsonObject();
            json.addProperty("name", this.path.toString());
            if (this.volume != 1.0F)
                json.addProperty("volume", this.volume);
            if (this.pitch != 1.0F)
                json.addProperty("pitch", this.pitch);
            if (this.weight != 1)
                json.addProperty("weight", this.weight);
            if (this.type != SoundType.FILE)
                json.addProperty("type", this.type.name().toLowerCase(Locale.ROOT));
            if (this.preload)
                json.addProperty("preload", true);
            if (this.stream)
                json.addProperty("stream", true);
            if (this.attenuationDistance != 16)
                json.addProperty("attenuation_distance", this.attenuationDistance);

            return json;
        }

        /**
         * Sets how loud the sound will play.
         *
         * @param volume The volume of the sound
         */
        public SoundInstance volume(float volume) {
            this.volume = volume;
            return this;
        }

        /**
         * Sets the pitch of the sound.
         *
         * @param pitch The pitch of the sound
         */
        public SoundInstance pitch(float pitch) {
            this.pitch = pitch;
            return this;
        }

        /**
         * Sets how often the sound will play relative to any others defined. This has no effect if there is only 1 sound in the definition.
         *
         * @param weight The relative weight of the sound
         */
        public SoundInstance weight(int weight) {
            this.weight = weight;
            return this;
        }

        /**
         * Sets the sound type, which is how the location will be interpreted by Minecraft when reading it.
         *
         * @param type The sound type
         */
        public SoundInstance type(SoundType type) {
            this.type = type;
            return this;
        }

        /**
         * Makes the finished sound instance reload along with other assets.
         */
        public SoundInstance preload() {
            this.preload = true;
            return this;
        }

        public SoundInstance preload(boolean preload) {
            this.preload = preload;
            return this;
        }

        /**
         * Makes the sound instance stream directly from the file. This means the sound cannot be modified in pitch or volume.
         */
        public SoundInstance stream() {
            this.stream = true;
            return this;
        }

        public SoundInstance stream(boolean stream) {
            this.stream = stream;
            return this;
        }

        /**
         * Sets how far players can hear the sound.
         *
         * @param attenuationDistance The distance players can hear the sound from
         */
        public SoundInstance attenuationDistance(int attenuationDistance) {
            this.attenuationDistance = attenuationDistance;
            return this;
        }
    }

    public enum SoundType {
        FILE,
        EVENT
    }
}
