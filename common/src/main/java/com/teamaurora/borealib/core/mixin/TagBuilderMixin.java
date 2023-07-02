package com.teamaurora.borealib.core.mixin;

import com.teamaurora.borealib.core.extensions.TagBuilderExtension;
import net.minecraft.tags.TagBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TagBuilder.class)
public class TagBuilderMixin implements TagBuilderExtension {

    @Unique
    private boolean replace;

    @Override
    public void borealib$setReplace(boolean replace) {
        this.replace = replace;
    }

    @Override
    public boolean borealib$replace() {
        return this.replace;
    }
}
