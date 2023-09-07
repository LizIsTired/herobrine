package net.lizistired.herobrinereturns.entities.renderers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lizistired.herobrinereturns.HerobrineReturnsClient;
import net.lizistired.herobrinereturns.entities.herobrinetypes.DecoyHerobrineEntity;
import net.lizistired.herobrinereturns.entities.herobrinetypes.HerobrineBoss;
import net.lizistired.herobrinereturns.entities.models.BaseHerobrineEntityModel;
import net.lizistired.herobrinereturns.entities.models.HerobrineBossEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.util.Identifier;

@Environment(value= EnvType.CLIENT)
public class HerobrineDecoyEntityRenderer<T extends SpellcastingIllagerEntity> extends MobEntityRenderer<DecoyHerobrineEntity, BaseHerobrineEntityModel<DecoyHerobrineEntity>> {
    public static final Identifier TEXTURE = new Identifier("herobrinereturns", "textures/entity/herobrine/herobrine.png");

    public HerobrineDecoyEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new BaseHerobrineEntityModel<>(context.getPart(HerobrineReturnsClient.MODEL_HEROBRINE_DECOY_LAYER)), 0.5f);
    }
    @Override
    public Identifier getTexture(DecoyHerobrineEntity entity) {
        return TEXTURE;
    }

}
