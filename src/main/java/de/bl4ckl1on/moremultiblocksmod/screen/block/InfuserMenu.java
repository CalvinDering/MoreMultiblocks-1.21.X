package de.bl4ckl1on.moremultiblocksmod.screen.block;

import de.bl4ckl1on.moremultiblocksmod.block.ModBlocks;
import de.bl4ckl1on.moremultiblocksmod.multi.entity.InfuserMultiblockEntity;
import de.bl4ckl1on.moremultiblocksmod.screen.BaseMenu;
import de.bl4ckl1on.moremultiblocksmod.screen.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;

public class InfuserMenu extends BaseMenu {

    public final InfuserMultiblockEntity blockEntity;

    public InfuserMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, inventory.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }

    public InfuserMenu(int containerId, Inventory inventory, BlockEntity blockEntity, ContainerData containerData) {
        super(ModMenuTypes.INFUSER_MENU.get(), containerId, inventory, containerData, blockEntity, ModBlocks.INFUSER.get(), 2);
        this.blockEntity = (InfuserMultiblockEntity) blockEntity;

        checkContainerDataCount(containerData, 2);

        this.addDataSlots(containerData);
    }

    public int getScaledProgress() {
        int progress = this.containerData.get(0);
        int maxProgress = this.containerData.get(1);
        int progressArrowSize = 26; // Height of arrow in pixel

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, ModBlocks.INFUSER.get());
    }
}
