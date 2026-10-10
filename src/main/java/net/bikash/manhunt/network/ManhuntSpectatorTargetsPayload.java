package net.bikash.manhunt.network;

import net.bikash.manhunt.Manhunt;
import net.bikash.manhunt.client.ManhuntSpectatorManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ManhuntSpectatorTargetsPayload (List<UUID> targets)


 implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ManhuntSpectatorTargetsPayload> TYPE = new Type<>(
            Identifier.fromNamespaceAndPath(
                    Manhunt.MOD_ID,
                    "spectator_targets"
            )
    );


    public static final StreamCodec<RegistryFriendlyByteBuf, ManhuntSpectatorTargetsPayload> CODEC = new StreamCodec<>() {
        @Override

        public ManhuntSpectatorTargetsPayload decode(
                RegistryFriendlyByteBuf buf
        ) {
            int count = buf.readVarInt();
            List<UUID> targets = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                targets.add(UUID.fromString(buf.readUtf()));
            }
            return new ManhuntSpectatorTargetsPayload(targets);
        }

        @Override
        public void encode(
                RegistryFriendlyByteBuf buf,
                ManhuntSpectatorTargetsPayload payload
        ) {

            buf.writeVarInt(payload.targets().size());

         for (UUID target : payload.targets())  {
                buf.writeUtf(target.toString());
            }
        }

    };
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
