package net.bikash.manhunt.hud;

import net.bikash.manhunt.Manhunt;
import net.bikash.manhunt.game.ManhuntGame;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class ManhuntHUD {
    private static boolean running = false;
    private static long elaspedTime = 0;
    private static String runnerName="";
    private static String hunterNames="";

    public static void register (){


        HudElementRegistry.addLast(
                Identifier.parse("manhunt:hud"),
                ManhuntHUD::render
        );
    }

    public static void update(
            boolean gameRunning,
            long time,
            String runner,
            String hunters
    ){
        running = gameRunning;
        elaspedTime=time;
        runnerName=runner;
        hunterNames=hunters;
    }
    private static void render(
            net.minecraft.client.gui.GuiGraphicsExtractor graphics,
            net.minecraft.client.DeltaTracker deltaTracker
    ){
    Minecraft minecraft = Minecraft.getInstance();

    if(minecraft.player ==null){
        return;
        }
    if(!running){
        return;
    }
    //manhunt
    graphics.text(
            minecraft.font,
            "MANHUNT",
            10,
            10,
            0xFFFFFFFF,
            true
    );

    //time
long totalSeconds = elaspedTime/1000;

long hours = totalSeconds/3600;
long minutes = (totalSeconds % 3600)/60;
long seconds = totalSeconds % 60;

String time =  String.format(
        "TIME : %02d:%02d:%02d",
        hours,
        minutes,
        seconds
);
graphics.text(
        minecraft.font,
        time,
        10,
        25,
        0xFFFFFFFF,
        true
);
//runner
graphics.text(
        minecraft.font,
        "RUNNERS: "+runnerName,

        10,
        55,
        0xFFFFFFFF,
        true
);
//hunters
graphics.text(
        minecraft.font,
        "HUNTERS:",
        10,55,0xFFFFFFFF,
        true

);
String[] hunters = hunterNames.split(",");
int y = 70;
for(String hunter : hunters){
    continue;
}
graphics.text(
        minecraft.font,
        hunterNames.trim(),
        20,
        y,
        0xFFFFFFFF,
        true
);
y+=15;

}
    }


