package de.bl4ckl1on.moremultiblocksmod.multi;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.state.BlockState;
import java.util.List;

public abstract class Multiblock {

    private boolean isValidMultiblock;

    public boolean getValidMultiblock() { return isValidMultiblock; }
    public void setValidMultiblock(boolean value) { isValidMultiblock = value; }
    protected void checkIfMultiblockIsValid(List<BlockState> states) {}

    private final MultiblockInteraction multiblockInteraction;

    public Multiblock(Builder builder) { this.multiblockInteraction = builder.multiblockInteraction; }
    protected Multiblock(MultiblockInteraction multiblockInteraction) {
        this.multiblockInteraction = multiblockInteraction;
    }

    public MultiblockInteraction getMultiblockInteraction() { return this.multiblockInteraction; }

    public static class Builder {
        private MultiblockInteraction multiblockInteraction = ($0, $1, $2, $3, $5, $6) -> InteractionResult.PASS;

        public Builder multiblockInteraction(MultiblockInteraction multiblockInteraction) {
            this.multiblockInteraction = multiblockInteraction;
            return this;
        }
    }

}
