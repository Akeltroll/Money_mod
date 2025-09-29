package com.akeltroll.moneymod;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = MoneyMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue ENABLE_COPPER_COIN = BUILDER
            .comment("Enable copper coins in the game")
            .define("enableCopperCoin", true);

    private static final ModConfigSpec.BooleanValue ENABLE_IRON_COIN = BUILDER
            .comment("Enable iron coins in the game")
            .define("enableIronCoin", true);

    private static final ModConfigSpec.BooleanValue ENABLE_GOLD_COIN = BUILDER
            .comment("Enable gold coins in the game")
            .define("enableGoldCoin", true);

    private static final ModConfigSpec.BooleanValue ENABLE_EMERALD_COIN = BUILDER
            .comment("Enable emerald coins in the game")
            .define("enableEmeraldCoin", true);

    private static final ModConfigSpec.BooleanValue ENABLE_DIAMOND_COIN = BUILDER
            .comment("Enable diamond coins in the game")
            .define("enableDiamondCoin", true);

    private static final ModConfigSpec.BooleanValue ENABLE_NETHERITE_COIN = BUILDER
            .comment("Enable netherite coins in the game")
            .define("enableNetheriteCoin", true);

    private static final ModConfigSpec.BooleanValue ENABLE_NETHER_STAR_COIN = BUILDER
            .comment("Enable nether star coins in the game")
            .define("enableNetherStarCoin", true);

    private static final ModConfigSpec.IntValue BASIC_POUCH_SLOTS = BUILDER
            .comment("Number of slots for basic money pouch")
            .defineInRange("basicPouchSlots", 9, 1, 54);

    private static final ModConfigSpec.IntValue ADVANCED_POUCH_SLOTS = BUILDER
            .comment("Number of slots for advanced money pouch")
            .defineInRange("advancedPouchSlots", 18, 1, 54);

    private static final ModConfigSpec.IntValue FINAL_POUCH_SLOTS = BUILDER
            .comment("Number of slots for final money pouch")
            .defineInRange("finalPouchSlots", 27, 1, 54);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean enableCopperCoin;
    public static boolean enableIronCoin;
    public static boolean enableGoldCoin;
    public static boolean enableEmeraldCoin;
    public static boolean enableDiamondCoin;
    public static boolean enableNetheriteCoin;
    public static boolean enableNetherStarCoin;
    public static int basicPouchSlots;
    public static int advancedPouchSlots;
    public static int finalPouchSlots;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        enableCopperCoin = ENABLE_COPPER_COIN.get();
        enableIronCoin = ENABLE_IRON_COIN.get();
        enableGoldCoin = ENABLE_GOLD_COIN.get();
        enableEmeraldCoin = ENABLE_EMERALD_COIN.get();
        enableDiamondCoin = ENABLE_DIAMOND_COIN.get();
        enableNetheriteCoin = ENABLE_NETHERITE_COIN.get();
        enableNetherStarCoin = ENABLE_NETHER_STAR_COIN.get();
        basicPouchSlots = BASIC_POUCH_SLOTS.get();
        advancedPouchSlots = ADVANCED_POUCH_SLOTS.get();
        finalPouchSlots = FINAL_POUCH_SLOTS.get();
    }
}