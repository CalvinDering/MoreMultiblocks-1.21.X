package de.bl4ckl1on.moremultiblocksmod.multi;

import de.bl4ckl1on.moremultiblocksmod.MoreMultiblocksMod;
import de.bl4ckl1on.moremultiblocksmod.block.ModBlocks;
import de.bl4ckl1on.moremultiblocksmod.multi.block.InfuserMultiblock;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class ModMultiblocks {

    public static final ResourceKey MULTIBLOCK_RESOURCE_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("moremultiblocksmod", "multiblocks"));
    public static final DeferredRegister<Multiblock> MULTIBLOCKS = DeferredRegister.create(MULTIBLOCK_RESOURCE_KEY, MoreMultiblocksMod.MOD_ID);

    public static final DeferredHolder<Multiblock, PlayerBuildMultiblock> INFUSER = MULTIBLOCKS.register("infuser",
            () -> PlayerBuildMultiblock.Builder.start()
                    .aisle("XX", "XX")
                    .aisle("AA", "AA")
                    .where('X', BlockStatePredicate.forBlock(Blocks.COAL_BLOCK))
                    .where('A', BlockStatePredicate.forBlock(ModBlocks.ENERGONITE_BLOCK.get()))
                    .finish()
                    .activationItem(Ingredient.of(Items.GLASS_BOTTLE))
                    .controller(0, 0, 0, ModBlocks.INFUSER.get().defaultBlockState())
                    .multiblockInteraction((state, level, blockPos, player, hitResult, offsetFromMaster) -> InfuserMultiblock.use(state, level, blockPos, player, hitResult))
                    .build());

    public static final Registry MULTIBLOCK_REGISTRY = new RegistryBuilder<>(MULTIBLOCK_RESOURCE_KEY).sync(true).create();

    public static void register(IEventBus eventBus) { MULTIBLOCKS.register(eventBus); }

}
