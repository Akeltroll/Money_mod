package com.akeltroll.moneymod.compat;

import com.akeltroll.moneymod.menu.MoneyPouchMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CuriosCompat {
    private static final Logger LOGGER = LoggerFactory.getLogger(CuriosCompat.class);
    
    public static void sendImc(InterModEnqueueEvent event) {
        // Dans Curios 9.5+, les slots sont enregistrés via datapacks
        // Le slot "belt" est déjà disponible par défaut
        LOGGER.info("Curios compatibility initialized for Money Mod");
    }
    
    public static void openPouchFromCurios(Player player, ItemStack stack, int slots, String tierName) {
        if (!player.level().isClientSide) {
            player.openMenu(new SimpleMenuProvider(
                (id, inv, p) -> new MoneyPouchMenu(id, inv, stack, slots),
                Component.translatable("container.moneymod.money_pouch_" + tierName)
            ));
        }
    }
}