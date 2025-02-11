package com.betafoprhoton.creativecomputerbugs

import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.block.AbstractBlockAPI
import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.block.BlockAPIs
import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.entity.AbstractEntityAPI
import com.betafoprhoton.creativecomputerbugs.foundation.computercraft.api.entity.EntityAPIs
import com.betafoprhoton.creativecomputerbugs.registy.BugComputerHolderRegister
import com.betafoprhoton.creativecomputerbugs.registy.CCBCreativeModeTab
import com.betafoprhoton.creativecomputerbugs.registy.CCBItems
import com.mojang.logging.LogUtils
import com.simibubi.create.foundation.data.CreateRegistrate
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext

import java.util.HashMap

@Mod(CCBMain.MODID)
class CCBMain {
    companion object {
        const val MODID = "creativecomputerbugs"
        @JvmField
        val REGISTRATE: CreateRegistrate = CreateRegistrate.create(MODID)
        @JvmField
        val BUG_COMPUTER_HOLDER_REGISTER = BugComputerHolderRegister(0)
        val LOGGER = LogUtils.getLogger()
        const val INFECTED_BLOCK_FLAG = "InfectedComputerID"
        val BLOCK_API_REGISTRY: HashMap<Class<out BlockEntity>, Class<out AbstractBlockAPI>> = BlockAPIs.getTypes()
        val ENTITY_API_REGISTRY: HashMap<Class<out Entity>, Class<out AbstractEntityAPI>> = EntityAPIs.getTypes()

    }

    init {
        val modEventBus = FMLJavaModLoadingContext.get().modEventBus

        REGISTRATE.registerEventListeners(modEventBus)

        CCBCreativeModeTab.register(modEventBus)
        CCBItems.register()

        MinecraftForge.EVENT_BUS.register(this)
    }
}
