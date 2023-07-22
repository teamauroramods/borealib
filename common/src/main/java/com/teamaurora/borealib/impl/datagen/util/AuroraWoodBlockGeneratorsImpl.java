package com.teamaurora.borealib.impl.datagen.util;

import com.google.common.base.Preconditions;
import com.teamaurora.borealib.api.base.v1.platform.Platform;
import com.teamaurora.borealib.api.block.v1.compat.CommonCompatBlockVariants;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodSet;
import com.teamaurora.borealib.api.block.v1.set.wood.WoodVariants;
import com.teamaurora.borealib.api.datagen.v1.providers.BorealibRecipeProvider;
import com.teamaurora.borealib.api.datagen.v1.providers.BorealibTagsProvider;
import com.teamaurora.borealib.api.datagen.v1.providers.BorealibTagsProvider.BorealibTagAppender;
import com.teamaurora.borealib.api.datagen.v1.providers.loot.BorealibBlockLootProvider;
import com.teamaurora.borealib.api.datagen.v1.util.CompatBlockTags;
import com.teamaurora.borealib.api.datagen.v1.util.CompatItemTags;
import com.teamaurora.borealib.api.datagen.v1.util.ModelGeneratorHelper;
import com.teamaurora.borealib.api.datagen.v1.util.BorealibModelTemplates;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

@ApiStatus.Internal
public class AuroraWoodBlockGeneratorsImpl {

    public static void createBlockTags(BorealibTagsProvider.BlockTagProvider provider, WoodSet... woodSets){
        Preconditions.checkArgument(woodSets.length > 0, "Must generate data for at least 1 wood set");

        BorealibTagAppender<Block> mineableWithAxe = provider.tag(BlockTags.MINEABLE_WITH_AXE);
        BorealibTagAppender<Block> mineableWithHoe = provider.tag(BlockTags.MINEABLE_WITH_HOE);
        BorealibTagAppender<Block> leaves = provider.tag(BlockTags.LEAVES);
        BorealibTagAppender<Block> overworldNaturalLogs = provider.tag(BlockTags.OVERWORLD_NATURAL_LOGS);
        BorealibTagAppender<Block> fenceGates = provider.tag(BlockTags.FENCE_GATES);
        BorealibTagAppender<Block> flowerPots = provider.tag(BlockTags.FLOWER_POTS);
        BorealibTagAppender<Block> logsThatBurn = provider.tag(BlockTags.LOGS_THAT_BURN);
        BorealibTagAppender<Block> planks = provider.tag(BlockTags.LOGS_THAT_BURN);
        BorealibTagAppender<Block> saplings = provider.tag(BlockTags.SAPLINGS);
        BorealibTagAppender<Block> standingSigns = provider.tag(BlockTags.STANDING_SIGNS);
        BorealibTagAppender<Block> wallSigns = provider.tag(BlockTags.WALL_SIGNS);
        BorealibTagAppender<Block> ceilingHangingSigns = provider.tag(BlockTags.CEILING_HANGING_SIGNS);
        BorealibTagAppender<Block> wallHangingSigns = provider.tag(BlockTags.WALL_HANGING_SIGNS);
        BorealibTagAppender<Block> woodenButtons = provider.tag(BlockTags.WOODEN_BUTTONS);
        BorealibTagAppender<Block> woodenDoors = provider.tag(BlockTags.WOODEN_DOORS);
        BorealibTagAppender<Block> woodenFences = provider.tag(BlockTags.WOODEN_FENCES);
        BorealibTagAppender<Block> woodenPressurePlates = provider.tag(BlockTags.WOODEN_PRESSURE_PLATES);
        BorealibTagAppender<Block> woodenSlabs = provider.tag(BlockTags.WOODEN_SLABS);
        BorealibTagAppender<Block> woodenStairs = provider.tag(BlockTags.WOODEN_STAIRS);
        BorealibTagAppender<Block> woodenTrapdoors = provider.tag(BlockTags.WOODEN_TRAPDOORS);
        BorealibTagAppender<Block> carpenterChests = provider.tag(CompatBlockTags.CARPENTER_CHESTS);
        BorealibTagAppender<Block> carpenterTrappedChests = provider.tag(CompatBlockTags.CARPENTER_TRAPPED_CHESTS);
        BorealibTagAppender<Block> carpenterBookshelves = provider.tag(CompatBlockTags.CARPENTER_BOOKSHELVES);
        BorealibTagAppender<Block> guardedByPiglins = provider.tag(BlockTags.GUARDED_BY_PIGLINS);

        for (WoodSet woodSet : woodSets) {
            TagKey<Block> logs = woodSet.getBlockLogTag();
            provider.tag(logs).add(woodSet.getBlock(WoodVariants.LOG), woodSet.getBlock(WoodVariants.WOOD), woodSet.getBlock(WoodVariants.STRIPPED_LOG), woodSet.getBlock(WoodVariants.STRIPPED_WOOD));
            mineableWithAxe.add(woodSet.getBlock(CommonCompatBlockVariants.BOOKSHELF), woodSet.getBlock(CommonCompatBlockVariants.WOODEN_CHEST), woodSet.getBlock(CommonCompatBlockVariants.WOODEN_TRAPPED_CHEST));

            overworldNaturalLogs.add(woodSet.getBlock(WoodVariants.LOG));
            fenceGates.add(woodSet.getBlock(WoodVariants.FENCE_GATE));
            logsThatBurn.addTag(logs);
            planks.add(woodSet.getBlock(WoodVariants.PLANKS));
            standingSigns.add(woodSet.getBlock(WoodVariants.STANDING_SIGN));
            wallSigns.add(woodSet.getBlock(WoodVariants.WALL_SIGN));
            ceilingHangingSigns.add(woodSet.getBlock(WoodVariants.HANGING_SIGN));
            wallHangingSigns.add(woodSet.getBlock(WoodVariants.WALL_HANGING_SIGN));
            woodenButtons.add(woodSet.getBlock(WoodVariants.BUTTON));
            woodenDoors.add(woodSet.getBlock(WoodVariants.DOOR));
            woodenFences.add(woodSet.getBlock(WoodVariants.FENCE));
            woodenPressurePlates.add(woodSet.getBlock(WoodVariants.PRESSURE_PLATE));
            woodenSlabs.add(woodSet.getBlock(WoodVariants.SLAB));
            woodenStairs.add(woodSet.getBlock(WoodVariants.STAIRS));
            woodenTrapdoors.add(woodSet.getBlock(WoodVariants.TRAPDOOR));

            if (woodSet.isFull()) {
                saplings.add(woodSet.getBlock(WoodVariants.SAPLING));
                flowerPots.add(woodSet.getBlock(WoodVariants.POTTED_SAPLING));
                Block leavesBlock = woodSet.getBlock(WoodVariants.LEAVES);
                leaves.add(leavesBlock);
                mineableWithHoe.add(leavesBlock);
            }

            // carpenter/chests in general
            carpenterChests.add(woodSet.getBlock(CommonCompatBlockVariants.WOODEN_CHEST));
            carpenterTrappedChests.add(woodSet.getBlock(CommonCompatBlockVariants.WOODEN_TRAPPED_CHEST));
            carpenterBookshelves.add(woodSet.getBlock(CommonCompatBlockVariants.BOOKSHELF));
            guardedByPiglins.add(woodSet.getBlock(CommonCompatBlockVariants.WOODEN_CHEST), woodSet.getBlock(CommonCompatBlockVariants.WOODEN_TRAPPED_CHEST));
        }
        addPlatformBlockTags(provider, woodSets);
    }

