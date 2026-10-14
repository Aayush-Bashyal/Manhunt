package net.bikash.manhunt.client;

import net.minecraft.client.Minecraft;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ManhuntSpectatorManager {

    private static final List<UUID> targets = new ArrayList<>();
    private static int currentTargetIndex = 0;

    public static void setTargets(List<UUID> playerUUIDs){
        targets.clear();
        targets.addAll(playerUUIDs);
        currentTargetIndex = 0;
    }

    public static void nextTarget(){
        if(targets.isEmpty()){
            return;
        }
currentTargetIndex = (currentTargetIndex + 1)% targets.size();

    }

    public static void previousTarget(){
        if(targets.isEmpty()){
            return;

        }

        currentTargetIndex = (currentTargetIndex-1 + targets.size()) % targets.size();
    }

    public static void updateCamera(){
        Minecraft minecraft = Minecraft.getInstance();

        if(minecraft.player == null || minecraft.level == null){
            return;
        }

        if(targets.isEmpty()){
            return;
        }

        UUID targetUUID = targets.get(currentTargetIndex);

        net.minecraft.world.entity.player.Player target = minecraft.level.getPlayerByUUID(targetUUID);

        if(target != null){
            minecraft.setCameraEntity(target);
        }
    }

    public static boolean hasTargets() {
        return !targets.isEmpty();
    }

}
