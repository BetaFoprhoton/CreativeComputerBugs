package com.betafoprhoton.creativecomputerbugs.foundation.computercraft.core.entity

import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.core.AbstractBugComputer
import dan200.computercraft.shared.computer.core.ComputerFamily
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class ParasiteComputer(
    level: ServerLevel?, position: BlockPos?, computerID: Int, label: String?,
    family: ComputerFamily?, terminalWidth: Int, terminalHeight: Int, bugItem: ItemStack,
    private val entity: Entity
) : AbstractBugComputer(level, position,
    computerID,
    label, family, terminalWidth, terminalHeight, bugItem
) {
    override fun getNewBlockPos(): BlockPos {
        return entity.blockPosition()
    }

    override fun getNewLevel(): Level {
        return entity.level()
    }

    override fun getEntity(): Entity {
        return entity
    }

}