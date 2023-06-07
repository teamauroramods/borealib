package com.teamaurora.borealib.impl.resource_condition.forge;

import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.*;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;

@ApiStatus.Internal
public class DefaultResourceConditionsImplImpl {

    private static final ResourceConditionProvider FALSE_CONDITION = wrap(FalseCondition.INSTANCE);
    private static final ResourceConditionProvider TRUE_CONDITION = wrap(TrueCondition.INSTANCE);

    public static ResourceConditionProvider and(ResourceConditionProvider... values) {
        return wrap(new AndCondition(Arrays.stream(values).map(ConditionWrapper::new).toArray(ICondition[]::new)));
    }

    public static ResourceConditionProvider FALSE() {
        return FALSE_CONDITION;
    }

    public static ResourceConditionProvider TRUE() {
        return TRUE_CONDITION;
    }

    public static ResourceConditionProvider not(ResourceConditionProvider value) {
        return wrap(new NotCondition(new ConditionWrapper(value)));
    }

    public static ResourceConditionProvider or(ResourceConditionProvider... values) {
        return wrap(new OrCondition(Arrays.stream(values).map(ConditionWrapper::new).toArray(ICondition[]::new)));
    }

    public static ResourceConditionProvider allModsLoaded(String... modIds) {
        return modIds.length == 1 ? wrap(new ModLoadedCondition(modIds[0])) : wrap(new AndCondition(Arrays.stream(modIds).map(ModLoadedCondition::new).toArray(ICondition[]::new)));
    }

    public static ResourceConditionProvider anyModsLoaded(String... modIds) {
        return modIds.length == 1 ? wrap(new ModLoadedCondition(modIds[0])) : wrap(new OrCondition(Arrays.stream(modIds).map(ModLoadedCondition::new).toArray(ICondition[]::new)));
    }

    private static ResourceConditionProvider wrap(ICondition condition) {
        return new ForgeResourceConditionProvider(condition);
    }

    private record ConditionWrapper(ResourceConditionProvider provider) implements ICondition {

        @Override
        public ResourceLocation getID() {
            return this.provider.getName();
        }

        @Override
        public boolean test(IContext context) {
            return false;
        }
    }
}
