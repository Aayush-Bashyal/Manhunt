package net.bikash.manhunt.command;
import com.mojang.brigadier.CommandDispatcher;
import net.bikash.manhunt.Manhunt;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

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
                    if (Manhunt.Game.isRunning()) {
                        context.getSource().sendSuccess(
                                () -> Component.literal("Manhunt is Currently running"),
                                false
                        );
                        return 0;
                    }
                    Manhunt.Game.start();
                    context.getSource().sendSuccess(
                            () -> Component.literal("Manhunt Started!"),
                            false
                    );

                    return 1;
                }))

                // /manhunt stop
                .then(Commands.literal("stop").executes(context -> {
                            if (!Manhunt.Game.isRunning()) {
                                context.getSource().sendSuccess(
                                        () -> Component.literal("Manhunt is not running!"),
                                        false
                                );
                                return 0;
                            }
                            Manhunt.Game.stop();
                            context.getSource().sendSuccess(
                                    () -> Component.literal("Manhunt Stopped!"),
                                    false
                            );
                            return 1;
                        }
                ))
                // /manhunt status
                .then(Commands.literal("status").executes(context -> {
                            if (Manhunt.Game.isRunning()) {
                                long elapsed = Manhunt.Game.getElapsedTime();
                                long totalseconds = elapsed/1000;
                                long minutes = totalseconds/60;
                                long  seconds = totalseconds%60;

                                context.getSource().sendSuccess(
                                        () -> Component.literal("Manhunt is Currently running!  Time: "+minutes+ "min " + seconds + "sec"),
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
                                                            ServerPlayer player = EntityArgument.getPlayer(
                                                                    context, "player"
                                                            );
                                                            Manhunt.Game.setRunner(player.getUUID());
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
                                                            ServerPlayer player = EntityArgument.getPlayer(
                                                                    context, "player"
                                                            );
                                                            Manhunt.Game.addHunter(player.getUUID());
                                                            context.getSource().sendSuccess(
                                                                    () -> Component.literal(player.getName().getString() + "is a Hunter now!"),
                                                                    false
                                                            );
                                                            return 1;

                                                        }
                                                ) ))
                );


    }
    }

