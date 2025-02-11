package com.betafoprhoton.creativecomputerbugs.foundation.computercraft.core.block

import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.core.AbstractBugComputer
import dan200.computercraft.shared.computer.core.ComputerFamily
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity

class WormComputer(
    level: ServerLevel?, position: BlockPos?, computerID: Int, label: String?,
    family: ComputerFamily?, terminalWidth: Int, terminalHeight: Int, bugItem: ItemStack,
    private val blockEntity: BlockEntity
) : AbstractBugComputer(level, position, computerID,
    label, family, terminalWidth, terminalHeight, bugItem
) {
    override fun getNewBlockPos(): BlockPos {
        return blockEntity.blockPos
    }

    override fun getNewLevel(): Level? {
        return blockEntity.level
    }

    override fun getEntity(): Entity? {
        return null
    }

}
