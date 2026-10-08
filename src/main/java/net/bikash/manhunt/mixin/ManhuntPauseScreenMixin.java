package net.bikash.manhunt.mixin;

import net.bikash.manhunt.gui.ManhuntPlayerSelectScreen;
import net.bikash.manhunt.network.ManhuntAdminNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.Renderable;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PauseScreen.class)
public abstract class ManhuntPauseScreenMixin {



    @Inject (method="init", at= @At("TAIL"))
   private void addManhuntButton(CallbackInfo ci) {
        if (!ManhuntAdminNetwork.isAdmin) {
                        return ;
        }

        ScreenAccessor screen = (ScreenAccessor) (Object) this;

        screen.manhunt$addRenderableWidget(
                Button.builder(
                        net.minecraft.network.chat.Component.literal("Player Selection"),
                        button -> Minecraft.getInstance().setScreen(
                                new ManhuntPlayerSelectScreen()

                        )
                ).bounds(
                        10,
                        10,
                        120,
                        20
                ).build()
        );


    }

}
