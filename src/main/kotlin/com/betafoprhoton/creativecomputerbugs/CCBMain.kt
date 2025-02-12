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

        MinecraftForge.EVENT_BUS.register(::clientSetup)
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun clientSetup(event: FMLClientSetupEvent) {
        val item = CCBItems.DEBUG_STICK.get()
        val key = ResourceLocation(MODID, "isActive")
        ItemProperties.register(item, key) { itemStack, _, _, _ ->
            val tag = itemStack.orCreateTag // lol kotlin's abbreviation
            if (tag.getBoolean("isActive")) 1.0f else 0.0f
        }
    }
}
