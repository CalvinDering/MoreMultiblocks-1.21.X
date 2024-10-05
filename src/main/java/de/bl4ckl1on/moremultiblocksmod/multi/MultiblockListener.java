package de.bl4ckl1on.moremultiblocksmod.multi;

import de.bl4ckl1on.moremultiblocksmod.MoreMultiblocksMod;
import de.bl4ckl1on.moremultiblocksmod.block.ModBlocks;
import de.bl4ckl1on.moremultiblocksmod.block.entity.ModularBlockEntity;
import de.bl4ckl1on.moremultiblocksmod.multi.entity.ModMultiblockEntities;
import de.bl4ckl1on.moremultiblocksmod.multi.module.MultiblockModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@EventBusSubscriber(modid = MoreMultiblocksMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class MultiblockListener {


    @SubscribeEvent
    public static void blockPlace(BlockEvent.EntityPlaceEvent event) {

        if(!(event.getEntity() instanceof Player) || event.getLevel().isClientSide()) { return; }

        final LevelAccessor level = event.getLevel();
        final BlockPos position = event.getPos();
        final BlockState blockState = event.getPlacedBlock();

        handlePlayerBuildMultiblocksPlaced(event, level, position, blockState);
    }

    private static void handlePlayerBuildMultiblocksPlaced(BlockEvent.EntityPlaceEvent event, LevelAccessor level, BlockPos pos, BlockState blockState) {
        for (PlayerBuildMultiblock multiblock : ModMultiblocks.MULTIBLOCKS.getRegistry().get().stream()
                .filter(PlayerBuildMultiblock.class::isInstance).map(PlayerBuildMultiblock.class::cast)
                .filter(mblock -> !mblock.requiresActivation()).toList()) {

            if(!multiblock.isValid(blockState)) { continue; }

            long startTime = System.currentTimeMillis();
            BlockPattern.BlockPatternMatch match = findSmall(multiblock.getPatternMatcher(), level, pos);
            long endTime = System.currentTimeMillis();
            MoreMultiblocksMod.LOGGER.info("Took {}ms to {}find a match", endTime - startTime, match == null ? "not " : "");

            if (match == null) { continue; }

            final Pair<Vec3i, BlockState> controller = multiblock.getController();
            final BlockPos controllerPosition = match.getBlock(controller.getKey().getX(), controller.getKey().getY(), controller.getKey().getZ()).getPos();

            List<BlockPos> positions = setPlayerBuildMultiblockBlocks(level, multiblock, match, controllerPosition);

            BlockState currentControllerState = event.getLevel().getBlockState(controllerPosition);
            event.getLevel().setBlock(controllerPosition, controller.getValue(), Block.UPDATE_ALL);
            if(event.getLevel().getBlockEntity(controllerPosition) instanceof ModularBlockEntity modularBlockEntity) {
                MultiblockModule multiblockModule = modularBlockEntity.getModule(MultiblockModule.class).orElseThrow(
                        () -> new IllegalStateException("Controller does not contain a multiblock module!"));

                multiblockModule.setPositions(positions);
                multiblockModule.setPreviousState(currentControllerState);
            }

            break;
        }
    }

    private static @Nullable BlockPattern.BlockPatternMatch findSmall(BlockPattern pattern, LevelAccessor level, BlockPos pos) {
        final int patternWidth = pattern.getWidth();
        final int patternDepth = pattern.getDepth();
        final int patternHeight = pattern.getHeight();

        for (int x = -patternWidth; x < patternWidth; x++) {
            for (int y = -patternDepth; y < patternDepth; y++) {
                for (int z = -patternHeight; z < patternHeight; z++) {
                    final BlockPos offset = pos.offset(x, y, z);
                    final BlockPattern.BlockPatternMatch found = pattern.find(level, offset);

                    if (found != null) return found;
                }
            }
        }

        return null;
    }

    private static List<BlockPos> setPlayerBuildMultiblockBlocks(LevelAccessor level, PlayerBuildMultiblock multiblock, BlockPattern.BlockPatternMatch match, BlockPos controllerPosition) {
        List<BlockPos> positions = new ArrayList<>();

        final int multiBlockWidth = multiblock.getPatternMatcher().getWidth();
        final int multiBlockHeight = multiblock.getPatternMatcher().getDepth();
        final int multiBlockDepth = multiblock.getPatternMatcher().getHeight();
        for (int x = 0; x < multiBlockWidth; x++) {
            for (int y = 0; y < multiBlockHeight; y++) {
                for (int z = 0; z < multiBlockDepth; z++) {
                    BlockInWorld inWorld = match.getBlock(x, z, y);
                    BlockPos pos = inWorld.getPos();

                    if (!pos.equals(controllerPosition) && !multiblock.isIgnoredPosition(pos)) {
                        level.setBlock(pos, ModBlocks.MULTIBLOCK.get().defaultBlockState(), Block.UPDATE_ALL);
                        level.getBlockEntity(pos, ModMultiblockEntities.MULTIBLOCK_BE.get()).ifPresent(blockEntity -> {
                            blockEntity.setController(controllerPosition);
                            blockEntity.setPreviousState(inWorld.getState());
                            blockEntity.setForRemoval(false);
                        });

                        positions.add(pos);
                    } else if (pos.equals(controllerPosition)) {
                        positions.add(pos);
                    }
                }
            }
        }

        return positions;
    }

    @SubscribeEvent
    public static void itemUse(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState blockState = level.getBlockState(pos);
        ItemStack itemStack = event.getItemStack();

        activationMultiblockValidation(level, pos, blockState, itemStack);
    }

    private static void activationMultiblockValidation(Level level, BlockPos pos, BlockState blockState, ItemStack itemStack) {
        List<PlayerBuildMultiblock> multiblocks = ModMultiblocks.MULTIBLOCKS.getRegistry().get().stream()
                .filter(PlayerBuildMultiblock.class::isInstance).map(PlayerBuildMultiblock.class::cast)
                .filter(multiblock -> multiblock.requiresActivation() && multiblock.isValidItem(itemStack)).toList();

        if(multiblocks.isEmpty()) { return; }

        for (PlayerBuildMultiblock multiblock : multiblocks) {
            if(!multiblock.isValid(blockState)) { continue; }

            long startTime = System.currentTimeMillis();
            BlockPattern.BlockPatternMatch match = findSmall(multiblock.getPatternMatcher(), level, pos);
            long endTime = System.currentTimeMillis();
            MoreMultiblocksMod.LOGGER.info("Took {}ms to {}find a match", endTime - startTime, match == null ? "not " : "");

            if(match == null) { continue; }

            final Pair<Vec3i, BlockState> controller = multiblock.getController();
            final BlockPos controllerPosition = match.getBlock(controller.getKey().getX(), controller.getKey().getY(), controller.getKey().getZ()).getPos();

            var isValid = new AtomicBoolean(true);
            multiblock.getActivationPosition().ifPresent(activationPosition -> {
                final BlockPos blockPos = match.getBlock(activationPosition.getX(), activationPosition.getY(), activationPosition.getZ()).getPos();

                if(!blockPos.equals(pos)) { isValid.set(false); }
            });

            if(!isValid.get()) { continue; }

            List<BlockPos> positions = setPlayerBuildMultiblockBlocks(level, multiblock, match, controllerPosition);

            BlockState currentControllerState = level.getBlockState(controllerPosition);
            level.setBlock(controllerPosition, controller.getValue(), Block.UPDATE_ALL);
            if(level.getBlockEntity(controllerPosition) instanceof ModularBlockEntity modularBlockEntity) {
                MultiblockModule multiblockModule = modularBlockEntity.getModule(MultiblockModule.class).orElseThrow(
                        () -> new IllegalStateException("Controller does not contain a multiblock module!"));

                multiblockModule.setPositions(positions);
                multiblockModule.setPreviousState(currentControllerState);
            }

            break;
        }

    }
}
