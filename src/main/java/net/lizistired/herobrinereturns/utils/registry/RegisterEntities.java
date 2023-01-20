package net.lizistired.herobrinereturns.utils.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.lizistired.herobrinereturns.entities.BaseHerobrineEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class RegisterEntities {
    public static final EntityType<BaseHerobrineEntity>
            HEROBRINE = Registry.register(Registries.ENTITY_TYPE, new Identifier("minecraft", "herobrine"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, BaseHerobrineEntity::new)
                    .dimensions(EntityDimensions.fixed(0.75f, 2f))
                    .fireImmune()
                    .build()
    );


    public static void init() {
        FabricDefaultAttributeRegistry.register(HEROBRINE, BaseHerobrineEntity.createHerobrineAttributes());
    }
}
