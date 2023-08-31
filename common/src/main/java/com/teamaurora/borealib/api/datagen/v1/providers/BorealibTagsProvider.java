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
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
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

/**
 * @param <T> The type of tag to generate
 * @author ebo2022
 * @since 1.0
 */
public abstract class BorealibTagsProvider<T> extends IntrinsicHolderTagsProvider<T> {

	private final String domain;

	public BorealibTagsProvider(BorealibPackOutput output, ResourceKey<? extends Registry<T>> registry, CompletableFuture<HolderLookup.Provider> registriesFuture, Function<T, ResourceKey<T>> keyExtractor) {
		super(output, registry, registriesFuture, keyExtractor);
		this.domain = output.getModId();
	}

	public BorealibTagsProvider(BorealibPackOutput output, ResourceKey<? extends Registry<T>> registry, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		this(output, registry, registriesFuture, element -> {
			RegistryWrapper<T> registryWrapper = RegistryWrapper.get(registry);
			if (registryWrapper != null) {
				return registryWrapper.getResourceKey(element).orElseThrow();
			}
			throw new UnsupportedOperationException("Adding intrinsic objects is not supported");
		});
	}

	public String getDomain() {
		return domain;
	}

	// Extra layer of precaution; access wideners are iffy on non-common platforms
	@Override
	public IntrinsicTagAppender<T> tag(TagKey<T> tagKey) {
		return super.tag(tagKey);
	}

	/**
	 * Registers the tags to be generated.
	 *
	 * @param registries The existing registries to use
	 */
	@Override
	protected abstract void addTags(HolderLookup.Provider registries);

	/**
	 * A specialized tag provider for blocks.
	 *
	 * @since 1.0
	 */
	public abstract static class BlockTagProvider extends BorealibTagsProvider<Block> {
		public BlockTagProvider(BorealibPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
			super(output, Registries.BLOCK, registriesFuture, e -> e.builtInRegistryHolder().key());
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
			super(output, Registries.ITEM, completableFuture, e -> e.builtInRegistryHolder().key());
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
	}

	/**
	 * A specialized tag provider for fluids.
	 *
	 * @since 1.0
	 */
	public abstract static class FluidTagProvider extends BorealibTagsProvider<Fluid> {
		public FluidTagProvider(BorealibPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
			super(output, Registries.FLUID, completableFuture, e -> e.builtInRegistryHolder().key());
		}
	}

	/**
	 * A specialized tag provider for enchantments.
	 *
	 * @since 1.0
	 */
	public abstract static class EnchantmentTagProvider extends BorealibTagsProvider<Enchantment> {
		public EnchantmentTagProvider(BorealibPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
			super(output, Registries.ENCHANTMENT, completableFuture, e -> RegistryWrapper.ENCHANTMENTS.getResourceKey(e).orElseThrow(() -> new IllegalArgumentException("Enchantment " + e + " is not registered")));
		}
	}

	/**
	 * A specialized tag provider for entities.
	 *
	 * @since 1.0
	 */
	public abstract static class EntityTypeTagProvider extends BorealibTagsProvider<EntityType<?>> {
		public EntityTypeTagProvider(BorealibPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
			super(output, Registries.ENTITY_TYPE, completableFuture, e -> e.builtInRegistryHolder().key());
		}
	}

	/**
	 * A specialized tag provider for game events.
	 *
	 * @since 1.0
	 */
	public abstract static class GameEventTagProvider extends BorealibTagsProvider<GameEvent> {
		public GameEventTagProvider(BorealibPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
			super(output, Registries.GAME_EVENT, completableFuture, e -> e.builtInRegistryHolder().key());
		}
	}
}