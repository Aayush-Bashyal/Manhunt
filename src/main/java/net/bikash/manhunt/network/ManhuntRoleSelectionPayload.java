package net.bikash.manhunt.network;

import net.bikash.manhunt.Manhunt;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ManhuntRoleSelectionPayload (
    UUID runner,
    List<UUID> hunters
) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ManhuntRoleSelectionPayload> TYPE =
            new CustomPacketPayload.Type<>(
                    Identifier.fromNamespaceAndPath(
                            Manhunt.MOD_ID,
                            "role_selection"
                    )
            );
    public static final StreamCodec<
            RegistryFriendlyByteBuf,ManhuntRoleSelectionPayload> CODEC = new StreamCodec<>(){
        @Override
        public ManhuntRoleSelectionPayload decode(
                RegistryFriendlyByteBuf buf
        ){
            UUID runner = UUID.fromString(
                    buf.readUtf()
            );
            int hunterCount = buf.readVarInt();
            List<UUID> hunters = new ArrayList<>();
            for (int i =0;i<hunterCount;i++){
                hunters.add(
                        UUID.fromString(
                                buf.readUtf()
                        )
                );
            }
            return new ManhuntRoleSelectionPayload(
                    runner,hunters
            );
        }
        @Override
        public void encode (
                RegistryFriendlyByteBuf buf,
                ManhuntRoleSelectionPayload payload
        ){
            buf.writeUtf( payload.runner().toString());
            buf.writeVarInt(payload.hunters().size());

            for(UUID hunter : payload.hunters()){
                buf.writeUtf(
                        hunter.toString()
                );
            }
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
