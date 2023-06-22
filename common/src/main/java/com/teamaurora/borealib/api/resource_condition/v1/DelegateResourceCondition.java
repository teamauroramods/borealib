package com.teamaurora.borealib.api.resource_condition.v1;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.teamaurora.borealib.api.base.v1.platform.Platform;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

/**
 * A placeholder resource condition that can serialize another mod's condition without accessing its classes.
 *
 * @param id       The id the condition should be referred to in data files
 * @param actualId The id of the condition to actually use
 * @param mapper   A consumer mapping the contents of the first json object onto the second one. This is used to convert the data into the format the external condition can read.
 *
 * @author ebo2022
 * @since 1.0
 */
public record DelegateResourceCondition(ResourceLocation id, ResourceLocation actualId, BiConsumer<JsonObject, JsonObject> mapper) implements ResourceCondition {

    @Override
    public boolean test(JsonObject json) throws JsonParseException {
        if (Platform.isModLoaded(this.actualId.getNamespace())) {
            JsonObject dummyObject = new JsonObject();
            dummyObject.addProperty(ResourceConditionRegistry.getConditionsKey(), this.actualId.toString());
            this.mapper.accept(json, dummyObject);
            return ResourceConditionRegistry.getCondition(dummyObject).test(dummyObject);
        }
        return false;
    }
}
