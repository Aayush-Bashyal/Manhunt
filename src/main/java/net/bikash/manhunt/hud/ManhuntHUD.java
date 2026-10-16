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
   if(minecraft.player == null){
       return;
   }
   if(!running){
       return;
   }
   int screenwidth = minecraft.getWindow().getGuiScaledWidth();

   //time

        long totalseconds = elaspedTime/1000;
        long hours = totalseconds/3600;
        long minutes = (totalseconds%3600)/60;
        long seconds = totalseconds%60;

        String time = String.format(
                "%02d:%02d",

                minutes,
                seconds
        );

        //panel

        int panelwidth = 150;
        int panelheight= 44;

        int left = (screenwidth-panelwidth)/2;
        int top = 6;
        int right = left + panelwidth;
        int bottom = top +panelheight;

        //bg

        graphics.fill(
                left,
                top,
                right,
                bottom,
                0xCC101010
        );

        //top border

        graphics.fill(
                left,
                top,
                right,
                top+3,
                0xFFFF3333
        );

        graphics.fill(
                left,
                top,
                left+1,
                bottom,
                0xFF555555
        );

        graphics.fill(
                right-1,
                top,
                right,
                bottom,
                0xFF555555
        );
        graphics.fill(
                left,
                bottom - 1,
                right,
                bottom,
                0xFF555555
        );

        //MANHUNT title

        String title = "MANHUNT";

        graphics.text(
                minecraft.font,
                title,
                screenwidth/2-minecraft.font.width(title)/2,
                top+6,
                0xFFFFFFFF,
                true


        );

        int lineWidth = 50 ;
        int lineLeft = (screenwidth - lineWidth) /2;

        graphics.fill(
                lineLeft,
                top+22,
                lineLeft+lineWidth,
                top+23,
                0xFF555555
        );


        //time

        graphics.text(
                minecraft.font,
                time,
                screenwidth/2-minecraft.font.width(time)/2,
                top+27,
                0xFFFFFFFF,
                true
        );
}
    }


