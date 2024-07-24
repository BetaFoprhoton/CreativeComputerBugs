package com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.block.create;

import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.block.AbstractBlockAPI
import com.simibubi.create.AllItems
import com.simibubi.create.content.redstone.link.LinkBehaviour
import com.simibubi.create.content.redstone.link.RedstoneLinkBlockEntity
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType
import dan200.computercraft.api.lua.LuaFunction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.minecraftforge.registries.ForgeRegistries

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
