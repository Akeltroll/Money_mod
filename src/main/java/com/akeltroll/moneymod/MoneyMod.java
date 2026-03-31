package com.akeltroll.moneymod;

import com.akeltroll.moneymod.CoinDropHandler;
import com.akeltroll.moneymod.client.ModKeybindings;
import com.akeltroll.moneymod.compat.CuriosCompat;
import com.akeltroll.moneymod.item.ModsItems;
import com.akeltroll.moneymod.menu.ModMenuTypes;
import com.akeltroll.moneymod.client.screen.MoneyPouchScreen;
import com.akeltroll.moneymod.network.OpenPouchPacket;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(MoneyMod.MOD_ID)
public class MoneyMod {
    public static final String MOD_ID = "moneymod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public MoneyMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::registerPayloads);

        NeoForge.EVENT_BUS.register(this);

        ModsItems.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
            OpenPouchPacket.TYPE,
            OpenPouchPacket.STREAM_CODEC,
            OpenPouchPacket::handle
        );
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Money Mod common setup");
        NeoForge.EVENT_BUS.register(CoinDropHandler.class);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("curios")) {
            LOGGER.info("Curios detected, registering compatibility");
            CuriosCompat.sendImc(event);
            // Enregistre l'event pour ouvrir la pouch depuis le slot belt (clic droit main vide)
            NeoForge.EVENT_BUS.register(CuriosCompat.class);
        }
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            if (Config.enableCopperCoin) event.accept(ModsItems.COPPER_COIN);
            if (Config.enableIronCoin) event.accept(ModsItems.IRON_COIN);
            if (Config.enableGoldCoin) event.accept(ModsItems.GOLD_COIN);
            if (Config.enableEmeraldCoin) event.accept(ModsItems.EMERALD_COIN);
            if (Config.enableDiamondCoin) event.accept(ModsItems.DIAMOND_COIN);
            if (Config.enableNetheriteCoin) event.accept(ModsItems.NETHERITE_COIN);
            if (Config.enableNetherStarCoin) event.accept(ModsItems.NETHER_STAR_COIN);

            event.accept(ModsItems.BASIC_MONEY_POUCH);
            event.accept(ModsItems.ADVANCED_MONEY_POUCH);
            event.accept(ModsItems.FINAL_MONEY_POUCH);
        }
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.MONEY_POUCH.get(), MoneyPouchScreen::new);
        }

        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            ModKeybindings.register(event);
        }

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            NeoForge.EVENT_BUS.addListener(ModKeybindings::onKeyInput);
        }
    }
}