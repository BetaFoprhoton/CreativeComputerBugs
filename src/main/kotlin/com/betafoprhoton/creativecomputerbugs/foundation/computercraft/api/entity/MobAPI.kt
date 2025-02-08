package com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.entity

import dan200.computercraft.api.lua.LuaFunction
import net.minecraft.world.entity.*

class MobAPI(override val entity: Mob, override val specificName: String = "Mob") : AbstractEntityAPI() {
    companion object {
        fun getSupportedClass(): Class<Mob> {
            return Mob::class.java
        }
    }

    /**
     * Let this mob jump.
     */
    @LuaFunction("jump")
    fun jump() {
        entity.jumpControl.jump()
    }
}