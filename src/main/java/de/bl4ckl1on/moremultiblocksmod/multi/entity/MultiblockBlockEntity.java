package de.bl4ckl1on.moremultiblocksmod.multi.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MultiblockBlockEntity extends BlockEntity {

    private BlockPos controller;
    private BlockState previousState;
    private boolean forRemoval;

    public MultiblockBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModMultiblockEntities.MULTIBLOCK_BE.get(), pos, blockState);
    }

    public BlockPos getController() { return this.controller; }
    public void setController(BlockPos controller) { this.controller = controller; }

    public BlockState getPreviousState() { return this.previousState; }
    public void setPreviousState(BlockState previousState) { this.previousState = previousState; }

    public boolean isForRemoval() { return this.forRemoval; }
    public void setForRemoval(boolean forRemoval) { this.forRemoval = forRemoval; }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.controller = NbtUtils.readBlockPos(tag, "controller").orElse(null);
        this.previousState = NbtUtils.readBlockState(level.holderLookup(Registries.BLOCK), tag.getCompound("previousState"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("controller", NbtUtils.writeBlockPos(this.controller));
        tag.put("previousState", NbtUtils.writeBlockState(this.previousState));
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if(level == null) {
            return;
        }

        if(level.isClientSide()) {
            this.level.setBlockAndUpdate(this.worldPosition, getBlockState());
        }
    }
}
