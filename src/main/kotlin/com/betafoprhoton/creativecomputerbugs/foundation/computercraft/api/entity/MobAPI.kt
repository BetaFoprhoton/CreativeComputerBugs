package com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.entity

import dan200.computercraft.api.lua.LuaFunction
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.item.ItemStack
import java.util.*

class MobAPI(override val abstractEntity: Entity) : AbstractEntityAPI() {
    override val specificName: String = "Mob"
    val entity = abstractEntity as Mob
    val entityGoals = entity.goalSelector.availableGoals
    val entityTargets = entity.targetSelector.availableGoals

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
        //entity.goalSelector.addGoal(100, MoveToBlockGoal() {
        //})
        entity.moveControl.setWantedPosition(x, y, z, entity.getAttributeValue(Attributes.MOVEMENT_SPEED))
    }

    /**
     * Force the mob look at the specified position.
     */
    @LuaFunction("lookAtPos")
    fun lockAtPos(x: Double, y: Double, z: Double) {
        //entity.goalSelector.addGoal(Goal)
    }

    /**
     * Force the mob attack the specified entity.
     */
    @LuaFunction("attack")
    fun attack(uuid: String) {
        //idk how to get entity by its uuid.
    }

    @LuaFunction("attackNearest")
    fun attackNearest() {
        //entity.goalSelector.addGoal(100, NearestAttackableTargetGoal<>(entity, LivingEntity::class.java, true))
    }

    /**
     * Get the mob's attribute value.
     * @return attribute value, or -1.0 if the attribute is not found.
     */
    @LuaFunction
    fun getAttribute(attributeName: String): Double {
        return when (attributeName.lowercase(Locale.getDefault())) {
            "health" -> entity.getAttributeValue(Attributes.MAX_HEALTH)
            "speed" -> entity.getAttributeValue(Attributes.MOVEMENT_SPEED)
            "attack_damage" -> entity.getAttributeValue(Attributes.ATTACK_DAMAGE)
            "attack_speed" -> entity.getAttributeValue(Attributes.ATTACK_SPEED)
            "knockback_resistance" -> entity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)
            "follow_range" -> entity.getAttributeValue(Attributes.FOLLOW_RANGE)
            "jump_strength" -> entity.getAttributeValue(Attributes.JUMP_STRENGTH)
            "armor" -> entity.getAttributeValue(Attributes.ARMOR)
            "armor_toughness" -> entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS)
            "luck" -> entity.getAttributeValue(Attributes.LUCK)
            else -> -1.0
        }
    }

    @LuaFunction
    fun setMobAI(enabled: Boolean, isTargetSelector: Boolean) {
        if (enabled) {
            if (isTargetSelector) {
                entityTargets.forEach {
                    entity.targetSelector.addGoal(it.priority, it.goal)
                }
            } else {
                entityGoals.forEach {
                    entity.goalSelector.addGoal(it.priority, it.goal)
                }
            }
        } else {
            if (isTargetSelector) {
                entity.targetSelector.removeAllGoals { true }
            } else {
                entity.goalSelector.removeAllGoals { true }
            }
        }
    }
}