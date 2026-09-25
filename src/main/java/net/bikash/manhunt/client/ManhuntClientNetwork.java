package net.bikash.manhunt.client;
import net.minecraft.core.component.DataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.bikash.manhunt.network.ManhuntNetwork;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import java.util.List;
public class ManhuntClientNetwork {
    private static double runnerAngle=0;
    private static boolean tracking = false;
    public static void register(){
        ClientPlayNetworking.registerGlobalReceiver(
                ManhuntNetwork.TYPE,
                (payLoad,context)->{

                    runnerAngle= payLoad.angle();
                    tracking = payLoad.tracking();

                        updateCompassModel();

                    System.out.println(
                            "Client received angle: "+runnerAngle
                    );
                }
        );
    }
    public static void updateCompassModel() {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        int model = (int) Math.floor(
                runnerAngle / 360.0 * 31.0
        );

        if (model >= 31) {
            model = 30;
        }

        for (int i = 0; i < minecraft.player.getInventory().getContainerSize(); i++) {
            ItemStack stack = minecraft.player.getInventory().getItem(i);

            if (stack.isEmpty()) {
                continue;
            }

            if (stack.is(net.bikash.manhunt.item.ModItems.MANHUNT_COMPASS)) {
                stack.set(
                        DataComponents.CUSTOM_MODEL_DATA,
                        new CustomModelData(
                                List.of((float) model),
                                List.of(),
                                List.of(),
                                List.of()
                        )
                );
            }
        }
    }

    public static double getRunnerAngle(){
        return runnerAngle;
    }
    public static boolean isTracking(){
        return tracking;
    }
}
