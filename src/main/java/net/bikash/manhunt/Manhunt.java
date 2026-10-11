package net.bikash.manhunt;

import net.bikash.manhunt.command.ManhuntCommands;
import net.bikash.manhunt.game.ManhuntGame;
import net.bikash.manhunt.game.ManhuntTracker;
import net.bikash.manhunt.item.ModItems;
import net.bikash.manhunt.network.*;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;

import net.minecraft.server.level.ServerPlayer;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
		PayloadTypeRegistry.clientboundPlay().register(
				ManhuntHUDNetwork.TYPE,
				ManhuntHUDNetwork.CODEC
		);
		PayloadTypeRegistry.serverboundPlay().register(
				ManhuntRoleSelectionPayload.TYPE,
				ManhuntRoleSelectionPayload.CODEC
		);
		PayloadTypeRegistry.clientboundPlay().register(
				ManhuntAdminNetwork.TYPE,
				ManhuntAdminNetwork.CODEC
		);

		PayloadTypeRegistry.clientboundPlay().register(
				ManhuntSpectatorTargetsPayload.TYPE,
				ManhuntSpectatorTargetsPayload.CODEC
		);


		ManhuntTracker.register();
		ManhuntCommands.register();
		ModItems.initialize();

		ServerPlayConnectionEvents.JOIN.register(
				(handler,sender,server)->{
					var game = Manhunt.getGame(server);
					var player = handler.getPlayer();

					if(game.getAdmins().isEmpty()){
						game.addAdmin(player.getUUID());

						System.out.println("MANHUNT ADMIN: "+player.getName().getString());
					}
					boolean isAdmin = game.isAdmin(player.getUUID());

					ServerPlayNetworking.send(player,new ManhuntAdminNetwork(isAdmin));

					
				}
		);

		ServerPlayNetworking.registerGlobalReceiver(
				ManhuntRoleSelectionPayload.TYPE,
				(payload,context)->{
					var server = context.server();
					var game = Manhunt.getGame(server);
					var player = context.player();

					if(!game.isAdmin(player.getUUID())){
						return;
					}



					game.setRunner(payload.runner());
					game.clearHunters();

					for (var hunter : payload.hunters()){
						game.addHunter(hunter);

						System.out.println("ROLES SAVED!");
					}

					game.clearSpectators();
					for (var spectator: payload.spectators()){
						game.addSpectator(spectator);
					}
					System.out.println("RUNNER: " +payload.runner());

					System.out.println("HUNTERS: "+payload.hunters());

					List<UUID> targets = new ArrayList<>();

					if (payload.runner() != null) {
						targets.add(payload.runner());
					}

					targets.addAll(payload.hunters());

					ManhuntSpectatorTargetsPayload targetsPayload = new ManhuntSpectatorTargetsPayload(targets);

					for(UUID spectatorUUID : game.getSpectators()){
						ServerPlayer spectator = server.getPlayerList().getPlayer(spectatorUUID);

						if(spectator != null){
							ServerPlayNetworking.send(spectator,targetsPayload);
						}
					}
				}
		);
	}

}
