package com.beta_foprhoton.creativecomputerbugs.foundation.helpers.extensions

import com.beta_foprhoton.creativecomputerbugs.CCBMain
import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.core.block.WormComputerHolder
import com.beta_foprhoton.creativecomputerbugs.foundation.item.bugs.AbstractBugItem.Companion.INFECTED_BLOCK_FLAG
import net.minecraft.world.level.block.entity.BlockEntity

object BlockEntityExtensions {
    fun BlockEntity.getBugComputerHolder(): WormComputerHolder? {
        if (this.persistentData.contains(INFECTED_BLOCK_FLAG))
            return CCBMain.BUG_COMPUTER_HOLDER_REGISTER.getWormComputerHolder(this.persistentData.getInt(INFECTED_BLOCK_FLAG))
        return null
    }

    fun BlockEntity.isAPISupported(): Boolean {
        CCBMain.BLOCK_API_REGISTRY[this.javaClass] ?: return false
        return true
    }
}
