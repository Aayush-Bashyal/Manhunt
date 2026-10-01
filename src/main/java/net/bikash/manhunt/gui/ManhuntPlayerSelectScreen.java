package net.bikash.manhunt.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ManhuntPlayerSelectScreen extends Screen {
    public ManhuntPlayerSelectScreen (){
        super(Component.literal("Select Players"));
    }

    @Override
    protected void init(){
        int buttonwidth = 220;
        int buttonheight = 20;

        int x = (this.width-buttonwidth)/2;

        // done button

        this.addRenderableWidget(
                Button.builder(
                        Component.literal("DONE"),
                        button -> {
                            this.onClose();
                        }
                ).bounds(
                        x,
                        this.height /2 +60,
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
        //making the bg dark

        graphics.fill(
                0,0,this.width,this.height,  0x66000000
        );

//panel
        int panelwidth = 400;
        int panelheight = 300;

        int left = (this.width - panelwidth)/2;
        int top = (this.height - panelheight)/2;
        int right = left + panelwidth;
        int bottom = top+panelheight;

        graphics.fill(
                left,
                top,
                right,
                bottom,
                0xEE151515
        );

        // top of border
        graphics.fill(
                left,
                top,
                right,
                top + 2,
                0xFF55FF55
        );
        graphics.text(
                this.font,
                "SELECT PLAYERS",
                this.width/2-this.font.width("SELECT PLAYERS")/2,
                this.height/2-50,
                0xFFFFFFFF,
                true
        );

        graphics.text(
                this.font,
                "RUNNER",
                left + 40,
                top + 70,
                0xFF55FF55,
                true
        );

        graphics.text(
                this.font,
                "HUNTERS",
                left + 40,
                top + 120,
                0xFFFF5555,
                true
        );
        super.extractRenderState(
                graphics,
                mouseX,
                mouseY,
                partialTick
        );
    }

    }
