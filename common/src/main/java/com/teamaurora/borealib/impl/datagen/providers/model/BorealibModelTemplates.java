package com.teamaurora.borealib.impl.datagen.providers.model;

import com.teamaurora.borealib.api.datagen.v1.providers.model.ModelGeneratorHelper;
import com.teamaurora.borealib.core.Borealib;
import net.minecraft.data.models.model.ModelTemplate;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class BorealibModelTemplates {

    public static final ModelTemplate CHEST_ITEM = ModelGeneratorHelper.template(Borealib.location("item/template_chest")); // No texture slots, just empty
}
