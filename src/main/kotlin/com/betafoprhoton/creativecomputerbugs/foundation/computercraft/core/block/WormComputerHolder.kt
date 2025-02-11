package com.betafoprhoton.creativecomputerbugs.foundation.computercraft.core.block

import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.block.BlockAPIs
import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.core.AbstractBugComputerHolder
import com.betafoprhoton.creativecomputerbugs.foundation.item.bugs.AbstractBugItem.Companion.INFECTED_BLOCK_FLAG
import com.betafoprhoton.creativecomputerbugs.registy.CCBConfig
import dan200.computercraft.shared.computer.core.ComputerFamily
import dan200.computercraft.shared.computer.core.ServerComputer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity

class WormComputerHolder(
    family: ComputerFamily,
    bugItem: ItemStack,
    id: Int,
    private val blockEntity: BlockEntity
) : AbstractBugComputerHolder(family, bugItem, id, blockEntity.level) {

    init {
        putMark()
    }

    override fun putMark() {
        blockEntity.persistentData.putInt(INFECTED_BLOCK_FLAG, id)
    }

    override fun removeMark() {
        blockEntity.persistentData.remove(INFECTED_BLOCK_FLAG)
    }

    override fun isUsableByPlayer(player: Player): Boolean {
        return true
    }

    override fun addAPIForComputer(computer: ServerComputer) {
        BlockAPIs.addAPI(computer, blockEntity)
    }

    override fun createComputer(id: Int, level: ServerLevel): ServerComputer {
        return WormComputer(
            level = level,
            position = blockEntity.blockPos,
            computerID = id,
            label = label,
            family = getFamily(),
            terminalWidth = CCBConfig.bugComputerTermWidth,
            terminalHeight = CCBConfig.bugComputerTermHeight,
            blockEntity = blockEntity,
            bugItem = getBug()
        )
    }

    override fun setChanged() {
        blockEntity.setChanged()
    }

    override fun popResource() {
        blockEntity.level?.let { Block.popResource(it, blockEntity.blockPos, getBug()) }
    }
}
