package net.bikash.manhunt.command;
import com.mojang.brigadier.CommandDispatcher;
import net.bikash.manhunt.Manhunt;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
public class ManhuntCommands {
    public static void register() {


        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
        {

            registerCommands(dispatcher);
        });
    }
    private static  void registerCommands(CommandDispatcher <CommandSourceStack> dispatcher)
    {


        dispatcher.register(Commands.literal("manhunt")
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
                .then(Commands.literal("status").executes(context -> {
                            if (Manhunt.Game.isRunning()) {
                                context.getSource().sendSuccess(
                                        () -> Component.literal("Manhunt is Currently running!"),
                                        false
                                );

                            } else {

                                context.getSource().sendSuccess(
                                        () -> Component.literal("Manhunt is not Running!"),
                                        false
                                );
                            }
                            return 1;
                        }
                )));
    }
    }

