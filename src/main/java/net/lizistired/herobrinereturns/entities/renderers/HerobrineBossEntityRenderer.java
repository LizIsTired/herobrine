package net.lizistired.herobrinereturns.entities.renderers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lizistired.herobrinereturns.HerobrineReturnsClient;
import net.lizistired.herobrinereturns.entities.BaseHerobrineEntity;
import net.lizistired.herobrinereturns.entities.herobrinetypes.HerobrineBoss;
import net.lizistired.herobrinereturns.entities.models.BaseHerobrineEntityModel;
import net.lizistired.herobrinereturns.entities.models.HerobrineBossEntityModel;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.IllagerEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.IllagerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.GiantEntity;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.util.Identifier;
@Environment(value= EnvType.CLIENT)
public class HerobrineBossEntityRenderer<T extends SpellcastingIllagerEntity> extends MobEntityRenderer<HerobrineBoss, HerobrineBossEntityModel> {
    public static final Identifier TEXTURE = new Identifier("herobrinereturns", "textures/entity/herobrine/herobrine.png");
    private final float scale;
    public HerobrineBossEntityRenderer(EntityRendererFactory.Context context, float scale) {
        super(context, new HerobrineBossEntityModel(context.getPart(HerobrineReturnsClient.MODEL_HEROBRINE_LAYER_BOSS)), 0.5f * scale);
        this.scale = scale;
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
