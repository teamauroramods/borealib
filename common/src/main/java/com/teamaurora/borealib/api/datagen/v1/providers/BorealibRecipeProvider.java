package com.teamaurora.borealib.api.datagen.v1.providers;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.teamaurora.borealib.api.datagen.v1.BorealibPackOutput;
import com.teamaurora.borealib.api.datagen.v1.SimpleConditionalDataProvider;
import com.teamaurora.borealib.api.registry.v1.RegistryWrapper;
import com.teamaurora.borealib.api.resource_condition.v1.ResourceConditionProvider;
import com.teamaurora.borealib.impl.datagen.providers.BorealibRecipeProviderImpl;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.BlockFamily.Variant;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @author ebo2022
 * @since 1.0
 */
public abstract class BorealibRecipeProvider extends SimpleConditionalDataProvider {

    protected final BorealibPackOutput output;
    private final PackOutput.PathProvider recipePathProvider;
    private final PackOutput.PathProvider advancementPathProvider;
    private static final Map<Variant, BiFunction<ItemLike, ItemLike, RecipeBuilder>> SHAPE_BUILDERS = ImmutableMap.<Variant, BiFunction<ItemLike, ItemLike, RecipeBuilder>>builder().put(Variant.BUTTON, (itemLike, itemLike2) -> {
        return buttonBuilder(itemLike, Ingredient.of(itemLike2));
    }).put(Variant.CHISELED, (itemLike, itemLike2) -> {
        return chiseledBuilder(RecipeCategory.BUILDING_BLOCKS, itemLike, Ingredient.of(itemLike2));
    }).put(Variant.CUT, (itemLike, itemLike2) -> {
        return cutBuilder(RecipeCategory.BUILDING_BLOCKS, itemLike, Ingredient.of(itemLike2));
    }).put(Variant.DOOR, (itemLike, itemLike2) -> {
        return doorBuilder(itemLike, Ingredient.of(itemLike2));
    }).put(Variant.CUSTOM_FENCE, (itemLike, itemLike2) -> {
        return fenceBuilder(itemLike, Ingredient.of(itemLike2));
    }).put(Variant.FENCE, (itemLike, itemLike2) -> {
        return fenceBuilder(itemLike, Ingredient.of(itemLike2));
    }).put(Variant.CUSTOM_FENCE_GATE, (itemLike, itemLike2) -> {
        return fenceGateBuilder(itemLike, Ingredient.of(itemLike2));
    }).put(Variant.FENCE_GATE, (itemLike, itemLike2) -> {
        return fenceGateBuilder(itemLike, Ingredient.of(itemLike2));
    }).put(Variant.SIGN, (itemLike, itemLike2) -> {
        return signBuilder(itemLike, Ingredient.of(itemLike2));
    }).put(Variant.SLAB, (itemLike, itemLike2) -> {
        return slabBuilder(RecipeCategory.BUILDING_BLOCKS, itemLike, Ingredient.of(itemLike2));
    }).put(Variant.STAIRS, (itemLike, itemLike2) -> {
        return stairBuilder(itemLike, Ingredient.of(itemLike2));
    }).put(Variant.PRESSURE_PLATE, (itemLike, itemLike2) -> {
        return pressurePlateBuilder(RecipeCategory.REDSTONE, itemLike, Ingredient.of(itemLike2));
    }).put(Variant.POLISHED, (itemLike, itemLike2) -> {
        return polishedBuilder(RecipeCategory.BUILDING_BLOCKS, itemLike, Ingredient.of(itemLike2));
    }).put(Variant.TRAPDOOR, (itemLike, itemLike2) -> {
        return trapdoorBuilder(itemLike, Ingredient.of(itemLike2));
    }).put(Variant.WALL, (itemLike, itemLike2) -> {
        return wallBuilder(RecipeCategory.DECORATIONS, itemLike, Ingredient.of(itemLike2));
    }).build();

