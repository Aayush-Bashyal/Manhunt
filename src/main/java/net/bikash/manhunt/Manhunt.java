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
import org.apache.logging.log4j.core.jmx.Server;
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



						List<UUID> previousSpectators =
								new ArrayList<>(game.getSpectators());

						List<UUID> desiredSpectators = new ArrayList<>();

						for (UUID id : new java.util.LinkedHashSet<>(payload.spectators())) {
							if (!id.equals(game.getRunner())
									&& !game.getHunters().contains(id)) {
								desiredSpectators.add(id);
							}
						}

// Remove players who lost the spectator role
						for (UUID oldId : previousSpectators) {
							if (!desiredSpectators.contains(oldId)) {
								game.removeSpectator(oldId);

								ServerPlayer oldPlayer =
										server.getPlayerList().getPlayer(oldId);

								if (oldPlayer != null) {
                                    // Restore the player's own camera and disable spectator POV

									ServerPlayNetworking.send(
											oldPlayer,
											new ManhuntSpectatorTargetsPayload(new ArrayList<>())
									);
                                    if(!oldId.equals(game.getRunner())&& !game.getHunters().contains(oldId)){
                                        oldPlayer.setGameMode(GameType.SURVIVAL);
                                    }
								}
							}
						}

// Add/update selected spectators
						for (UUID id : desiredSpectators) {
							if (!game.getSpectators().contains(id)) {
								game.addSpectator(id);
							}

							ServerPlayer spectator =
									server.getPlayerList().getPlayer(id);

							if (spectator != null) {
								spectator.setGameMode(GameType.SPECTATOR);
							}
						}


						//build pov targets : runner and hunters

						List<UUID> targets = new ArrayList<>();

						if(game.getRunner()!=null){
							targets.add(game.getRunner());
						}

						targets.addAll(game.getHunters());

						ManhuntSpectatorTargetsPayload targetsPayload = new ManhuntSpectatorTargetsPayload(targets);

						//send the updated targets to current spectators
						for(UUID spectatorUUID : game.getSpectators()){
							ServerPlayer spectator = server.getPlayerList().getPlayer(spectatorUUID	);

							if(spectator!=null){
								ServerPlayNetworking.send(spectator,targetsPayload);
							}
						}

						ServerPlayNetworking.send(
								player,
								new ManhuntCurrentRolesPayload(
										game.getRunner(),
										new ArrayList<>(game.getHunters()),
										new ArrayList<>(game.getSpectators())
								)
						);
						return;
					}


					List<UUID> previousSpectators = new ArrayList<>(game.getSpectators());
					game.setRunner(payload.runner());
					game.clearHunters();

					for (UUID hunter : new java.util.LinkedHashSet<>(payload.hunters())){
						game.addHunter(hunter);
					}



					game.clearSpectators();

					for(UUID id : new java.util.LinkedHashSet<>(payload.spectators())){
						if(!id.equals(game.getRunner()) && !game.getHunters().contains(id)){
							game.addSpectator(id);
						}
					}

					//reset players who lost spectators role

					for(UUID oldId : previousSpectators){
						if(!game.getSpectators().contains(oldId)){
							ServerPlayer oldPlayer = server.getPlayerList().getPlayer(oldId);

							if(oldPlayer !=null){
                                //clear spec pov first

								ServerPlayNetworking.send(
										oldPlayer,
										new ManhuntSpectatorTargetsPayload(new ArrayList<>())
								);
                                // keep the game mode consistent withthe newly assignd role
                                if(!oldId.equals(game.getRunner())&& !game.getHunters().contains(oldId)){
                                    oldPlayer.setGameMode(GameType.SURVIVAL);
                                }
							}
						}
					}

					//apply spectator mode to every selected spectator

					for(UUID id: game.getSpectators() ){
						ServerPlayer spectator = server.getPlayerList().getPlayer(id);

						if(spectator != null){

							spectator.setGameMode(GameType.SPECTATOR);
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
							spectator.setGameMode(GameType.SPECTATOR);

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
