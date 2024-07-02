package com.beta_foprhoton.creativecomputerbugs.foundation.computercraft.api.block.create;

import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.foundation.utility.NBTHelper;
import dan200.computercraft.api.lua.ILuaAPI;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.core.apis.OSAPI;
import net.minecraft.core.BlockPos;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.commands.data.BlockDataAccessor;
import net.minecraft.server.commands.data.DataCommands;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class MechanicalArmAPI implements ILuaAPI {
    public static final Class<ArmBlockEntity> SUPPORTED_BLOCK_ENTITY_CLASS = ArmBlockEntity.class;
    private final ArmBlockEntity blockEntity;

    public MechanicalArmAPI(BlockEntity blockEntity) {
        this.blockEntity = (ArmBlockEntity) blockEntity;
    }

    @Override
    public String[] getNames() {
        return new String[]{"apiArm"};
    }

    /**
     * Drop items that mechanical arm was held.
     */
    @LuaFunction("dropHeldItem")
    public final void dropHeldItem () {
        blockEntity.getPersistentData().putBoolean("DropItem", true);
    }

    /**
     * Set mechanical arm lock.
     * @param lock boolean, powered or not
     */
    @LuaFunction("setLock")
    public final void setLock (boolean lock) {
        blockEntity.getPersistentData().putBoolean("Powered", lock);
    }

    /**
     * Get whether mechanical arm is locked or not.
     * @return is Locked or not
     */
    @LuaFunction("getIsLocked")
    public final boolean getIsLocked () {
        return blockEntity.getPersistentData().getBoolean("Powered");
    }

    /**
     * Add a new target for mechanical arm.
     * @param x X of target
     * @param y y of target
     * @param z Z of target
     * @param mode point mode "deposit" or "take"
     * @return add successfully or not
     */
    @LuaFunction("addTarget")
    public final boolean addTarget (int x, int y, int z, String mode) {
        var pos = new BlockPos(x, y, z);
        var level = blockEntity.getLevel();
        var pData = blockEntity.getPersistentData();
        var point = ArmInteractionPoint.create(level, pos, level.getBlockState(pos));
        if (point == null) return false;
        if (mode.equals("take")) point.cycleMode();
        else if (mode.equals("deposit")) point.getMode();
        else return false;
        pData.put("InteractionPoint", point.serialize(blockEntity.getBlockPos()));
        blockEntity.tick();
        pData = blockEntity.getPersistentData();
        if (!pData.contains("InteractionPoint")) return true;
        pData.remove("InteractionPoint");
        return false;
    }

    /**
     * Remove a target from the mechanical arm.
     * @param x X of the Target
     * @param y Y of the Target
     * @param z Z of the Target
     * @return is removed or not
     */
    @LuaFunction("removeTarget")
    public final boolean removeTarget (int x, int y, int z) {
        var pos = new BlockPos(x, y, z);
        var level = blockEntity.getLevel();
        var pData = blockEntity.getPersistentData();
        if (level == null) return false;
        var point = ArmInteractionPoint.create(level, pos, level.getBlockState(pos));
        if (point == null) return false;
        pData.put("RemovePoint", point.serialize(blockEntity.getBlockPos()));
        blockEntity.tick();
        pData = blockEntity.getPersistentData();
        if (!pData.contains("RemovePoint")) return true;
        pData.remove("RemovePoint");
        return false;
    }

    /**
     * Get targets that the mechanical arm aims.
     * @return targets that the mechanical arm aims
     */
    @LuaFunction("getTargets")
    public final MethodResult getTargets () {
        var interactionPointTag = blockEntity.saveWithFullMetadata().getList("InteractionPoints", Tag.TAG_COMPOUND);
        return MethodResult.of(interactionPointTag);
    }

    /**
     * The phase in which the return arm is in.
     * @return Phase: dancing, move_to_input, move_to_output, search_inputs, search_outputs or none
     */
    @LuaFunction("getPhase")
    public final String getPhase () {
        var phase = NBTHelper.readEnum(blockEntity.saveWithFullMetadata(), "Phase", ArmBlockEntity.Phase.class);
        switch (phase) {
            case DANCING -> {return "dancing";}
            case MOVE_TO_INPUT -> {return "move_to_input";}
            case SEARCH_INPUTS -> {return "search_inputs";}
            case MOVE_TO_OUTPUT -> {return "move_to_output";}
            case SEARCH_OUTPUTS -> {return "search_outputs";}
        }
        return "none";
    }

    /**
     * Return the block is interactable or not.
     * @param x X of the block
     * @param y y of the block
     * @param z Z of the block
     * @return is interactable or not
     */
    @LuaFunction("isInteractable")
    public final boolean isInteractable (int x, int y, int z) {
        var pos = new BlockPos(x, y, z);
        return ArmInteractionPoint.isInteractable(blockEntity.getLevel(), pos, blockEntity.getLevel().getBlockState(pos));
    }
}
