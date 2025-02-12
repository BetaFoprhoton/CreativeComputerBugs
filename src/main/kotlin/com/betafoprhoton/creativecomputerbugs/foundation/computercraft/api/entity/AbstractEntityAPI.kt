package com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.entity

import dan200.computercraft.api.lua.ILuaAPI
import net.minecraft.world.entity.Entity

abstract class AbstractEntityAPI: ILuaAPI {
    abstract val abstractEntity: Entity
    abstract val specificName: String

    override fun getNames(): Array<String> {
        return Array(1){"api$specificName"}
    }
}