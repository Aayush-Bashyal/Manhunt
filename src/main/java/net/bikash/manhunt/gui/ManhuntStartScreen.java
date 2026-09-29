package net.bikash.manhunt.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ManhuntStartScreen extends Screen {

public  ManhuntStartScreen(){
    super(Component.literal("Manhunt"));
}
@Override
    public void extractRenderState(
            GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY,
            float partialTick
){
    graphics.fill(
            0,
            0,
            this.width,
            this.height,
            0xCC000000
    );
    graphics.text(
            this.font,
            "LET'S PLAY",
            this.width/2-this.font.width("LET'S PLAY")/2,
            this.height/2-50,
            0xFFFFFFFF,
            true
    );
    graphics.text(
            this.font,
            "MANHUNT",
            this.width/2-this.font.width("MANHUNT")/2,
            this.height/2-30,
            0xFFFF5555,
            true
    );
}
}
