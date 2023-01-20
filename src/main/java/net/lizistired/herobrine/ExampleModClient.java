package net.lizistired.herobrine;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.lizistired.herobrine.entities.HerobrineEntityModel;
import net.lizistired.herobrine.entities.HerobrineRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ExampleModClient implements ClientModInitializer {
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
        EntityRendererRegistry.register(ExampleMod.HEROBRINE, (context) -> {
            return new HerobrineRenderer(context);
        });

        EntityModelLayerRegistry.registerModelLayer(MODEL_HEROBRINE_LAYER, HerobrineEntityModel::getTexturedModelData);
    }
}
