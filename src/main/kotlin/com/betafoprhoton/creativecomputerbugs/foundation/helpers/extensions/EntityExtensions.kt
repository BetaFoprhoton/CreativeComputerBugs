package com.betafoprhoton.creativecomputerbugs.foundation.helpers.extensions

import com.betafoprhoton.creativecomputerbugs.CCBMain
import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.core.entity.ParasiteComputerHolder
import com.betafoprhoton.creativecomputerbugs.foundation.item.bugs.AbstractBugItem.Companion.INFECTED_BLOCK_FLAG
import net.minecraft.world.entity.Entity

fun Entity.getBugComputerHolder(): ParasiteComputerHolder? {
    if (this.persistentData.contains(INFECTED_BLOCK_FLAG))
        return CCBMain.BUG_COMPUTER_HOLDER_REGISTER.getParasiteComputerHolder(this.persistentData.getInt(INFECTED_BLOCK_FLAG))
    return null
}