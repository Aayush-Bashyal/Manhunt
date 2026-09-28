package net.bikash.manhunt.game;

import net.bikash.manhunt.Manhunt;
import net.bikash.manhunt.item.ModItems;
import net.bikash.manhunt.network.ManhuntHUDNetwork;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;
import net.bikash.manhunt.network.ManhuntNetwork;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.item.ItemStack;

public class ManhuntTracker {
private static int hudCounter = 0;
    private static int tickcounter = 0;
    private static double runnerAngle = 0;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {

            ManhuntGame game = Manhunt.getGame(server);

hudCounter++;
if(hudCounter>=5){
    hudCounter=0;
    String runnerName = "";

    if(game.getRunner()!=null){
        ServerPlayer runner = server.getPlayerList().getPlayer(
                game.getRunner()
        );

        if(runner!=null){
            runnerName=runner.getName().getString();
        }
    }
    StringBuilder hunterNames = new StringBuilder();

    for(var hunterUUID:game.getHunters()){
        ServerPlayer hunter = server.getPlayerList().getPlayer(hunterUUID);

        if(hunter!=null){
            if(hunterNames.length()>0){
                hunterNames.append(",");
            }
            hunterNames.append(
                    hunter.getName().getString()
            );
        }
    }
    ManhuntHUDNetwork hudPacket = new ManhuntHUDNetwork(
            game.isRunning(),
            game.isRunning()
                    ? game.getElapsedTime()
                    : game.getFinalTime(),
            runnerName,
            hunterNames.toString()

            );
    for(ServerPlayer player : server.getPlayerList().getPlayers()){
        ServerPlayNetworking.send(
                player,
                hudPacket
        );
    }
            }

            //sends the HUD to every single player in the world
            if(tickcounter%5==0){
                String runnerName = "";
                if(game.getRunner()!=null){
                    ServerPlayer runner = server.getPlayerList().getPlayer(
                            game.getRunner()
                    );
                    if(runner!=null){
                        runnerName=runner.getName().getString();
                    }
                }

                StringBuilder hunterNames = new StringBuilder();
                for(var hunterUUID : game.getHunters()){
                    ServerPlayer hunter = server.getPlayerList().getPlayer(hunterUUID);

                    if(hunter!=null){
                        if(hunterNames.length()>0){
                            hunterNames.append(",");
                        }
                        hunterNames.append(hunter.getName().getString()
                        );
                    }
                }
                ManhuntHUDNetwork hudPacket = new ManhuntHUDNetwork(
                        game.isRunning(),
                        game.isRunning()
                        ?game.getElapsedTime()
                                :game.getFinalTime(),
                        runnerName,
                        hunterNames.toString()
                );
                for(ServerPlayer player:server.getPlayerList().getPlayers()){
                    ServerPlayNetworking.send(
                            player,hudPacket
                    );
                }
            }

            //making that when the runner leaves the spawn chunk the manhunt automatically start


            if (!game.isRunning() && game.getRunner() != null) {
                ServerPlayer runner = server.getPlayerList().getPlayer(game.getRunner());


                if (runner != null) {
                    int runnerChunkX = runner.chunkPosition().x();
                    int runnerChunkZ = runner.chunkPosition().z();
                    if (runnerChunkX != game.getSpawnChunkX() || runnerChunkZ != game.getSpawnChunkZ()) {
                        game.start();

                        server.getPlayerList().broadcastSystemMessage
                                (net.minecraft.network.chat.Component.literal
                                                ("MANHUNT STARTED!!"),
                                        false
                                );

                        for (var hunterUUID : game.getHunters()) {
                            ServerPlayer hunter = server.getPlayerList().getPlayer(hunterUUID);
                            if (hunter != null) {
                                hunter.getInventory().add(net.bikash.manhunt.item.ModItems.MANHUNT_COMPASS.getDefaultInstance()
                                );
                            }
                        }
                    }
                }
            }


            if (!game.isRunning()) {
                return;
            }
            tickcounter++;

            if (tickcounter < 5) {
                return;
            }
            tickcounter = 0;




            if (game.getRunner() == null) {
                return;
            }
            ServerPlayer runner = server.getPlayerList().getPlayer(game.getRunner());

            if (runner == null) {
                return;
            }
            if (!runner.isAlive()) {
                game.finish("Hunters");
                server.getPlayerList()
                        .broadcastSystemMessage(net.minecraft.network.chat.Component.literal
                                        ("HUNTERSSS WINN!! \uD83D\uDE4C "),
                                false
                        );
                return;
            }

            for (var hunterUUID : game.getHunters()) {
                ServerPlayer hunter = server.getPlayerList().getPlayer(hunterUUID);

                if (hunter == null) {
                    continue;
                }
                if (hunter.level().dimension() != runner.level().dimension()) {
                    ServerPlayNetworking.send(
                            hunter,
                            new ManhuntNetwork(false, 0)
                    );
                    continue;
                }

                double dx = runner.getX() - hunter.getX();
                double dz = runner.getZ() - hunter.getZ();


                //calculate theee anglee form the hunter to the runner
                double angle = Math.toDegrees(
                        Math.atan2(dz, dx)
                );
                double hunterYaw = hunter.getYRot();
                angle = angle - (hunterYaw - 90);
                angle = (angle + 360) % 360;

                runnerAngle = angle;


                ServerPlayNetworking.send(
                        hunter,
                        new ManhuntNetwork(true, angle)
                );

                System.out.println(
                        "Hunter: " + hunter.getName().getString() + "| Runner angle:" + angle
                );
            }


        });

