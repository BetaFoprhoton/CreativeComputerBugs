package com.beta_foprhoton.creativecomputerbugs.foundation.helpers;

import com.beta_foprhoton.creativecomputerbugs.CCBMain;
import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.core.block.WormComputerHolder;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;

import static com.beta_foprhoton.creativecomputerbugs.CCBMain.INFECTED_BLOCK_FLAG;

public class BlockEntityHelpers {

    @Nullable
    public static WormComputerHolder getBugComputerHolder(BlockEntity blockEntity) {
        if (blockEntity.getPersistentData().contains(INFECTED_BLOCK_FLAG))
            return CCBMain.BUG_COMPUTER_HOLDER_REGISTER.getWormComputerHolder(blockEntity.getPersistentData().getInt(INFECTED_BLOCK_FLAG));
        return null;
    }
}
