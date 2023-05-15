package com.teamaurora.magnetosphere.core.client.render.entity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import com.teamaurora.magnetosphere.api.entity.v1.CustomBoat;
import com.teamaurora.magnetosphere.api.item.v1.CustomBoatType;
import com.teamaurora.magnetosphere.core.Magnetosphere;
import com.teamaurora.magnetosphere.core.registry.MagnetosphereRegistries;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Quaternionf;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ApiStatus.Internal
public class CustomBoatRenderer<T extends CustomBoat> extends EntityRenderer<T> {
    private final Map<CustomBoatType, Pair<ResourceLocation, BoatModel>> boatResources;
    private final EntityRendererProvider.Context ctx;
    private final boolean chest;

    public CustomBoatRenderer(EntityRendererProvider.Context context, boolean chest) {
        super(context);
        this.shadowRadius = 0.8F;
        this.chest = chest;

        this.ctx = context;
        this.boatResources = MagnetosphereRegistries.BOAT_TYPES
                .stream()
                .collect(
                        ImmutableMap.toImmutableMap(type -> type, type -> Pair.of(chest ? type.chestVariantTexture() : type.texture(), this.createBoatModel(context, type, chest)))
                );
    }

    @Override
    public void render(T boat, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {

        poseStack.pushPose();
        poseStack.translate(0.0D, 0.375D, 0.0D);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - f));
        float h = (float) boat.getHurtTime() - g;
        float j = boat.getDamage() - g;
        if(j < 0.0F) {
            j = 0.0F;
        }

        if(h > 0.0F) {
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(h) * h * j / 10.0F * (float) boat.getHurtDir()));
        }

        float k = boat.getBubbleAngle(g);
        if(!Mth.equal(k, 0.0F)) {
            poseStack.mulPose(new Quaternionf().setAngleAxis(boat.getBubbleAngle(g) * (float) (Math.PI / 180.0), 1.0F, 0.0F, 1.0F));
        }

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));

        Pair<ResourceLocation, BoatModel> data = boatResources.get(boat.getBoatCustomType());
        BoatModel model = data.getSecond();
        ResourceLocation texture = data.getFirst();

        model.setupAnim(boat, g, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(model.renderType(texture));
        model.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        if (!boat.isUnderWater()) {
            VertexConsumer vertexConsumer2 = multiBufferSource.getBuffer(RenderType.waterMask());
            model.waterPatch().render(poseStack, vertexConsumer2, i, OverlayTexture.NO_OVERLAY);
        }
        poseStack.popPose();
        super.render(boat, f, g, poseStack, multiBufferSource, i);
    }

    private BoatModel createBoatModel(EntityRendererProvider.Context context, CustomBoatType type, boolean bl) {
        ModelLayerLocation modelLayerLocation = bl ? createChestBoatModelName(type) : createBoatModelName(type);
        return bl ? new ChestBoatModel(context.bakeLayer(modelLayerLocation)) : new BoatModel(context.bakeLayer(modelLayerLocation));
    }

    public static ModelLayerLocation createBoatModelName(CustomBoatType type) {
        ResourceLocation location =  Objects.requireNonNull(MagnetosphereRegistries.BOAT_TYPES.getKey(type));
        return new ModelLayerLocation(new ResourceLocation(location.getNamespace(), "boat/" + location.getPath()), "main");
    }

    public static ModelLayerLocation createChestBoatModelName(CustomBoatType type) {
        ResourceLocation location =  Objects.requireNonNull(MagnetosphereRegistries.BOAT_TYPES.getKey(type));
        return new ModelLayerLocation(new ResourceLocation(location.getNamespace(), "chest_boat/" + location.getPath()), "main");
    }

    @Override
    public ResourceLocation getTextureLocation(T boat) {
        return this.getModelWithLocation(boat).getFirst();
    }

    public Pair<ResourceLocation, BoatModel> getModelWithLocation(T boat) {
        return this.boatResources.get(boat.getBoatCustomType());
    }
}