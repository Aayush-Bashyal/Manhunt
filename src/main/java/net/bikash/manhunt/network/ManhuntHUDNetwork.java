package net.bikash.manhunt.network;

import it.unimi.dsi.fastutil.bytes.Byte2BooleanArrayMap;
import net.bikash.manhunt.Manhunt;
import net.bikash.manhunt.hud.ManhuntHUD;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ManhuntHUDNetwork(
        boolean running,
        long elapsedTime

) implements CustomPacketPayload{
    public static final CustomPacketPayload.Type<ManhuntHUDNetwork> TYPE =
            new CustomPacketPayload.Type<>(
                    Identifier.fromNamespaceAndPath(
                            Manhunt.MOD_ID,
                            "manhunt_hud"
                    )
            );
    public static final StreamCodec<RegistryFriendlyByteBuf, ManhuntHUDNetwork>
    CODEC = StreamCodec.composite(


            ByteBufCodecs.BOOL,
            ManhuntHUDNetwork::running,

            ByteBufCodecs.VAR_LONG,
            ManhuntHUDNetwork::elapsedTime,


          ManhuntHUDNetwork::new


    );
    @Override
    public Type<? extends CustomPacketPayload> type(){
        return TYPE;
    }
}
