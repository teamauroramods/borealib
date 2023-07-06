package com.teamaurora.borealib.api.datagen.v1.util.forge;

import com.teamaurora.borealib.api.datagen.v1.util.ModelGeneratorHelper;
import com.teamaurora.borealib.core.Borealib;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureSlot;
import org.jetbrains.annotations.ApiStatus;

public class BorealibForgeModelTemplates {

    public static final ModelTemplate VERTICAL_PLANKS = ModelGeneratorHelper.template(Borealib.location("block/vertical_planks"), TextureSlot.ALL);
    public static final ModelTemplate POST = ModelGeneratorHelper.template(Borealib.location("block/post"), TextureSlot.TEXTURE);
    public static final ModelTemplate VERTICAL_SLAB = ModelGeneratorHelper.template(Borealib.location("block/vertical_slab"), TextureSlot.SIDE, TextureSlot.TOP, TextureSlot.BOTTOM);
}
