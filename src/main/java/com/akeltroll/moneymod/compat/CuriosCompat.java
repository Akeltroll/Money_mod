package com.akeltroll.moneymod.compat;

import com.akeltroll.moneymod.item.MoneyPouchItem;
import com.akeltroll.moneymod.menu.PouchMenuProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosCompat {
    private static final Logger LOGGER = LoggerFactory.getLogger(CuriosCompat.class);

    public static void sendImc(InterModEnqueueEvent event) {
        LOGGER.info("Curios compatibility initialized for Money Mod");
    }

    /**
     * Retourne la première pouch équipée dans un slot Curios, ou EMPTY.
     * Utilise l'API Curios 9.x : getCuriosInventory() + findFirstCurio().
     */
    public static ItemStack findEquippedPouch(Player player) {
        return CuriosApi.getCuriosInventory(player)
            .flatMap(inv -> inv.findFirstCurio(s -> s.getItem() instanceof MoneyPouchItem))
            .map(result -> result.stack())
            .orElse(ItemStack.EMPTY);
    }

    /**
     * Ouvre la pouch depuis un slot Curios (belt).
     */
    public static void openPouchFromCurios(Player player, ItemStack stack, int slots, String tierName) {
        if (!player.level().isClientSide) {
            player.openMenu(
                new PouchMenuProvider(stack, slots, true,
                    Component.translatable("container.moneymod.money_pouch_" + tierName)),
                buf -> {
                    buf.writeInt(slots);
                    buf.writeBoolean(true);
                }
            );
        }
    }

    /**
     * Clic droit avec main vide : ouvre la pouch si le joueur en a une dans un slot Curios belt.
     */
    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        Player player = event.getEntity();
        if (player.level().isClientSide) return;
        if (!player.getMainHandItem().isEmpty()) return;

        ItemStack curioStack = findEquippedPouch(player);
        if (!curioStack.isEmpty() && curioStack.getItem() instanceof MoneyPouchItem pouchItem) {
            openPouchFromCurios(
                player,
                curioStack,
                pouchItem.getSlots(),
                pouchItem.getTier().name().toLowerCase()
            );
        }
    }
}
