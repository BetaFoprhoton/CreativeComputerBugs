package com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.entity

import dan200.computercraft.api.lua.LuaFunction
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.item.ItemStack
import java.util.UUID

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
        entity.moveControl.setWantedPosition(x, y, z, entity.getAttributeValue(Attributes.MOVEMENT_SPEED))
    }

    /**
     * Force the mob look at the specified position.
     */
    @LuaFunction("lookAtPos")
    fun lockAtPos(x: Double, y: Double, z: Double) {
        entity.lookControl.setLookAt(x, y, z)
    }

    /**
     * Force the mob attack the specified entity.
     */
    @LuaFunction("attack")
    fun attack(uuid: String) {
        //idk how to get entity by its uuid.
    }
}