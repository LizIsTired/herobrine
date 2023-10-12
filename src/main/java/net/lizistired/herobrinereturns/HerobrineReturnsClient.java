package net.lizistired.herobrinereturns;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.lizistired.herobrinereturns.entities.herobrinetypes.HerobrineBoss;
import net.lizistired.herobrinereturns.entities.models.BaseHerobrineEntityModel;
import net.lizistired.herobrinereturns.entities.models.HerobrineBossEntityModel;
import net.lizistired.herobrinereturns.entities.models.HerobrineCowModel;
import net.lizistired.herobrinereturns.entities.models.ScaryHerobrineModel;
import net.lizistired.herobrinereturns.entities.renderers.*;
import net.lizistired.herobrinereturns.particles.HerobrineJumpscareParticle;
import net.lizistired.herobrinereturns.utils.registry.RegisterEntities;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import static net.lizistired.herobrinereturns.utils.registry.RegisterParticles.HEROBRINE_JUMPSCARE;

@Environment(EnvType.CLIENT)
public class HerobrineReturnsClient implements ClientModInitializer {
    public static final EntityModelLayer MODEL_HEROBRINE_LAYER = new EntityModelLayer(new Identifier("minecraft", "herobrine"), "main");
    public static final EntityModelLayer MODEL_HEROBRINE_LAYER_BOSS = new EntityModelLayer(new Identifier("minecraft", "herobrineboss"), "main");
    public static final EntityModelLayer MODEL_HEROBRINE_DECOY_LAYER = new EntityModelLayer(new Identifier("minecraft", "herobrine_decoy"), "main");
    public static final EntityModelLayer MODEL_HEROBRINE_SCARY_LAYER = new EntityModelLayer(new Identifier("minecraft", "scary_herobrine"), "main");
    public static final EntityModelLayer HEROBRINE_COW_ENTITY_LAYER = new EntityModelLayer(new Identifier("minecraft", "herobrine_cow"), "main");
    public static WorldRenderContext context;
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(RegisterEntities.STANDARD_HEROBRINE_ENTITY, BaseHerobrineEntityRenderer::new);
        EntityRendererRegistry.register(RegisterEntities.DECOY_HEROBRINE_ENTITY, HerobrineDecoyEntityRenderer::new);
        EntityRendererRegistry.register(RegisterEntities.SCARY_HEROBRINE, HerobrineScaryRenderer::new);
        EntityRendererRegistry.register(RegisterEntities.HEROBRINE_COW_ENTITY, HerobrineCowRenderer::new);

        EntityRendererRegistry.register(RegisterEntities.HEROBRINE_BOSS_ENTITY_TYPE, context -> new HerobrineBossEntityRenderer<>(context, HerobrineBoss.scale));
        //EntityRendererRegistry.register(RegisterEntities.HEROBRINE_SHRINE, CursedBookItemDisplayRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_HEROBRINE_LAYER, BaseHerobrineEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(HEROBRINE_COW_ENTITY_LAYER, HerobrineCowModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(MODEL_HEROBRINE_LAYER_BOSS, HerobrineBossEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(MODEL_HEROBRINE_DECOY_LAYER, BaseHerobrineEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(MODEL_HEROBRINE_SCARY_LAYER, ScaryHerobrineModel::getTexturedModelData);
        ParticleFactoryRegistry.getInstance().register(HEROBRINE_JUMPSCARE, HerobrineJumpscareParticle.Factory::new);
        //ParticleFactoryRegistry.getInstance().register(HEROBRINE_JUMPSCARE_GOOFY_AHHH, HerobrineJumpscareParticleGoofyAhhh.Factory::new);
        ClientTickEvents.START_CLIENT_TICK.register(this::testingFunction1);
        WorldRenderEvents.START.register(this::testingFunction2);
    }


    private void testingFunction2(WorldRenderContext worldRenderContext) {
        context = worldRenderContext;
    }

    private void testingFunction1(MinecraftClient minecraftClient) {
        if (minecraftClient.player != null) {
            PlayerEntity player = minecraftClient.player;
            //player.sendMessage(Text.of("He's getting closer..."), true);
        }
    }
}
