package com.teamaurora.borealib.core.extensions;

import com.teamaurora.borealib.impl.datagen.providers.ForcedTagEntry;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;

import java.util.Objects;

public interface IntrinsicTagAppenderExtension<T> {

    @SuppressWarnings("unchecked")
    default IntrinsicHolderTagsProvider.IntrinsicTagAppender<T> forceAddTag(TagKey<T> tag) {
        ((IntrinsicHolderTagsProvider.IntrinsicTagAppender<T>) this).builder.add(new ForcedTagEntry(TagEntry.tag(tag.location())));
        return (IntrinsicHolderTagsProvider.IntrinsicTagAppender<T>) this;
    }
}
