package net.lizistired.herobrine.entities;

import net.lizistired.herobrine.ExampleModClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class HerobrineRenderer extends MobEntityRenderer<Herobrine, HerobrineEntityModel> {
    public HerobrineRenderer(EntityRendererFactory.Context context) {
        super(context, new HerobrineEntityModel(context.getPart(ExampleModClient.MODEL_HEROBRINE_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(Herobrine entity) {
        return new Identifier("herobrine", "textures/entity/herobrine/herobrine.png");
    }
}
