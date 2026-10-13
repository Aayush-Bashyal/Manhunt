package net.bikash.manhunt.network;

import net.bikash.manhunt.Manhunt;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ManhuntRequestRolesPayload()
        implements CustomPacketPayload{
    
    public static final Type<ManhuntRequestRolesPayload> TYPE =
            new Type<>(
                    Identifier.fromNamespaceAndPath(
                            Manhunt.MOD_ID,
                            "request_roles"
                    )
            )                                   ;

    public  static final StreamCodec<RegistryFriendlyByteBuf,ManhuntRequestRolesPayload>  CODEC =
            StreamCodec.unit(new ManhuntRequestRolesPayload());

    @Override
    public Type<? extends CustomPacketPayload> type(){
        return TYPE;
    }
}
