package com.akeltroll.moneymod.client.screen;

import com.akeltroll.moneymod.menu.MoneyPouchMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MoneyPouchScreen extends AbstractContainerScreen<MoneyPouchMenu> {
    private static final ResourceLocation TEXTURE_9 =
            ResourceLocation.withDefaultNamespace("textures/gui/container/generic_54.png");
    private static final ResourceLocation TEXTURE_18 =
            ResourceLocation.withDefaultNamespace("textures/gui/container/generic_54.png");
    private static final ResourceLocation TEXTURE_27 =
            ResourceLocation.withDefaultNamespace("textures/gui/container/generic_54.png");

    private final ResourceLocation texture;
    private final int containerRows;

    public MoneyPouchScreen(MoneyPouchMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        
        int slots = menu.getPouchSlots();
        
        if (slots == 9) {
            this.texture = TEXTURE_9;
            this.containerRows = 1;
            this.imageHeight = 133;
            this.inventoryLabelY = this.imageHeight - 94; // Position pour 9 slots
        } else if (slots == 18) {
            this.texture = TEXTURE_18;
            this.containerRows = 2;
            this.imageHeight = 151;
            this.inventoryLabelY = this.imageHeight - 94; // Position pour 18 slots
        } else {
            this.texture = TEXTURE_27;
            this.containerRows = 3;
            this.imageHeight = 169;
            this.inventoryLabelY = this.imageHeight - 94; // Position pour 27 slots
        }
        
        this.imageWidth = 176;
    }

    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        
        // Dessiner le conteneur supérieur (slots de la bourse)
        graphics.blit(texture, x, y, 0, 0, this.imageWidth, this.containerRows * 18 + 17);
        
        // Dessiner l'inventaire du joueur en dessous
        graphics.blit(texture, x, y + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }

    
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}