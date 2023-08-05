package com.teamaurora.borealib.core.mixin;

import com.google.gson.JsonElement;
import com.teamaurora.borealib.core.extensions.TagBuilderExtension;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

@Mixin(TagsProvider.class)
public class TagsProviderMixin {

    @Inject(method = "method_27046", at = @At(value = "INVOKE", target = "Lnet/minecraft/data/DataProvider;saveStable(Lnet/minecraft/data/CachedOutput;Lcom/google/gson/JsonElement;Ljava/nio/file/Path;)Ljava/util/concurrent/CompletableFuture;"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void addReplaced(Predicate<ResourceLocation> predicate, Predicate<ResourceLocation> predicate2, CachedOutput cachedOutput, Map.Entry<ResourceLocation, TagBuilder> entry, CallbackInfoReturnable<CompletableFuture<?>> cir, ResourceLocation identifier, TagBuilder builder, List<TagEntry> list, List<TagEntry> list2, JsonElement jsonElement, Path path) {
        if (builder instanceof TagBuilderExtension borealibTagBuilder) {
            jsonElement.getAsJsonObject().addProperty("replace", borealibTagBuilder.borealib$replace());
        }
    }
}
