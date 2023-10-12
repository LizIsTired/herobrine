package net.lizistired.herobrinereturns.entities.renderers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lizistired.herobrinereturns.HerobrineReturnsClient;
import net.lizistired.herobrinereturns.entities.BaseHerobrineEntity;
import net.lizistired.herobrinereturns.entities.herobrinetypes.ScaryHerobrineEntity;
import net.lizistired.herobrinereturns.entities.models.BaseHerobrineEntityModel;
import net.lizistired.herobrinereturns.entities.models.ScaryHerobrineModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.util.Identifier;
@Environment(EnvType.CLIENT)
public class HerobrineScaryRenderer extends MobEntityRenderer<ScaryHerobrineEntity, ScaryHerobrineModel<ScaryHerobrineEntity>> {
    public static final Identifier TEXTURE = new Identifier("herobrinereturns", "textures/entity/herobrine/herobrine.png");
    public HerobrineScaryRenderer(EntityRendererFactory.Context context) {
        super(context, new ScaryHerobrineModel<>(context.getPart(HerobrineReturnsClient.MODEL_HEROBRINE_SCARY_LAYER)), 0.0f);
    }
    @Override
    public Identifier getTexture(ScaryHerobrineEntity entity) {
        return TEXTURE;
    }

}
