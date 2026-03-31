package com.akeltroll.moneymod.menu;

import com.akeltroll.moneymod.item.CoinItem;
import com.akeltroll.moneymod.item.MoneyPouchItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class MoneyPouchMenu extends AbstractContainerMenu {
    private final ItemStack pouchStack;
    private final int pouchSlots;
    private final ItemStackHandler handler;
    private final HolderLookup.Provider registries;
    private final Player player;
    private final boolean fromCuriosSlot;

    public MoneyPouchMenu(int id, Inventory playerInv, ItemStack pouchStack, int slots) {
        this(id, playerInv, pouchStack, slots, false);
    }

    public MoneyPouchMenu(int id, Inventory playerInv, ItemStack pouchStack, int slots, boolean fromCuriosSlot) {
        super(ModMenuTypes.MONEY_POUCH.get(), id);
        this.pouchStack = pouchStack;
        this.pouchSlots = slots;
        this.player = playerInv.player;
        this.registries = playerInv.player.registryAccess();
        this.fromCuriosSlot = fromCuriosSlot;

        this.handler = new ItemStackHandler(slots) {
            @Override
            protected void onContentsChanged(int slot) {
                saveToStack();
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                return stack.getItem() instanceof CoinItem;
            }
        };

        // Charger l'inventaire depuis le CustomData
        CustomData customData = pouchStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if (customData.contains("Inventory")) {
            handler.deserializeNBT(registries, customData.copyTag().getCompound("Inventory"));
        }

        int rows = (slots + 8) / 9;

        for (int i = 0; i < slots; i++) {
            int row = i / 9;
            int col = i % 9;
            this.addSlot(new SlotItemHandler(handler, i, 8 + col * 18, 17 + row * 18));
        }

        int yOffset = 17 + rows * 18 + 14;

        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9,
                        8 + col * 18, yOffset + row * 18));
            }
        }

        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInv, col, 8 + col * 18, yOffset + 58));
        }
    }

    private void saveToStack() {
        // Côté client (cas Curios), pouchStack est EMPTY — on ne sauvegarde pas,
        // le serveur est authoritative et sync via les packets de slots.
        if (pouchStack.isEmpty()) return;
        var tag = pouchStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        tag.put("Inventory", handler.serializeNBT(registries));
        pouchStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        // Notifier l'inventaire du joueur que le slot a changé pour la persistence
        player.getInventory().setChanged();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();

            if (index < pouchSlots) {
                if (!this.moveItemStackTo(stack, pouchSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (stack.getItem() instanceof CoinItem) {
                    if (!this.moveItemStackTo(stack, 0, pouchSlots, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        saveToStack();

        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        if (fromCuriosSlot) {
            return player.isAlive();
        }
        if (pouchStack.isEmpty() || !(pouchStack.getItem() instanceof MoneyPouchItem)) {
            return false;
        }
        return player.getMainHandItem() == pouchStack || player.getOffhandItem() == pouchStack;
    }

    public int getPouchSlots() {
        return pouchSlots;
    }
}
