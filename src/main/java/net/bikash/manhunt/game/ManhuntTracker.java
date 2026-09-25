package net.bikash.manhunt.game;

import net.bikash.manhunt.Manhunt;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;
import net.bikash.manhunt.network.ManhuntNetwork;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ManhuntTracker {

    private static int tickcounter = 0;
    private static double runnerAngle = 0;

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
            if (!runner.isAlive()) {
                Manhunt.Game.stop();
                server.getPlayerList()
                        .broadcastSystemMessage( net.minecraft.network.chat.Component.literal
                                        ( "HUNTERSSS WINN!! \uD83D\uDE4C " ),
                                false
                        );
                return;
            }

            for (var hunterUUID:Manhunt.Game.getHunters()){
                ServerPlayer hunter = server.getPlayerList().getPlayer(hunterUUID);

                if(hunter==null){
                    continue;
                }
                if(hunter.level().dimension()!=runner.level().dimension()){
                    ServerPlayNetworking.send(
                            hunter,
                            new ManhuntNetwork(false,0)
                    );
                    continue;
                }

                double dx = runner.getX() - hunter.getX();
                double dz = runner.getZ() - hunter.getZ();


                //calculate theee anglee form the hunter to the runner
                double angle = Math.toDegrees(
                        Math.atan2(dz,dx)
                );
                double hunterYaw = hunter.getYRot();
                angle = angle - hunterYaw;

                if(angle<0){
                    angle+=360;

                }
                if(angle>=360){
                    angle -=360;
                }
                runnerAngle = angle;



                    ServerPlayNetworking.send(
                            hunter,
                            new ManhuntNetwork(true, angle)
                    );

                System.out.println(
                        "Hunter: "+hunter.getName().getString()+"| Runner angle:" + angle
                );
            }


        });
    }
    public static double getRunnerAngle(){
        return runnerAngle;
    }
}