    @ExpectPlatform
    private static void addPlatformBlockTags(BorealibTagsProvider.BlockTagProvider provider, WoodSet... woodSets) {
        Platform.expect();
    }

    public static void createItemTags(BorealibTagsProvider.ItemTagProvider provider, WoodSet... woodSets) {
        Preconditions.checkArgument(woodSets.length > 0, "Must generate data for at least 1 wood set");

        // copy over block tags
        provider.copy(BlockTags.LEAVES, ItemTags.LEAVES);
        provider.copy(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES);
        provider.copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
        provider.copy(BlockTags.PLANKS, ItemTags.PLANKS);
        provider.copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
        provider.copy(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS);
        provider.copy(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS);
        provider.copy(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES);
        provider.copy(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
        provider.copy(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_STAIRS);
        provider.copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
        provider.copy(CompatBlockTags.CARPENTER_CHESTS, CompatItemTags.CARPENTER_CHESTS);
        provider.copy(CompatBlockTags.CARPENTER_TRAPPED_CHESTS, CompatItemTags.CARPENTER_TRAPPED_CHESTS);
        provider.copy(CompatBlockTags.CARPENTER_BOOKSHELVES, CompatItemTags.CARPENTER_BOOKSHELVES);

        BorealibTagAppender<Item> boats = provider.tag(ItemTags.BOATS);
        BorealibTagAppender<Item> chestBoats = provider.tag(ItemTags.CHEST_BOATS);
        BorealibTagAppender<Item> signs = provider.tag(ItemTags.SIGNS);
        BorealibTagAppender<Item> hangingSigns = provider.tag(ItemTags.HANGING_SIGNS);

        for (WoodSet woodSet : woodSets) {
            provider.copy(woodSet.getBlockLogTag(), woodSet.getItemLogTag());
            boats.add(woodSet.getItem(WoodVariants.BOAT));
            chestBoats.add(woodSet.getItem(WoodVariants.CHEST_BOAT));
            signs.add(woodSet.getItem(WoodVariants.SIGN_ITEM));
            hangingSigns.add(woodSet.getItem(WoodVariants.HANGING_SIGN_ITEM));
        }
        addPlatformItemTags(provider, woodSets);
    }

    @ExpectPlatform
    private static void addPlatformItemTags(BorealibTagsProvider.ItemTagProvider provider, WoodSet... woodSets) {
        Platform.expect();
    }

    public static void createBlockModels(BlockModelGenerators generators, WoodSet... woodSets) {
        Preconditions.checkArgument(woodSets.length > 0, "Must generate data for at least 1 wood set");
        for (WoodSet woodSet : woodSets) {
            Block planks = woodSet.getBlock(WoodVariants.PLANKS);
            Block log = woodSet.getBlock(WoodVariants.LOG);
            Block strippedLog = woodSet.getBlock(WoodVariants.STRIPPED_LOG);
            if (woodSet.isFull()) {
                generators.createPlant(woodSet.getBlock(WoodVariants.SAPLING), woodSet.getBlock(WoodVariants.POTTED_SAPLING), BlockModelGenerators.TintState.NOT_TINTED);
                if (woodSet.colorLeaves())
                    generators.createTrivialBlock(woodSet.getBlock(WoodVariants.LEAVES), TexturedModel.LEAVES);
                else
                    generators.createTrivialCube(woodSet.getBlock(WoodVariants.LEAVES));
            }
            generators.woodProvider(log).logWithHorizontal(log).wood(woodSet.getBlock(WoodVariants.WOOD));
            generators.woodProvider(strippedLog).logWithHorizontal(strippedLog).wood(woodSet.getBlock(WoodVariants.STRIPPED_WOOD));
            generators.family(planks).generateFor(woodSet.getOrCreateBlockFamily());
            createChest(generators, woodSet.getBlock(CommonCompatBlockVariants.WOODEN_CHEST), planks);
            createChest(generators, woodSet.getBlock(CommonCompatBlockVariants.WOODEN_TRAPPED_CHEST), planks);
            createBookshelf(generators, woodSet.getBlock(CommonCompatBlockVariants.BOOKSHELF), planks);
            generators.createHangingSign(strippedLog, woodSet.getBlock(WoodVariants.HANGING_SIGN), woodSet.getBlock(WoodVariants.WALL_HANGING_SIGN));
        }
        addPlatformBlockModels(generators, woodSets);
    }

    @ExpectPlatform
    public static void addPlatformBlockModels(BlockModelGenerators generators, WoodSet... woodSets) {
        Platform.expect();
    }

    private static void createChest(BlockModelGenerators generators, Block chest, Block planks) {
        ResourceLocation blockModelLocation = ModelTemplates.PARTICLE_ONLY.create(chest, TextureMapping.particle(planks), generators.modelOutput);
        BorealibModelTemplates.CHEST_ITEM.create(ModelLocationUtils.getModelLocation(chest.asItem()), ModelGeneratorHelper.EMPTY_TEXTURE_MAPPING, generators.modelOutput);
        generators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(chest, blockModelLocation));
    }

