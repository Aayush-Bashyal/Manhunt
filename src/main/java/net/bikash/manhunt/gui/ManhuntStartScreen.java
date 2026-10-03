package net.bikash.manhunt.gui;

import com.llamalad7.mixinextras.sugar.Share;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.ShareToLanScreen;
import net.minecraft.network.chat.Component;

public class ManhuntStartScreen extends Screen {
    private boolean openinglan = false;
public  ManhuntStartScreen(){

    super(Component.literal("Manhunt"));
}

@Override
    protected void init(){
    int buttonwidth = 220;
    int buttonheight = 20;

    int x = (this.width - buttonwidth)/2;
    int y = this.height / 2 +20;

    this.addRenderableWidget(
            Button.builder(
                    Component.literal("OPEN LAN WORLD"),
                            button -> {
                        openinglan  = true;
                        this.minecraft.setScreen(
                                new ShareToLanScreen(this)
                        );
                            }
                            ).bounds(
                                    x,
                            y,
                            buttonwidth,
                            buttonheight

                    ).build()

    );

    this.addRenderableWidget(
            Button.builder(
                    Component.literal("START MANHUNT"),
                    button -> {
                        this.minecraft.setScreen(
                                new ManhuntPlayerSelectScreen()
                        );
                    }
            ).bounds(
                    x,
                    y+30,
                    buttonwidth,
                    buttonheight
            ).build()
    );
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
                0xFF55AA55
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

        super.extractRenderState( graphics,

                mouseX,
                mouseY,
                partialTick );
    }

    @Override
    public void onClose(){
    if(openinglan){
        openinglan = false;

        if(this.minecraft!=null){
            this.minecraft.setScreen(
                    new ManhuntStartScreen()
            );
        }
        return;
    }
    super.onClose();
    }
}
