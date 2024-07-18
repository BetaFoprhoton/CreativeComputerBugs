package com.beta_foprhoton.creativecomputerbugs.foundation.item.bugs;

import com.beta_foprhoton.creativecomputerbugs.foundation.helpers.extensions.ItemStackExtensions.getUpgrade
import dan200.computercraft.shared.computer.core.ComputerFamily
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

abstract class AbstractBugItem(properties: Properties) : Item(properties) {
    var family: ComputerFamily = ComputerFamily.NORMAL
    constructor(properties: Properties, family: ComputerFamily) : this(properties) {
        this.family = family
    }

    companion object {
        const val INFECTED_BLOCK_FLAG = "InfectedBugComputerID"
        const val NBT_UPGRADE = "Upgrade"
        const val NBT_UPGRADE_INFO = "UpgradeInfo"
        const val NBT_IS_ACTIVE = "IsActive"
    }

    var isActive = false
        set(value) = if (value) setActive() else setInActive()

    private fun setActive() {

    }

    private fun setInActive() {

    }

    override fun inventoryTick(itemStack: ItemStack, level: Level, entity: Entity, p_41407_: Int, p_41408_: Boolean) {
        itemStack.getOrCreateTag().putBoolean(NBT_IS_ACTIVE, isActive)
    }

    override fun getName(stack: ItemStack): Component {
        val baseString = getDescriptionId(stack)
        val upgrade = stack.getUpgrade() ?: return super.getName(stack);
        return Component.translatable("$baseString.upgraded", Component.translatable(upgrade.unlocalisedAdjective))
    }
}
