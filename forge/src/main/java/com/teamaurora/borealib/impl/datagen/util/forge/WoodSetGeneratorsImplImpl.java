package com.teamaurora.borealib.impl.datagen.util.forge;

import com.teamaurora.borealib.api.base.v1.util.Mods;
import com.teamaurora.borealib.api.block.v1.compat.CommonCompatBlockVariants;
import com.teamaurora.borealib.api.block.v1.compat.forge.BorealibHedgeBlock;
import com.teamaurora.borealib.api.block.v1.compat.forge.BorealibVerticalSlabBlock;
import com.teamaurora.borealib.api.block.v1.compat.forge.ForgeCompatBlockVariants;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodVariants;
import com.teamaurora.borealib.api.datagen.v1.providers.BorealibRecipeProvider;
import com.teamaurora.borealib.api.datagen.v1.providers.BorealibTagsProvider;
import com.teamaurora.borealib.api.datagen.v1.providers.BorealibTagsProvider.BorealibTagAppender;
import com.teamaurora.borealib.api.datagen.v1.providers.loot.BorealibBlockLootProvider;
import com.teamaurora.borealib.api.datagen.v1.util.CompatBlockTags;
import com.teamaurora.borealib.api.datagen.v1.util.CompatItemTags;
import com.teamaurora.borealib.api.datagen.v1.util.ModelGeneratorHelper;
import com.teamaurora.borealib.api.resource_condition.v1.DefaultResourceConditions;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionProvider;
import com.teamaurora.borealib.api.datagen.v1.util.forge.BorealibForgeModelTemplates;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.*;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

@ApiStatus.Internal
public class WoodSetGeneratorsImplImpl {

    public static void addPlatformBlockTags(BorealibTagsProvider.BlockTagProvider provider, WoodSet... woodSets) {
        BorealibTagAppender<Block> forgeMineableWithAxe = provider.tag(BlockTags.MINEABLE_WITH_AXE);
        BorealibTagAppender<Block> forgeWoodenChests = provider.tag(Tags.Blocks.CHESTS_WOODEN);
        BorealibTagAppender<Block> forgeTrappedChests = provider.tag(Tags.Blocks.CHESTS_TRAPPED);
        BorealibTagAppender<Block> forgeFences = provider.tag(Tags.Blocks.FENCES_WOODEN);
        BorealibTagAppender<Block> forgeFenceGates = provider.tag(Tags.Blocks.FENCE_GATES_WOODEN);
        BorealibTagAppender<Block> forgeBookshelves = provider.tag(Tags.Blocks.BOOKSHELVES);
        BorealibTagAppender<Block> morePlanks = provider.tag(BlockTags.PLANKS);
        BorealibTagAppender<Block> woodenVerticalSlabs = provider.tag(CompatBlockTags.WOODEN_VERTICAL_SLABS);
        BorealibTagAppender<Block> hedges = provider.tag(CompatBlockTags.HEDGES);

        for (WoodSet woodSet : woodSets) {
            forgeMineableWithAxe.add(woodSet.getBlock(ForgeCompatBlockVariants.POST), woodSet.getBlock(ForgeCompatBlockVariants.STRIPPED_POST), woodSet.getBlock(ForgeCompatBlockVariants.HEDGE));
            forgeWoodenChests.add(woodSet.getBlock(CommonCompatBlockVariants.WOODEN_CHEST));
            forgeTrappedChests.add(woodSet.getBlock(CommonCompatBlockVariants.WOODEN_TRAPPED_CHEST));
            forgeFenceGates.add(woodSet.getBlock(WoodVariants.FENCE_GATE));
            forgeFences.add(woodSet.getBlock(WoodVariants.FENCE));
            forgeBookshelves.add(woodSet.getBlock(CommonCompatBlockVariants.BOOKSHELF));
            morePlanks.add(woodSet.getBlock(ForgeCompatBlockVariants.VERTICAL_PLANKS));
            woodenVerticalSlabs.add(woodSet.getBlock(ForgeCompatBlockVariants.WOODEN_VERTICAL_SLAB));
            if (woodSet.isFull()) hedges.add(woodSet.getBlock(ForgeCompatBlockVariants.HEDGE));
        }
    }

