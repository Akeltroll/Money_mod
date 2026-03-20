package com.akeltroll.moneymod.menu;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

/**
 * MenuProvider propre pour remplacer SimpleMenuProvider.
 * Pas d'envoi de données custom via buffer (IMenuProvider n'existe pas en 21.1.73).
 * Le client reconstruit le menu depuis son propre état dans ModMenuTypes.
 */
public class PouchMenuProvider implements MenuProvider {
    private final ItemStack stack;
    private final int slots;
    private final boolean fromCuriosSlot;
    private final Component title;

    public PouchMenuProvider(ItemStack stack, int slots, boolean fromCuriosSlot, Component title) {
        this.stack = stack;
        this.slots = slots;
        this.fromCuriosSlot = fromCuriosSlot;
        this.title = title;
    }

    @Override
    public Component getDisplayName() {
        return title;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new MoneyPouchMenu(id, inv, stack, slots, fromCuriosSlot);
    }
}
