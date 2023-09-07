package net.lizistired.herobrinereturns.utils.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.lizistired.herobrinereturns.entities.BaseHerobrineEntity;
import net.lizistired.herobrinereturns.entities.herobrinetypes.DecoyHerobrineEntity;
import net.lizistired.herobrinereturns.entities.herobrinetypes.HerobrineBoss;
import net.lizistired.herobrinereturns.entities.herobrinetypes.StandardHerobrineEntity;
import net.lizistired.herobrinereturns.entities.models.HerobrineDecoyEntityModel;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class RegisterEntities {
    public static final EntityType<StandardHerobrineEntity>
            STANDARD_HEROBRINE_ENTITY = Registry.register(Registries.ENTITY_TYPE, new Identifier("minecraft", "herobrine"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, StandardHerobrineEntity::new)
                    .dimensions(EntityDimensions.fixed(0.75f, 2f))
                    .fireImmune()
                    .build()
    );
    public static final EntityType<HerobrineBoss>
            HEROBRINE_BOSS_ENTITY_TYPE = Registry.register(Registries.ENTITY_TYPE, new Identifier("minecraft", "herobrine_boss"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, HerobrineBoss::new)
                    .dimensions(EntityDimensions.fixed(0.75f * HerobrineBoss.scale, 2f * HerobrineBoss.scale))
                    .fireImmune()
                    .build()
    );

    public static final EntityType<DecoyHerobrineEntity>
            DECOY_HEROBRINE_ENTITY = Registry.register(Registries.ENTITY_TYPE, new Identifier("minecraft", "herobrine_decoy"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, DecoyHerobrineEntity::new)
                    .dimensions(EntityDimensions.fixed(0.75f, 2f))
                    .fireImmune()
                    .build()
    );



    //public static final EntityType<HerobrineShrine>
    //        HEROBRINE_SHRINE = Registry.register(Registries.ENTITY_TYPE, new Identifier("minecraft", "herobrine_shrine"),
    //        FabricEntityTypeBuilder.create(SpawnGroup.MISC, HerobrineShrine::new)
    //                .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
    //                .fireImmune()
    //                .build()
    //);


    /*public static final EntityType<ScaryHerobrineEntity>
            SCARY_HEROBRINE = Registry.register(Registries.ENTITY_TYPE, new Identifier("minecraft", "scary_herobrine"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ScaryHerobrineEntity::new)
                    .dimensions(EntityDimensions.fixed(0.75f, 2f))
                    .fireImmune()
                    .build()
    );*/


    public static void init() {
        FabricDefaultAttributeRegistry.register(STANDARD_HEROBRINE_ENTITY, BaseHerobrineEntity.createHerobrineAttributes());
        FabricDefaultAttributeRegistry.register(HEROBRINE_BOSS_ENTITY_TYPE, HerobrineBoss.createHerobrineAttributes());
        FabricDefaultAttributeRegistry.register(DECOY_HEROBRINE_ENTITY, DecoyHerobrineEntity.createHerobrineAttributes());
        //FabricDefaultAttributeRegistry.register(SCARY_HEROBRINE, ScaryHerobrineEntity.createHerobrineAttributes());
    }
}
