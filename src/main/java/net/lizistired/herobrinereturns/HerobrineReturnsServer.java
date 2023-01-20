package net.lizistired.herobrinereturns;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.lizistired.herobrinereturns.utils.registry.RegisterEntities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HerobrineReturnsServer implements DedicatedServerModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("herobrinereturns");

	@Override
	public void onInitializeServer() {

	}
}
