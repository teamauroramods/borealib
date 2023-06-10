package com.teamaurora.borealib.impl.resource_condition;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceCondition;
import com.teamaurora.borealib.core.Borealib;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class TestsEnabledResourceCondition implements ResourceCondition {

    public static final ResourceLocation NAME = Borealib.location("tests_enabled");

    @Override
    public boolean test(JsonObject json) throws JsonParseException {
        return Borealib.TESTS_ENABLED;
    }
}
