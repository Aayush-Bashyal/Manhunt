package net.bikash.manhunt.command;
import com.mojang.brigadier.CommandDispatcher;
import net.bikash.manhunt.Manhunt;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.bikash.manhunt.item.ModItems;
import net.bikash.manhunt.game.ManhuntGame;
import java.util.UUID;

public class ManhuntCommands {
    public static void register() {


        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
        {

            registerCommands(dispatcher);
        });
    }
    private static  void registerCommands(CommandDispatcher <CommandSourceStack> dispatcher)
    {

//like adding or registering the commands in minecraft when we type /manhunt ... the function executes as it is written in the code
        dispatcher.register(Commands.literal("manhunt")

                // /manhunt start
                .then(Commands.literal("start").executes(context -> {

                    ManhuntGame game = Manhunt.getGame(
                            context.getSource().getServer()
                    );

                    if (game.isRunning()) {
                        context.getSource().sendSuccess(
                                () -> Component.literal("Manhunt is Currently running"),
                                false
                        );
                        return 0;
                    }
                   game.start();
                    context.getSource().sendSuccess(
                            () -> Component.literal("Manhunt Started!"),
                            false
                    );
                    for(UUID hunterUUID: game.getHunters()){
                        ServerPlayer hunter = context.getSource().
                                getServer().getPlayerList().getPlayer(hunterUUID);
                        if(hunter!=null){
                            hunter.getInventory().add(ModItems.MANHUNT_COMPASS.getDefaultInstance());
                        }
                    }
                    return 1;
                }))



                // /manhunt stop
                .then(Commands.literal("stop").executes(context -> {
                    ManhuntGame game = Manhunt.getGame(
                            context.getSource().getServer()
                    );
                            if (!game.isRunning()) {
                                context.getSource().sendSuccess(
                                        () -> Component.literal("Manhunt is not running!"),
                                        false
                                );
                                return 0;
                            }
                            game.stop();
                            context.getSource().sendSuccess(
                                    () -> Component.literal("Manhunt Stopped!"),
                                    false
                            );
                            return 1;
                        }
                ))
                // /manhunt status
                .then(Commands.literal("status").executes(context -> {
                    ManhuntGame game = Manhunt.getGame(
                            context.getSource().getServer()
                    );
                            if (game.isRunning()) {


                                context.getSource().sendSuccess(
                                        () -> Component.literal("Manhunt is Currently running! "),
                                        false
                                );

                            } else {

                                context.getSource().sendSuccess(
                                        () -> Component.literal("Manhunt is not Running!"),
                                        false
                                );
                            }
                            return 1;
                        }))

                                // /manhunt runner <player>
                                .then(Commands.literal("runner")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(context -> {
                                                    ManhuntGame game = Manhunt.getGame(
                                                            context.getSource().getServer()
                                                    );
                                                            ServerPlayer player = EntityArgument.getPlayer(
                                                                    context, "player"
                                                            );

                                                            game.setRunner(player.getUUID());

                                                            int spawnX = player.level().getRespawnData().pos().getX();
                                                            int spawnZ = player.level().getRespawnData().pos().getZ();

                                                            game.setSpawnChunk(
                                                                    spawnX >> 4,
                                                                    spawnZ >> 4
                                                            );



                                                            context.getSource().sendSuccess(
                                                                    () -> Component.literal(player.getName().getString() + "is the Speedrunner!"),
                                                                    false
                                                            );
                                                            return 1;

                                                        }
                                                )))

                                // /manhunt hunter <player>
                                .then(Commands.literal("hunter")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(context -> {

                                                    ManhuntGame game = Manhunt.getGame(
                                                            context.getSource().getServer()
                                                    );

                                                            ServerPlayer player = EntityArgument.getPlayer(
                                                                    context, "player"
                                                            );
                                                            game.addHunter(player.getUUID());
                                                            context.getSource().sendSuccess(
                                                                    () -> Component.literal(player.getName().getString() + "is a Hunter now!"),
                                                                    false
                                                            );
                                                            return 1;

                                                        }
                                                ) ))

                // removing hunter command
                        .then(Commands.literal("removehunter")
                                .then (Commands.argument("player",EntityArgument.player())
                                        .executes(context -> {


                                            ManhuntGame game = Manhunt.getGame(
                                                    context.getSource().getServer()
                                            );

                                            ServerPlayer player = EntityArgument.getPlayer(context,"player");
                                         boolean removed = game.removeHunter(player.getUUID());
                                         if(removed) {
                                             context.getSource().sendSuccess(
                                                     () -> Component.literal(
                                                             player.getName().getString() + "IS REMOVED AS HUNTER!"
                                                     ),
                                                     false
                                             );
                                         }
                                          else {
                                              context.getSource().sendSuccess(
                                                      ()->Component.literal(
                                                              player.getName().getString()+"IS NOT A HUNTER !"
                                                      ),
                                                      false
                                              );
                                         }
                                          return 1;

                                        })))

                //removing runner command
                .then(Commands.literal("removerunner")
                        .then (Commands.argument("player",EntityArgument.player())
                                .executes(context -> {

                                    ManhuntGame game = Manhunt.getGame(
                                            context.getSource().getServer()
                                    );

                                    ServerPlayer player = EntityArgument.getPlayer(context,"player");
                                   if (game.getRunner() !=null && game.getRunner().equals(player.getUUID())){

                                       game.removeRunner();



                                       context.getSource().sendSuccess(
                                               () -> Component.literal(
                                                       player.getName().getString() + "IS REMOVED AS SPEEDRUNNER!"
                                               ),
                                               false
                                       );

                                   }

                                    else {
                                        context.getSource().sendSuccess(
                                                ()->Component.literal(
                                                        player.getName().getString()+"IS NOT A SPEEDRUNNER !"
                                                ),
                                                false
                                        );
                                    }
                                    return 1;

                                })))


        );


    }
    }

