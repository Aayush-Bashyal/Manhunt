package net.bikash.manhunt.network;
import net.bikash.manhunt.Manhunt;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.network.codec.ByteBufCodecs;


public record ManhuntNetwork(boolean tracking,double angle)
        implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ManhuntNetwork> TYPE
            = new CustomPacketPayload.Type<>(
                    Identifier.fromNamespaceAndPath( Manhunt.MOD_ID,
            "manhunt_direction" )
    );
    public static final StreamCodec<RegistryFriendlyByteBuf,
            ManhuntNetwork>
            CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            ManhuntNetwork::tracking,

            ByteBufCodecs.DOUBLE,
            ManhuntNetwork::angle,
            ManhuntNetwork::new
    );
    @Override public Type<? extends CustomPacketPayload>
    type() {
        return TYPE;
    }
}