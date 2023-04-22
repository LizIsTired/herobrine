package net.lizistired.herobrinereturns.entities.renderers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lizistired.herobrinereturns.HerobrineReturnsClient;
import net.lizistired.herobrinereturns.entities.BaseHerobrineEntity;
import net.lizistired.herobrinereturns.entities.herobrinetypes.HerobrineBoss;
import net.lizistired.herobrinereturns.entities.models.BaseHerobrineEntityModel;
import net.lizistired.herobrinereturns.entities.models.HerobrineBossEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.GiantEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BaseHerobrineEntityRenderer extends MobEntityRenderer<BaseHerobrineEntity, BaseHerobrineEntityModel> {
    public static final Identifier TEXTURE = new Identifier("herobrinereturns", "textures/entity/herobrine/herobrine.png");
    public BaseHerobrineEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new BaseHerobrineEntityModel(context.getPart(HerobrineReturnsClient.MODEL_HEROBRINE_LAYER)), 0.5f);
    }
    @Override
    public Identifier getTexture(BaseHerobrineEntity entity) {
        return TEXTURE;
    }

    /*protected boolean hasLabel(Herobrine herobrineEntity) {
        double d = this.dispatcher.getSquaredDistanceToCamera(herobrineEntity);
        float f = herobrineEntity.isInSneakingPose() ? 32.0F : 64.0F;
        return d >= (double)(f * f) ? false : herobrineEntity.isCustomNameVisible();
    }*/
}
