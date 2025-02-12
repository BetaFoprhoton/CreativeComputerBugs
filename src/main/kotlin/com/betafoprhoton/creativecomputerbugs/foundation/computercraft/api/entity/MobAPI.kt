package com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.entity

import dan200.computercraft.api.lua.LuaFunction
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.item.ItemStack

class MobAPI(override val abstractEntity: Entity) : AbstractEntityAPI() {
    override val specificName: String = "Mob"
    val entity = abstractEntity as Mob

    companion object {
        fun getSupportedClass(): Class<Mob> {
            return Mob::class.java
        }
    }

    /**
     * Force this mob to jump.
     */
    @LuaFunction("jump")
    fun jump() {
        entity.jumpControl.jump()
    }

    /**
     * Get the item in the mob's hand.
     */
    @LuaFunction("getItemInHand")
    fun getItemInHand(isMainHand: Boolean): String {
        val itemStack = entity.getItemInHand(if (isMainHand) InteractionHand.MAIN_HAND else InteractionHand.OFF_HAND)
        return itemStack.displayName.string
    }

    /**
     * Force the mob move to the specified position.
     */
    @LuaFunction("moveTo")
    fun moveTo(x: Double, y: Double, z: Double) {
        //TODO: seem have to use goal finder
    }
}