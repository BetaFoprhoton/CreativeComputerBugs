package com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.block.create;

import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.block.AbstractBlockAPI
import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity
import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity.Phase.*
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint
import com.simibubi.create.foundation.utility.NBTHelper
import dan200.computercraft.api.lua.LuaFunction
import dan200.computercraft.api.lua.MethodResult
import net.minecraft.core.BlockPos
import net.minecraft.nbt.Tag

class MechanicalArmAPI(override val blockEntity: ArmBlockEntity) : AbstractBlockAPI() {
    override val specificName = "Motor"

    companion object {
        fun getSupportedClass(): Class<ArmBlockEntity> {
            return ArmBlockEntity::class.java
        }
    }

    /**
     * Drop items that mechanical arm was held.
     */
    @LuaFunction("dropHeldItem")
    fun dropHeldItem() {
        blockEntity.getPersistentData().putBoolean("DropItem", true);
    }

    /**
     * Set mechanical arm lock.
     * @param lock boolean, powered or not
     */
    @LuaFunction("setLock")
    fun setLock(lock: Boolean) {
        blockEntity.getPersistentData().putBoolean("Powered", lock);
    }

    /**
     * Get whether mechanical arm is locked or not.
     * @return is Locked or not
     */
    @LuaFunction("getIsLocked")
    fun getIsLocked(): Boolean {
        return blockEntity.getPersistentData().getBoolean("Powered")
    }

    /**
     * Add a new target for mechanical arm.
     * @param x X of target
     * @param y y of target
     * @param z Z of target
     * @param mode point mode "deposit" or "take"
     * @return add successfully or not
     */
    @LuaFunction("addTarget")
    fun addTarget (x: Int, y: Int, z: Int, mode: String): Boolean {
        val pos = BlockPos(x, y, z)
        val level = blockEntity.level ?: return false
        var pData = blockEntity.getPersistentData()
        val point = ArmInteractionPoint.create(level, pos, level.getBlockState(pos)) ?: return false
        when (mode) {
            "take" -> point.cycleMode()
            "deposit" -> point.mode
            else -> return false
        }
        pData.put("InteractionPoint", point.serialize(blockEntity.blockPos))
        blockEntity.tick()
        pData = blockEntity.getPersistentData()
        if (!pData.contains("InteractionPoint")) return true
        pData.remove("InteractionPoint")
        return false;
    }

    /**
     * Remove a target from the mechanical arm.
     * @param x X of the Target
     * @param y Y of the Target
     * @param z Z of the Target
     * @return is removed or not
     */
    @LuaFunction("removeTarget")
     fun removeTarget (x: Int, y: Int, z: Int): Boolean {
        val pos = BlockPos(x, y, z)
        val level = blockEntity.level ?: return false
        var pData = blockEntity.persistentData
        val point = ArmInteractionPoint.create(level, pos, level.getBlockState(pos)) ?: return false
        pData.put("RemovePoint", point.serialize(blockEntity.blockPos))
        blockEntity.tick()
        pData = blockEntity.persistentData
        if (!pData.contains("RemovePoint")) return true
        pData.remove("RemovePoint")
        return false
    }

    /**
     * Get targets that the mechanical arm aims.
     * @return targets that the mechanical arm aims
     */
    @LuaFunction("getTargets")
    fun getTargets(): MethodResult? {
        val interactionPointTag = blockEntity.saveWithFullMetadata().getList("InteractionPoints", Tag.TAG_COMPOUND.toInt())
        return MethodResult.of(interactionPointTag)
    }

    /**
     * The phase in which the return arm is in.
     * @return Phase: dancing, move_to_input, move_to_output, search_inputs, search_outputs or none
     */
    @LuaFunction("getPhase")
    fun getPhase(): String {
        val phase = NBTHelper.readEnum(blockEntity.saveWithFullMetadata(), "Phase", ArmBlockEntity.Phase::class.java)
        return when (phase) {
            SEARCH_INPUTS -> SEARCH_INPUTS.name
            MOVE_TO_INPUT -> MOVE_TO_INPUT.name
            SEARCH_OUTPUTS -> SEARCH_OUTPUTS.name
            MOVE_TO_OUTPUT -> MOVE_TO_OUTPUT.name
            DANCING -> DANCING.name
            null -> "none"
        }
    }

    /**
     * Return the block is interactable or not.
     * @param x X of the block
     * @param y y of the block
     * @param z Z of the block
     * @return is interactable or not
     */
    @LuaFunction("isInteractable")
    fun isInteractable(x: Int, y: Int, z: Int): Boolean {
        val pos = BlockPos(x, y, z)
        return ArmInteractionPoint.isInteractable(blockEntity.level, pos, blockEntity.level?.getBlockState(pos) ?: return false)
    }
}
