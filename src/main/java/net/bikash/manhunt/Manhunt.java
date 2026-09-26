package net.bikash.manhunt;

import net.bikash.manhunt.command.ManhuntCommands;
import net.bikash.manhunt.game.ManhuntGame;
import net.bikash.manhunt.game.ManhuntTracker;
import net.bikash.manhunt.item.ModItems;
import net.bikash.manhunt.network.ManhuntNetwork;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.resources.Identifier;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Manhunt implements ModInitializer {



	private static final java.util.Map<net.minecraft.server.MinecraftServer,ManhuntGame> GAMES = new java.util.WeakHashMap<>();
	public static ManhuntGame getGame(net.minecraft.server.MinecraftServer server){
		return GAMES.computeIfAbsent(server, s -> new ManhuntGame());
	}
	public static final String MOD_ID = "manhunt";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		PayloadTypeRegistry.clientboundPlay().register(
				ManhuntNetwork.TYPE,
				ManhuntNetwork.CODEC
		);

		ManhuntTracker.register();
		ManhuntCommands.register();
		ModItems.initialize();
	}

}
