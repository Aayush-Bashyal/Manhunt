package net.bikash.manhunt;

import net.bikash.manhunt.client.ManhuntClientNetwork;
import net.bikash.manhunt.client.ManhuntCompassRenderer;
import net.bikash.manhunt.gui.ManhuntStartScreen;
import net.bikash.manhunt.hud.ManhuntHUD;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

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
    }

}
