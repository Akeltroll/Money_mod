package com.akeltroll.moneymod.client;

import com.akeltroll.moneymod.network.OpenPouchPacket;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class ModKeybindings {

    public static KeyMapping OPEN_POUCH;

    public static void register(RegisterKeyMappingsEvent event) {
        OPEN_POUCH = new KeyMapping(
            "key.moneymod.open_pouch",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_B,
            "key.categories.moneymod"
        );
        event.register(OPEN_POUCH);
    }

    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;
        if (OPEN_POUCH.consumeClick()) {
            PacketDistributor.sendToServer(new OpenPouchPacket());
        }
    }
}
