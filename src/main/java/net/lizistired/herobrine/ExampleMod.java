package net.lizistired.herobrine;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.lizistired.herobrine.entities.Herobrine;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("herobrine");

	public static final EntityType<Herobrine> HEROBRINE = Registry.register(Registries.ENTITY_TYPE, new Identifier("minecraft", "herobrine"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, Herobrine::new).dimensions(EntityDimensions.fixed(0.75f, 2f)).fireImmune().build()
	);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		FabricDefaultAttributeRegistry.register(HEROBRINE, Herobrine.createHerobrineAttributes());
		LOGGER.info("Hello Fabric world!");
	}
}
