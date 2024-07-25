package com.beta_foprhoton.creativecomputerbugs.mixin;

import com.beta_foprhoton.creativecomputerbugs.CCBMain;
import com.beta_foprhoton.creativecomputerbugs.foundation.helpers.BlockEntityHelpers;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin {
    @Inject(method = "setRemoved", at = @At("HEAD"), remap = false)
    public void setRemoved(CallbackInfo ci) {
        var holder = BlockEntityHelpers.getBugComputerHolder((BlockEntity) (Object) this);
        if (holder != null)
            CCBMain.BUG_COMPUTER_HOLDER_REGISTER.remove(holder.getId());
    }

}