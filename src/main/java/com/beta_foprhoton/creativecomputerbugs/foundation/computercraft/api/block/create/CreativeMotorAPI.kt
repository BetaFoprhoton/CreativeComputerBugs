package com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.block.create;

import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.block.AbstractBlockAPI
import com.simibubi.create.content.kinetics.RotationPropagator
import com.simibubi.create.content.kinetics.motor.CreativeMotorBlockEntity
import dan200.computercraft.api.lua.LuaFunction
import kotlin.math.abs


class CreativeMotorAPI(override val blockEntity: CreativeMotorBlockEntity) : AbstractBlockAPI() {
    override val specificName = "Motor"
    companion object {
        fun getSupportedClass(): Class<CreativeMotorBlockEntity> {
            return CreativeMotorBlockEntity::class.java
        }
    }
    override fun update() {
        blockEntity.updateGeneratedRotation();
    }

    /**
     * Set a new speed for creative motor.
     * @param speed
     */
    @LuaFunction("setSpeed")
    fun setSpeed (speed: Int): Boolean {
        if (abs(speed) > CreativeMotorBlockEntity.MAX_SPEED || speed == 0) return false
        blockEntity.persistentData.putInt("ReSpeed", speed)
        blockEntity.getGeneratedSpeed()
        blockEntity.updateGeneratedRotation()
        updateRotation()
        return true
    }

    private fun updateRotation() {
        if (blockEntity.hasNetwork())
            blockEntity.getOrCreateNetwork().remove(blockEntity)
        RotationPropagator.handleRemoved(blockEntity.level, blockEntity.blockPos, blockEntity)
        blockEntity.removeSource()
        blockEntity.attachKinetics()
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
        return blockEntity.speed
    }
}
