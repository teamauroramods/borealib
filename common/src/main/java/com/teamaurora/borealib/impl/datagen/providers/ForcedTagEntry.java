package com.teamaurora.borealib.impl.datagen.providers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;
import java.util.function.Predicate;

@ApiStatus.Internal
public class ForcedTagEntry extends TagEntry {
	private final TagEntry parent;

	public ForcedTagEntry(TagEntry parent) {
		super(parent.id, true, parent.required);
		this.parent = parent;
	}

	@Override
	public <T> boolean build(TagEntry.Lookup<T> arg, Consumer<T> consumer) {
		return this.parent.build(arg, consumer);
	}

	@Override
	public boolean verifyIfPresent(Predicate<ResourceLocation> objectExistsTest, Predicate<ResourceLocation> tagExistsTest) {
		return true;
	}
}