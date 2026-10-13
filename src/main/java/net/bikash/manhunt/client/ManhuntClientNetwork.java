package net.bikash.manhunt.client;
import net.bikash.manhunt.Manhunt;
import net.bikash.manhunt.gui.ManhuntPlayerSelectScreen;
import net.bikash.manhunt.hud.ManhuntHUD;
import net.bikash.manhunt.network.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
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


                }
        );


        ClientPlayNetworking.registerGlobalReceiver(
                ManhuntHUDNetwork.TYPE,
                (payload, context)->{
                    ManhuntHUD.update(
                            payload.running(),
                            payload.elapsedTime(),
                           "",
                            ""


                    );
                    ManhuntPlayerSelectScreen.setManhuntRunning(payload.running());
                }
        );

        ClientPlayNetworking.registerGlobalReceiver(ManhuntAdminNetwork.TYPE,(payload, context) -> {

                ManhuntAdminNetwork.isAdmin = payload.admin();
            }
        );

        ClientPlayNetworking.registerGlobalReceiver(
                ManhuntSpectatorTargetsPayload.TYPE,((payload, context) -> {
                    context.client().execute(()->{
                        ManhuntSpectatorManager.setTargets(payload.targets());
                        ManhuntSpectatorManager.updateCamera();
                    });
                })
        );

        ClientPlayNetworking.registerGlobalReceiver(
                ManhuntCurrentRolesPayload.TYPE,
                (payload,context)-> {
                    context.client().execute(()->{
                        ManhuntPlayerSelectScreen.updateSavedRoles(
                                payload.runner(),
                                payload.hunters(),
                                payload.spectators()
                        );
                    });
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
        if (model<0){
            model=0;
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
