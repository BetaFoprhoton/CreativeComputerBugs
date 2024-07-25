package com.beta_foprhoton.creativecomputerbugs.registy;

import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.core.AbstractBugComputerHolder
import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.core.block.WormComputerHolder
import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.core.entity.ParasiteComputerHolder
import com.beta_foprhoton.creativecomputerbugs.foundation.helpers.extensions.BlockEntityExtensions.getBugComputerHolder
import com.beta_foprhoton.creativecomputerbugs.foundation.helpers.extensions.EntityExtensions.getBugComputerHolder
import dan200.computercraft.api.pocket.IPocketUpgrade
import dan200.computercraft.api.upgrades.UpgradeData
import dan200.computercraft.shared.computer.core.ComputerFamily
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.entity.BlockEntity

class BugComputerHolderRegister(private var idCounter: Int) {
    private val wormRegistry = HashMap<Int , WormComputerHolder>()
    private val parasiteRegistry = HashMap<Int , ParasiteComputerHolder>()

    fun getWormComputerHolder(id: Int): WormComputerHolder? {
        return wormRegistry[id]
    }

    fun getParasiteComputerHolder(id: Int): ParasiteComputerHolder? {
        return parasiteRegistry[id]
    }
    
    fun tick() {
        wormRegistry.forEach { it.value.tick() }
        parasiteRegistry.forEach { it.value.tick() }
    }

    fun createWormComputerHolder(infectBlockEntity: BlockEntity, upgrade: UpgradeData<IPocketUpgrade>?, family: ComputerFamily): WormComputerHolder? {
        val holderOld = infectBlockEntity.getBugComputerHolder()
        if (holderOld != null) return null
        val holder = WormComputerHolder(
            family = family,
            bugItem = CCBItems.BUG_WORM.asStack(),
            upgrade = upgrade,
            id = idCounter,
            blockEntity = infectBlockEntity
        )
        wormRegistry[holder.id] = holder
        idCounter ++
        return holder
    }

    fun createParasiteComputerHolder(entity: Entity, upgrade: UpgradeData<IPocketUpgrade>?, family: ComputerFamily): ParasiteComputerHolder? {
        val holderOld = entity.getBugComputerHolder()
        if (holderOld != null) return null
        val holder = ParasiteComputerHolder(
            family = family,
            bugItem = CCBItems.BUG_WORM.asStack(),
            upgrade = upgrade,
            id = idCounter,
            entity = entity
        )
        parasiteRegistry[holder.id] = holder
        idCounter ++
        return holder
    }

    fun remove(id: Int) {
        val holder1 = wormRegistry[id]
        if (holder1 != null) {
            holder1.unload()
            wormRegistry.remove(id)
            holder1.popResource()
        }
        val holder2 = parasiteRegistry[id]
        if (holder2 != null) {
            holder2.unload()
            parasiteRegistry.remove(id)
            holder2.popResource()
        }
    }
}