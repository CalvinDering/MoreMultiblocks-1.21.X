package de.bl4ckl1on.moremultiblocksmod.multi;

import de.bl4ckl1on.moremultiblocksmod.MoreMultiblocksMod;
import de.bl4ckl1on.moremultiblocksmod.block.ModBlocks;
import de.bl4ckl1on.moremultiblocksmod.multi.block.InfuserMultiblock;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMultiblocks {

    public static final ResourceKey MULTIBLOCK_RESOURCE_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("moremultiblocksmod", "multiblocks"));
    public static final DeferredRegister<Multiblock> MULTIBLOCKS = DeferredRegister.create(MULTIBLOCK_RESOURCE_KEY, MoreMultiblocksMod.MOD_ID);

    public static final DeferredHolder<Multiblock, PlayerBuildMultiblock> INFUSER = MULTIBLOCKS.register("infuser",
            () -> PlayerBuildMultiblock.Builder.start()
                    //.aisle("XXX","XXX", "XXX")
                    //.aisle("XXX","XAX", "XXX")
                    //.aisle("XXX","XXX", "XXX")
                    .aisle("X")
                    .aisle("A")
                    .where('X', BlockStatePredicate.forBlock(Blocks.COAL_BLOCK))
                    .where('A', BlockStatePredicate.forBlock(ModBlocks.ENERGONITE_BLOCK.get()))
                    .finish()
                    .activationItem(Ingredient.of(Items.GLASS_BOTTLE))
                    .controller(0, 1, 0, ModBlocks.INFUSER.get().defaultBlockState())
                    //.multiblockInteraction(($0, level, blockPos, player, $1, $2, $3) -> InfuserMultiblock.use(level, blockPos, player))
                    .build());


    public static void register(IEventBus eventBus) { MULTIBLOCKS.register(eventBus); }

}
