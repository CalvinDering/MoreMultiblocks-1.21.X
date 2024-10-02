package de.bl4ckl1on.moremultiblocksmod.item;

import de.bl4ckl1on.moremultiblocksmod.MoreMultiblocksMod;
import de.bl4ckl1on.moremultiblocksmod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MoreMultiblocksMod.MOD_ID);

    public static final Supplier<CreativeModeTab> MOREMULTIBLOCKS_ITEMS_TAB = CREATIVE_MODE_TAB.register("moremultiblocks_items_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.ENERGONITE.get()))
                    .title(Component.translatable("creativetab.moremultiblocksmod.moremultiblocks_items"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.ENERGONITE);
                        output.accept(ModItems.ZYTHERIUM);
                    }).build());

    public static final Supplier<CreativeModeTab> MOREMULTIBLOCKS_BLOCK_TAB = CREATIVE_MODE_TAB.register("moremultiblocks_blocks_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.ENERGONITE_BLOCK.get()))
                    .withTabsBefore(ResourceLocation.fromNamespaceAndPath(MoreMultiblocksMod.MOD_ID, "moremultiblocks_items_tab"))
                    .title(Component.translatable("creativetab.moremultiblocksmod.moremultiblocks_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.ENERGONITE_BLOCK);
                        output.accept(ModBlocks.ZYTHERIUM_BLOCK);
                    }).build());

    public static void register(IEventBus eventBus) { CREATIVE_MODE_TAB.register(eventBus); }

}
