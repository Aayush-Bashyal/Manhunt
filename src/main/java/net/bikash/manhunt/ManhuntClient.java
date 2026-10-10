package net.bikash.manhunt;

import net.bikash.manhunt.client.ManhuntClientNetwork;
import net.bikash.manhunt.client.ManhuntCompassRenderer;
import net.bikash.manhunt.gui.ManhuntStartScreen;
import net.bikash.manhunt.hud.ManhuntHUD;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.ShareToLanScreen;
import org.lwjgl.glfw.GLFW;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import com.mojang.blaze3d.platform.InputConstants;

public class ManhuntClient implements ClientModInitializer {
private static boolean opened = false;
    @Override

    public void onInitializeClient(){
        ManhuntHUD.register();
        ManhuntClientNetwork.register();

        ClientTickEvents.END_CLIENT_TICK.register(client-> {
            if(client.player ==null){
                opened = false;
                return;
            }
            if(!opened){
                opened=true;
                client.setScreen(new ManhuntStartScreen());
            }
        });

        KeyMapping.Category category = KeyMapping.Category.register(
                net.minecraft.resources.Identifier.fromNamespaceAndPath("manhunt","spectator")
        );

        KeyMapping nextTargetKey = KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                "key.manhunt.spectator_next",
                GLFW.GLFW_KEY_RIGHT_BRACKET,
                category
                )
        );
        KeyMapping previousTargetKey = KeyMappingHelper.registerKeyMapping(


                new KeyMapping(
                "key.manhunt.spectator_previous",
                GLFW.GLFW_KEY_LEFT_BRACKET,
                category
                )
                );
    }

}
