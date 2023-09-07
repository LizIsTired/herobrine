package net.lizistired.herobrinereturns.renderlayers;

import com.mojang.datafixers.types.Func;
import net.lizistired.herobrinereturns.HerobrineReturns;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.EndPortalBlockEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.util.function.Function;

public class EmissiveRenderLayer extends RenderLayer {
    public static RenderLayer emissiveHudTris = newEmissiveRenderLayer(new Identifier(HerobrineReturns.LOGGER.getName(), "white.png"), VertexFormat.DrawMode.TRIANGLES, true);

    public EmissiveRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    public static final ShaderProgram END_PORTAL_PROGRAM1 = new ShaderProgram(GameRenderer::getRenderTypeEndPortalProgram);
    public static RenderLayer newEmissiveRenderLayer(Identifier texture, VertexFormat.DrawMode drawMode, boolean transparent) {
        return EmissiveRenderLayer.of("emissive", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, drawMode, 256, false, true, MultiPhaseParameters.builder().program(EYES_PROGRAM).texture(new RenderPhase.Texture((Identifier)texture, false, false)).transparency(transparent ? ADDITIVE_TRANSPARENCY : NO_TRANSPARENCY).writeMaskState(COLOR_MASK).cull(Cull.DISABLE_CULLING).depthTest(DepthTest.ALWAYS_DEPTH_TEST).build(false));
    }

    public static final RenderLayer END_PORTAL5 = RenderLayer.of("end_portal", VertexFormats.POSITION, VertexFormat.DrawMode.QUADS, 256, false, false, MultiPhaseParameters.builder().program(END_PORTAL_PROGRAM).texture(RenderPhase.Textures.create().add(EndPortalBlockEntityRenderer.SKY_TEXTURE, false, false).add(EndPortalBlockEntityRenderer.PORTAL_TEXTURE, false, false).build()).build(false));
    public static RenderLayer getEndPortalWithTexture(Identifier texture) {
        return END_CUTOUT.apply(texture);
    }

    public static RenderLayer getEntityCutout(Identifier texture) {
        return ENTITY_CUTOUT.apply(texture);
    }

    private static final Function<Identifier, RenderLayer> END_CUTOUT = Util.memoize(texture -> {
        MultiPhaseParameters.Builder multiPhaseParameters = MultiPhaseParameters.builder().program(END_PORTAL_PROGRAM1).texture(Textures.create().add(EndPortalBlockEntityRenderer.SKY_TEXTURE, false, false).add(EndPortalBlockEntityRenderer.PORTAL_TEXTURE, false, false).build());
        return RenderLayer.of("entity_end_portal", VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS, 256, true, false, multiPhaseParameters.build(false));
    });

    private static final Function<Identifier, RenderLayer> ENTITY_CUTOUT = Util.memoize(texture -> {
        MultiPhaseParameters multiPhaseParameters = MultiPhaseParameters.builder().program(ENTITY_CUTOUT_PROGRAM).texture(new RenderPhase.Texture((Identifier)texture, false, false)).transparency(NO_TRANSPARENCY).lightmap(ENABLE_LIGHTMAP).overlay(ENABLE_OVERLAY_COLOR).build(true);
        return RenderLayer.of("entity_cutout", VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 256, true, false, multiPhaseParameters);
    });



}