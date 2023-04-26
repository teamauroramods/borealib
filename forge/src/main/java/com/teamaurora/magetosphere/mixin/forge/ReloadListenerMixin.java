package com.teamaurora.magetosphere.mixin.forge;

import com.teamaurora.magetosphere.api.resource_loader.v1.NamedReloadListener;
import com.teamaurora.magetosphere.api.resource_loader.v1.ReloadListenerKeys;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.ServerFunctionLibrary;
import net.minecraft.tags.TagManager;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.storage.loot.LootTables;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Locale;

@Mixin({RecipeManager.class, ServerAdvancementManager.class, ServerFunctionLibrary.class, LootTables.class, TagManager.class})
public abstract class ReloadListenerMixin implements NamedReloadListener {

    @Unique
    private ResourceLocation magnetosphere$id;

    @Override
    @SuppressWarnings({"ConstantConditions"})
    public ResourceLocation getId() {
        if (this.magnetosphere$id == null) {
            Object self = this;

            if (self instanceof RecipeManager) {
                this.magnetosphere$id = ReloadListenerKeys.RECIPES;
            } else if (self instanceof ServerAdvancementManager) {
                this.magnetosphere$id = ReloadListenerKeys.ADVANCEMENTS;
            } else if (self instanceof ServerFunctionLibrary) {
                this.magnetosphere$id = ReloadListenerKeys.FUNCTIONS;
            } else if (self instanceof LootTables) {
                this.magnetosphere$id = ReloadListenerKeys.LOOT_TABLES;
            } else if (self instanceof TagManager) {
                this.magnetosphere$id = ReloadListenerKeys.TAGS;
            } else {
                this.magnetosphere$id = new ResourceLocation("minecraft", "private/" + self.getClass().getSimpleName().toLowerCase(Locale.ROOT));
            }
        }
        return this.magnetosphere$id;
    }
}
