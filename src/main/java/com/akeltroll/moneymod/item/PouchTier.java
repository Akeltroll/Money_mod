package com.akeltroll.moneymod.item;

public enum PouchTier {
    BASIC(9),
    ADVANCED(18),
    FINAL(27);

    private final int slots;

    PouchTier(int slots) {
        this.slots = slots;
    }

    public int getSlots() {
        return slots;
    }
}