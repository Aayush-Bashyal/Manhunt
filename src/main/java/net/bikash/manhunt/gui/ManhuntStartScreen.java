package net.bikash.manhunt.gui;

import com.llamalad7.mixinextras.sugar.Share;
import net.bikash.manhunt.network.ManhuntAdminNetwork;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.ShareToLanScreen;
import net.minecraft.network.chat.Component;

public class ManhuntStartScreen extends Screen {
@Override
public boolean shouldCloseOnEsc()
{
    return true;
}

public  ManhuntStartScreen(){

    super(Component.literal("Manhunt"));
}

@Override
    protected void init() {
    int buttonwidth = 220;
    int buttonheight = 20;

    int x = (this.width - buttonwidth) / 2;
    int y = this.height / 2 + 20;


    if (ManhuntAdminNetwork.isAdmin) {
        this.addRenderableWidget(
                Button.builder(
                        Component.literal("Player Selection"),
                        button -> {
                            this.minecraft.setScreen(
                                    new ManhuntPlayerSelectScreen()
                            );
                        }
                ).bounds(
                        x,
                        y + 30,
                        buttonwidth,
                        buttonheight
                ).build()
        );
    }
    this.addRenderableWidget(Button.builder(
            Component.literal("Start Manhunt"),
            button->{

            }
    ).bounds(
            x,
            y+60,
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

        graphics.pose().pushMatrix();
        graphics.pose().scale(1.5f,1.5f);

        graphics.text(
                this.font,
                "LET'S PLAY",
                (int) ((this.width / 2.0f - this.font.width("LET'S PLAY") / 2.0f) / 1.5f),
                (int) ((this.height / 2.0f - 50) / 1.5f),
                0xFFFFFFFF,
                true
        );
        graphics.text(
                this.font,
                "MANHUNT",
                (int) ((this.width / 2.0f - this.font.width("MANHUNT") / 2.0f) / 1.5f),
                (int) ((this.height / 2.0f - 30) / 1.5f),
                0xFFFF5555,
                true
        );
        graphics.pose().popMatrix();

        super.extractRenderState( graphics,

                mouseX,
                mouseY,
                partialTick );
    }


}
