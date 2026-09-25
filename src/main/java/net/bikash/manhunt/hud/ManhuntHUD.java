package net.bikash.manhunt.hud;

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
    }
}
