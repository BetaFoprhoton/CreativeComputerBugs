package com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.block.create;

import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.speedController.SpeedControllerBlockEntity;
import dan200.computercraft.api.lua.ILuaAPI;
import dan200.computercraft.api.lua.LuaFunction;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SpeedControllerAPI implements ILuaAPI {
    public static final Class<SpeedControllerBlockEntity> SUPPORTED_BLOCK_ENTITY_CLASS = SpeedControllerBlockEntity.class;
    private final SpeedControllerBlockEntity blockEntity;

    public SpeedControllerAPI(BlockEntity blockEntity) {
        this.blockEntity = (SpeedControllerBlockEntity) blockEntity;
    }

    @Override
    public String[] getNames() {
        return new String[]{"apiController"};
    }

    /**
     * Set a new speed for the speed controller.
     * @param speed
     */
    @LuaFunction("setTargetSpeed")
    public final void setTargetSpeed (int speed) {
        if (blockEntity.targetSpeed.value == speed || speed == 0)
            return;
        blockEntity.targetSpeed.setValue(speed);
        blockEntity.targetSpeed.withCallback(i -> this.updateTargetRotation());
    }

    private void updateTargetRotation() {
        if (blockEntity.hasNetwork())
            blockEntity.getOrCreateNetwork().remove(blockEntity);
        RotationPropagator.handleRemoved(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity);
        blockEntity.updateSpeed = true;
    }

    /**
     * @return Current Generated Speed
     */
    @LuaFunction("getGeneratedSpeed")
    public final float getGeneratedSpeed () {
        return blockEntity.getGeneratedSpeed();
    }

    /**
     * @return Current Speed
     */
    @LuaFunction("getSpeed")
    public final float getSpeed () {
        return blockEntity.getSpeed();
    }

}
