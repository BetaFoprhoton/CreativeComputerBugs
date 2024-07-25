package com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.core

import com.beta_foprhoton.creativecomputerbugs.foundation.helpers.extensions.ItemStackExtensions.getUpgrade
import com.google.common.base.Strings
import dan200.computercraft.api.ComputerCraftAPI
import dan200.computercraft.api.pocket.IPocketAccess
import dan200.computercraft.api.pocket.IPocketUpgrade
import dan200.computercraft.api.upgrades.UpgradeData
import dan200.computercraft.core.computer.ComputerSide
import dan200.computercraft.shared.ModRegistry
import dan200.computercraft.shared.computer.core.ComputerFamily
import dan200.computercraft.shared.computer.core.ServerComputer
import dan200.computercraft.shared.computer.core.ServerContext
import dan200.computercraft.shared.computer.inventory.ComputerMenuWithoutInventory
import dan200.computercraft.shared.util.BlockEntityHelpers
import dan200.computercraft.shared.util.IDAssigner
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.MenuProvider
import net.minecraft.world.Nameable
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import java.util.*

abstract class AbstractBugComputerHolder(
    private val family: ComputerFamily,
    private val bugItem: ItemStack,
    private var upgrade: UpgradeData<IPocketUpgrade>?,
    val id: Int
) : IBugComputerHolder, Nameable, MenuProvider {
    private var instanceID = -1
    private var computerID = -1
    protected var label: String? = null
    private var on = false
    private var startOn = false
    private var fresh = false
    private val level: Level? = this.getLevel()

    init {
        this.putMark()
    }

    abstract fun putMark()
    abstract fun removeMark()
    abstract fun getLevel(): Level?
    abstract fun isUsableByPlayer(player: Player): Boolean
    abstract fun addAPIForComputer(computer: ServerComputer)

    override fun unload() {
        if (getLevel()?.isClientSide == true) return
        getServerComputer()?.close()
        removeMark()
        instanceID = -1;
    }

    protected fun getInteractRange(): Double {
        return BlockEntityHelpers.DEFAULT_INTERACT_RANGE
    }

    @Override
    override fun tick() {
        if (level?.isClientSide == true) return
        if (computerID < 0 && !startOn) return // Don't tick if we don't need a computer!

        val computer = createServerComputer()

        var upgrade1: IPocketUpgrade? = null
        if (upgrade != null) {
            upgrade1 = upgrade!!.upgrade();
        }

        if (startOn || (fresh && on)) {
            computer.turnOn()
            startOn = false
        }

        computer.keepAlive()

        fresh = false
        computerID = computer.id
        label = computer.label
        on = computer.isOn

        upgrade1?.update(computer as IPocketAccess, computer.getPeripheral(ComputerSide.BACK))
    }

    protected abstract fun createComputer(id: Int, level: ServerLevel): ServerComputer

    override fun getComputerID(): Int = computerID

    override fun getComputerLabel(): String? = label

    override fun setComputerID(id: Int) {
        if (level?.isClientSide ?: return || computerID == id) return
        computerID = id
        setChanged()
    }

    override fun setComputerLabel(label: String?) {
        if (level?.isClientSide ?: return || Objects.equals(this.label, label)) return
        this.label = label
        getServerComputer()?.label = label
        setChanged()
    }

    abstract fun setChanged()

    override fun getFamily(): ComputerFamily = family

    fun createServerComputer(): ServerComputer {
        val server = level?.server ?: throw IllegalStateException("Cannot access server computer on the client.")
        var computer = ServerContext.get(server).registry().get(instanceID)
        if (computer == null) {
            if (computerID < 0) {
                computerID = ComputerCraftAPI.createUniqueNumberedSaveDir(server, IDAssigner.COMPUTER);
            }

            computer = createComputer(computerID, level as ServerLevel)
            instanceID = computer.register()
            fresh = true

            val upgrade = bugItem.getUpgrade()

            //computer.updateValues(upgrade)

            addAPIForComputer(computer)
        }
        return computer;
    }

    fun getServerComputer(): ServerComputer? {
        level?: return null
        return if (level.isClientSide || level.server == null)
             null else ServerContext.get(level.server).registry().get(instanceID)
    }

    // Networking stuff

    /*
    protected void transferStateFrom(BugComputerHolder copy) {
        if (copy.computerID != computerID || copy.instanceID != instanceID) {
            unload();
            instanceID = copy.instanceID;
            computerID = copy.computerID;
            label = copy.label;
            on = copy.on;
            startOn = copy.startOn;
            lockCode = copy.lockCode;
            BlockEntityHelpers.updateBlock(infectedBlockEntity);
        }
        copy.instanceID = -1;
    }
    */

    override fun getName(): Component {
        label ?: return Component.empty()
        return Component.literal(label)

    }

    override fun hasCustomName(): Boolean {
        return !Strings.isNullOrEmpty(label)
    }


    override fun getCustomName(): Component? {
        return if (hasCustomName()) Component.literal(label) else null
    }


    override fun getDisplayName(): Component {
        return if (super.getDisplayName() == null) Component.empty() else super.getDisplayName()
    }

    override fun createMenu(id: Int, inventory: Inventory, player: Player): AbstractContainerMenu {
        return ComputerMenuWithoutInventory(ModRegistry.Menus.COMPUTER.get(), id, inventory, this::isUsableByPlayer, createServerComputer(), getFamily());
    }

    fun getBug() = bugItem
}