package net.lizistired.herobrine.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

public class Herobrine extends PathAwareEntity {
    public Herobrine(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }
}
