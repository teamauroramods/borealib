package com.teamaurora.borealib.api.block.v1.compat;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

public class ChestVariant {
	private final ResourceLocation single, left, right;
	private final Material singleMaterial, leftMaterial, rightMaterial;

	public ChestVariant(String modId, String type, boolean trapped) {
		String chest = trapped ? "trapped" : "normal";
		this.single = new ResourceLocation(modId, "entity/chest/" + type + "/" + chest);
		this.left = new ResourceLocation(modId, "entity/chest/" + type + "/" + chest + "_left");
		this.right = new ResourceLocation(modId, "entity/chest/" + type + "/" + chest + "_right");
		this.singleMaterial = new Material(Sheets.CHEST_SHEET, this.single);
		this.leftMaterial = new Material(Sheets.CHEST_SHEET, this.left);
		this.rightMaterial = new Material(Sheets.CHEST_SHEET, this.right);
	}

	public Material getSingleMaterial() {
		return this.singleMaterial;
	}
	public Material getLeftMaterial() {
		return this.leftMaterial;
	}

	public Material getRightMaterial() {
		return this.rightMaterial;
	}
}