    public static void addPlatformItemTags(BorealibTagsProvider.ItemTagProvider provider, WoodSet... woodSets) {
        provider.copy(Tags.Blocks.CHESTS_WOODEN, Tags.Items.CHESTS_WOODEN);
        provider.copy(Tags.Blocks.CHESTS_TRAPPED, Tags.Items.CHESTS_TRAPPED);
        provider.copy(Tags.Blocks.FENCES_WOODEN, Tags.Items.FENCES_WOODEN);
        provider.copy(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);
        provider.copy(Tags.Blocks.BOOKSHELVES, Tags.Items.BOOKSHELVES);
        provider.copy(CompatBlockTags.WOODEN_VERTICAL_SLABS, CompatItemTags.WOODEN_VERTICAL_SLABS);
        provider.copy(CompatBlockTags.HEDGES, CompatItemTags.HEDGES);

        BorealibTagAppender<Item> quarkBoatableChests = provider.tag(CompatItemTags.BOATABLE_CHESTS);
        BorealibTagAppender<Item> quarkRevertableChests = provider.tag(CompatItemTags.REVERTABLE_CHESTS);

        for (WoodSet woodSet : woodSets) {
            Item chest = woodSet.getItem(CommonCompatBlockVariants.WOODEN_CHEST);
            quarkBoatableChests.add(chest);
            quarkRevertableChests.add(chest);
        }
    }

