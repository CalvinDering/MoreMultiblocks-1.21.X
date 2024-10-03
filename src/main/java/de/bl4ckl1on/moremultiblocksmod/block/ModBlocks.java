package de.bl4ckl1on.moremultiblocksmod.block;

import de.bl4ckl1on.moremultiblocksmod.MoreMultiblocksMod;
import de.bl4ckl1on.moremultiblocksmod.item.ModItems;
import de.bl4ckl1on.moremultiblocksmod.multi.Multiblock;
import de.bl4ckl1on.moremultiblocksmod.multi.block.InfuserMultiblock;
import de.bl4ckl1on.moremultiblocksmod.multi.entity.InfuserMultiblockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MoreMultiblocksMod.MOD_ID);

    public static final DeferredBlock<Block> ENERGONITE_BLOCK = registerBlock("energonite_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> ZYTHERIUM_BLOCK = registerBlock("zytherium_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .strength(4f)
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<InfuserMultiblock> INFUSER = BLOCKS.register("infuser",
            () -> new InfuserMultiblock(BlockBehaviour.Properties.of()
                    .noLootTable()
                    .dynamicShape()
                    .noOcclusion()
                    .sound(SoundType.STONE)));


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) { BLOCKS.register(eventBus); }

}
