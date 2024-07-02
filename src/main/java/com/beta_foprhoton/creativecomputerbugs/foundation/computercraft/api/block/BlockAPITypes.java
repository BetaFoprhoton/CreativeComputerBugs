package com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.block;

import com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.block.create.*;
import dan200.computercraft.api.lua.ILuaAPI;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;

public enum BlockAPITypes {
    CREATIVE_MOTOR(CreativeMotorAPI.SUPPORTED_BLOCK_ENTITY_CLASS, CreativeMotorAPI.class),
    SPEED_CONTROLLER(SpeedControllerAPI.SUPPORTED_BLOCK_ENTITY_CLASS, SpeedControllerAPI.class),
    MECHANICAL_ARM(MechanicalArmAPI.SUPPORTED_BLOCK_ENTITY_CLASS, MechanicalArmAPI.class)

    ;

    final Class<? extends BlockEntity> blockEntity;
    final Class<? extends ILuaAPI> api;
    public static final HashMap<Class<? extends BlockEntity>, Class<? extends ILuaAPI>> TYPES = getTypes();
    BlockAPITypes(Class<? extends BlockEntity> blockEntity, Class<? extends  ILuaAPI> api) {
        this.blockEntity = blockEntity;
       this.api = api;
    }

    public static HashMap<Class<? extends BlockEntity>, Class<? extends ILuaAPI>> getTypes() {
        HashMap<Class<? extends BlockEntity>, Class<? extends ILuaAPI>> values = new HashMap<>();
        Arrays.stream(BlockAPITypes.values()).toList().forEach(t -> values.put(t.blockEntity, t.api));
        return values;
    }

    @Nullable
    public static Class<? extends ILuaAPI> getSuitableAPI(@NotNull BlockEntity blockEntity) {
        return TYPES.get(blockEntity.getClass());
    }
}
