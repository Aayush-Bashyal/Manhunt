package net.bikash.manhunt;

import net.bikash.manhunt.command.ManhuntCommands;
import net.bikash.manhunt.game.ManhuntGame;
import net.bikash.manhunt.game.ManhuntTracker;
import net.bikash.manhunt.item.ModItems;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Manhunt implements ModInitializer {
	public static final ManhuntGame  Game = new ManhuntGame();
	public static final String MOD_ID = "manhunt";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ManhuntTracker.register();
		ManhuntCommands.register();
		ModItems.initialize();
	}

}
