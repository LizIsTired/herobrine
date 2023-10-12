package net.lizistired.herobrinereturns.entities.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lizistired.herobrinereturns.HerobrineReturnsClient;
import net.lizistired.herobrinereturns.entities.herobrinetypes.DecoyHerobrineEntity;
import net.lizistired.herobrinereturns.entities.herobrinetypes.HerobrineBoss;
import net.lizistired.herobrinereturns.entities.models.BaseHerobrineEntityModel;
import net.lizistired.herobrinereturns.entities.models.HerobrineBossEntityModel;
import net.lizistired.herobrinereturns.entities.models.HerobrineDecoyEntityModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import static net.lizistired.herobrinereturns.HerobrineReturnsClient.MODEL_HEROBRINE_DECOY_LAYER;

@Environment(value= EnvType.CLIENT)
public class HerobrineDecoyEntityRenderer<T extends DecoyHerobrineEntity> extends MobEntityRenderer<T, HerobrineDecoyEntityModel<T>> {
    public static final Identifier TEXTURE = new Identifier("herobrinereturns", "textures/entity/herobrine/herobrine.png");
    public static final RenderLayer LAYER = RenderLayer.getEntityTranslucent(TEXTURE, false);

    public HerobrineDecoyEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new HerobrineDecoyEntityModel<T>(context.getPart(MODEL_HEROBRINE_DECOY_LAYER)), 0.5f);
    }
    @Override
    public Identifier getTexture(DecoyHerobrineEntity entity) {
        return TEXTURE;
    }

    @Nullable
    @Override
    protected RenderLayer getRenderLayer(T entity, boolean showBody, boolean translucent, boolean showOutline) {
        return LAYER;
    }

    //@Override
    //public void render(T mobEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
    //    matrixStack.push();
    //    matrixStack.multiply(this.dispatcher.getRotation().rotateLocalZ(180.0F));
    //    VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(mobEntity)));
    //    matrixStack.pop();
    //    this.model.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(mobEntity, 0.0f), 1.0f, 1.0f, 1.0f, 0.5f);
    //    //super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    //}
}
