package com.betafoprhoton.creativecomputerbugs.registy

import com.betafoprhoton.creativecomputerbugs.CCBMain.Companion.LOGGER
import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.core.block.WormComputerHolder
import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.core.entity.ParasiteComputerHolder
import com.betafoprhoton.creativecomputerbugs.foundation.item.bugs.AbstractBugItem.Companion.INFECTED_FLAG
import dan200.computercraft.shared.computer.core.ComputerFamily
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity

class BugComputerHolderRegister(private var idCounter: Int = 0) {
    private val wormRegistry = HashMap<Int , WormComputerHolder>()
    private val parasiteRegistry = HashMap<Int , ParasiteComputerHolder>()

    companion object {
        private val INSTANCE = BugComputerHolderRegister()

        /***
         * Creates a new worm computer holder for a block entity, returns null if there is already a holder or the block entity is not infectable.
         * @return WormComputerHolder
         */
        fun createWormComputerHolder(infectBlockEntity: BlockEntity, bugItem: ItemStack, family: ComputerFamily): WormComputerHolder? =
            INSTANCE.createWormComputerHolder(infectBlockEntity, bugItem, family)

        /***
         * Creates a new parasite computer holder for an entity, returns null if there is already a holder or the entity is not infectable.
         * @return ParasiteComputerHolder
         */
        fun createParasiteComputerHolder(entity: Entity, bugItem: ItemStack, family: ComputerFamily): ParasiteComputerHolder? =
            INSTANCE.createParasiteComputerHolder(entity, bugItem, family)

        /***
         * Returns the worm computer holder of a block entity, returns null if there is no holder.
         * @return WormComputerHolder?
         */
        fun BlockEntity.getBugComputerHolder(): WormComputerHolder? {
            if (this.persistentData.contains(INFECTED_FLAG))
                return INSTANCE.getWormComputerHolder(this.persistentData.getInt(INFECTED_FLAG))
            return null
        }

        /***
         * Returns the parasite computer holder of a block entity, returns null if there is no holder.
         * @return ParasiteComputerHolder?
         */
        fun Entity.getBugComputerHolder(): ParasiteComputerHolder? {
            if (this.persistentData.contains(INFECTED_FLAG))
                return INSTANCE.getParasiteComputerHolder(this.persistentData.getInt(INFECTED_FLAG))
            return null
        }

        fun tick() = INSTANCE.tick()

        fun remove(id: Int) = INSTANCE.remove(id)

    }

    private fun getWormComputerHolder(id: Int): WormComputerHolder? {
        return wormRegistry[id]
    }

    private fun getParasiteComputerHolder(id: Int): ParasiteComputerHolder? {
        return parasiteRegistry[id]
    }
    
    fun tick() {
        wormRegistry.forEach { it.value.tick() }
        parasiteRegistry.forEach { it.value.tick() }
    }

    private fun createWormComputerHolder(infectBlockEntity: BlockEntity, bugItem: ItemStack, family: ComputerFamily): WormComputerHolder? {
        val holderOld = infectBlockEntity.getBugComputerHolder()
        if (holderOld != null) return null
        val holder = WormComputerHolder(
            family = family,
            bugItem = bugItem,
            id = idCounter,
            blockEntity = infectBlockEntity
        )
        wormRegistry[holder.id] = holder
        idCounter ++
        LOGGER.debug("A block bug computer has registered at pos: {}. Holder ID: {}.", infectBlockEntity.blockPos, holder.id)
        return holder
    }

    private fun createParasiteComputerHolder(entity: Entity, bugItem: ItemStack, family: ComputerFamily): ParasiteComputerHolder? {
        val holderOld = entity.getBugComputerHolder()
        if (holderOld != null) return null
        val holder = ParasiteComputerHolder(
            family = family,
            bugItem = bugItem,
            id = idCounter,
            entity = entity
        )
        parasiteRegistry[holder.id] = holder
        idCounter ++
        LOGGER.debug("A entity bug computer has registered at entity: {}. Holder ID: {}.", entity.uuid, holder.id)
        return holder
    }

    fun remove(id: Int) {
        val holder1 = wormRegistry[id]
        if (holder1 != null) {
            holder1.unload()
            wormRegistry.remove(id)
            holder1.popResource()
            LOGGER.debug("A block bug computer has been removed. Holder ID: {}.", holder1.id)
        }
        val holder2 = parasiteRegistry[id]
        if (holder2 != null) {
            holder2.unload()
            parasiteRegistry.remove(id)
            holder2.popResource()
            LOGGER.debug("A entity bug computer has been removed. Holder ID: {}.", holder2.id)
        }
    }
}