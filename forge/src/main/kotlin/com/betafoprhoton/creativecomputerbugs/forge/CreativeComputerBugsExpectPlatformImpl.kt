package com.betafoprhoton.creativecomputerbugs.forge

import net.minecraftforge.fml.loading.FMLPaths
import java.nio.file.Path

object CreativeComputerBugsExpectPlatformImpl {
    /**
     * This is our actual method to [CreativeComputerBugsExpectPlatform.getConfigDirectory].
     */
    @JvmStatic // Jvm Static is required so that java can access it
    fun getConfigDirectory(): Path {
        return FMLPaths.CONFIGDIR.get()
    }
}