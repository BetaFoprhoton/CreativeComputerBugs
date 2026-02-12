package com.betafoprhoton.creativecomputerbugs.foundation.computercraft.core;

import dan200.computercraft.shared.computer.core.ComputerFamily;

interface IBugComputerHolder {
    fun getComputerID(): Int

    fun saveData()

    fun removeData()

    fun setComputerID(id: Int)

    fun getComputerLabel(): String?

    fun tick()

    fun unload()

    fun load()

    fun setComputerLabel(label: String?)

    fun getFamily(): ComputerFamily

    fun popResource()
}