    public BorealibRecipeProvider(BorealibPackOutput output) {
        this.output = output;
        this.recipePathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "recipes");
        this.advancementPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
    }

    public abstract void buildRecipes(Consumer<FinishedRecipe> exporter);

    /**
     * Creates an exporter that adds conditions to entries passed through it.
     *
     * @param original   The original recipe exporter
     * @param conditions The conditions to add
     * @return A new exporter that also adds the conditions
     */
    public Consumer<FinishedRecipe> withConditions(Consumer<FinishedRecipe> original, ResourceConditionProvider... conditions) {
        return finishedRecipe -> {
            this.addConditions(this.getId(finishedRecipe.getId()), conditions);
            ResourceLocation advId = finishedRecipe.getAdvancementId();
            if (advId != null) this.addConditions(this.getId(advId), conditions);
        };
    }

    protected ResourceLocation getId(ResourceLocation original) {
        return new ResourceLocation(this.output.getModId(), original.getPath());
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        Set<ResourceLocation> generatedRecipes = new HashSet<>();
        List<CompletableFuture<?>> futures = new ArrayList<>();
        this.buildRecipes(finishedRecipe -> {
            ResourceLocation id = this.getId(finishedRecipe.getId());
            if (!generatedRecipes.add(id))
                throw new IllegalStateException("Duplicate recipe " + id);

            JsonObject recipeJson = finishedRecipe.serializeRecipe();
            this.injectConditions(id, recipeJson);

            futures.add(DataProvider.saveStable(cachedOutput, recipeJson, this.recipePathProvider.json(id)));
            JsonObject advancementJson = finishedRecipe.serializeAdvancement();

            if (advancementJson != null) {
                ResourceLocation advancmentId = this.getId(finishedRecipe.getAdvancementId());
                this.injectConditions(advancmentId, advancementJson);
                futures.add(DataProvider.saveStable(cachedOutput, advancementJson, this.advancementPathProvider.json(advancmentId)));
            }
        });
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    public static void oneToOneConversionRecipe(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2, @Nullable String string) {
        oneToOneConversionRecipe(consumer, itemLike, itemLike2, string, 1);
    }

    public static void oneToOneConversionRecipe(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2, @Nullable String string, int i) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, itemLike, i).requires(itemLike2).group(string).unlockedBy(getHasName(itemLike2), has(itemLike2)).save(consumer, getConversionRecipeName(itemLike, itemLike2));
    }

    public static void oreSmelting(Consumer<FinishedRecipe> consumer, List<ItemLike> list, RecipeCategory recipeCategory, ItemLike itemLike, float f, int i, String string) {
        oreCooking(consumer, RecipeSerializer.SMELTING_RECIPE, list, recipeCategory, itemLike, f, i, string, "_from_smelting");
    }

    public static void oreBlasting(Consumer<FinishedRecipe> consumer, List<ItemLike> list, RecipeCategory recipeCategory, ItemLike itemLike, float f, int i, String string) {
        oreCooking(consumer, RecipeSerializer.BLASTING_RECIPE, list, recipeCategory, itemLike, f, i, string, "_from_blasting");
    }

    public static void oreCooking(Consumer<FinishedRecipe> consumer, RecipeSerializer<? extends AbstractCookingRecipe> recipeSerializer, List<ItemLike> list, RecipeCategory recipeCategory, ItemLike itemLike, float f, int i, String string, String string2) {
        for (ItemLike itemLike2 : list){
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemLike2), recipeCategory, itemLike, f, i, recipeSerializer).group(string).unlockedBy(getHasName(itemLike2), has(itemLike2)).save(consumer, getItemName(itemLike) + string2 + "_" + getItemName(itemLike2));
        }
    }

    public static void netheriteSmithing(Consumer<FinishedRecipe> consumer, Item item, RecipeCategory recipeCategory, Item item2) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(item), Ingredient.of(Items.NETHERITE_INGOT), recipeCategory, item2).unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT)).save(consumer, getItemName(item2) + "_smithing");
    }

    public static void trimSmithing(Consumer<FinishedRecipe> consumer, Item item, ResourceLocation resourceLocation) {
        SmithingTrimRecipeBuilder.smithingTrim(Ingredient.of(item), Ingredient.of(ItemTags.TRIMMABLE_ARMOR), Ingredient.of(ItemTags.TRIM_MATERIALS), RecipeCategory.MISC).unlocks("has_smithing_trim_template", has(item)).save(consumer, resourceLocation);
    }

    public static void twoByTwoPacker(Consumer<FinishedRecipe> consumer, RecipeCategory recipeCategory, ItemLike itemLike, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(recipeCategory, itemLike, 1).define('#', itemLike2).pattern("##").pattern("##").unlockedBy(getHasName(itemLike2), has(itemLike2)).save(consumer);
    }

    public static void threeByThreePacker(Consumer<FinishedRecipe> consumer, RecipeCategory recipeCategory, ItemLike itemLike, ItemLike itemLike2, String string) {
        ShapelessRecipeBuilder.shapeless(recipeCategory, itemLike).requires(itemLike2, 9).unlockedBy(string, has(itemLike2)).save(consumer);
    }

    public static void threeByThreePacker(Consumer<FinishedRecipe> consumer, RecipeCategory recipeCategory, ItemLike itemLike, ItemLike itemLike2) {
        threeByThreePacker(consumer, recipeCategory, itemLike, itemLike2, getHasName(itemLike2));
    }

    public static void planksFromLog(Consumer<FinishedRecipe> consumer, ItemLike itemLike, TagKey<Item> tagKey, int i) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, itemLike, i).requires(tagKey).group("planks").unlockedBy("has_log", has(tagKey)).save(consumer);
    }

    public static void planksFromLogs(Consumer<FinishedRecipe> consumer, ItemLike itemLike, TagKey<Item> tagKey, int i) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, itemLike, i).requires(tagKey).group("planks").unlockedBy("has_logs", has(tagKey)).save(consumer);
    }

    public static void woodFromLogs(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, itemLike, 3).define('#', itemLike2).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(itemLike2)).save(consumer);
    }

    public static void woodenBoat(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, itemLike).define('#', itemLike2).pattern("# #").pattern("###").group("boat").unlockedBy("in_water", insideOf(Blocks.WATER)).save(consumer);
    }

    public static void chestBoat(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        BorealibRecipeProviderImpl.chestBoat(consumer, itemLike, itemLike2);
    }

    public static RecipeBuilder buttonBuilder(ItemLike itemLike, Ingredient ingredient) {
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, itemLike).requires(ingredient);
    }

    public static RecipeBuilder doorBuilder(ItemLike itemLike, Ingredient ingredient) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, itemLike, 3).define('#', ingredient).pattern("##").pattern("##").pattern("##");
    }

    public static RecipeBuilder fenceBuilder(ItemLike itemLike, Ingredient ingredient) {
        return BorealibRecipeProviderImpl.fenceBuilder(itemLike, ingredient);
    }

    public static RecipeBuilder fenceGateBuilder(ItemLike itemLike, Ingredient ingredient) {
        return BorealibRecipeProviderImpl.fenceGateBuilder(itemLike, ingredient);
    }

    public static void pressurePlate(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        pressurePlateBuilder(RecipeCategory.REDSTONE, itemLike, Ingredient.of(itemLike2)).unlockedBy(getHasName(itemLike2), has(itemLike2)).save(consumer);
    }

    public static RecipeBuilder pressurePlateBuilder(RecipeCategory recipeCategory, ItemLike itemLike, Ingredient ingredient) {
        return ShapedRecipeBuilder.shaped(recipeCategory, itemLike).define('#', ingredient).pattern("##");
    }

    public static void slab(Consumer<FinishedRecipe> consumer, RecipeCategory recipeCategory, ItemLike itemLike, ItemLike itemLike2) {
        slabBuilder(recipeCategory, itemLike, Ingredient.of(itemLike2)).unlockedBy(getHasName(itemLike2), has(itemLike2)).save(consumer);
    }

    public static RecipeBuilder slabBuilder(RecipeCategory recipeCategory, ItemLike itemLike, Ingredient ingredient) {
        return ShapedRecipeBuilder.shaped(recipeCategory, itemLike, 6).define('#', ingredient).pattern("###");
    }

    public static RecipeBuilder stairBuilder(ItemLike itemLike, Ingredient ingredient) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, itemLike, 4).define('#', ingredient).pattern("#  ").pattern("## ").pattern("###");
    }

    public static RecipeBuilder trapdoorBuilder(ItemLike itemLike, Ingredient ingredient) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, itemLike, 2).define('#', ingredient).pattern("###").pattern("###");
    }

    public static RecipeBuilder signBuilder(ItemLike itemLike, Ingredient ingredient) {
        return BorealibRecipeProviderImpl.signBuilder(itemLike, ingredient);
    }

    public static void hangingSign(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, itemLike, 6).group("hanging_sign").define('#', itemLike2).define('X', Items.CHAIN).pattern("X X").pattern("###").pattern("###").unlockedBy("has_stripped_logs", has(itemLike2)).save(consumer);
    }

    public static void colorBlockWithDye(Consumer<FinishedRecipe> consumer, List<Item> list, List<Item> list2, String string) {
        for(int i = 0; i < list.size(); ++i) {
            Item item = list.get(i);
            Item item2 = list2.get(i);
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, item2).requires(item).requires(Ingredient.of(list2.stream().filter((item2x) -> {
                return !item2x.equals(item2);
            }).map(ItemStack::new))).group(string).unlockedBy("has_needed_dye", has(item)).save(consumer, "dye_" + getItemName(item2));
        }

    }

    public static void carpet(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, itemLike, 3).define('#', itemLike2).pattern("##").group("carpet").unlockedBy(getHasName(itemLike2), has(itemLike2)).save(consumer);
    }

    public static void bedFromPlanksAndWool(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, itemLike).define('#', itemLike2).define('X', ItemTags.PLANKS).pattern("###").pattern("XXX").group("bed").unlockedBy(getHasName(itemLike2), has(itemLike2)).save(consumer);
    }

    public static void banner(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, itemLike).define('#', itemLike2).define('|', Items.STICK).pattern("###").pattern("###").pattern(" | ").group("banner").unlockedBy(getHasName(itemLike2), has(itemLike2)).save(consumer);
    }

    public static void stainedGlassFromGlassAndDye(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, itemLike, 8).define('#', Blocks.GLASS).define('X', itemLike2).pattern("###").pattern("#X#").pattern("###").group("stained_glass").unlockedBy("has_glass", has(Blocks.GLASS)).save(consumer);
    }

    public static void stainedGlassPaneFromStainedGlass(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, itemLike, 16).define('#', itemLike2).pattern("###").pattern("###").group("stained_glass_pane").unlockedBy("has_glass", has(itemLike2)).save(consumer);
    }

    public static void stainedGlassPaneFromGlassPaneAndDye(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, itemLike, 8).define('#', Blocks.GLASS_PANE).define('$', itemLike2).pattern("###").pattern("#$#").pattern("###").group("stained_glass_pane").unlockedBy("has_glass_pane", has(Blocks.GLASS_PANE)).unlockedBy(getHasName(itemLike2), has(itemLike2)).save(consumer, getConversionRecipeName(itemLike, Blocks.GLASS_PANE));
    }

    public static void coloredTerracottaFromTerracottaAndDye(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, itemLike, 8).define('#', Blocks.TERRACOTTA).define('X', itemLike2).pattern("###").pattern("#X#").pattern("###").group("stained_terracotta").unlockedBy("has_terracotta", has(Blocks.TERRACOTTA)).save(consumer);
    }

    public static void concretePowder(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, itemLike, 8).requires(itemLike2).requires(Blocks.SAND, 4).requires(Blocks.GRAVEL, 4).group("concrete_powder").unlockedBy("has_sand", has(Blocks.SAND)).unlockedBy("has_gravel", has(Blocks.GRAVEL)).save(consumer);
    }

    public static void candle(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, itemLike).requires(Blocks.CANDLE).requires(itemLike2).group("dyed_candle").unlockedBy(getHasName(itemLike2), has(itemLike2)).save(consumer);
    }

    public static void wall(Consumer<FinishedRecipe> consumer, RecipeCategory recipeCategory, ItemLike itemLike, ItemLike itemLike2) {
        wallBuilder(recipeCategory, itemLike, Ingredient.of(itemLike2)).unlockedBy(getHasName(itemLike2), has(itemLike2)).save(consumer);
    }

    public static RecipeBuilder wallBuilder(RecipeCategory recipeCategory, ItemLike itemLike, Ingredient ingredient) {
        return ShapedRecipeBuilder.shaped(recipeCategory, itemLike, 6).define('#', ingredient).pattern("###").pattern("###");
    }

    public static void polished(Consumer<FinishedRecipe> consumer, RecipeCategory recipeCategory, ItemLike itemLike, ItemLike itemLike2) {
        polishedBuilder(recipeCategory, itemLike, Ingredient.of(itemLike2)).unlockedBy(getHasName(itemLike2), has(itemLike2)).save(consumer);
    }

    public static RecipeBuilder polishedBuilder(RecipeCategory recipeCategory, ItemLike itemLike, Ingredient ingredient) {
        return ShapedRecipeBuilder.shaped(recipeCategory, itemLike, 4).define('S', ingredient).pattern("SS").pattern("SS");
    }

    public static void cut(Consumer<FinishedRecipe> consumer, RecipeCategory recipeCategory, ItemLike itemLike, ItemLike itemLike2) {
        cutBuilder(recipeCategory, itemLike, Ingredient.of(itemLike2)).unlockedBy(getHasName(itemLike2), has(itemLike2)).save(consumer);
    }

    public static ShapedRecipeBuilder cutBuilder(RecipeCategory recipeCategory, ItemLike itemLike, Ingredient ingredient) {
        return ShapedRecipeBuilder.shaped(recipeCategory, itemLike, 4).define('#', ingredient).pattern("##").pattern("##");
    }

    public static void chiseled(Consumer<FinishedRecipe> consumer, RecipeCategory recipeCategory, ItemLike itemLike, ItemLike itemLike2) {
        chiseledBuilder(recipeCategory, itemLike, Ingredient.of(itemLike2)).unlockedBy(getHasName(itemLike2), has(itemLike2)).save(consumer);
    }

    public static void mosaicBuilder(Consumer<FinishedRecipe> consumer, RecipeCategory recipeCategory, ItemLike itemLike, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(recipeCategory, itemLike).define('#', itemLike2).pattern("#").pattern("#").unlockedBy(getHasName(itemLike2), has(itemLike2)).save(consumer);
    }

    public static ShapedRecipeBuilder chiseledBuilder(RecipeCategory recipeCategory, ItemLike itemLike, Ingredient ingredient) {
        return ShapedRecipeBuilder.shaped(recipeCategory, itemLike).define('#', ingredient).pattern("#").pattern("#");
    }

    public static void stonecutterResultFromBase(Consumer<FinishedRecipe> consumer, RecipeCategory recipeCategory, ItemLike itemLike, ItemLike itemLike2) {
        stonecutterResultFromBase(consumer, recipeCategory, itemLike, itemLike2, 1);
    }

    public static void stonecutterResultFromBase(Consumer<FinishedRecipe> consumer, RecipeCategory recipeCategory, ItemLike itemLike, ItemLike itemLike2, int i) {
        SingleItemRecipeBuilder var10000 = SingleItemRecipeBuilder.stonecutting(Ingredient.of(itemLike2), recipeCategory, itemLike, i).unlockedBy(getHasName(itemLike2), has(itemLike2));
        String var10002 = getConversionRecipeName(itemLike, itemLike2);
        var10000.save(consumer, var10002 + "_stonecutting");
    }

    public static void smeltingResultFromBase(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(itemLike2), RecipeCategory.BUILDING_BLOCKS, itemLike, 0.1F, 200).unlockedBy(getHasName(itemLike2), has(itemLike2)).save(consumer);
    }

    public static void nineBlockStorageRecipes(Consumer<FinishedRecipe> consumer, RecipeCategory recipeCategory, ItemLike itemLike, RecipeCategory recipeCategory2, ItemLike itemLike2) {
        nineBlockStorageRecipes(consumer, recipeCategory, itemLike, recipeCategory2, itemLike2, getSimpleRecipeName(itemLike2), null, getSimpleRecipeName(itemLike), null);
    }

    public static void nineBlockStorageRecipesWithCustomPacking(Consumer<FinishedRecipe> consumer, RecipeCategory recipeCategory, ItemLike itemLike, RecipeCategory recipeCategory2, ItemLike itemLike2, String string, String string2) {
        nineBlockStorageRecipes(consumer, recipeCategory, itemLike, recipeCategory2, itemLike2, string, string2, getSimpleRecipeName(itemLike), null);
    }

    public static void nineBlockStorageRecipesRecipesWithCustomUnpacking(Consumer<FinishedRecipe> consumer, RecipeCategory recipeCategory, ItemLike itemLike, RecipeCategory recipeCategory2, ItemLike itemLike2, String string, String string2) {
        nineBlockStorageRecipes(consumer, recipeCategory, itemLike, recipeCategory2, itemLike2, getSimpleRecipeName(itemLike2), null, string, string2);
    }

    public static void nineBlockStorageRecipes(Consumer<FinishedRecipe> consumer, RecipeCategory recipeCategory, ItemLike itemLike, RecipeCategory recipeCategory2, ItemLike itemLike2, String string, @Nullable String string2, String string3, @Nullable String string4) {
        ShapelessRecipeBuilder.shapeless(recipeCategory, itemLike, 9).requires(itemLike2).group(string4).unlockedBy(getHasName(itemLike2), has(itemLike2)).save(consumer, new ResourceLocation(string3));
        ShapedRecipeBuilder.shaped(recipeCategory2, itemLike2).define('#', itemLike).pattern("###").pattern("###").pattern("###").group(string2).unlockedBy(getHasName(itemLike), has(itemLike)).save(consumer, new ResourceLocation(string));
    }

    public static void copySmithingTemplate(Consumer<FinishedRecipe> consumer, ItemLike itemLike, TagKey<Item> tagKey) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, itemLike, 2).define('#', Items.DIAMOND).define('C', tagKey).define('S', itemLike).pattern("#S#").pattern("#C#").pattern("###").unlockedBy(getHasName(itemLike), has(itemLike)).save(consumer);
    }

    public static void copySmithingTemplate(Consumer<FinishedRecipe> consumer, ItemLike itemLike, ItemLike itemLike2) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, itemLike, 2).define('#', Items.DIAMOND).define('C', itemLike2).define('S', itemLike).pattern("#S#").pattern("#C#").pattern("###").unlockedBy(getHasName(itemLike), has(itemLike)).save(consumer);
    }

    public static void cookRecipes(Consumer<FinishedRecipe> consumer, String string, RecipeSerializer<? extends AbstractCookingRecipe> recipeSerializer, int i) {
        simpleCookingRecipe(consumer, string, recipeSerializer, i, Items.BEEF, Items.COOKED_BEEF, 0.35F);
        simpleCookingRecipe(consumer, string, recipeSerializer, i, Items.CHICKEN, Items.COOKED_CHICKEN, 0.35F);
        simpleCookingRecipe(consumer, string, recipeSerializer, i, Items.COD, Items.COOKED_COD, 0.35F);
        simpleCookingRecipe(consumer, string, recipeSerializer, i, Items.KELP, Items.DRIED_KELP, 0.1F);
        simpleCookingRecipe(consumer, string, recipeSerializer, i, Items.SALMON, Items.COOKED_SALMON, 0.35F);
        simpleCookingRecipe(consumer, string, recipeSerializer, i, Items.MUTTON, Items.COOKED_MUTTON, 0.35F);
        simpleCookingRecipe(consumer, string, recipeSerializer, i, Items.PORKCHOP, Items.COOKED_PORKCHOP, 0.35F);
        simpleCookingRecipe(consumer, string, recipeSerializer, i, Items.POTATO, Items.BAKED_POTATO, 0.35F);
        simpleCookingRecipe(consumer, string, recipeSerializer, i, Items.RABBIT, Items.COOKED_RABBIT, 0.35F);
    }

    public static void simpleCookingRecipe(Consumer<FinishedRecipe> consumer, String string, RecipeSerializer<? extends AbstractCookingRecipe> recipeSerializer, int i, ItemLike itemLike, ItemLike itemLike2, float f) {
        SimpleCookingRecipeBuilder var10000 = SimpleCookingRecipeBuilder.generic(Ingredient.of(itemLike), RecipeCategory.FOOD, itemLike2, f, i, recipeSerializer).unlockedBy(getHasName(itemLike), has(itemLike));
        String var10002 = getItemName(itemLike2);
        var10000.save(consumer, var10002 + "_from_" + string);
    }

    public static void waxRecipes(Consumer<FinishedRecipe> consumer) {
        HoneycombItem.WAXABLES.get().forEach((block, block2) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, block2).requires(block).requires(Items.HONEYCOMB).group(getItemName(block2)).unlockedBy(getHasName(block), has(block)).save(consumer, getConversionRecipeName(block2, Items.HONEYCOMB));
        });
    }

    public static void generateRecipes(Consumer<FinishedRecipe> consumer, BlockFamily blockFamily) {
        blockFamily.getVariants().forEach((variant, block) -> {
            BiFunction<ItemLike, ItemLike, RecipeBuilder> biFunction = SHAPE_BUILDERS.get(variant);
            ItemLike itemLike = getBaseBlock(blockFamily, variant);
            if (biFunction != null) {
                RecipeBuilder recipeBuilder = biFunction.apply(block, itemLike);
                blockFamily.getRecipeGroupPrefix().ifPresent((string) -> {
                    recipeBuilder.group(string + (variant == BlockFamily.Variant.CUT ? "" : "_" + variant.getName()));
                });
                recipeBuilder.unlockedBy(blockFamily.getRecipeUnlockedBy().orElseGet(() -> {
                    return getHasName(itemLike);
                }), has(itemLike));
                recipeBuilder.save(consumer);
            }

            if (variant == BlockFamily.Variant.CRACKED) {
                smeltingResultFromBase(consumer, block, itemLike);
            }

        });
    }

    public static Block getBaseBlock(BlockFamily blockFamily, BlockFamily.Variant variant) {
        if (variant == BlockFamily.Variant.CHISELED) {
            if (!blockFamily.getVariants().containsKey(BlockFamily.Variant.SLAB)) {
                throw new IllegalStateException("Slab is not defined for the family.");
            } else {
                return blockFamily.get(BlockFamily.Variant.SLAB);
            }
        } else {
            return blockFamily.getBaseBlock();
        }
    }

    public static EnterBlockTrigger.TriggerInstance insideOf(Block block) {
        return new EnterBlockTrigger.TriggerInstance(ContextAwarePredicate.ANY, block, StatePropertiesPredicate.ANY);
    }

    public static InventoryChangeTrigger.TriggerInstance has(MinMaxBounds.Ints ints, ItemLike itemLike) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(itemLike).withCount(ints).build());
    }

    public static InventoryChangeTrigger.TriggerInstance has(ItemLike itemLike) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(new ItemLike[]{itemLike}).build());
    }

    public static InventoryChangeTrigger.TriggerInstance has(TagKey<Item> tagKey) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(tagKey).build());
    }

    public static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... itemPredicates) {
        return new InventoryChangeTrigger.TriggerInstance(ContextAwarePredicate.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, itemPredicates);
    }

    public static String getHasName(ItemLike itemLike) {
        return "has_" + getItemName(itemLike);
    }

    public static String getItemName(ItemLike itemLike) {
        return RegistryWrapper.ITEM.getKey(itemLike.asItem()).getPath();
    }

    public static String getSimpleRecipeName(ItemLike itemLike) {
        return getItemName(itemLike);
    }

    public static String getConversionRecipeName(ItemLike itemLike, ItemLike itemLike2) {
        String var10000 = getItemName(itemLike);
        return var10000 + "_from_" + getItemName(itemLike2);
    }

    public static String getSmeltingRecipeName(ItemLike itemLike) {
        return getItemName(itemLike) + "_from_smelting";
    }

    public static String getBlastingRecipeName(ItemLike itemLike) {
        return getItemName(itemLike) + "_from_blasting";
    }

    @Override
    public String getName() {
        return "Recipes";
    }
}
