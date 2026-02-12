package com.betafoprhoton.creativecomputerbugs.registy;

import com.betafoprhoton.creativecomputerbugs.CCBMain;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CCBCreativeModeTab {
    private static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CCBMain.MODID);

    public static final RegistryObject<CreativeModeTab> CCB_CREATIVE_TAB = REGISTER.register(CCBMain.MODID,
            () -> CreativeModeTab.builder()
                    .title(Components.translatable("itemGroup.creativecomputerbugs.base"))
                    .icon(AllBlocks.REFINED_RADIANCE_CASING::asStack)
                    .build());

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
}
