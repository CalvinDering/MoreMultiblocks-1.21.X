package de.bl4ckl1on.moremultiblocksmod.multi.module;

import de.bl4ckl1on.moremultiblocksmod.block.entity.ModularBlockEntity;
import de.bl4ckl1on.moremultiblocksmod.multi.Multiblock;
import de.bl4ckl1on.moremultiblocksmod.multi.entity.ModMultiblockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class MultiblockModule implements Module {

    private final Supplier<? extends Multiblock> multiblock;
    private List<BlockPos> positions = new ArrayList<>();
    private BlockState previousState;
    private boolean forRemoval;

    public MultiblockModule(Supplier<? extends Multiblock> multiblockSupplier) {
        this.multiblock = multiblockSupplier;
    }

    public Multiblock getMultiblock() {
        return this.multiblock.get();
    }

    public List<BlockPos> getPositions() {
        return positions;
    }
    public void setPositions(Collection<BlockPos> positions) {
        this.positions = positions.stream().toList();
    }
    public BlockState getPreviousState() {
        return this.previousState;
    }
    public void setPreviousState(BlockState previousState) {
        this.previousState = previousState;
    }

    public boolean isForRemoval() {
        return this.forRemoval;
    }
    public void setForRemoval(boolean forRemoval) {
        this.forRemoval = forRemoval;
    }

    @Override
    public void deserialize(ModularBlockEntity blockEntity, CompoundTag compoundTag) {
        ListTag compounds = compoundTag.getList("Positions", Tag.TAG_COMPOUND);
        for (Tag tag : compounds) {
            var compound = (CompoundTag) tag;
            this.positions.add(NbtUtils.readBlockPos(compound, "Positions").orElse(null));
        }
    }

    @Override
    public void serialize(ModularBlockEntity blockEntity, CompoundTag compoundTag) {
        var compounds = new ListTag();
        this.positions.stream().map(NbtUtils::writeBlockPos).forEach(compounds::add);
        compoundTag.put("Positions", compounds);
    }

    public void removeMultiblock(Level level, BlockPos excluded) {
        var positions = new ArrayList<>(this.positions);
        positions.remove(excluded);

        for (BlockPos position : positions) {
            level.getBlockEntity(position, ModMultiblockEntities.MULTIBLOCK_BE.get()).ifPresentOrElse((blockEntity) -> {
                this.setForRemoval(true);
                level.setBlock(position, blockEntity.getPreviousState(), Block.UPDATE_ALL);
            }, () -> {
                BlockEntity blockEntity = level.getBlockEntity(position);
                if(blockEntity instanceof ModularBlockEntity modularBlockEntity) {
                    modularBlockEntity.getModule(MultiblockModule.class).ifPresent(multiblockModule -> {
                        multiblockModule.forRemoval = true;
                        level.setBlock(position, getPreviousState(), Block.UPDATE_ALL);
                    });
                }
            });
        }
    }
}