    public static void addPlatformRecipes(BorealibRecipeProvider provider, Consumer<FinishedRecipe> consumer, WoodSet... woodSets) {
        ResourceConditionProvider carpenterLoaded = DefaultResourceConditions.allModsLoaded(Mods.CARPENTER);
        Consumer<FinishedRecipe> chestExporter = provider.withConditions(consumer, DefaultResourceConditions.or(carpenterLoaded, DefaultResourceConditions.quarkFlag("variant_chests"), DefaultResourceConditions.woodworksFlag("wooden_chests")));
        Consumer<FinishedRecipe> shelfExporter = provider.withConditions(consumer, DefaultResourceConditions.or(carpenterLoaded, DefaultResourceConditions.quarkFlag("variant_bookshelves"), DefaultResourceConditions.woodworksFlag("wooden_bookshelves")));
        Consumer<FinishedRecipe> verticalPlankExporter = provider.withConditions(consumer, DefaultResourceConditions.quarkFlag("vertical_planks"));
        Consumer<FinishedRecipe> postsExporter = provider.withConditions(consumer, DefaultResourceConditions.quarkFlag("wooden_posts"));
        Consumer<FinishedRecipe> verticalSlabExporter = provider.withConditions(consumer, DefaultResourceConditions.quarkFlag("vertical_slabs"));
        Consumer<FinishedRecipe> woodChestRecipeExporter = provider.withConditions(consumer, DefaultResourceConditions.and(DefaultResourceConditions.quarkFlag("variant_chests"), DefaultResourceConditions.quarkFlag("wood_to_chest_recipes")));
        Consumer<FinishedRecipe> hedgesExporter = provider.withConditions(consumer, DefaultResourceConditions.quarkFlag("hedges"));

        // TODO: change the recipe category when these mods update to 1.20 if needed
        for (WoodSet woodSet : woodSets) {
            Block planks = woodSet.getBlock(WoodVariants.PLANKS);
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, woodSet.getBlock(CommonCompatBlockVariants.WOODEN_CHEST)).define('#', planks).pattern("###").pattern("# #").pattern("###").unlockedBy("has_lots_of_items", new InventoryChangeTrigger.TriggerInstance(ContextAwarePredicate.ANY, MinMaxBounds.Ints.atLeast(10), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, new ItemPredicate[0])).group("wooden_chests").save(chestExporter);
            ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, woodSet.getBlock(CommonCompatBlockVariants.WOODEN_TRAPPED_CHEST)).requires(woodSet.getBlock(CommonCompatBlockVariants.WOODEN_CHEST)).requires(Blocks.TRIPWIRE_HOOK).unlockedBy("has_tripwire_hook", BorealibRecipeProvider.has(Blocks.TRIPWIRE_HOOK)).save(chestExporter);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.BOOKSHELF).define('#', planks).define('X', Items.BOOK).pattern("###").pattern("XXX").pattern("###").unlockedBy("has_book", BorealibRecipeProvider.has(Items.BOOK)).save(shelfExporter);
            Block verticalPlanks = woodSet.getBlock(ForgeCompatBlockVariants.VERTICAL_PLANKS);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, verticalPlanks, 3).define('#', planks).pattern("#").pattern("#").pattern("#").group("vertical_planks").unlockedBy(BorealibRecipeProvider.getHasName(planks), BorealibRecipeProvider.has(planks)).save(verticalPlankExporter);
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks).requires(verticalPlanks).unlockedBy(BorealibRecipeProvider.getHasName(verticalPlanks), BorealibRecipeProvider.has(verticalPlanks)).save(verticalPlankExporter, RecipeBuilder.getDefaultRecipeId(verticalPlanks).withPath(s -> s + "_revert"));
            Block wood = woodSet.getBlock(WoodVariants.WOOD);
            Block strippedWood = woodSet.getBlock(WoodVariants.STRIPPED_WOOD);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodSet.getBlock(ForgeCompatBlockVariants.POST), 8).define('#', wood).pattern("#").pattern("#").pattern("#").group("wooden_post").unlockedBy(BorealibRecipeProvider.getHasName(wood), BorealibRecipeProvider.has(wood)).save(postsExporter);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, woodSet.getBlock(ForgeCompatBlockVariants.STRIPPED_POST), 8).define('#', strippedWood).pattern("#").pattern("#").pattern("#").group("wooden_post").unlockedBy(BorealibRecipeProvider.getHasName(strippedWood), BorealibRecipeProvider.has(strippedWood)).save(postsExporter);
            Block verticalSlab = woodSet.getBlock(ForgeCompatBlockVariants.WOODEN_VERTICAL_SLAB);
            Block slab = woodSet.getBlock(WoodVariants.SLAB);
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, verticalSlab, 6).define('#', slab).pattern("#").pattern("#").pattern("#").unlockedBy(BorealibRecipeProvider.getHasName(planks), BorealibRecipeProvider.has(planks)).save(verticalSlabExporter);
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, slab).requires(verticalSlab).unlockedBy(BorealibRecipeProvider.getHasName(verticalSlab), BorealibRecipeProvider.has(verticalSlab)).save(verticalSlabExporter, RecipeBuilder.getDefaultRecipeId(verticalSlab).withPath(s -> s + "_revert"));
            ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, woodSet.getBlock(CommonCompatBlockVariants.WOODEN_CHEST), 4).define('#', woodSet.getItemLogTag()).pattern("###").pattern("# #").pattern("###").group("wooden_chest").unlockedBy("has_lots_of_items", new InventoryChangeTrigger.TriggerInstance(ContextAwarePredicate.ANY, MinMaxBounds.Ints.atLeast(10), MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, new ItemPredicate[0])).save(woodChestRecipeExporter);
            Block leaves = woodSet.getBlock(WoodVariants.LEAVES);
            if (woodSet.isFull()) ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, woodSet.getBlock(ForgeCompatBlockVariants.HEDGE), 2).define('#', leaves).define('L', woodSet.getItemLogTag()).pattern("#").pattern("L").group("hedge").unlockedBy(BorealibRecipeProvider.getHasName(leaves), BorealibRecipeProvider.has(leaves)).save(hedgesExporter);
        }
    }

    public static void addPlatformBlockModels(BlockModelGenerators generators, WoodSet... woodSets) {
        for (WoodSet woodSet : woodSets) {
            createVerticalPlanks(generators, woodSet.getBlock(ForgeCompatBlockVariants.VERTICAL_PLANKS), woodSet.getBlock(WoodVariants.PLANKS));
            createPost(generators, woodSet.getBlock(ForgeCompatBlockVariants.STRIPPED_POST), woodSet.getBlock(WoodVariants.STRIPPED_LOG));
            createPost(generators, woodSet.getBlock(ForgeCompatBlockVariants.POST), woodSet.getBlock(WoodVariants.LOG));
            createVerticalSlab(generators, woodSet.getBlock(ForgeCompatBlockVariants.WOODEN_VERTICAL_SLAB));
            if (woodSet.isFull()) createHedge(generators, woodSet.getBlock(ForgeCompatBlockVariants.HEDGE), woodSet.getBlock(WoodVariants.LEAVES), woodSet.getBlock(WoodVariants.LOG));
        }
    }

    private static BlockStateGenerator verticalSlabState(Block block, ResourceLocation modelLocation) {
        return MultiVariantGenerator.multiVariant(block).with(PropertyDispatch.property(BorealibVerticalSlabBlock.TYPE)
                .select(BorealibVerticalSlabBlock.Type.NORTH, Variant.variant().with(VariantProperties.MODEL, modelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R0))
                .select(BorealibVerticalSlabBlock.Type.SOUTH, Variant.variant().with(VariantProperties.MODEL, modelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(BorealibVerticalSlabBlock.Type.EAST, Variant.variant().with(VariantProperties.MODEL, modelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
                .select(BorealibVerticalSlabBlock.Type.WEST, Variant.variant().with(VariantProperties.MODEL, modelLocation).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .select(BorealibVerticalSlabBlock.Type.DOUBLE, Variant.variant().with(VariantProperties.MODEL, modelLocation)));
    }

    private static BlockStateGenerator hedgeState(Block block, ResourceLocation post, ResourceLocation extend, ResourceLocation side) {
        return MultiPartGenerator.multiPart(block)
                .with(Condition.condition().term(BorealibHedgeBlock.EXTEND, false), Variant.variant().with(VariantProperties.MODEL, post))
                .with(Condition.condition().term(BorealibHedgeBlock.EXTEND, true), Variant.variant().with(VariantProperties.MODEL, extend))
                .with(Condition.condition().term(BlockStateProperties.NORTH, true), Variant.variant().with(VariantProperties.MODEL, side).with(VariantProperties.UV_LOCK, true))
                .with(Condition.condition().term(BlockStateProperties.EAST, true), Variant.variant().with(VariantProperties.MODEL, side).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90).with(VariantProperties.UV_LOCK, true))
                .with(Condition.condition().term(BlockStateProperties.SOUTH, true), Variant.variant().with(VariantProperties.MODEL, side).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180).with(VariantProperties.UV_LOCK, true))
                .with(Condition.condition().term(BlockStateProperties.WEST, true), Variant.variant().with(VariantProperties.MODEL, side).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270).with(VariantProperties.UV_LOCK, true));
    }

    private static void createVerticalPlanks(BlockModelGenerators generators, Block verticalPlanks, Block planks) {
        ResourceLocation blockModelLocation = BorealibForgeModelTemplates.VERTICAL_PLANKS.create(verticalPlanks, TextureMapping.cube(planks), generators.modelOutput);
        generators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(verticalPlanks, blockModelLocation));
    }

    private static void createPost(BlockModelGenerators generators, Block post, Block parentLog) {
        ResourceLocation blockModelLocation = BorealibForgeModelTemplates.POST.create(post, TextureMapping.defaultTexture(parentLog), generators.modelOutput);
        generators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(post, blockModelLocation));
    }

    private static void createVerticalSlab(BlockModelGenerators generators, Block verticalSlab) {
        ResourceLocation block = TextureMapping.getBlockTexture(verticalSlab);
        ResourceLocation blockModelLocation = BorealibForgeModelTemplates.VERTICAL_SLAB.create(verticalSlab, new TextureMapping().put(TextureSlot.SIDE, block).put(TextureSlot.TOP, block).put(TextureSlot.BOTTOM, block), generators.modelOutput);
        generators.blockStateOutput.accept(verticalSlabState(verticalSlab, blockModelLocation));
    }

    private static void createHedge(BlockModelGenerators generators, Block hedge, Block leaves, Block log) {
        ResourceLocation logTexture = TextureMapping.getBlockTexture(log);
        ResourceLocation post = BorealibForgeModelTemplates.HEDGE_POST.create(hedge, new TextureMapping().put(BorealibForgeModelTemplates.LEAF_SLOT, TextureMapping.getBlockTexture(leaves)).put(BorealibForgeModelTemplates.LOG_SLOT, logTexture), generators.modelOutput);
        ResourceLocation extend = BorealibForgeModelTemplates.HEDGE_EXTEND.create(hedge, new TextureMapping().put(BorealibForgeModelTemplates.LOG_SLOT, logTexture), generators.modelOutput);
        ResourceLocation side = BorealibForgeModelTemplates.HEDGE_SIDE.create(hedge, new TextureMapping().put(BorealibForgeModelTemplates.LOG_SLOT, logTexture), generators.modelOutput);
        generators.blockStateOutput.accept(hedgeState(hedge, post, extend, side));
    }

    public static void addPlatformBlockLoot(BorealibBlockLootProvider provider, WoodSet... woodSets) {
        for (WoodSet woodSet : woodSets) {
            provider.dropSelf(woodSet.getBlock(ForgeCompatBlockVariants.VERTICAL_PLANKS));
            provider.dropSelf(woodSet.getBlock(ForgeCompatBlockVariants.STRIPPED_POST));
            provider.dropSelf(woodSet.getBlock(ForgeCompatBlockVariants.POST));
            provider.add(woodSet.getBlock(ForgeCompatBlockVariants.WOODEN_VERTICAL_SLAB), block -> createVerticalSlabTable(block, provider));
            if (woodSet.isFull()) provider.dropSelf(woodSet.getBlock(ForgeCompatBlockVariants.HEDGE));
        }
    }

    private static LootTable.Builder createVerticalSlabTable(Block block, BorealibBlockLootProvider provider) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add((provider.applyExplosionDecay(block, LootItem.lootTableItem(block).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BorealibVerticalSlabBlock.TYPE, BorealibVerticalSlabBlock.Type.DOUBLE))))))));
    }
}
