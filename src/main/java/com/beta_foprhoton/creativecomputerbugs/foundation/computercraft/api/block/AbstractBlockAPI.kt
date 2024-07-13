package com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.block

import dan200.computercraft.api.lua.ILuaAPI
import net.minecraft.world.level.block.entity.BlockEntity

abstract class AbstractBlockAPI: ILuaAPI {
    abstract val blockEntity: BlockEntity
    abstract val specificName: String


    constructor() {


    }

    override fun getNames(): Array<String> {
        return Array<String>(1){"api$specificName"}
    }
}