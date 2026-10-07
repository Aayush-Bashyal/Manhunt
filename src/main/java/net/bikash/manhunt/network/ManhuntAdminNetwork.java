package net.bikash.manhunt.network;

import com.mojang.datafixers.types.Type;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ManhuntAdminNetwork(boolean admin)
    implements CustomPacketPayload {
    public static boolean isAdmin = false;

        public static final Identifier ID = Identifier.fromNamespaceAndPath("manhunt", "admin");

        public static final Type<ManhuntAdminNetwork> TYPE = new Type<>(ID);

        public static final StreamCodec<RegistryFriendlyByteBuf,ManhuntAdminNetwork> CODEC =

                StreamCodec.composite(
                        net.minecraft.network.codec.ByteBufCodecs.BOOL,
                        ManhuntAdminNetwork::admin,
                        ManhuntAdminNetwork::new
                );

        @Override
                public Type<? extends CustomPacketPayload> type(){
            return TYPE;
        }
    }


