package de.bl4ckl1on.moremultiblocksmod.multi.entity;

import de.bl4ckl1on.moremultiblocksmod.MoreMultiblocksMod;
import de.bl4ckl1on.moremultiblocksmod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMultiblockEntities {

    public static final DeferredRegister<BlockEntityType<?>> MULTIBLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MoreMultiblocksMod.MOD_ID);

    public static final Supplier<BlockEntityType<MultiblockBlockEntity>> MULTIBLOCK_BE = MULTIBLOCK_ENTITY_TYPES.register(
            "multiblock_be",
            () -> BlockEntityType.Builder.of(
                    MultiblockBlockEntity::new,
                    ModBlocks.MULTIBLOCK.get()
            ).build(null));

    public static final Supplier<BlockEntityType<InfuserMultiblockEntity>> INFUSER_BE = MULTIBLOCK_ENTITY_TYPES.register(
            "infuser_be",
            () -> BlockEntityType.Builder.of(
                    InfuserMultiblockEntity::new,
                    ModBlocks.INFUSER.get()
            ).build(null));

    public static void register(IEventBus eventBus) { MULTIBLOCK_ENTITY_TYPES.register(eventBus); }

}
