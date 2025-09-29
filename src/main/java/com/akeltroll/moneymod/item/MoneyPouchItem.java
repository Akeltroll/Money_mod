package com.akeltroll.moneymod.item;

import com.akeltroll.moneymod.menu.MoneyPouchMenu;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Supplier;

public class MoneyPouchItem extends Item {
    private final Supplier<Integer> slotsSupplier;
    private final PouchTier tier;

    public MoneyPouchItem(Properties properties, Supplier<Integer> slotsSupplier, PouchTier tier) {
        super(properties.stacksTo(1));
        this.slotsSupplier = slotsSupplier;
        this.tier = tier;
    }

    public int getSlots() {
        return slotsSupplier.get();
    }

    public PouchTier getTier() {
        return tier;
    }


    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("tooltip.moneymod.pouch.capacity", getSlots()));
        
        int totalValue = calculateTotalValue(stack, context);
        if (totalValue > 0) {
            tooltipComponents.add(Component.translatable("tooltip.moneymod.pouch.total_value", totalValue));
        }
        
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    private int calculateTotalValue(ItemStack pouchStack, Item.TooltipContext context) {
        CustomData customData = pouchStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        
        if (!customData.contains("Inventory")) {
            return 0;
        }
        
        CompoundTag tag = customData.copyTag();
        
        int total = 0;
        ListTag items = tag.getCompound("Inventory").getList("Items", 10);
        
        for (int i = 0; i < items.size(); i++) {
            CompoundTag itemTag = items.getCompound(i);
            ItemStack itemStack = ItemStack.parseOptional(context.registries(), itemTag);
            
            if (itemStack.getItem() instanceof CoinItem coinItem) {
                total += coinItem.getCoinType().getValue() * itemStack.getCount();
            }
        }
        
        return total;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        
        if (!level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            player.openMenu(new SimpleMenuProvider(
                (id, inv, p) -> new MoneyPouchMenu(id, inv, stack, getSlots()),
                Component.translatable("container.moneymod.money_pouch_" + tier.name().toLowerCase())
            ));
            return InteractionResultHolder.success(stack);
        }
        
        return InteractionResultHolder.pass(stack);
    }
}