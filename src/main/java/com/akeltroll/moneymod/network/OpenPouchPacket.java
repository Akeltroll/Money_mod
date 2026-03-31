package com.akeltroll.moneymod.network;

import com.akeltroll.moneymod.compat.CuriosCompat;
import com.akeltroll.moneymod.item.MoneyPouchItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record OpenPouchPacket() implements CustomPacketPayload {

    public static final Type<OpenPouchPacket> TYPE = new Type<>(
        ResourceLocation.fromNamespaceAndPath("moneymod", "open_pouch")
    );
    public static final StreamCodec<ByteBuf, OpenPouchPacket> STREAM_CODEC =
        StreamCodec.unit(new OpenPouchPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenPouchPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = (Player) context.player();
            if (ModList.get().isLoaded("curios")) {
                ItemStack pouch = CuriosCompat.findEquippedPouch(player);
                if (!pouch.isEmpty() && pouch.getItem() instanceof MoneyPouchItem pouchItem) {
                    CuriosCompat.openPouchFromCurios(
                        player, pouch,
                        pouchItem.getSlots(),
                        pouchItem.getTier().name().toLowerCase()
                    );
                }
            }
        });
    }
}
