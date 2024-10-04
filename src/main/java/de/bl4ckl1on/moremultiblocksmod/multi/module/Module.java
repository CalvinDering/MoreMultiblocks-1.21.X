package de.bl4ckl1on.moremultiblocksmod.multi.module;

import de.bl4ckl1on.moremultiblocksmod.block.entity.ModularBlockEntity;
import net.minecraft.nbt.CompoundTag;

public interface Module {

    void deserialize(ModularBlockEntity blockEntity, CompoundTag compoundTag);

    default void onLoad(ModularBlockEntity blockEntity) {}

    default void onRemoved(ModularBlockEntity blockEntity) {}

    void serialize(ModularBlockEntity blockEntity, CompoundTag nbt);

    default void tick(ModularBlockEntity blockEntity) {}
}
