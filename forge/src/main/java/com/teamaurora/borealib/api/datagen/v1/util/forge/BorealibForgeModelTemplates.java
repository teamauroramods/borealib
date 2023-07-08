package com.teamaurora.borealib.api.datagen.v1.util.forge;

import com.teamaurora.borealib.api.base.v1.util.Mods;
import com.teamaurora.borealib.api.datagen.v1.util.ModelGeneratorHelper;
import com.teamaurora.borealib.core.Borealib;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureSlot;
import org.jetbrains.annotations.ApiStatus;

public class BorealibForgeModelTemplates {

    public static final TextureSlot LEAF_SLOT = ModelGeneratorHelper.slot("leaf");
    public static final TextureSlot LOG_SLOT = ModelGeneratorHelper.slot("log");
    public static final ModelTemplate VERTICAL_PLANKS = ModelGeneratorHelper.template(Borealib.location("block/vertical_planks"), TextureSlot.ALL);
    public static final ModelTemplate POST = ModelGeneratorHelper.template(Borealib.location("block/post"), TextureSlot.TEXTURE);
    public static final ModelTemplate VERTICAL_SLAB = ModelGeneratorHelper.template(Borealib.location("block/vertical_slab"), TextureSlot.SIDE, TextureSlot.TOP, TextureSlot.BOTTOM);
    public static final ModelTemplate HEDGE_POST = ModelGeneratorHelper.template(Borealib.location("block/hedge_post"), "_post", LEAF_SLOT, LOG_SLOT);
    public static final ModelTemplate HEDGE_EXTEND = ModelGeneratorHelper.template(Borealib.location("block/hedge_extend"), "_extend", LEAF_SLOT);
    public static final ModelTemplate HEDGE_SIDE = ModelGeneratorHelper.template(Borealib.location("block/hedge_side"), "_side",  LEAF_SLOT);
    public static final ModelTemplate LADDER = ModelGeneratorHelper.template(Borealib.location("block/template_ladder"), TextureSlot.TEXTURE);
}
