package de.bl4ckl1on.moremultiblocksmod.multi.entity;

import de.bl4ckl1on.moremultiblocksmod.block.entity.ModularBlockEntity;
import de.bl4ckl1on.moremultiblocksmod.multi.ModMultiblocks;
import de.bl4ckl1on.moremultiblocksmod.multi.module.MultiblockModule;
import de.bl4ckl1on.moremultiblocksmod.screen.block.InfuserMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class InfuserMultiblockEntity extends ModularBlockEntity implements MenuProvider {

    private static final Component TITLE = Component.translatable("menu.title.moremultiblocksmod.infuser_menu");

    public final MultiblockModule multiblockModule;

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 80;
    private final int slotAmount = 2;
    private static final int INPUT_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    public InfuserMultiblockEntity(BlockPos pos, BlockState state) {
        super(ModMultiblockEntities.INFUSER_BE.get(), pos, state);
        this.multiblockModule = addModule(new MultiblockModule(ModMultiblocks.INFUSER));
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> InfuserMultiblockEntity.this.progress;
                    case 1 -> InfuserMultiblockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> InfuserMultiblockEntity.this.progress = value;
                    case 1 -> InfuserMultiblockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return slotAmount;
            }
        };
    }


    @Override
    public Component getDisplayName() { return TITLE; }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new InfuserMenu(containerId, playerInventory, this, this.data);
    }

    public void dropContents() {
        /*SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);*/
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, pos, state);

            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private boolean hasRecipe() {
        return false;
    }

    private void craftItem() {

    }

    private void resetProgress() {
        progress = 0;
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

}
