package net.bikash.manhunt.mixin;
import net.minecraft.world.entity.player.Player;
import net.bikash.manhunt.item.ModItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.security.auth.callback.Callback;
import java.util.ArrayList;
import java.util.List;

@Mixin(Player.class)
public class PlayerInventoryMixin {

    //making the manhunt compass not drop when the hunter dies

    @Redirect(
            method = "dropEquipment",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Inventory;dropAll()V")
    )
    private void manhunt$propertyCompass(Inventory inventory){
        List<ItemStack> protectedCompasses = new ArrayList<>();
        List<Integer> protectedSlots = new ArrayList<>();

        for(int slot=0; slot<41 ;slot++){
            ItemStack stack = inventory.getItem(slot);

            if(stack.is(ModItems.MANHUNT_COMPASS)){
                protectedCompasses.add(stack.copy());
                protectedSlots.add(slot);

                inventory.setItem(slot,ItemStack.EMPTY);
            }

        }
        inventory.dropAll();

        for(int i=0;i<protectedCompasses.size();i++){
            int slot = protectedSlots.get(i);
            ItemStack compass = protectedCompasses.get(i);
            inventory.setItem(slot,compass);
        }
    }
}
