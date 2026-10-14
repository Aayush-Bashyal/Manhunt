package net.bikash.manhunt.mixin;

import net.bikash.manhunt.Manhunt;
import net.bikash.manhunt.game.ManhuntGame;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;


@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin {




    @Unique
    private static UUID manhunt$trackedRunner;

    @Unique
    private static Vec3 manhunt$allowedNetherPosition;

    @Inject(
            method = "getPortalDestination",
            at = @At("RETURN"),
            cancellable = true
    )
    private void manhunt$checkPortal(
            ServerLevel currentLevel,
            Entity entity,
            BlockPos portalEntryPos,
            CallbackInfoReturnable< TeleportTransition> cir
    ){

        if(!(entity instanceof ServerPlayer player)){
            return;
        }

        if(currentLevel.getServer() == null){
            return;
        }



        ManhuntGame game = Manhunt.getGame(currentLevel.getServer());

        if(!game.isRunning() || game.getRunner() == null){
            return;
        }

      UUID runnerId = player.getUUID();

        if(!runnerId.equals(game.getRunner())){
            return;
        }

        TeleportTransition transition = cir.getReturnValue();

        if(transition == null){
            return;
        }




        //runer enters the nether forn the overworld
        if(currentLevel.dimension() == Level.OVERWORLD){
            manhunt$trackedRunner = runnerId;
            manhunt$allowedNetherPosition  = transition.position();
            return;
        }

        //when runner attempts to retuen form the nether.

        if(currentLevel.dimension() == Level.NETHER && runnerId.equals(manhunt$trackedRunner) && manhunt$allowedNetherPosition!= null){

                BlockPos allowedPortal = BlockPos.containing(
                        manhunt$allowedNetherPosition.x(),
                        manhunt$allowedNetherPosition.y(),
                        manhunt$allowedNetherPosition.z()
                );
                if(portalEntryPos.distSqr(allowedPortal)>64.0){
                    cir.setReturnValue(null);
                }

                player.sendSystemMessage(
                        Component.literal(
                                "You cannot use another portal! Use the same portal to return."
                        ).withStyle(ChatFormatting.RED)

                );

        }

}}
