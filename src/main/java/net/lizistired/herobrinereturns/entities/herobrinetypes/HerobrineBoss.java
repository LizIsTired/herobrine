package net.lizistired.herobrinereturns.entities.herobrinetypes;

import net.lizistired.herobrinereturns.entities.BaseHerobrineEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;

public class HerobrineBoss extends BaseHerobrineEntity {
    public static final float scale = 2f;
    public HerobrineBoss(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 1.7400001666666667f * scale;
    }

    public static enum State {
        ATTACKING,
        SPELLCASTING,
        NEUTRAL;
    }
}
