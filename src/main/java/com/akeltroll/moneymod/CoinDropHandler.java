package com.akeltroll.moneymod;

import com.akeltroll.moneymod.item.CoinItem;
import com.akeltroll.moneymod.item.ModsItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.ArrayList;
import java.util.List;

public class CoinDropHandler {

    /**
     * Format config : "namespace:entity_name:coin_type:chance"
     *              ou "namespace:entity_name:coin_type:chance:count"
     * Exemple      : "minecraft:zombie:copper_coin:0.08"
     *              : "minecraft:wither:iron_coin:1.0:3"
     */
    private record DropEntry(String entityId, Item coinItem, float chance, int count) {}

    private static List<DropEntry> drops = List.of();

    // -------------------------------------------------------------------------
    // Config reload
    // -------------------------------------------------------------------------

    public static void reload(List<? extends String> raw) {
        List<DropEntry> parsed = new ArrayList<>();
        for (String s : raw) {
            String[] parts = s.split(":");
            // 4 parts = sans count, 5 parts = avec count
            if (parts.length < 4 || parts.length > 5) continue;

            String entityId = parts[0] + ":" + parts[1];
            String coinType = parts[2];

            float chance;
            try {
                chance = Float.parseFloat(parts[3]);
            } catch (NumberFormatException e) {
                continue;
            }
            if (chance <= 0f || chance > 1f) continue;

            int count = 1;
            if (parts.length == 5) {
                try {
                    count = Integer.parseInt(parts[4]);
                } catch (NumberFormatException e) {
                    continue;
                }
                if (count < 1) continue;
            }

            Item coin = getCoin(coinType);
            if (coin != null) {
                parsed.add(new DropEntry(entityId, coin, chance, count));
            }
        }
        drops = List.copyOf(parsed);
    }

    private static Item getCoin(String type) {
        return switch (type) {
            case "copper_coin"      -> ModsItems.COPPER_COIN.get();
            case "iron_coin"        -> ModsItems.IRON_COIN.get();
            case "gold_coin"        -> ModsItems.GOLD_COIN.get();
            case "emerald_coin"     -> ModsItems.EMERALD_COIN.get();
            case "diamond_coin"     -> ModsItems.DIAMOND_COIN.get();
            case "netherite_coin"   -> ModsItems.NETHERITE_COIN.get();
            case "nether_star_coin" -> ModsItems.NETHER_STAR_COIN.get();
            default                 -> null;
        };
    }

    // -------------------------------------------------------------------------
    // Drops de coins à la mort d'un mob
    // -------------------------------------------------------------------------

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (event.getEntity() instanceof Player) return;

        String entityId = BuiltInRegistries.ENTITY_TYPE
                .getKey(event.getEntity().getType())
                .toString();

        var entity = event.getEntity();
        var rng = entity.getRandom();

        for (DropEntry entry : drops) {
            if (entry.entityId().equals(entityId) && rng.nextFloat() < entry.chance()) {
                ItemEntity itemEntity = new ItemEntity(
                        entity.level(),
                        entity.getX(), entity.getY(), entity.getZ(),
                        new ItemStack(entry.coinItem(), entry.count())
                );
                itemEntity.setDefaultPickUpDelay();
                event.getDrops().add(itemEntity);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Wandering Trader — injecté directement sur l'entité au spawn
    // Probabilités : copper→iron 100%, puis /2 à chaque palier
    // Plusieurs échanges peuvent coexister sur le même trader.
    // -------------------------------------------------------------------------

    @SubscribeEvent
    public static void onWandererSpawn(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof WanderingTrader trader)) return;

        var offers = trader.getOffers();

        // Anti-doublon au rechargement depuis la sauvegarde
        boolean alreadyHasCoinTrades = offers.stream().anyMatch(o ->
                o.getCostA().getItem() instanceof CoinItem
                || o.getResult().getItem() instanceof CoinItem
        );
        if (alreadyHasCoinTrades) return;

        var rng = trader.getRandom();

        // 100% — copper → iron
        offers.add(new MerchantOffer(
                new ItemCost(ModsItems.COPPER_COIN.get(), 10),
                new ItemStack(ModsItems.IRON_COIN.get()),
                5, 2, 0.0f
        ));

        // 50% — iron → gold
        if (rng.nextFloat() < 0.50f) {
            offers.add(new MerchantOffer(
                    new ItemCost(ModsItems.IRON_COIN.get(), 10),
                    new ItemStack(ModsItems.GOLD_COIN.get()),
                    5, 2, 0.0f
            ));
        }

        // 25% — gold → emerald
        if (rng.nextFloat() < 0.25f) {
            offers.add(new MerchantOffer(
                    new ItemCost(ModsItems.GOLD_COIN.get(), 10),
                    new ItemStack(ModsItems.EMERALD_COIN.get()),
                    3, 5, 0.0f
            ));
        }

        // 12.5% — emerald → diamond
        if (rng.nextFloat() < 0.125f) {
            offers.add(new MerchantOffer(
                    new ItemCost(ModsItems.EMERALD_COIN.get(), 10),
                    new ItemStack(ModsItems.DIAMOND_COIN.get()),
                    2, 10, 0.0f
            ));
        }

        // 6.25% — diamond → netherite
        if (rng.nextFloat() < 0.0625f) {
            offers.add(new MerchantOffer(
                    new ItemCost(ModsItems.DIAMOND_COIN.get(), 10),
                    new ItemStack(ModsItems.NETHERITE_COIN.get()),
                    1, 20, 0.0f
            ));
        }

        // 3.125% — netherite → nether_star
        if (rng.nextFloat() < 0.03125f) {
            offers.add(new MerchantOffer(
                    new ItemCost(ModsItems.NETHERITE_COIN.get(), 10),
                    new ItemStack(ModsItems.NETHER_STAR_COIN.get()),
                    1, 30, 0.0f
            ));
        }
    }
}
