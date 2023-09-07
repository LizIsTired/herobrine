package net.lizistired.herobrinereturns.entities.renderers;

import net.lizistired.herobrinereturns.entities.HerobrineShrine;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.world.World;

public class CursedItemBookDisplayRenderer extends DisplayEntity.ItemDisplayEntity {
    public CursedItemBookDisplayRenderer(EntityType<?> entityType, World world) {
        super(entityType, world);
    }


}
