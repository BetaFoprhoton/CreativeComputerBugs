package com.betafoprhoton.creativecomputerbugs.fabric

import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Path

object CreativeComputerBugsExpectPlatformImpl {
    /**
     * This is our actual method to [CreativeComputerBugsExpectPlatform.getConfigDirectory].
     */
    @JvmStatic // Jvm Static is required so that java can access it
    fun getConfigDirectory(): Path {
        return FabricLoader.getInstance().configDir
    }
}