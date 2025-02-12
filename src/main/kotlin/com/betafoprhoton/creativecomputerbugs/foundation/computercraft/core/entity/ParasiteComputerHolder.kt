package com.betafoprhoton.creativecomputerbugs.foundation.computercraft.core.entity

import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.entity.EntityAPIs
import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.core.AbstractBugComputerHolder
import com.betafoprhoton.creativecomputerbugs.foundation.item.bugs.AbstractBugItem.Companion.INFECTED_BLOCK_FLAG
import com.betafoprhoton.creativecomputerbugs.registy.CCBConfig
import dan200.computercraft.shared.computer.core.ComputerFamily
import dan200.computercraft.shared.computer.core.ServerComputer
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import kotlin.random.Random

class ParasiteComputerHolder(
    family: ComputerFamily,
    bugItem: ItemStack,
    id: Int,
    val entity: Entity
    ): AbstractBugComputerHolder(family, bugItem, id, entity.level()) {

    override fun tick() {
        super.tick()
        val level = entity.level()
        if ((1..10).random() <= 9) return
        level.addParticle(ParticleTypes.SOUL,
            entity.x,
            entity.eyeY,
            entity.z,
            (Random.nextDouble() * 2 - 1) * 0.2,
            (Random.nextDouble() * 2 - 1) * 0.3,
            (Random.nextDouble() * 2 - 1) * 0.2
        )
    }

    init {
        putMark()
    }

    override fun putMark() {
        entity.persistentData.putInt(INFECTED_BLOCK_FLAG, id)
    }

    override fun removeMark() {
        entity.persistentData.remove(INFECTED_BLOCK_FLAG)
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
            terminalWidth = CCBConfig.bugComputerTermWidth,
            terminalHeight = CCBConfig.bugComputerTermHeight,
            bugItem = getBug(),
            entity = entity
        )
    }

    override fun setChanged() {

    }

    override fun popResource() {
        Block.popResource(entity.level(), entity.blockPosition(), getBug())
    }
}