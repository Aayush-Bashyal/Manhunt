package net.bikash.manhunt;

import net.bikash.manhunt.hud.ManhuntHUD;
import net.fabricmc.api.ClientModInitializer;

public class ManhuntClient implements ClientModInitializer {

    @Override

    public void onInitializeClient(){
        ManhuntHUD.register();
    }

}
