package net.lizistired.herobrinereturns;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.lizistired.herobrinereturns.entities.models.BaseHerobrineEntityModel;
import net.lizistired.herobrinereturns.entities.renderers.BaseHerobrineEntityRenderer;
import net.lizistired.herobrinereturns.utils.registry.RegisterEntities;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class HerobrineReturnsClient implements ClientModInitializer {
    public static final EntityModelLayer MODEL_HEROBRINE_LAYER = new EntityModelLayer(new Identifier("minecraft", "herobrine"), "main");
    @Override
    public void onInitializeClient() {
        /*
         * Registers our Cube Entity's renderer, which provides a model and texture for the entity.
         *
         * Entity Renderers can also manipulate the model before it renders based on entity context (EndermanEntityRenderer#render).
         */
        /*EntityRendererRegistry.INSTANCE.register(ExampleMod.HEROBRINE, (context) -> {
            return new CubeEntityRenderer(context);
        }); */
        // In 1.17, use EntityRendererRegistry.register (seen below) instead of EntityRendererRegistry.INSTANCE.register (seen above)
        EntityRendererRegistry.register(RegisterEntities.HEROBRINE, BaseHerobrineEntityRenderer::new);

        EntityModelLayerRegistry.registerModelLayer(MODEL_HEROBRINE_LAYER, BaseHerobrineEntityModel::getTexturedModelData);

        ParticleFactoryRegistry.getInstance().register(HerobrineReturns.HEROBRINE_JUMPSCARE, HerobrineJumpscareParticle.Factory::new);
    }
}
