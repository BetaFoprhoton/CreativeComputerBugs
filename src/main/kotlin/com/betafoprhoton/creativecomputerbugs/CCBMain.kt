package com.betafoprhoton.creativecomputerbugs

import com.betafoprhoton.creativecomputerbugs.registy.CCBCreativeModeTab
import com.betafoprhoton.creativecomputerbugs.registy.CCBItems
import com.mojang.logging.LogUtils
import com.simibubi.create.foundation.data.CreateRegistrate
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext


@Mod(CCBMain.MODID)
class CCBMain {
    companion object {
        internal const val MODID = "creativecomputerbugs"
        @JvmField
        val REGISTRATE: CreateRegistrate = CreateRegistrate.create(MODID)
        @JvmField
        val LOGGER = LogUtils.getLogger()
        const val INFECTED_BLOCK_FLAG = "InfectedComputerID"
    }

    init {
        val modEventBus = FMLJavaModLoadingContext.get().modEventBus

        REGISTRATE.registerEventListeners(modEventBus)

        CCBCreativeModeTab.register(modEventBus)
        CCBItems.register()

        MinecraftForge.EVENT_BUS.register(this)
    }
}
