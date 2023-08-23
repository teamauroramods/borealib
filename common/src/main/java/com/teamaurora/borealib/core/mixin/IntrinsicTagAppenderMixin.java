package com.teamaurora.borealib.core.mixin;

import com.teamaurora.borealib.core.extensions.IntrinsicTagAppenderExtension;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(IntrinsicHolderTagsProvider.IntrinsicTagAppender.class)
public class IntrinsicTagAppenderMixin<T> implements IntrinsicTagAppenderExtension<T> {
}
