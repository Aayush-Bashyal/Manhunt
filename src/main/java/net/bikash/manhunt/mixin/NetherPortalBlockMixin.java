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
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;


@Mixin(NetherPortalBlock.class)
public class NetherPortalBlockMixin {

    private static BlockPos manhunt$allowedNetherPortal;
    private static java.util.UUID manhunt$trackedRunner;

    @Inject(
            method = "getPortalDestination",
            at = @At("RETURN")

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
        if(currentLevel.dimension() != Level.OVERWORLD) {
            return;
        }
        if(cir.getReturnValue() == null){
            return;
        }

        ManhuntGame game = Manhunt.getGame(currentLevel.getServer());

        if(!game.isRunning() || game.getRunner() == null){
            return;
        }
        if(!player.getUUID().equals(game.getRunner())){
            return;
        }
      UUID runnerId = player.getUUID();


        //runer enters the nether forn the overworld
        if(currentLevel.dimension() == Level.OVERWORLD){
            manhunt$trackedRunner = runnerId;
            manhunt$allowedNetherPortal  = portalEntryPos.immutable();
            return;
        }

        //when runner attempts to retuen form the nether.

        if(currentLevel.dimension() == Level.NETHER && runnerId.equals(manhunt$trackedRunner) && manhunt$allowedNetherPortal!= null){
            if(!manhunt$allowedNetherPortal.equals(portalEntryPos)){
                player.sendSystemMessage(
                        Component.literal(
                                "You cannot use another portal! Use the same portal to return."
                        ).withColor(0xFF0000),
                        true
                );
            }
        }

}}
