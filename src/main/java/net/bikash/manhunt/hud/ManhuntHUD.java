package net.bikash.manhunt.hud;

import net.bikash.manhunt.Manhunt;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

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
                    "Time: %02d:%02d:%02d",
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
    }
}
