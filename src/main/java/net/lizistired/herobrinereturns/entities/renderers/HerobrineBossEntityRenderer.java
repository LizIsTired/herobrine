package net.lizistired.herobrinereturns.entities.renderers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lizistired.herobrinereturns.HerobrineReturnsClient;
import net.lizistired.herobrinereturns.entities.herobrinetypes.HerobrineBoss;
import net.lizistired.herobrinereturns.entities.models.HerobrineBossEntityModel;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EndermanEyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.feature.PlayerHeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.util.Identifier;
@Environment(value= EnvType.CLIENT)
public class HerobrineBossEntityRenderer<T extends HerobrineBoss> extends MobEntityRenderer<T, HerobrineBossEntityModel<T>> {
    public static final Identifier TEXTURE = new Identifier("herobrinereturns", "textures/entity/herobrine/herobrine.png");
    private final float scale;
    public HerobrineBossEntityRenderer(EntityRendererFactory.Context context, float scale) {
        super(context, new HerobrineBossEntityModel<>(context.getPart(HerobrineReturnsClient.MODEL_HEROBRINE_LAYER_BOSS)), 0.5f * scale);
        this.scale = scale;
        this.addFeature(new HerobrineBossEyesFeatureRenderer<>(this));
        this.addFeature(new HeldItemFeatureRenderer<T, HerobrineBossEntityModel<T>>(this, context.getHeldItemRenderer()) {
            @Override
            public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T spellcastingIllagerEntity, float f, float g, float h, float j, float k, float l) {
                if ((spellcastingIllagerEntity).isSpellcasting()) {
                    super.render(matrixStack, vertexConsumerProvider, i, spellcastingIllagerEntity, f, g, h, j, k, l);
                }
            }
        });


    }

    @Override
    protected void scale(HerobrineBoss herobrineBossEntity, MatrixStack matrixStack, float f) {
        matrixStack.scale(this.scale, this.scale, this.scale);
    }
    @Override
    public Identifier getTexture(HerobrineBoss entity) {
        return TEXTURE;
    }

}
