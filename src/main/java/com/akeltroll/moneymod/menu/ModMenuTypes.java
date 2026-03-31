package com.akeltroll.moneymod.menu;

import com.akeltroll.moneymod.MoneyMod;
import com.akeltroll.moneymod.compat.CuriosCompat;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(BuiltInRegistries.MENU, MoneyMod.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<MoneyPouchMenu>> MONEY_POUCH =
            MENUS.register("money_pouch",
                    () -> IMenuTypeExtension.create((id, inv, buf) -> {
                        // Le serveur écrit slots + fromCuriosSlot dans le buffer.
                        // On lit directement — plus de devinette client-side.
                        int slots = buf.readInt();
                        boolean fromCuriosSlot = buf.readBoolean();

                        ItemStack stack;
                        if (fromCuriosSlot && ModList.get().isLoaded("curios")) {
                            stack = CuriosCompat.findEquippedPouch(inv.player);
                        } else {
                            stack = inv.player.getMainHandItem();
                        }

                        return new MoneyPouchMenu(id, inv, stack, slots, fromCuriosSlot);
                    }));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
