package net.bikash.manhunt.game;

import net.bikash.manhunt.Manhunt;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;

public class ManhuntTracker {

    private static int tickcounter = 0;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {



if(!Manhunt.Game.isRunning()){
    return;
}
 tickcounter++;

if(tickcounter<5){
    return;
}
tickcounter= 0;

if(Manhunt.Game.getRunner()==null){
    return;
}
            ServerPlayer runner = server.getPlayerList().getPlayer(Manhunt.Game.getRunner());

if(runner==null){
    return;
}



//tracking the position of the runner
            double x = runner.getX();
            double y = runner.getY();
            double z = runner.getZ();


        });
    }
}
