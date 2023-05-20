package com.teamaurora.magnetosphere.api.resource_condition.v1;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public interface ResourceCondition {

    boolean test(JsonObject json) throws JsonParseException;
}