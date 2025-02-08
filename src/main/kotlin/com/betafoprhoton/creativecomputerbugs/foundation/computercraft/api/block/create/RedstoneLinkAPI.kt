package com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.block.create;

import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.block.AbstractBlockAPI
import com.simibubi.create.content.redstone.link.LinkBehaviour
import com.simibubi.create.content.redstone.link.RedstoneLinkBlockEntity
import dan200.computercraft.api.lua.LuaFunction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack

class RedstoneLinkAPI(override val blockEntity: RedstoneLinkBlockEntity): AbstractBlockAPI() {
    override val specificName: String = "Link"

    /**
     * @param itemName name of the item
     * @param isFirst is the first frequency or the second one
     * @return set successfully or not
     */
    @LuaFunction("setFrequency")
    fun setFrequency(itemName: String, isFirst: Boolean): Boolean {
         val tag = CompoundTag()
        tag.putString("id", itemName)
        val itemStack = ItemStack.of(tag) ?: return false
        blockEntity.forEachBehaviour {
            if (it is LinkBehaviour)
            it.setFrequency(isFirst, itemStack)
        }
        return true
    }
}
