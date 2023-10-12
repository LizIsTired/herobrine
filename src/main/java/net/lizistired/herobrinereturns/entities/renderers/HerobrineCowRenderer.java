package net.lizistired.herobrinereturns.entities.renderers;

import net.lizistired.herobrinereturns.HerobrineReturnsClient;
import net.lizistired.herobrinereturns.entities.herobrinetypes.HerobrineCow;
import net.lizistired.herobrinereturns.entities.models.HerobrineCowModel;
import net.lizistired.herobrinereturns.utils.registry.RegisterEntities;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.SheepWoolFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;
import net.minecraft.client.render.entity.model.SheepEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.Identifier;

public class HerobrineCowRenderer extends MobEntityRenderer<HerobrineCow, HerobrineCowModel<HerobrineCow>> {
    public static final Identifier TEXTURE = new Identifier("herobrinereturns", "textures/entity/herobrine/herobrine_cow.png");

    public HerobrineCowRenderer(EntityRendererFactory.Context context) {
        super(context, new HerobrineCowModel<>(context.getPart(HerobrineReturnsClient.HEROBRINE_COW_ENTITY_LAYER)), 0.7f);
        }

    @Override
    public Identifier getTexture(HerobrineCow sheepEntity) {
        return TEXTURE;
    }
}
