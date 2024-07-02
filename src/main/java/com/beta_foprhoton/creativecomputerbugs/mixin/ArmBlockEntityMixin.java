package com.beta_foprhoton.creativecomputerbugs.mixin;

import com.jozufozu.flywheel.backend.instancing.InstancedRenderDispatcher;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmAngleTarget;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static net.minecraft.world.level.block.Block.popResource;

@Mixin(ArmBlockEntity.class)
public abstract class ArmBlockEntityMixin {
    @Shadow
    List<ArmInteractionPoint> inputs;
    @Shadow
    List<ArmInteractionPoint> outputs;
    @Shadow
    ItemStack heldItem;
    @Shadow
    ListTag interactionPointTag;
    @Shadow
    boolean updateInteractionPoints;
    @Shadow
    protected boolean redstoneLocked;
    @Shadow
    ArmBlockEntity.Phase phase;
    @Shadow
    ArmAngleTarget previousTarget;
    LerpedFloat lowerArmAngle;
    LerpedFloat upperArmAngle;
    LerpedFloat baseAngle;
    LerpedFloat headAngle;
    LerpedFloat clawAngle;
    float previousBaseAngle;
    int tooltipWarmup;
    int chasedPointIndex;

    //
    protected ScrollOptionBehaviour<ArmBlockEntity.SelectionMode> selectionMode;
    protected int lastInputIndex = -1;
    protected int lastOutputIndex = -1;
    @Shadow
    protected abstract boolean isOnCeiling();
    @Shadow
    protected abstract ArmInteractionPoint getTargetedInteractionPoint();

    @Inject(method = "initInteractionPoints", at = @At("TAIL"), remap = false)
    protected void initInteractionPoints(CallbackInfo ci) {
        int previousIndex = chasedPointIndex;
        ArmBlockEntity.Phase previousPhase = phase;
        ListTag interactionPointTagBefore = interactionPointTag;

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> InstancedRenderDispatcher.enqueueUpdate((ArmBlockEntity)(Object)this));

        boolean ceiling = isOnCeiling();
        if (interactionPointTagBefore == null || interactionPointTagBefore.size() != interactionPointTag.size())
            updateInteractionPoints = true;
        if (previousIndex != chasedPointIndex || (previousPhase != phase)) {
            ArmInteractionPoint previousPoint = null;
            if (previousPhase == ArmBlockEntity.Phase.MOVE_TO_INPUT && previousIndex < inputs.size())
                previousPoint = inputs.get(previousIndex);
            if (previousPhase == ArmBlockEntity.Phase.MOVE_TO_OUTPUT && previousIndex < outputs.size())
                previousPoint = outputs.get(previousIndex);
            if (previousPoint != null) {
                previousTarget = previousPoint.getTargetAngles(((ArmBlockEntity)(Object)this).getBlockPos(), ceiling);
            }
        }
        ArmInteractionPoint targetedPoint = getTargetedInteractionPoint();
        if (targetedPoint != null)
            targetedPoint.updateCachedState();
    }

    @Inject(method = "tick", at = @At("HEAD"), remap = false)
    public void tick(CallbackInfo ci) {
        var level = ((ArmBlockEntity)(Object)this).getLevel();
        var anchorPos = ((ArmBlockEntity)(Object)this).getBlockPos();
        var pData = ((ArmBlockEntity)(Object)this).getPersistentData();
        if (pData.contains("InteractionPoint")) {
            var tagPoint = pData.getCompound("InteractionPoint");
            var iPoint = ArmInteractionPoint.deserialize(tagPoint, level, anchorPos);
            boolean isExist = false;
            if (!interactionPointTag.isEmpty()) {
                for (Tag tag : interactionPointTag.copy()) {
                    var point = ArmInteractionPoint.deserialize((CompoundTag) tag, level, anchorPos);
                    if (point == null || iPoint == null)
                        continue;
                    if (point.getPos().equals(iPoint.getPos())) {
                        if (!point.getMode().equals(iPoint.getMode()))
                            interactionPointTag.remove(tag);
                        isExist = true;
                    }
                }
            }
            if (!isExist) {
                interactionPointTag.add(tagPoint);
                pData.remove("InteractionPoints");
                updateInteractionPoints = true;
            }
        }
        if (pData.contains("Powered")) {
            redstoneLocked = pData.getBoolean("Powered");
        } else pData.putBoolean("Powered", redstoneLocked);
        if (pData.contains("RemovePoint")) {
            var delPoint = ArmInteractionPoint.deserialize(pData.getCompound("RemovePoint"), level, anchorPos);
            boolean isDeleted = false;
            if (!interactionPointTag.isEmpty() && delPoint != null) {
                for (Tag tag : interactionPointTag.copy()) {
                    var point = ArmInteractionPoint.deserialize((CompoundTag) tag, level, anchorPos);
                    if (point == null)
                        continue;
                    if (point.getPos().equals(delPoint.getPos())) {
                        isDeleted = true;
                        interactionPointTag.remove(tag);
                    }
                }
                if (isDeleted) {
                    pData.remove("RemovePoint");
                    updateInteractionPoints = true;
                }
            }
        }
        if (pData.getBoolean("DropItem")) {
            pData.remove("DropItem");
            if (!heldItem.isEmpty()) {
                popResource(((ArmBlockEntity)(Object)this).getLevel(), ((ArmBlockEntity)(Object)this).getBlockPos(), heldItem.copy());
                heldItem = ItemStack.EMPTY;
                phase = ArmBlockEntity.Phase.SEARCH_INPUTS;
            }
        }
    }
}