    private static void createBookshelf(BlockModelGenerators generators, Block bookshelf, Block planks) {
        TextureMapping textureMapping = TextureMapping.column(TextureMapping.getBlockTexture(bookshelf), TextureMapping.getBlockTexture(planks));
        ResourceLocation resourceLocation = ModelTemplates.CUBE_COLUMN.create(bookshelf, textureMapping, generators.modelOutput);
        generators.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(bookshelf, resourceLocation));
    }

    public static void createItemModels(ItemModelGenerators generators, WoodSet... woodSets) {
        Preconditions.checkArgument(woodSets.length > 0, "Must generate data for at least 1 wood set");
        for (WoodSet woodSet : woodSets) {
            generators.generateFlatItem(woodSet.getItem(WoodVariants.BOAT), ModelTemplates.FLAT_ITEM);
            generators.generateFlatItem(woodSet.getItem(WoodVariants.CHEST_BOAT), ModelTemplates.FLAT_ITEM);
            generators.generateFlatItem(woodSet.getItem(WoodVariants.SIGN_ITEM), ModelTemplates.FLAT_ITEM);
            generators.generateFlatItem(woodSet.getItem(WoodVariants.HANGING_SIGN_ITEM), ModelTemplates.FLAT_ITEM);
        }
    }

    public static void createBlockLoot(BorealibBlockLootProvider provider, WoodSet... woodSets) {
        Preconditions.checkArgument(woodSets.length > 0, "Must generate data for at least 1 wood set");
        for (WoodSet woodSet : woodSets) {
            provider.dropSelf(woodSet.getBlock(WoodVariants.PLANKS));
            provider.dropSelf(woodSet.getBlock(WoodVariants.LOG));
            provider.dropSelf(woodSet.getBlock(WoodVariants.WOOD));
            provider.dropSelf(woodSet.getBlock(WoodVariants.STRIPPED_LOG));
            provider.dropSelf(woodSet.getBlock(WoodVariants.STRIPPED_WOOD));
            provider.dropSelf(woodSet.getBlock(WoodVariants.SAPLING));
            provider.dropPottedContents(woodSet.getBlock(WoodVariants.POTTED_SAPLING));
            provider.dropSelf(woodSet.getBlock(WoodVariants.STANDING_SIGN));
            provider.dropSelf(woodSet.getBlock(WoodVariants.HANGING_SIGN));
            provider.dropSelf(woodSet.getBlock(WoodVariants.PRESSURE_PLATE));
            provider.dropSelf(woodSet.getBlock(WoodVariants.FENCE));
            provider.dropSelf(woodSet.getBlock(WoodVariants.TRAPDOOR));
            provider.dropSelf(woodSet.getBlock(WoodVariants.FENCE_GATE));
            provider.dropSelf(woodSet.getBlock(WoodVariants.BUTTON));
            provider.dropSelf(woodSet.getBlock(WoodVariants.STAIRS));
            provider.add(woodSet.getBlock(WoodVariants.SLAB), provider::createSlabItemTable);
            provider.add(woodSet.getBlock(WoodVariants.DOOR), provider::createDoorTable);
            provider.add(woodSet.getBlock(WoodVariants.LEAVES), b1 -> provider.createLeavesDrops(b1, woodSet.variantOrThrow(WoodVariants.SAPLING).get(), BorealibBlockLootProvider.NORMAL_LEAVES_SAPLING_CHANCES));
            provider.add(woodSet.getBlock(CommonCompatBlockVariants.BOOKSHELF), block -> BorealibBlockLootProvider.createSilkTouchDispatchTable(block, provider.applyExplosionDecay(block, LootItem.lootTableItem(Items.BOOK).apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F))))));
            provider.add(woodSet.variantOrThrow(CommonCompatBlockVariants.WOODEN_CHEST).get(), provider::createNameableBlockEntityTable);
            provider.add(woodSet.variantOrThrow(CommonCompatBlockVariants.WOODEN_TRAPPED_CHEST).get(), provider::createNameableBlockEntityTable);
        }
        addPlatformBlockLoot(provider, woodSets);
    }

    @ExpectPlatform
    public static void addPlatformBlockLoot(BorealibBlockLootProvider provider, WoodSet... woodSets) {
        Platform.expect();
    }

    public static void createRecipes(BorealibRecipeProvider provider, Consumer<FinishedRecipe> consumer, WoodSet... woodSets) {
        Preconditions.checkArgument(woodSets.length > 0, "Must generate data for at least 1 wood set");
        for (WoodSet woodSet : woodSets) {
            BorealibRecipeProvider.generateRecipes(consumer, woodSet.getOrCreateBlockFamily());
            Block planks = woodSet.getBlock(WoodVariants.PLANKS);
            Item boat = woodSet.getItem(WoodVariants.BOAT);
            BorealibRecipeProvider.planksFromLogs(consumer, planks, woodSet.getItemLogTag(), 4);
            BorealibRecipeProvider.woodFromLogs(consumer, woodSet.getBlock(WoodVariants.WOOD), woodSet.getBlock(WoodVariants.LOG));
            BorealibRecipeProvider.woodFromLogs(consumer, woodSet.getBlock(WoodVariants.STRIPPED_WOOD), woodSet.getBlock(WoodVariants.STRIPPED_LOG));
            BorealibRecipeProvider.woodenBoat(consumer, planks, boat);
            BorealibRecipeProvider.chestBoat(consumer, boat, woodSet.getItem(WoodVariants.CHEST_BOAT));
            BorealibRecipeProvider.hangingSign(consumer, woodSet.itemVariantOrThrow(WoodVariants.HANGING_SIGN_ITEM).get(), woodSet.getBlock(WoodVariants.STRIPPED_LOG));
        }
        addPlatformRecipes(provider, consumer, woodSets);
    }

    @ExpectPlatform
    private static void addPlatformRecipes(BorealibRecipeProvider provider, Consumer<FinishedRecipe> consumer, WoodSet... woodSets) {
        Platform.expect();
    }
}
