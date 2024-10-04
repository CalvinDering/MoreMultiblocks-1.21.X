package de.bl4ckl1on.moremultiblocksmod.block.entity;

import cpw.mods.util.Lazy;
import de.bl4ckl1on.moremultiblocksmod.multi.module.Module;
import de.bl4ckl1on.moremultiblocksmod.multi.module.ModuleList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ModularBlockEntity extends TickableBlockEntity {

    private boolean multiblockHasChanged;
    protected final ModuleList modules = new ModuleList();

    public ModularBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    protected <T extends Module> T addModule(T module) {
        this.modules.add(module);
        return module;
    }

    public <T extends Module> Optional<T> getModule(Class<T> moduleClass) {
        return this.modules.stream().filter(moduleClass::isInstance).map(it -> (T) it).findFirst();
    }

    public <T extends Module> boolean hasModule(Class<T> moduleClass) {
        return this.modules.stream().anyMatch(moduleClass::isInstance);
    }

    @Override
    public void invalidateCapabilities() {
        super.invalidateCapabilities();
        this.modules.invalidate();
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.modules.deserialize(this, tag);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.modules.onLoad(this);

        if(this.level == null) { return; }

        if(this.level.isClientSide()) {
            //ModMessages.sendToServer(new SClientBlockEntityLoadPacket(this.worldPosition));
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.modules.onRemoved(this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        this.modules.serialize(this, tag);
    }

    protected List<Consumer<CompoundTag>> getWriteSyncData() {
        return this.modules.stream().map(
                module -> (Consumer<CompoundTag>) tag -> modules.serialize(this, tag)).collect(Collectors.toList());
    }

    protected List<Consumer<CompoundTag>> getReadSyncData() {
        return this.modules.stream().map(
                module -> (Consumer<CompoundTag>) tag -> modules.deserialize(this, tag)).collect(Collectors.toList());
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        getWriteSyncData().forEach(writer -> writer.accept(tag));
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        if(getWriteSyncData().isEmpty()) { return super.getUpdatePacket(); }

        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        if(getReadSyncData().isEmpty()) { return; }

        CompoundTag tag = pkt.getTag();
        getReadSyncData().forEach(reader -> reader.accept(tag));
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        if(getReadSyncData().isEmpty()) { return; }

        getReadSyncData().forEach(reader -> reader.accept(tag));
    }

    public void setMultiblockHasChanged(boolean multiblockHasChanged) { this.multiblockHasChanged = multiblockHasChanged; }
    public boolean getMultiblockHasChanged() { return multiblockHasChanged; }
}
