package com.teamaurora.borealib.api.block.v1.compat;

import com.google.common.base.Suppliers;
import com.teamaurora.borealib.api.registry.v1.DeferredRegister;
import com.teamaurora.borealib.core.Borealib;
import com.teamaurora.borealib.core.registry.BorealibRegistries;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ChestBlock;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

/**
 * Stores material instances for Borealib chest blocks. Based on Blueprint's chest system and ported to work on both platforms.
 * <p>The chest variant registry uses lazy suppliers to avoid using the materials until registries are fully loaded.
 *
 * @author ebo2022
 * @since 1.0
 */
public record ChestVariant(Material single, Material left, Material right) {

	@ApiStatus.Internal
	public static final DeferredRegister<Supplier<ChestVariant>> WRITER = DeferredRegister.customWriter(BorealibRegistries.CHEST_VARIANTS, Borealib.MOD_ID);

	/**
	 * Registers a chest variant.
	 *
	 * @param name    The name of the chest variant
	 * @param trapped Whether it is trapped
	 */
	public static void register(ResourceLocation name, boolean trapped) {
		String chestType = trapped ? "trapped" : "normal";
		WRITER.register(name, () -> Suppliers.memoize(() -> {
			Material single = new Material(Sheets.CHEST_SHEET, new ResourceLocation(name.getNamespace(), "entity/chest/" + name + "/" + chestType));
			Material left = new Material(Sheets.CHEST_SHEET, new ResourceLocation(name.getNamespace(), "entity/chest/" + name + "/" + chestType + "_left"));
			Material right = new Material(Sheets.CHEST_SHEET, new ResourceLocation(name.getNamespace(), "entity/chest/" + name + "/" + chestType + "_right"));
			return new ChestVariant(single, left, right);
		}));
	}
}