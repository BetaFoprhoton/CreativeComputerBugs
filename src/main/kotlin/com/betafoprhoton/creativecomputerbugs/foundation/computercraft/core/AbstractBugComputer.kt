package com.betafoprhoton.creativecomputerbugs.foundation.computercraft.core

import dan200.computercraft.api.peripheral.IPeripheral
import dan200.computercraft.api.pocket.IPocketAccess
import dan200.computercraft.api.pocket.IPocketUpgrade
import dan200.computercraft.api.upgrades.UpgradeData
import dan200.computercraft.core.computer.ComputerSide
import dan200.computercraft.shared.computer.core.ComputerFamily
import dan200.computercraft.shared.computer.core.ServerComputer
import dan200.computercraft.shared.pocket.items.PocketComputerItem
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import java.util.*
import kotlin.collections.MutableMap

abstract class AbstractBugComputer(
    level: ServerLevel?,
    position: BlockPos?,
    computerID: Int,
    label: String?,
    family: ComputerFamily?,
    terminalWidth: Int,
    terminalHeight: Int,
    private val bugItem: ItemStack
) : ServerComputer(level, position, computerID, label, family, terminalWidth, terminalHeight), IPocketAccess {
    protected var upgrade: IPocketUpgrade? = null

    abstract fun getNewBlockPos(): BlockPos
    abstract fun getNewLevel(): Level?

    override fun getColour(): Int {
        return -1
    }

    override fun setColour(colour: Int) {

    }

    override fun getLight(): Int {
        return -1
    }

    override fun setLight(colour: Int) {
    }

    override fun getUpgradeNBTData(): CompoundTag {
        return PocketComputerItem.getUpgradeInfo(bugItem)
    }

    override fun updateUpgradeNBTData() {
    }

    override fun invalidatePeripheral() {
        val peripheral: IPeripheral? = upgrade?.createPeripheral(this)
        setPeripheral(ComputerSide.BACK, peripheral)
    }

    override fun getUpgrades(): MutableMap<ResourceLocation, IPeripheral> {
        return if (upgrade == null) mutableMapOf() else Collections.singletonMap(upgrade!!.upgradeID, getPeripheral(ComputerSide.BACK))
    }

    fun getUpgrade(): UpgradeData<IPocketUpgrade>? {
        return if (upgrade == null) null else UpgradeData.of(upgrade, upgradeNBTData)
    }

    fun setUpgrade(upgrade: UpgradeData<IPocketUpgrade?>?) {
        synchronized(this) {
            PocketComputerItem.setUpgrade(bugItem, upgrade)
            updateUpgradeNBTData()
            this.upgrade = upgrade?.upgrade()
            invalidatePeripheral()
        }
    }

    @Synchronized
    fun updateValues(upgrade: IPocketUpgrade?) {
        level = getNewLevel() as ServerLevel
        position = getNewBlockPos()
        if (this.upgrade !== upgrade) {
            this.upgrade = upgrade
            invalidatePeripheral()
        }
    }
}