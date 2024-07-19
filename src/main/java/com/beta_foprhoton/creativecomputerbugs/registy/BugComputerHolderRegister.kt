package com.beta_foprhoton.creativecomputerbugs.registy;

import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.computer.blocks.IBugComputerHolder
import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.core.WormComputerHolder
import dan200.computercraft.api.pocket.IPocketUpgrade
import dan200.computercraft.api.upgrades.UpgradeData
import dan200.computercraft.shared.computer.core.ComputerFamily
import net.minecraft.core.Direction
import net.minecraft.world.level.block.entity.BlockEntity

class BugComputerHolderRegister(private var idCounter: Int) {
    private val registry = HashMap<Int , IBugComputerHolder>()

    fun getHolder(id: Int): IBugComputerHolder? {
        return registry[id]
    }
    
    fun tick() {
        registry.forEach {
            it.value.tick()
        }
    }

    fun createBugHolder(infectBlockEntity: BlockEntity, upgrade: UpgradeData<IPocketUpgrade>, family: ComputerFamily, direction: Direction): WormComputerHolder? {
        val holderOld = WormComputerHolder.getBugComputerHolder(infectBlockEntity)
        if (holderOld != null) return null
        val holder =
            WormComputerHolder(
                infectBlockEntity,
                CCBItems.BUG_WORM.asStack(),
                upgrade,
                family,
                direction,
                idCounter
            )
        registry[holder.id] = holder
        idCounter ++
        return holder
    }

    fun remove(id: Int) {
        var holder = registry[id] ?: return
        holder.unload()
        registry.remove(id)
        holder.popResource()
    }
}