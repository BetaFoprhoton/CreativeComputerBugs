package com.betafoprhoton.creativecomputerbugs.mixin;

import com.betafoprhoton.creativecomputerbugs.CCBMain;
import com.betafoprhoton.creativecomputerbugs.foundation.helpers.BlockEntityHelpersKt;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {
    @Inject(method = "setRemoved", at = @At("HEAD"), remap = false)
    public void setRemoved(CallbackInfo ci) {
        var holder = BlockEntityHelpersKt.getBugComputerHolder((BlockEntity) (Object) this);
        if (holder != null)
            CCBMain.BUG_COMPUTER_HOLDER_REGISTER.remove(holder.getId());
    }

}