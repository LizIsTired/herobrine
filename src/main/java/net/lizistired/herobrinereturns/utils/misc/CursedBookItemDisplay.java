package net.lizistired.herobrinereturns.utils.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lizistired.herobrinereturns.entities.HerobrineShrine;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;

@Environment(EnvType.CLIENT)
public class CursedBookItemDisplay extends EntityRenderer<HerobrineShrine> {
    public CursedBookItemDisplay(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.shadowRadius = 0.15f;
        this.shadowOpacity = 0.75f;
    }

    private static final float field_32924 = 0.15f;
    private static final int MAX_COUNT_FOR_4_ITEMS_RENDERED = 48;
    private static final int MAX_COUNT_FOR_3_ITEMS_RENDERED = 32;
    private static final int MAX_COUNT_FOR_2_ITEMS_RENDERED = 16;
    private static final int MAX_COUNT_FOR_1_ITEM_RENDERED = 1;
    private static final float field_32929 = 0.0f;
    private static final float field_32930 = 0.0f;
    private static final float field_32931 = 0.09375f;
    private final ItemRenderer itemRenderer;
    private final Random random = Random.create();

    /* @Override //todo fix this
    public void render(HerobrineShrine shrineEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        float t;
        float s;
        matrixStack.push();
        HerobrineShrine shrineEntity = shrineEntity;
        int j = shrineEntity.isEmpty() ? 187 : Item.getRawId(shrineEntity.getItem()) + shrineEntity.getDamage();
        this.random.setSeed(j);
        BakedModel bakedModel = this.itemRenderer.getModel(shrineEntity, itemEntity.world, null, itemEntity.getId());
        boolean bl = bakedModel.hasDepth();/
        int k = this.getRenderedAmount(shrineEntity);
        float h = 0.25f;
        float l = MathHelper.sin(((float)itemEntity.getItemAge() + g) / 10.0f + itemEntity.uniqueOffset) * 0.1f + 0.1f;
        float m = bakedModel.getTransformation().getTransformation((ModelTransformationMode)ModelTransformationMode.GROUND).scale.y();
        matrixStack.translate(0.0f, l + 0.25f * m, 0.0f);
        float n = itemEntity.getRotation(g);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(n));
        float o = bakedModel.getTransformation().ground.scale.x();
        float p = bakedModel.getTransformation().ground.scale.y();
        float q = bakedModel.getTransformation().ground.scale.z();
        if (!bl) {
            float r = -0.0f * (float)(k - 1) * 0.5f * o;
            s = -0.0f * (float)(k - 1) * 0.5f * p;
            t = -0.09375f * (float)(k - 1) * 0.5f * q;
            matrixStack.translate(r, s, t);
        }
        for (int u = 0; u < k; ++u) {
            matrixStack.push();
            if (u > 0) {
                if (bl) {
                    s = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    t = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    float v = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    matrixStack.translate(s, t, v);
                } else {
                    s = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
                    t = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
                    matrixStack.translate(s, t, 0.0f);
                }
            }
            this.itemRenderer.renderItem(shrineEntity, ModelTransformationMode.GROUND, false, matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV, bakedModel);
            matrixStack.pop();
            if (bl) continue;
            matrixStack.translate(0.0f * o, 0.0f * p, 0.09375f * q);
        }
        matrixStack.pop();
        super.render(shrineEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }*/
    @Override
    public Identifier getTexture(HerobrineShrine entity) {
        return null;
    }
}
