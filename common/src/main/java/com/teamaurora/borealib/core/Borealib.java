package com.teamaurora.borealib.core;

import com.teamaurora.borealib.api.base.v1.util.ParallelDispatcher;
import com.teamaurora.borealib.api.resource_condition.v1.DelegateResourceCondition;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionRegistry;
import com.teamaurora.borealib.core.registry.BorealibBlockEntityTypes;
import com.teamaurora.borealib.core.registry.BorealibEntityTypes;
import com.teamaurora.borealib.impl.biome.modifier.BuiltInBiomeModifierActions;
import com.teamaurora.borealib.impl.biome.modifier.BuiltInBiomeSelectors;
import com.teamaurora.borealib.impl.resource_condition.ConfigResourceCondition;
import com.teamaurora.borealib.impl.resource_condition.DefaultResourceConditionsImpl;
import com.teamaurora.borealib.impl.resource_condition.RegistryKeyExistsResourceCondition;
import com.teamaurora.borealib.impl.resource_condition.TestsEnabledResourceCondition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class Borealib {

    public static final String MOD_ID = "borealib";
    public static final boolean TESTS_ENABLED;
    public static final Logger LOGGER = LogManager.getLogger();

    static {
        TESTS_ENABLED = "true".equalsIgnoreCase(System.getProperty("borealib.enableTests"));
        if (TESTS_ENABLED)
            LogManager.getLogger().info("Borealib testing mode enabled!");
    }

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static void onCommonInit() {
        BorealibEntityTypes.ENTITIES.register();
        BorealibBlockEntityTypes.BLOCK_ENTITIES.register();
        BuiltInBiomeModifierActions.init();
        BuiltInBiomeSelectors.init();
        ResourceConditionRegistry.register(ConfigResourceCondition.NAME, new ConfigResourceCondition());
        ResourceConditionRegistry.register(RegistryKeyExistsResourceCondition.NAME, new RegistryKeyExistsResourceCondition());
        ResourceConditionRegistry.register(TestsEnabledResourceCondition.NAME, new TestsEnabledResourceCondition());
        ResourceConditionRegistry.register(DefaultResourceConditionsImpl.QUARK_FLAG, new DelegateResourceCondition(location("quark_flag"), new ResourceLocation("quark:flag"), (original, mapped) -> mapped.addProperty("flag", GsonHelper.getAsString(original, "flag"))));
        ResourceConditionRegistry.register(DefaultResourceConditionsImpl.WOODWORKS_FLAG, new DelegateResourceCondition(location("woodworks_flag"), new ResourceLocation("woodworks:config"), (original, mapped) -> mapped.addProperty("value", GsonHelper.getAsString(original, "value"))));
    }

    public static void onCommonPostInit(ParallelDispatcher dispatcher) { }
}
