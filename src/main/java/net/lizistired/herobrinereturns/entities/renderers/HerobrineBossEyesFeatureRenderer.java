package net.lizistired.herobrinereturns.entities.renderers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lizistired.herobrinereturns.HerobrineReturns;
import net.lizistired.herobrinereturns.entities.herobrinetypes.HerobrineBoss;
import net.lizistired.herobrinereturns.entities.models.HerobrineBossEntityModel;
import net.lizistired.herobrinereturns.renderlayers.EmissiveRenderLayer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.util.Identifier;

@Environment(value= EnvType.CLIENT)
public class HerobrineBossEyesFeatureRenderer<T extends HerobrineBoss, M extends HerobrineBossEntityModel<T>>
        extends EyesFeatureRenderer<T, M> {
    private static final RenderLayer SKIN = RenderLayer.getEntityCutout(new Identifier(HerobrineReturns.LOGGER.getName(), "textures/entity/herobrine/herobrine_eyes.png"));
    //private static final RenderLayer ANOTHER_ONE = EmissiveRenderLayer.getEndPortalWithTexture(new Identifier(HerobrineReturns.LOGGER.getName(), "textures/entity/herobrine/herobrine_eyes.png"));
    public HerobrineBossEyesFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext) {
        super(featureRendererContext);
    }


    @Override
    public RenderLayer getEyesTexture() {
        return SKIN;
    }
}
