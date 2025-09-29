package com.akeltroll.moneymod.menu;

import com.akeltroll.moneymod.MoneyMod;
import com.akeltroll.moneymod.item.MoneyPouchItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(BuiltInRegistries.MENU, MoneyMod.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<MoneyPouchMenu>> MONEY_POUCH =
            MENUS.register("money_pouch",
                    () -> IMenuTypeExtension.create((id, inv, data) -> {
                        ItemStack stack = inv.player.getMainHandItem();
                        int slots = 9; // Valeur par défaut
                        
                        // Récupérer le nombre de slots depuis l'item
                        if (stack.getItem() instanceof MoneyPouchItem pouchItem) {
                            slots = pouchItem.getSlots();
                        }
                        
                        return new MoneyPouchMenu(id, inv, stack, slots);
                    }));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}