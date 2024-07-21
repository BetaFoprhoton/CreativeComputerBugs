package com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.core;

import dan200.computercraft.shared.computer.core.ComputerFamily;

interface IBugComputerHolder {
    fun getComputerID(): Int

    fun setComputerID(id: Int)


    fun getLabel(): String?

    fun tick()

    fun unload()

    fun setLabel(label: String?)

    fun getFamily(): ComputerFamily

    fun popResource()
}
