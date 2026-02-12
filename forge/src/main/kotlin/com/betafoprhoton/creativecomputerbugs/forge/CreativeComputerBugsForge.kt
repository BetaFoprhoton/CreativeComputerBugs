package com.betafoprhoton.creativecomputerbugs.forge

import dev.architectury.platform.forge.EventBuses
import com.betafoprhoton.creativecomputerbugs.CreativeComputerBugs
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(CreativeComputerBugs.MODID)
object CreativeComputerBugsForge {
    init {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(CreativeComputerBugs.MODID, MOD_BUS)
        CreativeComputerBugs.init()
    }
}