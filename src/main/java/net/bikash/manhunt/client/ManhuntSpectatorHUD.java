package net.bikash.manhunt.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class ManhuntSpectatorHUD {

    private static boolean wasLeftMouseDown = false;

    public static void register() {
        HudElementRegistry.addLast(Identifier.parse("manhunt:spectator_hud"),
                ManhuntSpectatorHUD::render
        );

        ClientTickEvents.END_CLIENT_TICK.register(
                client -> {
                    if(client.player ==null|| client.level==null){
                        wasLeftMouseDown = false;
                        return;
                    }

                    boolean leftMouseDown = GLFW.glfwGetMouseButton(
                            org.lwjgl.glfw.GLFW.glfwGetCurrentContext(),
                            GLFW.GLFW_MOUSE_BUTTON_LEFT
                    )==GLFW.GLFW_PRESS;

if(leftMouseDown&&!wasLeftMouseDown){
    double mouseX = client.mouseHandler.xpos() * client.getWindow().getGuiScaledWidth() / client.getWindow().getWidth();

    double mouseY = client.mouseHandler.ypos() * client.getWindow().getGuiScaledHeight() / client.getWindow().getHeight();

    // ....................

}
wasLeftMouseDown = leftMouseDown;
                }
        );
    }

    private static void render(
            GuiGraphicsExtractor graphics,
            net.minecraft.client.DeltaTracker deltaTracker
    ) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.level == null) {

            return;
        }
        if (!ManhuntSpectatorManager.hasTargets()) {
            return;
        }

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        int buttonWidth = 90;
        int buttonHeight = 22;
        int gap = 6;
        int margin = 10;

        int nextX = width - margin - buttonWidth;
        int previousX = nextX - gap - buttonWidth;
        int y = height - margin - buttonHeight;

        graphics.fill(
                previousX, y,
                previousX + buttonWidth, y + buttonHeight,
                0xCC222222
        );

        graphics.fill(
                nextX,
                y,
                nextX+buttonWidth,
                y+buttonHeight,
                0xCC222222
        );

        graphics.text(
                mc.font,
                "< PREVIOUS",
                previousX + 13,
                y + 7,
                0xFFFFFFFF,
                true
        );

        graphics.text(
                mc.font,
                "NEXT >",
                nextX + 25,
                y + 7,
                0xFFFFFFFF,
                true
        );

    }
}