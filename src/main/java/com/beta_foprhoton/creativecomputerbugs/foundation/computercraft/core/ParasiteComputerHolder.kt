package com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.core

import com.beta_foprhoton.creativecomputerbugs.foundation.item.bugs.AbstractBugItem.Companion.INFECTED_BLOCK_FLAG
import dan200.computercraft.api.pocket.IPocketUpgrade
import dan200.computercraft.api.upgrades.UpgradeData
import dan200.computercraft.shared.computer.core.ComputerFamily
import dan200.computercraft.shared.computer.core.ServerComputer
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class ParasiteComputerHolder(
    family: ComputerFamily,
    bugItem: ItemStack,
    upgrade: UpgradeData<IPocketUpgrade>,
    id: Int,
    private val entity: Entity
    ): AbstractBugComputerHolder(family, bugItem, upgrade, id) {

    override fun putMark() {
        entity.persistentData.putInt(INFECTED_BLOCK_FLAG, id)
    }

    override fun removeMark() {
        entity.persistentData.remove(INFECTED_BLOCK_FLAG)
    }

    override fun getLevel(): Level {
        return entity.level()
    }

    override fun isUsableByPlayer(player: Player): Boolean {
        return true
    }

    override fun getDirection(): Direction {
        return entity.direction
    }

    override fun addAPIForComputer(computer: ServerComputer) {
        if (entity is LivingEntity)

    }

    override fun createComputer(id: Int): ServerComputer {
        return
    }

    override fun setChanged() {
        TODO("Not yet implemented")
    }

    override fun popResource() {
        TODO("Not yet implemented")
    }
}