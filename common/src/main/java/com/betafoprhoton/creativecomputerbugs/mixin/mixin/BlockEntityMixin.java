package com.betafoprhoton.creativecomputerbugs.mixin;

import com.betafoprhoton.creativecomputerbugs.registy.BugComputerHolderRegister;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {
    @Inject(method = "setRemoved", at = @At("HEAD"), remap = false)
    public void setRemoved(CallbackInfo ci) {
        var holder = BugComputerHolderRegister.Companion.getBugComputerHolder((BlockEntity) (Object) this);
        if (holder != null)
            BugComputerHolderRegister.Companion.remove(holder.getId());
    }
}