package com.betafoprhoton.creativecomputerbugs.mixin;

import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.level.block.Block.popResource;

@Mixin(ArmBlockEntity.class)
public abstract class ArmBlockEntityMixin {
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
