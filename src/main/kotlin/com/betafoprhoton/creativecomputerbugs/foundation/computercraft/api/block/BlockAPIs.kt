package com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.block

import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.block.create.CreativeMotorAPI
import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.block.create.MechanicalArmAPI
import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.block.create.SpeedControllerAPI
import dan200.computercraft.shared.computer.core.ServerComputer
import net.minecraft.world.level.block.entity.BlockEntity

enum class BlockAPIs(val blockEntity: Class<out BlockEntity>, val api: Class<out AbstractBlockAPI>) {
    CREATIVE_MOTOR(CreativeMotorAPI.getSupportedClass(), CreativeMotorAPI::class.java),
    SPEED_CONTROLLER(SpeedControllerAPI.getSupportedClass(), SpeedControllerAPI::class.java),
    MECHANICAL_ARM(MechanicalArmAPI.getSupportedClass(), MechanicalArmAPI::class.java)

    ;

    companion object {
        private val BLOCK_API_REGISTRY = getTypes()

        private fun getTypes(): HashMap<Class<out BlockEntity>, Class<out AbstractBlockAPI>> {
            val values = HashMap<Class<out BlockEntity>, Class<out AbstractBlockAPI>>()
            entries.forEach { values[it.blockEntity] = it.api }
            return values
        }

        fun addAPI(computer: ServerComputer, blockEntity: BlockEntity): Boolean {
            val apiClass = BLOCK_API_REGISTRY[blockEntity.javaClass] ?: return false
            val api = apiClass.getDeclaredConstructor(BlockEntity::class.java).newInstance(blockEntity) ?: return false
            computer.addAPI(api)
            return true
        }

        fun isAPISupported(blockEntity: BlockEntity): Boolean {
            BLOCK_API_REGISTRY[blockEntity.javaClass] ?: return false
            return true
        }
    }
}
