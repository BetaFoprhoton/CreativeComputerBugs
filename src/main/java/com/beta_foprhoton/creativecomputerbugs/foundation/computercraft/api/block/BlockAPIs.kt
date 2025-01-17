package com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.block;

import com.beta_foprhoton.creativecomputerbugs.CCBMain
import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.block.create.*;
import dan200.computercraft.api.lua.ILuaAPI;
import dan200.computercraft.shared.computer.core.ServerComputer
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;

enum class BlockAPIs(val blockEntity: Class<out BlockEntity>, val api: Class<out AbstractBlockAPI>) {
    CREATIVE_MOTOR(CreativeMotorAPI.getSupportedClass(), CreativeMotorAPI::class.java),
    SPEED_CONTROLLER(SpeedControllerAPI.getSupportedClass(), SpeedControllerAPI::class.java),
    MECHANICAL_ARM(MechanicalArmAPI.getSupportedClass(), MechanicalArmAPI::class.java)

    ;

    companion object {
        fun getTypes(): HashMap<Class<out BlockEntity>, Class<out AbstractBlockAPI>> {
            val values = HashMap<Class<out BlockEntity>, Class<out AbstractBlockAPI>>()
            entries.forEach { values[it.blockEntity] = it.api }
            return values
        }

        fun addAPI(computer: ServerComputer, blockEntity: BlockEntity): Boolean {
            val apiClass = CCBMain.BLOCK_API_REGISTRY[blockEntity.javaClass] ?: return false
            val api = apiClass.getDeclaredConstructor(BlockEntity::class.java).newInstance(blockEntity) ?: return false
            computer.addAPI(api)
            return true
        }
    }
}
