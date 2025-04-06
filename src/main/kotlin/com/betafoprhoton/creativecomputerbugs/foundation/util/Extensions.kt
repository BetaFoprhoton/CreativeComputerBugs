package com.betafoprhoton.creativecomputerbugs.foundation.util

import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.block.BlockAPIs
import com.betafoprhoton.creativecomputerbugs.foundation.item.bugs.AbstractBugItem.Companion.NBT_UPGRADE
import com.betafoprhoton.creativecomputerbugs.foundation.item.bugs.AbstractBugItem.Companion.NBT_UPGRADE_INFO
import dan200.computercraft.api.pocket.IPocketUpgrade
import dan200.computercraft.api.upgrades.UpgradeData
import dan200.computercraft.impl.PocketUpgrades
import dan200.computercraft.shared.util.NBTUtil
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import kotlin.reflect.KClass

//BlockEntity
fun BlockEntity.isAPISupported(): Boolean =
    BlockAPIs.isAPISupported(this)

//ItemStack
fun ItemStack.getUpgrade(): IPocketUpgrade? {
    val compound = this.tag
    if (compound == null || !compound.contains(NBT_UPGRADE)) return null
    return PocketUpgrades.instance().get(compound.getString(NBT_UPGRADE))
}

fun ItemStack.getUpgradeWithData(): UpgradeData<IPocketUpgrade>? {
    val compound = this.tag
    if (compound == null || !compound.contains(NBT_UPGRADE)) return null
    val upgrade = PocketUpgrades.instance().get(compound.getString(NBT_UPGRADE)) ?: return null
    return UpgradeData.of(upgrade, NBTUtil.getCompoundOrEmpty(compound, NBT_UPGRADE_INFO))
}

fun ItemStack.setUpgrade(upgrade: UpgradeData<IPocketUpgrade>?) {
    val compound = this.getOrCreateTag()

    if (upgrade == null) {
        compound.remove(NBT_UPGRADE)
        compound.remove(NBT_UPGRADE_INFO)
    } else {
        compound.putString(NBT_UPGRADE, upgrade.upgrade().upgradeID.toString())
        compound.put(NBT_UPGRADE_INFO, upgrade.data().copy())
    }
}

fun ItemStack.getUpgradeInfo(): CompoundTag {
    return this.getOrCreateTagElement(NBT_UPGRADE_INFO);
}

//KClass
val KClass<*>.superclasses: List<KClass<*>>
    get() = this.supertypes.map { it.classifier as KClass<*> }

// 递归检查是否间接继承
fun KClass<*>.isIndirectSuperclassOf(child: KClass<*>): Boolean {
    if (child.superclasses.any { it == this }) return true
    return child.superclasses.any { this.isIndirectSuperclassOf(it) }
}