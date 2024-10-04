package de.bl4ckl1on.moremultiblocksmod.multi.module;

import de.bl4ckl1on.moremultiblocksmod.block.entity.ModularBlockEntity;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.Collection;

public class ModuleList extends ArrayList<Module> {

    @Override
    public boolean add(Module module) {
        if(alreadyContainsModule(module)) {
            return false;
        }
        return super.add(module);
    }

    @Override
    public void add(int index, Module element) {
        throw new UnsupportedOperationException("Can not add a module directly to the module list!");
    }

    @Override
    public boolean addAll(Collection<? extends Module>c) {
        throw new UnsupportedOperationException("Can not add a module directly to the module list!");
    }

    @Override
    public boolean addAll(int index, Collection<? extends Module>c) {
        throw new UnsupportedOperationException("Can not add a module directly to the module list!");
    }

    @Override
    public Module set(int index, Module element) {
        throw new UnsupportedOperationException("Can not add a module directly to the module list!");
    }

    public boolean alreadyContainsModule(Module module) { return stream().anyMatch(m -> m.getClass().isInstance(module)); }

    public void deserialize(ModularBlockEntity blockEntity, CompoundTag nbt) {
        forEach(module -> module.deserialize(blockEntity, nbt));
    }

    public void invalidate() {

    }

    public void onLoad(ModularBlockEntity modularBlockEntity) {forEach(module -> module.onLoad(modularBlockEntity)); }

    public void onRemoved(ModularBlockEntity modularBlockEntity) { forEach(module -> module.onRemoved(modularBlockEntity)); }

    public void serialize(ModularBlockEntity modularBlockEntity, CompoundTag tag) { forEach(module -> module.serialize(modularBlockEntity, tag));}

    public void tick(ModularBlockEntity modularBlockEntity) { forEach(module -> module.tick(modularBlockEntity)); }
}
