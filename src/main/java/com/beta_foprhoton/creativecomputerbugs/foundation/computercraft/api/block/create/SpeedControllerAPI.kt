package com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.block.create;

import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.block.AbstractBlockAPI
import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.speedController.SpeedControllerBlockEntity;
import dan200.computercraft.api.lua.LuaFunction;

class SpeedControllerAPI(override val blockEntity: SpeedControllerBlockEntity) : AbstractBlockAPI() {
    override val specificName = "SpeedController"

    companion object {
        fun getSupportedClass(): Class<SpeedControllerBlockEntity> {
            return SpeedControllerBlockEntity::class.java
        }
    }

    /**
     * Set a new speed for the speed controller.
     * @param speed
     * @return set successfully or not
     */
    @LuaFunction("setTargetSpeed")
    fun setTargetSpeed (speed: Int): Boolean {
        if (blockEntity.targetSpeed.value == speed || speed == 0)
            return false
        blockEntity.targetSpeed.setValue(speed)
        blockEntity.updateSpeed = true
        return true
    }

    private fun updateTargetRotation() {
        if (blockEntity.hasNetwork())
            blockEntity.getOrCreateNetwork().remove(blockEntity)
        RotationPropagator.handleRemoved(blockEntity.level, blockEntity.blockPos, blockEntity)
        blockEntity.updateSpeed = true
    }

    /**
     * @return Current Generated Speed
     */
    @LuaFunction("getGeneratedSpeed")
    fun getGeneratedSpeed(): Float {
        return blockEntity.generatedSpeed
    }

    /**
     * @return Current Speed
     */
    @LuaFunction("getSpeed")
    fun getSpeed(): Float {
        return blockEntity.getSpeed()
    }

    /**
     * @return Current Target Speed
     */
    @LuaFunction("getTargetSpeed")
    fun getTargetSpeed(): Int {
        return blockEntity.targetSpeed.value
    }

}