//this detectss enderdragonn death
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {

            if (entity.level().getServer() == null) {
                return;
            }

            ManhuntGame game = Manhunt.getGame(
                    entity.level().getServer()
            );

            if(!game.isRunning()){
                return;
            }
            if(!(entity instanceof EnderDragon)){
                return;
            }

            //if runner wins ,
            game.finish("Runner");

            //setting all hunters name ,

            StringBuilder hunters = new StringBuilder();
            for (var hunterUUID : game.getHunters()){
                ServerPlayer hunter = entity.level().getServer().getPlayerList().getPlayer(hunterUUID);


                    if (hunter != null) {
                        if(hunters.length() > 0 ){
                            hunters.append(", ");
                        }
                        hunters.append(
                                hunter.getName().getString()
                        );
                    }
                }
                //now getting hunters name
                ServerPlayer runnerPlayer = entity.level().getServer().getPlayerList().getPlayer(game.getRunner());
                String runnerName = "Unknown";

                if(runnerPlayer!=null){
                    runnerName = runnerPlayer.getName().getString();
                }
                // the display shown in the screen

                entity.level().getServer().getPlayerList().broadcastSystemMessage(
                        net.minecraft.network.chat.Component.literal(
                                "\uD83C\uDFC6 RUNNER WINS!!"
                        ),
                        false
                );
                entity.level().getServer().getPlayerList().broadcastSystemMessage(
                        net.minecraft.network.chat.Component.literal(
                                "Runner: " + runnerName),
                        false
                );
                entity.level().getServer().getPlayerList().broadcastSystemMessage(
                        net.minecraft.network.chat.Component.literal(
                                "Hunters: " + hunters
                        ),
                        false
                );
                entity.level().getServer().getPlayerList().broadcastSystemMessage(
                        net.minecraft.network.chat.Component.literal(
                                "Final Time: "
                                        + game.getFinalTimeFormatted()
                        ),
                        false
                );
                entity.level().getServer().getPlayerList().broadcastSystemMessage(
                        net.minecraft.network.chat.Component.literal(
                                "***************************************"
                        ),
                        false
                 );
                // not letting the manhunt compass drop when the hunter dies

             });

        ServerPlayerEvents.AFTER_RESPAWN.register(
                (oldPlayer, newPlayer, alive) -> {
                    if(newPlayer.level().getServer()==null){
                        return;
                    }
                    ManhuntGame game = Manhunt.getGame(newPlayer.level().getServer());
                    if(!game.isRunning()){
                        return;
                    }

                    if(!game.getHunters().contains(
                            newPlayer.getUUID()
                    )){
                      return;
                    }
                    //gives the compass back to the hunters
                    newPlayer.getInventory().add(
                            ModItems.MANHUNT_COMPASS.getDefaultInstance()
                    );

                }
        );

    }
    public static double getRunnerAngle(){
        return runnerAngle;
    }
}
