package com.akeltroll.moneymod.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class CoinItem extends Item {
    private final CoinType coinType;

    public CoinItem(Properties properties, CoinType coinType) {
        super(properties);
        this.coinType = coinType;
    }

    public CoinType getCoinType() {
        return coinType;
    }

    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.moneymod.coin.value", coinType.getValue()));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}