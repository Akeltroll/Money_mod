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
            this.inventoryLabelY = 38;  // 40 - 2
        } else if (slots == 18) {
            this.texture = TEXTURE_18;
            this.containerRows = 2;
            this.imageHeight = 151;
            this.inventoryLabelY = 56;  // 58 - 2
        } else {
            this.texture = TEXTURE_27;
            this.containerRows = 3;
            this.imageHeight = 169;
            this.inventoryLabelY = 74;  // 76 - 2
        }
        
        this.imageWidth = 176;
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        
        // Dessiner le conteneur supérieur (slots de la bourse)
        graphics.blit(texture, x, y, 0, 0, this.imageWidth, this.containerRows * 18 + 17);
        
        // Dessiner l'inventaire du joueur en dessous
        graphics.blit(texture, x, y + this.containerRows * 18 + 17, 0, 126, this.imageWidth, 96);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        // Dessiner le titre centré
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
        
        // Dessiner le label "Inventory" centré
        int invLabelX = (this.imageWidth - this.font.width(this.playerInventoryTitle)) / 2;
        graphics.drawString(this.font, this.playerInventoryTitle, invLabelX, this.inventoryLabelY, 4210752, false);
    }
    
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}