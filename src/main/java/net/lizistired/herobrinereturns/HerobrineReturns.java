package net.lizistired.herobrinereturns;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.*;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.lizistired.herobrinereturns.utils.registry.RegisterEntities;
import net.lizistired.herobrinereturns.utils.registry.RegisterItems;
import net.lizistired.herobrinereturns.utils.registry.RegisterParticles;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HerobrineReturns implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("herobrinereturns");



	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		//ServerPlayerEvents
		RegisterEntities.init();
		RegisterItems.init();
		RegisterParticles.init();
		LOGGER.info("Hello Fabric world!");

	}

	void testingFunction(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
	}
}
