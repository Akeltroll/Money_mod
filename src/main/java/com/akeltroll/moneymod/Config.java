package com.akeltroll.moneymod;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

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

    private static final ModConfigSpec.ConfigValue<List<? extends String>> COIN_DROPS_RAW = BUILDER
            .comment(
                "Coin drop configuration per mob.",
                "Format court  : \"namespace:entity_name:coin_type:chance\"",
                "Format complet: \"namespace:entity_name:coin_type:chance:count\"",
                "  - namespace:entity_name : Minecraft entity id (e.g. minecraft:zombie)",
                "  - coin_type             : copper_coin | iron_coin | gold_coin | emerald_coin | diamond_coin | netherite_coin | nether_star_coin",
                "  - chance                : probabilite de drop, 0.0 a 1.0 (ex: 0.08 = 8%)",
                "  - count                 : nombre de pieces droppees (defaut: 1, ex: 1.0:3 = toujours 3 pieces)",
                "Plusieurs entrees pour le meme mob sont supportees."
            )
            .defineList("coinDrops", List.of(
                // --- Mobs communs (overworld facile) — copper, taux faibles ---
                "minecraft:zombie:copper_coin:0.08",
                "minecraft:skeleton:copper_coin:0.08",
                "minecraft:spider:copper_coin:0.05",
                "minecraft:cave_spider:copper_coin:0.07",
                "minecraft:drowned:copper_coin:0.08",
                "minecraft:husk:copper_coin:0.08",
                "minecraft:stray:copper_coin:0.08",
                "minecraft:zombie_villager:copper_coin:0.06",
                "minecraft:slime:copper_coin:0.04",
                "minecraft:silverfish:copper_coin:0.03",
                "minecraft:endermite:copper_coin:0.04",
                // --- Mobs moyens (plus dangereux) — copper, taux un peu plus hauts ---
                "minecraft:creeper:copper_coin:0.12",
                "minecraft:witch:copper_coin:0.15",
                "minecraft:phantom:copper_coin:0.10",
                "minecraft:magma_cube:copper_coin:0.07",
                // --- Mobs élite / Nether — copper, taux modérés ---
                "minecraft:pillager:copper_coin:0.18",
                "minecraft:vindicator:copper_coin:0.18",
                "minecraft:zombie_piglin:copper_coin:0.12",
                "minecraft:piglin:copper_coin:0.12",
                "minecraft:piglin_brute:copper_coin:0.25",
                "minecraft:wither_skeleton:copper_coin:0.22",
                "minecraft:blaze:copper_coin:0.18",
                "minecraft:ghast:copper_coin:0.15",
                "minecraft:guardian:copper_coin:0.15",
                // --- Mobs rares / dangereux — copper, taux plus élevés (< 30%) ---
                "minecraft:enderman:copper_coin:0.10",
                "minecraft:evoker:copper_coin:0.28",
                "minecraft:elder_guardian:copper_coin:0.28",
                "minecraft:ravager:copper_coin:0.25",
                "minecraft:shulker:copper_coin:0.18",
                // --- Bosses — drops garantis, montants fixes ---
                // Warden : 1 gold garanti
                "minecraft:warden:gold_coin:1.0:1",
                // Wither : 3 iron garantis
                "minecraft:wither:iron_coin:1.0:3",
                // Ender Dragon : 2 iron garantis
                "minecraft:ender_dragon:iron_coin:1.0:2"
            ), s -> s instanceof String);

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
    public static List<? extends String> coinDropsRaw;

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
        coinDropsRaw = COIN_DROPS_RAW.get();
        CoinDropHandler.reload(coinDropsRaw);
    }
}