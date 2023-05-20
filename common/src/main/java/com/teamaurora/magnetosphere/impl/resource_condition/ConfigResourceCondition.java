package com.teamaurora.magnetosphere.impl.resource_condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.teamaurora.magnetosphere.api.base.v1.util.NumberComparator;
import com.teamaurora.magnetosphere.api.config.v1.ConfigRegistry;
import com.teamaurora.magnetosphere.api.config.v1.ConfigValue;
import com.teamaurora.magnetosphere.api.config.v1.ModConfig;
import com.teamaurora.magnetosphere.api.resource_condition.v1.ResourceCondition;
import com.teamaurora.magnetosphere.api.resource_condition.v1.ResourceConditionProvider;
import com.teamaurora.magnetosphere.core.Magnetosphere;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.ApiStatus;

import java.util.Locale;
import java.util.Optional;

@ApiStatus.Internal
public class ConfigResourceCondition implements ResourceCondition {

    public static final ResourceLocation NAME = Magnetosphere.location("config");

    @Override
    public boolean test(JsonObject json) throws JsonParseException {
        ResourceLocation configId = new ResourceLocation(GsonHelper.getAsString(json, "config"));
        Optional<ModConfig> optional = ConfigRegistry.get(configId.getNamespace(), byName(configId.getPath()));
        if (optional.isEmpty() || optional.get().getConfigData() == null)
            return false;

        String configKey = GsonHelper.getAsString(json, "name");
        if (!json.has("value"))
            throw new JsonSyntaxException("Expected 'value'");

        Object entry = optional.get().getConfigData().get(configKey);
        if (entry == null)
            throw new JsonSyntaxException("Unknown config key: " + configKey);

        return testEntry(entry instanceof ConfigValue<?> ? ((ConfigValue<?>) entry).get() : entry, json, json.get("value"));
    }

    private static boolean testEntry(Object value, JsonObject json, JsonElement jsonValue) {
        if (value instanceof Number)
            return testNumber((Number) value, json, jsonValue);
        return testSimple(value, jsonValue);
    }

    private static ModConfig.Type byName(String name) {
        for (ModConfig.Type type : ModConfig.Type.values())
            if (type.name().toLowerCase(Locale.ROOT).equals(name))
                return type;
        throw new JsonSyntaxException("Unknown config type: " + name);
    }

    private static boolean testSimple(Object entry, JsonElement value) {
        return String.valueOf(entry).equals(toString(value));
    }

    private static String toString(JsonElement json) {
        if (json == null || json.isJsonNull())
            return "null";
        if (json.isJsonPrimitive())
            return json.getAsString();
        throw new JsonSyntaxException("Unsupported generic config type: " + GsonHelper.getType(json));
    }

    private static boolean testNumber(Number entry, JsonObject json, JsonElement value) {
        if (!value.isJsonPrimitive() || !value.getAsJsonPrimitive().isNumber())
            throw new JsonSyntaxException("Expected Number, got " + GsonHelper.getType(value));
        JsonPrimitive primitiveValue = value.getAsJsonPrimitive();
        NumberComparator compareMode = json.has("comparator") ? NumberComparator.byName(GsonHelper.getAsString(json, "mode")) : NumberComparator.EQUAL;
        return compareMode.test(entry, primitiveValue.getAsNumber());
    }

    public static class SimpleProvider implements ResourceConditionProvider {

        private final String modId;
        private final ModConfig.Type type;
        private final String key;
        private final String value;

        public SimpleProvider(String modId, ModConfig.Type type, String key, Object value) {
            this.modId = modId;
            this.type = type;
            this.key = key;
            this.value = String.valueOf(value);
        }

        @Override
        public void write(JsonObject json) {
            json.addProperty("config", this.modId + ":" + this.type.name().toLowerCase(Locale.ROOT));
            json.addProperty("name", this.key);
            json.addProperty("value", this.value);
        }

        @Override
        public ResourceLocation getName() {
            return NAME;
        }
    }

    public static class NumberProvider extends SimpleProvider {

        private final NumberComparator mode;

        public NumberProvider(String modId, ModConfig.Type type, String key, Number value, NumberComparator mode) {
            super(modId, type, key, value);
            this.mode = mode;
        }

        @Override
        public void write(JsonObject json) {
            super.write(json);
            if (this.mode != NumberComparator.EQUAL)
                json.addProperty("comparator", this.mode.getSymbol());
        }
    }
}