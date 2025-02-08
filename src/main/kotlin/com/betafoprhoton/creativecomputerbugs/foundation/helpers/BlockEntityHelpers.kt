package com.betafoprhoton.creativecomputerbugs.foundation.helpers;

import com.betafoprhoton.creativecomputerbugs.CCBMain;
import com.betafoprhoton.creativecomputerbugs.CCBMain.Companion.INFECTED_BLOCK_FLAG
import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.core.block.WormComputerHolder;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
fun getBugComputerHolder(blockEntity: BlockEntity): WormComputerHolder? {
    if (blockEntity.getPersistentData().contains(INFECTED_BLOCK_FLAG))
        return CCBMain.BUG_COMPUTER_HOLDER_REGISTER.getWormComputerHolder(blockEntity.getPersistentData().getInt(INFECTED_BLOCK_FLAG));
    return null
}
