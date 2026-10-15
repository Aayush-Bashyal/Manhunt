package net.bikash.manhunt.network;

import com.mojang.datafixers.types.Type;
import net.bikash.manhunt.Manhunt;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.UUID;

public record ManhuntCurrentRolesPayload (
    UUID runner,
    List<UUID> hunters,
    List<UUID> spectators
            ) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ManhuntCurrentRolesPayload> TYPE = new CustomPacketPayload.Type<>(
            Identifier.fromNamespaceAndPath(
                    Manhunt.MOD_ID,
                    "current_roles"
            )
        );

    public static final StreamCodec<RegistryFriendlyByteBuf, ManhuntCurrentRolesPayload> CODEC =
            StreamCodec.of(
                    (buf,payload) -> {
                        buf.writeBoolean(payload.runner()!=null);

                        if(payload.runner()!=null){
                            buf.writeUUID(payload.runner());
                        }
                        buf.writeVarInt(payload.hunters().size());
                        for(UUID id : payload.hunters()){
                            buf.writeUUID(id);
                        }
                        buf.writeVarInt(payload.spectators().size());
                        for(UUID id : payload.spectators()){
                            buf.writeUUID(id);
                        }
                    },
                    buf->{
                        UUID runner = buf.readBoolean() ?buf.readUUID() :null;

                        int hunterCount = buf.readVarInt();
                        List<UUID> hunters = new java.util.ArrayList<>();

                        for(int i=0 ; i<hunterCount;i++){
                            hunters.add(buf.readUUID());
                        }

                        int spectatorCount = buf.readVarInt();
                        List<UUID> spectators = new java.util.ArrayList<>();

                        for (int i = 0; i < spectatorCount; i++) {
                            spectators.add(buf.readUUID());
                        }

                        return new ManhuntCurrentRolesPayload(
                                runner,hunters,spectators
                        );
                    }
            );
    @Override
            public CustomPacketPayload.Type<? extends CustomPacketPayload> type(){
        return TYPE;
        }
    }


