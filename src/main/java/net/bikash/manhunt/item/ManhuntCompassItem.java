package net.bikash.manhunt.item;

import net.bikash.manhunt.Manhunt;
import net.bikash.manhunt.game.ManhuntGame;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ManhuntCompassItem extends Item {
    public ManhuntCompassItem(Properties properties){
        super(properties);
    }
    @Override
    public InteractionResult use(
            net.minecraft.world.level.Level level,
            Player player,
            InteractionHand hand
    ) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            if (!(player instanceof ServerPlayer hunter)) {
                return InteractionResult.PASS;
            }
            ManhuntGame game =
                    Manhunt.getGame(level.getServer());

            if (!game.isRunning()) {
                hunter.sendSystemMessage(
                        net.minecraft.network.chat.Component.literal(
                                "Manhunt is not Running!"
                        )

                );
                return InteractionResult.SUCCESS;
            }
            ServerPlayer runner = level.getServer()
                    .getPlayerList()
                    .getPlayer(game.getRunner());
            if (runner == null || hunter.level().dimension() != runner.level().dimension()) {
                hunter.sendSystemMessage(
                        Component.literal("Player not Found")
                );
                return InteractionResult.SUCCESS;
            }
            hunter.sendSystemMessage(
                    Component.literal("Runner Found!!")


            );
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }}
    