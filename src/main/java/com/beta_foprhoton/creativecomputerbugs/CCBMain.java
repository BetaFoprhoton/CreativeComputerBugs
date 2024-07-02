package com.beta_foprhoton.creativecomputerbugs;

import com.beta_foprhoton.creativecomputerbugs.registy.BugComputerHolderRegister;
import com.beta_foprhoton.creativecomputerbugs.registy.CCBCreativeModeTab;
import com.beta_foprhoton.creativecomputerbugs.registy.CCBItems;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CCBMain.MODID)
public class CCBMain {

    public static final String MODID = "creativecomputerbugs";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(CCBMain.MODID);
    public static final BugComputerHolderRegister BUG_COMPUTER_HOLDER_REGISTER = new BugComputerHolderRegister();
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String WRITE_TO_DATA_LICENSE = "WRITETODATALICENSE";

    public CCBMain() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get()
                .getModEventBus();

        REGISTRATE.registerEventListeners(modEventBus);

        CCBItems.register();

        MinecraftForge.EVENT_BUS.register(this);

    }

    public static void authorizeWriteInData (BlockEntity blockEntity, CompoundTag tag) {
        blockEntity.getPersistentData().put(WRITE_TO_DATA_LICENSE, tag);
    }
}
