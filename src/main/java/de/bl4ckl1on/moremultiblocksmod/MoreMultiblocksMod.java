package de.bl4ckl1on.moremultiblocksmod;

import de.bl4ckl1on.moremultiblocksmod.block.ModBlocks;
import de.bl4ckl1on.moremultiblocksmod.item.ModCreativeModeTabs;
import de.bl4ckl1on.moremultiblocksmod.item.ModItems;
import de.bl4ckl1on.moremultiblocksmod.multi.ModMultiblocks;
import de.bl4ckl1on.moremultiblocksmod.multi.entity.ModMultiblockEntities;
import de.bl4ckl1on.moremultiblocksmod.screen.ModMenuTypes;
import de.bl4ckl1on.moremultiblocksmod.screen.block.InfuserScreen;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MoreMultiblocksMod.MOD_ID)
public class MoreMultiblocksMod {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "moremultiblocksmod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public MoreMultiblocksMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        ModCreativeModeTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModMenuTypes.register(modEventBus);

        ModMultiblocks.register(modEventBus);
        ModMultiblockEntities.register(modEventBus);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }

        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModMenuTypes.INFUSER_MENU.get(), InfuserScreen::new);
        }

        @SubscribeEvent
        public static void registerRegistries(NewRegistryEvent event) {
            event.register(ModMultiblocks.MULTIBLOCK_REGISTRY);
        }
    }
}
