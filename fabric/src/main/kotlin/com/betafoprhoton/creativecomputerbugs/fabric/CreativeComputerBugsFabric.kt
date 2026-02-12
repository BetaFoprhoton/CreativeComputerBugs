package com.betafoprhoton.creativecomputerbugs.fabric

import com.betafoprhoton.creativecomputerbugs.fabriclike.CreativeComputerBugsFabricLike
import net.fabricmc.api.ModInitializer


object CreativeComputerBugsFabric: ModInitializer {
    override fun onInitialize() {
        CreativeComputerBugsFabricLike.init()
    }
}