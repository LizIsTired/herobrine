package net.lizistired.herobrine.entities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lizistired.herobrine.ExampleModClient;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class HerobrineRenderer extends MobEntityRenderer<Herobrine, HerobrineEntityModel> {
    public HerobrineRenderer(EntityRendererFactory.Context context) {
        super(context, new HerobrineEntityModel(context.getPart(ExampleModClient.MODEL_HEROBRINE_LAYER)), 0.5f);
    }

    @Override
    public Identifier getTexture(Herobrine entity) {
        return new Identifier("herobrine", "textures/entity/herobrine/herobrine.png");
    }
    /*protected boolean hasLabel(Herobrine herobrineEntity) {
        double d = this.dispatcher.getSquaredDistanceToCamera(herobrineEntity);
        float f = herobrineEntity.isInSneakingPose() ? 32.0F : 64.0F;
        return d >= (double)(f * f) ? false : herobrineEntity.isCustomNameVisible();
    }*/
}
