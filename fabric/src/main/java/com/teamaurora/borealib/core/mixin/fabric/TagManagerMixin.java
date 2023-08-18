package com.teamaurora.borealib.core.mixin.fabric;

import com.teamaurora.borealib.impl.registry.fabric.RegistryWrapperImplImpl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TagManager.class)
public class TagManagerMixin {

    @Redirect(method = "getTagDir", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;getPath()Ljava/lang/String;"))
    private static String prefix(ResourceLocation instance) {
        return RegistryWrapperImplImpl.prefix(instance);
    }
}
