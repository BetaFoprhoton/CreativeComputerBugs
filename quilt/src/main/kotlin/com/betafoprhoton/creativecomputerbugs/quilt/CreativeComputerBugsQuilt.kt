package com.betafoprhoton.creativecomputerbugs.quilt

import com.betafoprhoton.creativecomputerbugs.fabriclike.CreativeComputerBugsFabricLike
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer

object CreativeComputerBugsQuilt: ModInitializer {
    override fun onInitialize(mod: ModContainer?) {
        CreativeComputerBugsFabricLike.init()
    }
}