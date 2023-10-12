package net.lizistired.herobrinereturns;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.lizistired.herobrinereturns.utils.registry.networking.NetworkConstants;
import net.lizistired.herobrinereturns.utils.registry.RegisterEntities;
import net.lizistired.herobrinereturns.utils.registry.RegisterItems;
import net.lizistired.herobrinereturns.utils.registry.RegisterParticles;
import net.lizistired.herobrinereturns.utils.registry.networking.handlers.NetworkHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HerobrineReturns implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("herobrinereturns");
	public static final GameStateChangeS2CPacket.Reason HEROBRINE_APPEARANCE_EFFECT = new GameStateChangeS2CPacket.Reason(12);

	private static final Identifier DESERT_PYRAMID_CHEST_ID = LootTables.DESERT_PYRAMID_CHEST;
	private static final Identifier SIMPLE_DUNGEON_CHEST = LootTables.SIMPLE_DUNGEON_CHEST;

	public static MinecraftServer minecraftServer;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		//ServerPlayerEvents
		RegisterEntities.init();
		RegisterItems.init();
		RegisterParticles.init();
		NetworkHandler.init();
		LOGGER.info("Hello Fabric world!");




		ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
			minecraftServer = server;
		});


		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (source.isBuiltin() && DESERT_PYRAMID_CHEST_ID.equals(id) || source.isBuiltin() && SIMPLE_DUNGEON_CHEST.equals(id)) {
				LootPool.Builder poolBuilder = LootPool.builder()
						.with(ItemEntry.builder(RegisterItems.CURSED_BOOK));

				tableBuilder.pool(poolBuilder);
			}

			});
		}
}
