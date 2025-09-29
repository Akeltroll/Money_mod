package com.akeltroll.moneymod.item;

public enum CoinType {
    COPPER(1),
    IRON(10),
    GOLD(100),
    EMERALD(1000),
    DIAMOND(10000),
    NETHERITE(100000),
    NETHER_STAR(1000000);

    private final int value;

    CoinType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}