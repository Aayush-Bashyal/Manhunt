package net.bikash.manhunt;

import net.bikash.manhunt.command.ManhuntCommands;
import net.bikash.manhunt.game.ManhuntGame;
import net.bikash.manhunt.game.ManhuntTracker;
import net.bikash.manhunt.item.ModItems;
import net.bikash.manhunt.network.*;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.message.v1.ServerMessageDecoratorEvent;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
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
		PayloadTypeRegistry.serverboundPlay().register(
				ManhuntRequestRolesPayload.TYPE,
				ManhuntRequestRolesPayload.CODEC
		);
		PayloadTypeRegistry.clientboundPlay().register(
				ManhuntCurrentRolesPayload.TYPE,
				ManhuntCurrentRolesPayload.CODEC
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

					if(game.isRunning()){
						//runner cannot be changed when the manhunt starts

						if(!java.util.Objects.equals(payload.runner(),game.getRunner())){
							return;
						}

						// hunters
						if(!new java.util.HashSet<>(payload.hunters()).equals(new java.util.HashSet<>(game.getHunters()))){
							return;
						}

						//removed spectatators are no longer selected as spectators

						if(!payload.spectators().containsAll(game.getSpectators())){
							return;
						}

						//only players with no role cn be spectators after the manhunt starts
						for(UUID spectatorsID:payload.spectators()){
							boolean alreadyHasRole= spectatorsID.equals(game.getRunner())||game.getHunters().contains(spectatorsID)|| game.getSpectators().contains(spectatorsID);

							if(!alreadyHasRole){
								game.addSpectator(spectatorsID);
							}
						}
						List<UUID> targets = new ArrayList<>();

								if (game.getRunner()!=null){
							targets.add(game.getRunner());
						}

								targets.addAll(game.getHunters());

								ManhuntSpectatorTargetsPayload targetsPayload = new ManhuntSpectatorTargetsPayload(targets);

								for(UUID spectatorsUUID : game.getSpectators()){
									ServerPlayer spectator = server.getPlayerList().getPlayer(spectatorsUUID);

									System.out.println("DEBUG: Checking spectator: " + spectatorsUUID);

									System.out.println("DEBUG: Player found: " + (spectator != null));

									if(spectator != null){
										spectator.setGameMode(
												net.minecraft.world.level.GameType.SPECTATOR
										);

										System.out.println("DEBUG: Switched " + spectator.getName().getString() + " to Spectator Mode");

										ServerPlayNetworking.send(spectator,targetsPayload)
										;
									}
								}
								return;



					}



					game.setRunner(payload.runner());
					game.clearHunters();

					for (UUID hunter : new java.util.LinkedHashSet<>(payload.hunters())){
						game.addHunter(hunter);
					}

					List<UUID> previousSpectators = new ArrayList<>(game.getSpectators());

					game.clearSpectators();
					for(UUID spectatorID : new java.util.LinkedHashSet<>(payload.spectators())) {
						game.addSpectator(spectatorID);

					}

					for(UUID oldSpectator : previousSpectators){
						if(!game.getSpectators().contains(oldSpectator)){
							ServerPlayer oldPlayer = server.getPlayerList().getPlayer(oldSpectator);

							if(oldPlayer != null){
								ServerPlayNetworking.send(
										oldPlayer,
										new ManhuntSpectatorTargetsPayload(new ArrayList<>())
								);
							}
						}
					}

						for(ServerPlayer onlinePlayer : server.getPlayerList().getPlayers()){
							UUID id = onlinePlayer.getUUID();

							if(game.getSpectators().contains(id)){
								onlinePlayer.setGameMode(GameType.SPECTATOR);
							}
							else{
								onlinePlayer.setGameMode(GameType.SURVIVAL);
							}
						}






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


		ServerPlayNetworking.registerGlobalReceiver(
				ManhuntRequestRolesPayload.TYPE,
				(payload, context) -> {
					var server = context.server();
					var player = context.player();
					var game = Manhunt.getGame(server);

					// onlyyy the admin can requet saved roless
					if (!game.isAdmin(player.getUUID())) {
						return;
					}

					ManhuntCurrentRolesPayload savedRoles =
							new ManhuntCurrentRolesPayload(
									game.getRunner(),
									new ArrayList<>(game.getHunters()),
									new ArrayList<>(game.getSpectators())
							);

					ServerPlayNetworking.send(player, savedRoles);
				}
		);
		ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(
				(message , player, params)->{
					ManhuntGame game = Manhunt.getGame(player.level().getServer());

					return !game.getSpectators().contains(player.getUUID());
				}
		);

	}

}
