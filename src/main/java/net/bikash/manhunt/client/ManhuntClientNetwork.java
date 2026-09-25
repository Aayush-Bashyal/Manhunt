package net.bikash.manhunt.client;

import net.bikash.manhunt.network.ManhuntNetwork;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ManhuntClientNetwork {
    public static double runnerAngle=0;
    private static boolean tracking = false;
    public static void register(){
        ClientPlayNetworking.registerGlobalReceiver(
                ManhuntNetwork.TYPE,
                (payLoad,context)->{

                    runnerAngle= payLoad.angle();
                    tracking = payLoad.tracking();
                    System.out.println(
                            "Client received angle: "+runnerAngle
                    );
                }
        );
    }

    public static double getRunnerAngle(){
        return runnerAngle;
    }
    public static boolean isTracking(){
        return tracking;
    }
}
