package net.lizistired.herobrinereturns.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lizistired.herobrinereturns.HerobrineReturnsClient;
import net.lizistired.herobrinereturns.entities.models.BaseHerobrineEntityModel;
import net.lizistired.herobrinereturns.entities.renderers.BaseHerobrineEntityRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class HerobrineJumpscareParticle extends Particle {
    private final ModelPart model;
    private final RenderLayer layer;

    HerobrineJumpscareParticle(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
        this.layer = RenderLayer.getEntityTranslucent(BaseHerobrineEntityRenderer.TEXTURE);
        this.model = new BaseHerobrineEntityModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(HerobrineReturnsClient.MODEL_HEROBRINE_LAYER)).head;
        this.gravityStrength = 0.0F;
        this.maxAge = 5;
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.CUSTOM;
    }

    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        float f = ((float)this.age + tickDelta) / (float)this.maxAge;
        float h = ((float)this.age + tickDelta);
        //float g = 0.05F + 0.5F * MathHelper.sin(f * 3.1415927F);
        float g = h / 0.5f;
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.multiply(camera.getRotation());
        //matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(150.0F * f * 30 - 60.0F));
        //matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(150.0F * f - 60.0F));
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        //matrixStack.translate(0.0F, 0.101F, 0.4F);
        matrixStack.translate(0.0F, 0.201F, 3F - h);
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        VertexConsumer vertexConsumer2 = immediate.getBuffer(this.layer);
        this.model.render(matrixStack, vertexConsumer2, 15728880, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, g);
        immediate.draw();
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<DefaultParticleType> {

        public Factory(SpriteProvider spriteProvider) {
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new HerobrineJumpscareParticle(clientWorld, d, e, f);
        }
    }
}