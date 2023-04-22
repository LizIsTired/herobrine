package net.lizistired.herobrinereturns;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.lizistired.herobrinereturns.utils.registry.RegisterEntities;
import net.lizistired.herobrinereturns.utils.registry.RegisterItems;
import net.lizistired.herobrinereturns.utils.registry.RegisterParticles;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
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

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (source.isBuiltin() && DESERT_PYRAMID_CHEST_ID.equals(id) || source.isBuiltin() && SIMPLE_DUNGEON_CHEST.equals(id)) {
				LootPool.Builder poolBuilder = LootPool.builder()
						.with(ItemEntry.builder(RegisterItems.CURSED_BOOK));

				tableBuilder.pool(poolBuilder);
			}

			});
		}
}
