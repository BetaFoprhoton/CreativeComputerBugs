package com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.block

import dan200.computercraft.api.lua.ILuaAPI
import net.minecraft.world.level.block.entity.BlockEntity

abstract class AbstractBlockAPI: ILuaAPI {
    abstract val blockEntity: BlockEntity
    abstract val specificName: String

    open fun getSupportClass(): Class<out BlockEntity> { return BlockEntity::class.java }

    override fun getNames(): Array<String> {
        return Array<String>(1){"api$specificName"}
    }
}