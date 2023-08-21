package com.teamaurora.borealib.api.block.v1.compat;

import com.google.common.base.Suppliers;
import com.teamaurora.borealib.api.base.v1.platform.EnvExecutor;
import com.teamaurora.borealib.api.base.v1.platform.Environment;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Stores material instances for Borealib chest blocks. Based on Blueprint's chest system and ported to work on both platforms.
 * <p>The chest variant registry uses lazy suppliers to avoid loading the sheets too early.
 *
 * @author ebo2022
 * @since 1.0
 */
public record ChestVariant(Material single, Material left, Material right) {

	private static final Map<ResourceLocation, Supplier<ChestVariant>> REGISTRY = new HashMap<>();

	/**
	 * Registers a chest variant.
	 *
	 * @param name    The name of the chest variant
	 * @param trapped Whether it is trapped
	 * @return The name of the registered variant
	 */
	public static ResourceLocation register(ResourceLocation name, boolean trapped) {
		String chestType = trapped ? "trapped" : "normal";
		ResourceLocation registryName = name.withSuffix("_" + chestType);
		EnvExecutor.unsafeRunWhenOn(Environment.CLIENT, () -> () -> {
			REGISTRY.put(registryName, Suppliers.memoize(() -> {
				Material single = new Material(Sheets.CHEST_SHEET, new ResourceLocation(name.getNamespace(), "entity/chest/" + name.getPath() + "/" + chestType));
				Material left = new Material(Sheets.CHEST_SHEET, new ResourceLocation(name.getNamespace(), "entity/chest/" + name.getPath() + "/" + chestType + "_left"));
				Material right = new Material(Sheets.CHEST_SHEET, new ResourceLocation(name.getNamespace(), "entity/chest/" + name.getPath() + "/" + chestType + "_right"));
				return new ChestVariant(single, left, right);
			}));
		});
		return registryName;
	}

	/**
	 * Retrieves a {@link ChestVariant} with the specified id.
	 *
	 * @param name The name of the chest variant to look for
	 * @return The chest variant if it exists, otherwise <code>null</code>
	 */
	@Nullable
	public static ChestVariant get(ResourceLocation name) {
		return REGISTRY.get(name).get();
	}
}