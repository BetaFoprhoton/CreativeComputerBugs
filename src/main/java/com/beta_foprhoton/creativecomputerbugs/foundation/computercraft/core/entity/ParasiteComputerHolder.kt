package com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.core.entity

import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.entity.EntityAPIs
import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.core.AbstractBugComputerHolder
import com.beta_foprhoton.creativecomputerbugs.foundation.item.bugs.AbstractBugItem.Companion.INFECTED_BLOCK_FLAG
import dan200.computercraft.api.pocket.IPocketUpgrade
import dan200.computercraft.api.upgrades.UpgradeData
import dan200.computercraft.shared.computer.core.ComputerFamily
import dan200.computercraft.shared.computer.core.ServerComputer
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block

class ParasiteComputerHolder(
    family: ComputerFamily,
    bugItem: ItemStack,
    upgrade: UpgradeData<IPocketUpgrade>?,
    id: Int,
    private val entity: Entity
    ): AbstractBugComputerHolder(family, bugItem, upgrade, id) {

    override fun tick() {
        super.tick()
        val level = entity.level()
        level.addParticle(ParticleTypes.SOUL, entity.x, entity.y, entity.z, level.random.nextDouble(), level.random.nextDouble(), level.random.nextDouble())
    }

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

    override fun addAPIForComputer(computer: ServerComputer) {
        EntityAPIs.addAPI(computer, entity)
    }

    override fun createComputer(id: Int, level: ServerLevel): ServerComputer {
        return ParasiteComputer(
            level = level,
            position = entity.blockPosition(),
            computerID = id,
            label = label,
            family = getFamily(),
            terminalWidth = 1,
            terminalHeight = 1,
            bugItem = getBugItem(),
            entity = entity
        )
    }

    override fun setChanged() {

    }

    override fun popResource() {
        Block.popResource(entity.level(), entity.blockPosition(), getBugItem())
    }
}