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
            0x66000000
    );
    int panewidth = 400;
    int paneheight = 250;

    int left = (this.width - panewidth)/2;
    int top = (this.height-paneheight)/2;
    int right = left + panewidth;
    int bottom= top + paneheight;

    graphics.fill(
            left,
            top,
            right,
            top+2,
            0xFFFF5555
    );

    graphics.fill(
            left,
            bottom-2,
            right,
            bottom,
            0xFFFF5555
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
