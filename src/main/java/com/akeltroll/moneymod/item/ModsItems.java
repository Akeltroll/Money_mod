package com.akeltroll.moneymod.item;

import com.akeltroll.moneymod.Config;
import com.akeltroll.moneymod.MoneyMod;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.minecraft.world.item.Item;

public class ModsItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(MoneyMod.MOD_ID);

    public static final DeferredItem<Item> COPPER_COIN = ITEMS.register("copper_coin",
            () -> new CoinItem(new Item.Properties().stacksTo(64), CoinType.COPPER));

    public static final DeferredItem<Item> IRON_COIN = ITEMS.register("iron_coin",
            () -> new CoinItem(new Item.Properties().stacksTo(64), CoinType.IRON));

    public static final DeferredItem<Item> GOLD_COIN = ITEMS.register("gold_coin",
            () -> new CoinItem(new Item.Properties().stacksTo(64), CoinType.GOLD));

    public static final DeferredItem<Item> EMERALD_COIN = ITEMS.register("emerald_coin",
            () -> new CoinItem(new Item.Properties().stacksTo(64), CoinType.EMERALD));

    public static final DeferredItem<Item> DIAMOND_COIN = ITEMS.register("diamond_coin",
            () -> new CoinItem(new Item.Properties().stacksTo(64), CoinType.DIAMOND));

    public static final DeferredItem<Item> NETHERITE_COIN = ITEMS.register("netherite_coin",
            () -> new CoinItem(new Item.Properties().stacksTo(64), CoinType.NETHERITE));

    public static final DeferredItem<Item> NETHER_STAR_COIN = ITEMS.register("nether_star_coin",
            () -> new CoinItem(new Item.Properties().stacksTo(64), CoinType.NETHER_STAR));

    public static final DeferredItem<Item> BASIC_MONEY_POUCH = ITEMS.register("basic_money_pouch",
        () -> new MoneyPouchItem(new Item.Properties(), () -> Config.basicPouchSlots, PouchTier.BASIC));

    public static final DeferredItem<Item> ADVANCED_MONEY_POUCH = ITEMS.register("advanced_money_pouch",
        () -> new MoneyPouchItem(new Item.Properties(), () -> Config.advancedPouchSlots, PouchTier.ADVANCED));

    public static final DeferredItem<Item> FINAL_MONEY_POUCH = ITEMS.register("final_money_pouch",
        () -> new MoneyPouchItem(new Item.Properties(), () -> Config.finalPouchSlots, PouchTier.FINAL));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}