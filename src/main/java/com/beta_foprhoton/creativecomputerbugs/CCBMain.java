package com.beta_foprhoton.creativecomputerbugs;

import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.block.AllBlockAPIs;
import com.beta_foprhoton.creativecomputerbugs.registy.BugComputerHolderRegister;
import com.beta_foprhoton.creativecomputerbugs.registy.CCBItems;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import dan200.computercraft.api.lua.ILuaAPI;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(CCBMain.MODID)
public class CCBMain {

    public static final String MODID = "creativecomputerbugs";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(CCBMain.MODID);
    public static final BugComputerHolderRegister BUG_COMPUTER_HOLDER_REGISTER = new BugComputerHolderRegister();
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String WRITE_TO_DATA_LICENSE = "WRITETODATALICENSE";
    public static final HashMap<Class<? extends BlockEntity>, Class<? extends ILuaAPI>> BLOCK_API_REGISTRY = AllBlockAPIs.Companion.getTypes();

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
