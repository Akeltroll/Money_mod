package com.akeltroll.moneymod.item;

import com.akeltroll.moneymod.menu.MoneyPouchMenu;
import com.akeltroll.moneymod.menu.PouchMenuProvider;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.function.Supplier;

public class MoneyPouchItem extends Item implements ICurioItem {
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
        
        if (ModList.get().isLoaded("curios")) {
            tooltipComponents.add(Component.translatable("tooltip.moneymod.pouch.curios_compatible")
                .withStyle(style -> style.withColor(0xFFD700)));
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

        // Shift + clic droit = équiper dans le slot Curios (géré par canEquipFromUse)
        // Clic droit simple = ouvrir l'interface de la bourse
        if (hand == InteractionHand.MAIN_HAND && !player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                int slots = getSlots();
                player.openMenu(
                    new PouchMenuProvider(stack, slots, false,
                        Component.translatable("container.moneymod.money_pouch_" + tier.name().toLowerCase())),
                    buf -> {
                        buf.writeInt(slots);
                        buf.writeBoolean(false);
                    }
                );
            }
            return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
        }

        return InteractionResultHolder.pass(stack);
    }

    // Méthodes ICurioItem pour Curios
    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return slotContext.entity().isShiftKeyDown();
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (!slotContext.entity().level().isClientSide && slotContext.entity() instanceof Player player) {
            player.displayClientMessage(
                Component.translatable("message.moneymod.pouch_equipped"),
                true
            );
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (!slotContext.entity().level().isClientSide && slotContext.entity() instanceof Player player) {
            player.displayClientMessage(
                Component.translatable("message.moneymod.pouch_unequipped"),
                true
            );
        }
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if (slotContext == null) return false;
        String id = String.valueOf(slotContext.identifier());
        return id.equals("belt") || id.endsWith(":belt") || id.endsWith("/belt");
    }


}