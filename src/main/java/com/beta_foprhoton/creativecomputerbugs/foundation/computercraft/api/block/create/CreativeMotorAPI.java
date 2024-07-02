package com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.block.create;

import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.motor.CreativeMotorBlockEntity;
import dan200.computercraft.api.lua.ILuaAPI;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import net.minecraft.world.level.block.entity.BlockEntity;

import static java.lang.Math.abs;

public class CreativeMotorAPI implements ILuaAPI {
    public static final Class<CreativeMotorBlockEntity> SUPPORTED_BLOCK_ENTITY_CLASS = CreativeMotorBlockEntity.class;
    private final CreativeMotorBlockEntity blockEntity;

    public CreativeMotorAPI(BlockEntity blockEntity) {
        this.blockEntity = (CreativeMotorBlockEntity) blockEntity;
    }

    @Override
    public String[] getNames() {
        return new String[]{"apiMotor"};
    }

    @Override
    public void startup() {
        System.out.println("Start!");
    }

    @Override
    public void update() {
        blockEntity.updateGeneratedRotation();
    }

    @Override
    public void shutdown() {
        System.out.println("Shut down");
    }

    /**
     * Set a new speed for creative motor.
     * @param speed
     */
    @LuaFunction("setSpeed")
    public final void setSpeed (int speed) throws LuaException {
        if (abs(speed) > CreativeMotorBlockEntity.MAX_SPEED || speed == 0) throw new LuaException("Speed out of range.");
        blockEntity.getPersistentData().putInt("ReSpeed", speed);
        blockEntity.getGeneratedSpeed();
        blockEntity.updateGeneratedRotation();
        updateRotation();
    }

    private void updateRotation() {
        if (blockEntity.hasNetwork())
            blockEntity.getOrCreateNetwork().remove(blockEntity);
        RotationPropagator.handleRemoved(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity);
        blockEntity.removeSource();
        blockEntity.attachKinetics();
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
