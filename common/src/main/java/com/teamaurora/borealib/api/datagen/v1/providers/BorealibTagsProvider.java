/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.teamaurora.borealib.api.datagen.v1.providers;

import com.teamaurora.borealib.api.datagen.v1.BorealibPackOutput;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import com.teamaurora.borealib.core.extensions.TagBuilderExtension;
import com.teamaurora.borealib.impl.datagen.providers.ForcedTagEntry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @param <T> The type of tag to generate
 * @author ebo2022
 * @since 1.0
 */
public abstract class BorealibTagsProvider<T> extends TagsProvider<T> {

	public BorealibTagsProvider(BorealibPackOutput output, ResourceKey<? extends Registry<T>> registry, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registry, registriesFuture);
	}

	/**
	 * Registers the tags to be generated.
	 *
	 * @param registries The existing registries to use
	 */
	@Override
	protected abstract void addTags(HolderLookup.Provider registries);

	/**
	 * Used to get a {@link ResourceKey} for the given object which allows direct values to be defined in the tag appender. This can be overriden.
	 *
	 * @param element The element to get a key for
	 * @return The resource key if it exists
	 */
	@Nullable
	protected ResourceKey<T> reverseLookup(T element) {
		RegistryWrapper<T> registryWrapper = RegistryWrapper.get(this.registryKey);
		if (registryWrapper != null) {
			return registryWrapper.getResourceKey(element).orElseThrow();
		}
		throw new UnsupportedOperationException("Adding intrinsic objects is not supported by " + this.getClass());
	}

	@Override
	public BorealibTagAppender<T> tag(TagKey<T> tag) {
		return new BorealibTagAppender<>(super.tag(tag), this);
	}

	/**
	 * A specialized tag provider for blocks.
	 *
	 * @since 1.0
	 */
	public abstract static class BlockTagProvider extends BorealibTagsProvider<Block> {
		public BlockTagProvider(BorealibPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
			super(output, Registries.BLOCK, registriesFuture);
		}

		@Override
		protected ResourceKey<Block> reverseLookup(Block element) {
			return element.builtInRegistryHolder().key();
		}
	}

	/**
	 * A specialized tag provider for items, with the ability to copy block tag entries over to item tags.
	 *
	 * @since 1.0
	 */
	public abstract static class ItemTagProvider extends BorealibTagsProvider<Item> {

		@Nullable
		private final Function<TagKey<Block>, TagBuilder> blockTagBuilderProvider;

		public ItemTagProvider(BorealibPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable BorealibTagsProvider.BlockTagProvider blockTagProvider) {
			super(output, Registries.ITEM, completableFuture);
			this.blockTagBuilderProvider = blockTagProvider == null ? null : blockTagProvider::getOrCreateRawBuilder;
		}

		public ItemTagProvider(BorealibPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
			this(output, completableFuture, null);
		}

		/**
		 * Copies the entries from the specified block tag into the item tag.
		 * <p>The {@link ItemTagProvider} must be constructed with an associated {@link BlockTagProvider} tag provider to use this method.
		 *
		 * @param blockTag The block tag to copy from
		 * @param itemTag  The item tag to copy to
		 */
		public void copy(TagKey<Block> blockTag, TagKey<Item> itemTag) {
			TagBuilder blockTagBuilder = Objects.requireNonNull(this.blockTagBuilderProvider, "Pass Block tag provider via constructor to use copy").apply(blockTag);
			TagBuilder itemTagBuilder = this.getOrCreateRawBuilder(itemTag);
			blockTagBuilder.build().forEach(itemTagBuilder::add);
		}

		@Override
		protected ResourceKey<Item> reverseLookup(Item element) {
			return element.builtInRegistryHolder().key();
		}
	}

	/**
	 * A specialized tag provider for fluids.
	 *
	 * @since 1.0
	 */
	public abstract static class FluidTagProvider extends BorealibTagsProvider<Fluid> {
		public FluidTagProvider(BorealibPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
			super(output, Registries.FLUID, completableFuture);
		}

		@Override
		protected ResourceKey<Fluid> reverseLookup(Fluid element) {
			return element.builtInRegistryHolder().key();
		}
	}

	/**
	 * A specialized tag provider for enchantments.
	 *
	 * @since 1.0
	 */
	public abstract static class EnchantmentTagProvider extends BorealibTagsProvider<Enchantment> {
		public EnchantmentTagProvider(BorealibPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
			super(output, Registries.ENCHANTMENT, completableFuture);
		}

		@Override
		protected ResourceKey<Enchantment> reverseLookup(Enchantment element) {
			return RegistryWrapper.ENCHANTMENT.getResourceKey(element)
					.orElseThrow(() -> new IllegalArgumentException("Enchantment " + element + " is not registered"));
		}
	}

	/**
	 * A specialized tag provider for entities.
	 *
	 * @since 1.0
	 */
	public abstract static class EntityTypeTagProvider extends BorealibTagsProvider<EntityType<?>> {
		public EntityTypeTagProvider(BorealibPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
			super(output, Registries.ENTITY_TYPE, completableFuture);
		}

		@Override
		protected ResourceKey<EntityType<?>> reverseLookup(EntityType<?> element) {
			return element.builtInRegistryHolder().key();
		}
	}

	/**
	 * A specialized tag provider for game events.
	 *
	 * @since 1.0
	 */
	public abstract static class GameEventTagProvider extends BorealibTagsProvider<GameEvent> {
		public GameEventTagProvider(BorealibPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
			super(output, Registries.GAME_EVENT, completableFuture);
		}

		@Override
		protected ResourceKey<GameEvent> reverseLookup(GameEvent element) {
			return element.builtInRegistryHolder().key();
		}
	}

	/**
	 * Wraps the vanilla tag builder for extra functionality.
	 *
	 * @since 1.0
	 */
	public static final class BorealibTagAppender<T> extends TagAppender<T> {
		private final TagAppender<T> parent;
		private final BorealibTagsProvider<T> provider;

		private BorealibTagAppender(TagAppender<T> parent, BorealibTagsProvider<T> provider) {
			super(parent.builder);
			this.parent = parent;
			this.provider = provider;
		}

		/**
		 * Sets the `replace` flag in the resultant tag file to the specified value.
		 * <p>When set to true, the tag file will replace any existing tag entries.
		 *
		 * @param replace The new replace value
		 */
		public BorealibTagAppender<T> setReplace(boolean replace) {
			((TagBuilderExtension) this.builder).borealib$setReplace(replace);
			return this;
		}

		/**
		 * Adds an intrinsic element to the tag file.
		 *
		 * @param element The element to add
		 */
		public BorealibTagAppender<T> add(T element) {
			this.add(this.provider.reverseLookup(element));
			return this;
		}

		/**
		 * Adds multiple intrinsic elements to the tag file.
		 *
		 * @param elements The elements to add
		 */
		@SafeVarargs
		public final BorealibTagAppender<T> add(T... elements) {
			Stream.of(elements).map(this.provider::reverseLookup).forEach(this::add);
			return this;
		}

		/**
		 * Adds an element to the tag.
		 *
		 * @param element The element to add
		 */
		@Override
		public BorealibTagAppender<T> add(ResourceKey<T> element) {
			this.parent.add(element);
			return this;
		}

		/**
		 * Directly adds a {@link ResourceLocation} to the tag file.
		 *
		 * @param id The id to add
		 */
		public BorealibTagAppender<T> addDirect(ResourceLocation id) {
			this.builder.addElement(id);
			return this;
		}

		/**
		 * Adds an optional {@link ResourceLocation} to the tag file.
		 *
		 * @param id The optional id to add
		 */
		@Override
		public BorealibTagAppender<T> addOptional(ResourceLocation id) {
			this.parent.addOptional(id);
			return this;
		}

		/**
		 * Add an optional {@link ResourceKey} element to the tag file.
		 *
		 * @param element The key to add
		 */
		public BorealibTagAppender<T> addOptional(ResourceKey<? extends T> element) {
			return this.addOptional(element.location());
		}

		/**
		 * Adds another tag to the tag file.
		 *
		 * @param tag The tag to add
		 */
		@Override
		public BorealibTagAppender<T> addTag(TagKey<T> tag) {
			this.builder.addTag(tag.location());
			return this;
		}

		/**
		 * Add another optional tag to the tag file.
		 *
		 * @param id The id of the optional tag to add
		 */
		@Override
		public BorealibTagAppender<T> addOptionalTag(ResourceLocation id) {
			this.parent.addOptionalTag(id);
			return this;
		}

		/**
		 * Add another optional tag to the tag file.
		 *
		 * @param tag The optional tag to add
		 */
		public BorealibTagAppender<T> addOptionalTag(TagKey<T> tag) {
			return this.addOptionalTag(tag.location());
		}

		/**
		 * Force-adds the specified tag to the tag file ignoring any potential warnings. It is expected that this tag is present by default.
		 *
		 * @param tag The tag to force-add
		 */
		public BorealibTagAppender<T> forceAddTag(TagKey<T> tag) {
			builder.add(new ForcedTagEntry(TagEntry.tag(tag.location())));
			return this;
		}

		/**
		 * Adds multiple elements to the tag file.
		 *
		 * @param ids The ids to add
		 */
		public BorealibTagAppender<T> add(ResourceLocation... ids) {
			for (ResourceLocation id : ids)
				this.add(id);
			return this;
		}

		/**
		 * Adds multiple elements to this tag.
		 *
		 * @param elements The elements to add
		 */
		@SafeVarargs
		@Override
		public final BorealibTagAppender<T> add(ResourceKey<T>... elements) {
			for (ResourceKey<T> element : elements)
				add(element);
			return this;
		}
	}
}