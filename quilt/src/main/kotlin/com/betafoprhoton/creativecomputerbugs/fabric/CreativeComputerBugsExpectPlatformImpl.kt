package com.betafoprhoton.creativecomputerbugs.fabric

import org.quiltmc.loader.api.QuiltLoader
import java.nio.file.Path

object CreativeComputerBugsExpectPlatformImpl {
    /**
     * This is our actual method to [CreativeComputerBugsExpectPlatform.getConfigDirectory].
     */
    @JvmStatic // Jvm Static is required so that java can access it
    fun getConfigDirectory(): Path {
        return QuiltLoader.getConfigDir()
    }
}