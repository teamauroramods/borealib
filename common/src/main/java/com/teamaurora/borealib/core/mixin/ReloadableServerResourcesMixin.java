package com.teamaurora.borealib.core.mixin;

import com.teamaurora.borealib.impl.content_registries.TagAdder;
import com.teamaurora.borealib.impl.content_registries.TagRegistryImpl;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.tags.TagManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ReloadableServerResources.class)
public class ReloadableServerResourcesMixin {
	@ModifyVariable(method = "updateRegistryTags(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/tags/TagManager$LoadResult;)V", at = @At(value = "HEAD"), argsOnly = true)
	private static <T> TagManager.LoadResult<T> load(
			TagManager.LoadResult<T> value) {
		return TagRegistryImpl.handle(new TagAdder<>(value));
	}
}