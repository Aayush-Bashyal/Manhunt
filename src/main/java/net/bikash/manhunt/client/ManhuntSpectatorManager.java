package net.bikash.manhunt.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ManhuntSpectatorManager {
    private static boolean spectatorMode = false;
    private static final List<UUID> targets = new ArrayList<>();
    private static int currentTargetIndex = 0;

    public static void setTargets(List<UUID> playerUUIDs){
        targets.clear();
        if(playerUUIDs != null){
            targets.addAll(playerUUIDs);
        }
        currentTargetIndex = 0;

//do not enable spectator mode automatically
        if(!spectatorMode||targets.isEmpty()){

            returnToOwnPOV();
            return;
        }

        updateCamera();
    }

    public static void nextTarget(){
        if( !spectatorMode ||  targets.isEmpty()){
            return;
        }
currentTargetIndex = (currentTargetIndex + 1)% targets.size();
        updateCamera();

    }

    public static void previousTarget(){
        if( !spectatorMode ||targets.isEmpty()){
            return;

        }

        currentTargetIndex = (currentTargetIndex-1 + targets.size()) % targets.size();
        updateCamera();
    }

    public static void updateCamera(){
        Minecraft minecraft = Minecraft.getInstance();

        if(minecraft.player == null || minecraft.level == null){
            return;
        }
        if (!spectatorMode || targets.isEmpty()) {
            returnToOwnPOV();
            return;
        }

        if (currentTargetIndex < 0 || currentTargetIndex >= targets.size()) {
            currentTargetIndex = 0;
        }


        UUID targetUUID = targets.get(currentTargetIndex);
        Player target = minecraft.level.getPlayerByUUID(targetUUID);



        if(target != null){
            minecraft.setCameraEntity(target);
        }else{
            returnToOwnPOV();
        }
    }

    public static String getCurrentTargetName(){
        Minecraft minecraft = Minecraft.getInstance();

        if(minecraft.player == null){
            return "";
        }



        if(!spectatorMode || targets.isEmpty()||
        currentTargetIndex <0 || currentTargetIndex >= targets.size()){
            return minecraft.player.getName().getString();
        }

        if(minecraft.level == null){
            return "Loading player...";
        }


        Player target = minecraft.level.getPlayerByUUID(
                targets.get(currentTargetIndex)
        );

        return target != null ? target.getName().getString() : "Loading player...";
    }

    public static boolean hasTargets(){

        return spectatorMode &&  !targets.isEmpty();
    }
    public static void setSpectatorMode(boolean value){
        spectatorMode=value;

       if(!value){
           currentTargetIndex = 0;
           returnToOwnPOV();
           return;
       }
       if(targets.isEmpty()){
           returnToOwnPOV();
           return;
       }
       updateCamera();

    }
    public static boolean isSpectatorMode() {

        return spectatorMode;
    }

public static void returnToOwnPOV(){
        Minecraft minecraft = Minecraft.getInstance();

        if(minecraft.player!= null){
            minecraft.setCameraEntity(minecraft.player);
        }
}

}
