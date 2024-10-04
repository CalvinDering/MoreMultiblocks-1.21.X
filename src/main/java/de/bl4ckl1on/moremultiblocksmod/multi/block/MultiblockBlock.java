package de.bl4ckl1on.moremultiblocksmod.multi.block;

import de.bl4ckl1on.moremultiblocksmod.block.entity.ModularBlockEntity;
import de.bl4ckl1on.moremultiblocksmod.multi.entity.ModMultiblockEntities;
import de.bl4ckl1on.moremultiblocksmod.multi.entity.MultiblockBlockEntity;
import de.bl4ckl1on.moremultiblocksmod.multi.module.MultiblockModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class MultiblockBlock extends Block implements EntityBlock {
    public MultiblockBlock(Properties properties) { super(properties); }

    @Override
    protected RenderShape getRenderShape(BlockState state) { return RenderShape.ENTITYBLOCK_ANIMATED; }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(!(blockEntity instanceof MultiblockBlockEntity multiblockBlockEntity) || multiblockBlockEntity.getController() == null) {
            return Shapes.empty();
        }

        BlockEntity controller = level.getBlockEntity(multiblockBlockEntity.getController());
        if(controller == null) { return Shapes.empty(); }

        return controller.getBlockState().getShape(level, pos, context).move(
                multiblockBlockEntity.getController().getX() - pos.getX(),
                multiblockBlockEntity.getController().getY() - pos.getY(),
                multiblockBlockEntity.getController().getZ() - pos.getZ());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return ModMultiblockEntities.MULTIBLOCK_BE.get().create(pos, state);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        MultiblockBlockEntity blockEntity = level.getBlockEntity(pos, ModMultiblockEntities.MULTIBLOCK_BE.get()).orElse(null);
        if(blockEntity == null) { return super.useWithoutItem(state, level, pos, player, hitResult); }

        if(blockEntity.getController() == null) { return super.useWithoutItem(state, level, pos, player, hitResult); }

        BlockEntity controller = level.getBlockEntity(blockEntity.getController());
        if(controller == null) { return super.useWithoutItem(state, level, pos, player, hitResult); }

        if(controller instanceof ModularBlockEntity modularBlockEntity) {
            MultiblockModule module = modularBlockEntity.getModule(MultiblockModule.class).orElse(null);
            if(module == null) { return super.useWithoutItem(state, level, pos, player, hitResult); }

            return module.getMultiblock().getMultiblockInteraction().use(state, level, blockEntity.getController(), player, hitResult, blockEntity.getController().subtract(pos));
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(state.is(newState.getBlock())) { return; }

        MultiblockBlockEntity multiblockBlockEntity = level.getBlockEntity(pos, ModMultiblockEntities.MULTIBLOCK_BE.get()).orElse(null);
        if(multiblockBlockEntity == null) { return; }

        if(!multiblockBlockEntity.isForRemoval() && multiblockBlockEntity.getController() != null) {
            BlockEntity controller = level.getBlockEntity(multiblockBlockEntity.getController());
            if(controller instanceof ModularBlockEntity modularBlockEntity) {
                MultiblockModule module = modularBlockEntity.getModule(MultiblockModule.class).orElseThrow(
                        () -> new IllegalStateException("Multiblock module not found"));
                multiblockBlockEntity.setForRemoval(true);
                module.removeMultiblock(level, pos);
            }
        }
    }
}
