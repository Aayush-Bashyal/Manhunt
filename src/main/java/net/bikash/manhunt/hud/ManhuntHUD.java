package net.bikash.manhunt.hud;

import net.bikash.manhunt.Manhunt;
import net.bikash.manhunt.game.ManhuntGame;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class ManhuntHUD {
    public static void register (){


        HudElementRegistry.addLast(
                Identifier.parse("manhunt:hud"),
                ManhuntHUD::render
        );
    }
    private static void render(
            net.minecraft.client.gui.GuiGraphicsExtractor graphics,
            net.minecraft.client.DeltaTracker deltaTracker
    ){

        Minecraft minecraft = Minecraft.getInstance();

        if(minecraft.getSingleplayerServer() == null){
            return;
        }
        ManhuntGame game = Manhunt.getGame(minecraft.getSingleplayerServer());

        graphics.text(
                minecraft.font,
                "MANHUNT",
                10,
                10,
                0XFFFFFFFF,
                true
        );

        //adding timer of the manhunt which tracks the total time taken in minecraft on the top left corner
        if(game.isRunning()){


            long elasped = game.getElapsedTime();

            long totalseconds = elasped/1000;
            long hours = totalseconds / 3600;
            long minutes = (totalseconds%3600)/60;
            long seconds = totalseconds%60;

            String time = String.format(
                    "TIME: %02d:%02d:%02d",
                    hours,
                    minutes,
                    seconds);
            graphics.text(
                    minecraft.font,
                    time,
                    10,
                    25,
                    0xFFFFFFFF,
                    true

            );
        }
        if(game.isRunning() ){


        if(game.getRunner()!=null){
            ServerPlayer runner = minecraft.getSingleplayerServer().getPlayerList().getPlayer(game.getRunner());

            if(runner!=null) {
                graphics.text(
                        minecraft.font,
                        "RUNNER: " + runner.getName().getString(),
                        10,
                        40,
                        0xFFFFFFFF,
                        true

                );
            }
            }
        }
if (game.isRunning()){
    graphics.text(
            minecraft.font,
            "HUNTERS: ",
            10,
            55,
            0xFFFFFFFF,
            true
    );

        int y=70;
        for(UUID hunterUUID : game.getHunters()){
            ServerPlayer hunter = minecraft.getSingleplayerServer().getPlayerList().getPlayer(hunterUUID);

            if(hunter!=null){
                graphics.text(
                        minecraft.font,
                        hunter.getName().getString(),
                        20,
                        y,
                        0xFFFFFFFF,
                        true
                );
                y+=15;
            }

        }





        }
}
    }


