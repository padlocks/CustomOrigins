package io.github.padlocks.customorigins.entity.feature;

import io.github.padlocks.customorigins.client.Config;
import io.github.padlocks.customorigins.networking.NetworkingConstants;
import io.github.padlocks.customorigins.power.CustomOriginsPowers;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.EnergySwirlOverlayFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.SkinOverlayOwner;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class PlayerWitherArmorEnergySwirlOverlayFeatureRenderer<T extends LivingEntity & SkinOverlayOwner>
        extends EnergySwirlOverlayFeatureRenderer<T, BipedEntityModel<T>> {

    private static final Identifier SKIN = new Identifier("textures/entity/wither/wither_armor.png");
    private final BipedEntityModel<T> model = new BipedEntityModel(2.0F);
    private static boolean showPlayerOverlays = true;

    public PlayerWitherArmorEnergySwirlOverlayFeatureRenderer(
            FeatureRendererContext<T, BipedEntityModel<T>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity,
            float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw,
            float headPitch) {
        
        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.UPDATE_CONFIG_PLAYER_OVERLAYS,
            (server, serverPlayer, handler, buf, responseSender) -> {
                // Read packet data on the event loop
                Boolean clientOverlays = buf.readBoolean();

                server.execute(() -> {
                    showPlayerOverlays = !clientOverlays;

                });
            });

        if (CustomOriginsPowers.UNDEAD_RESISTANCE.isActive(entity) && showPlayerOverlays) {
            float f = (float) entity.age + tickDelta;
            BipedEntityModel<T> entityModel = this.getContextModel();
            entityModel.animateModel(entity, limbAngle, limbDistance, tickDelta);
            this.getContextModel().copyStateTo(entityModel);
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(
                    RenderLayer.getEnergySwirl(this.getEnergySwirlTexture(), this.getEnergySwirlX(f), f * 0.01F));
            entityModel.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
            entityModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 0.5F, 0.5F, 0.5F, 1.0F);
        }
    }

    protected float getEnergySwirlX(float partialAge) {
        return partialAge * 0.01F;
    }

    protected net.minecraft.util.Identifier getEnergySwirlTexture() {
        return SKIN;
    }

    protected EntityModel<T> getEnergySwirlModel() {
        return this.model;
    }
}
