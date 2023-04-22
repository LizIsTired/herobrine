package net.lizistired.herobrinereturns.entities.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.lizistired.herobrinereturns.entities.BaseHerobrineEntity;
import net.lizistired.herobrinereturns.entities.herobrinetypes.HerobrineBoss;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;

@Environment(EnvType.CLIENT)
public class HerobrineBossEntityModel extends BipedEntityModel<HerobrineBoss> {
    public HerobrineBossEntityModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F , 8.0F , 8.0F ), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        modelPartData.addChild("hat", ModelPartBuilder.create().uv(32, 0).cuboid(-4.0F, -8.0F, -4.0F, 8.0F , 8.0F , 8.0F ), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        modelPartData.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 0.0F, -2.0F, 8.0F , 12.0F , 4.0F ), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        modelPartData.addChild("right_arm", ModelPartBuilder.create().uv(40, 16).cuboid(-3.0F, -2.0F, -2.0F, 4.0F , 12.0F , 4.0F ), ModelTransform.pivot(-5.0F, 2.0F, 0.0F));
        modelPartData.addChild("left_arm", ModelPartBuilder.create().uv(40, 16).mirrored().cuboid(-1.0F, -2.0F, -2.0F, 4.0F , 12.0F , 4.0F ), ModelTransform.pivot(5.0F, 2.0F, 0.0F));
        modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F , 12.0F , 4.0F ), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));
        modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-2.0F, 0.0F, -2.0F, 4.0F , 12.0F , 4.0F ), ModelTransform.pivot(1.9F, 12.0F,0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }
    public void setAngles(HerobrineBoss herobrineBossEntity, float f, float g, float h, float i, float j) {
        super.setAngles(herobrineBossEntity, f, g, h, i, j);
    }
}
