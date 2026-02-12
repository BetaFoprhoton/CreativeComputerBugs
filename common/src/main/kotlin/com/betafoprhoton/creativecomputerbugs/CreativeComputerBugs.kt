package com.betafoprhoton.creativecomputerbugs

import dev.architectury.registry.CreativeTabRegistry
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import com.betafoprhoton.creativecomputerbugs.CreativeComputerBugsExpectPlatform.getConfigDirectory
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

object CreativeComputerBugs {
    const val MODID = "creativecomputerbugs"

    private val createModeTabs = DeferredRegister.create(MODID, Registries.CREATIVE_MODE_TAB)
    val creativeComputerBugsTab: RegistrySupplier<CreativeModeTab> = createModeTabs.register("${MODID}_tab") {
        CreativeTabRegistry.create(Component.translatable("category.creativecomputerbugs")) {
            ItemStack(exampleItem.get())
        }
    }

    private val items = DeferredRegister.create(MODID, Registries.ITEM)

    val exampleItem: RegistrySupplier<Item> = items.register("example_item") {
        Item(
            Item.Properties().`arch$tab`(creativeComputerBugsTab) // DON'T CALL GET ON exampleTab HERE
        )
    }

    fun init() {
        createModeTabs.register()
        items.register()

        println("CONFIG DIR: ${getConfigDirectory().toAbsolutePath().normalize()}")
    }
}