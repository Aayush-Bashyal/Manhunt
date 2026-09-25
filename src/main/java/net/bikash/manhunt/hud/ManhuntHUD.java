package net.bikash.manhunt.hud;

import net.bikash.manhunt.Manhunt;
import net.bikash.manhunt.client.ManhuntClientNetwork;
import net.bikash.manhunt.game.ManhuntTracker;
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
        graphics.text(
                minecraft.font,
                "MANHUNT",
                10,
                10,
                0XFFFFFFFF,
                true
        );

        //adding timer of the manhunt which tracks the total time taken in minecraft on the top left corner
        if(Manhunt.Game.isRunning()){
            long elasped = Manhunt.Game.getElapsedTime();

            long totalseconds = elasped/1000;
            long hours = totalseconds / 3600;
            long minutes = (totalseconds%3600)/60;
            long seconds = totalseconds%60;

            String time = String.format(
                    "⏲\uFE0F: %02d:%02d:%02d",
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
        if(Manhunt.Game.isRunning() ){


        if(Manhunt.Game.getRunner()!=null){
            ServerPlayer runner = minecraft.getSingleplayerServer().getPlayerList().getPlayer(Manhunt.Game.getRunner());

            if(runner!=null) {
                graphics.text(
                        minecraft.font,
                        "\uD83C\uDFC3: " + runner.getName().getString(),
                        10,
                        40,
                        0xFFFFFFFF,
                        true

                );
            }
            }
        }
if (Manhunt.Game.isRunning()){
    graphics.text(
            minecraft.font,
            "\uD83C\uDFF9:",
            10,
            55,
            0xFFFFFFFF,
            true
    );

        int y=70;
        for(UUID hunterUUID : Manhunt.Game.getHunters()){
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




            double angle = ManhuntClientNetwork.getRunnerAngle();

            String direction;

            if (angle >= 337.5 || angle < 22.5) {
                direction = "→";
            } else if (angle < 67.5) {
                direction = "↗";
            } else if (angle < 112.5) {
                direction = "↑";
            } else if (angle < 157.5) {
                direction = "↖";
            } else if (angle < 202.5) {
                direction = "←";
            } else if (angle < 247.5) {
                direction = "↙";
            } else if (angle < 292.5) {
                direction = "↓";
            } else {
                direction = "↘";
            }

            graphics.text(
                    minecraft.font,
                    "🧭 " + direction + " "
                            + String.format("%.0f°", angle),
                    10,
                    y + 20,
                    0xFFFFFFFF,
                    true
            );
        }
}
    }


