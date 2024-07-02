package com.beta_foprhoton.creativecomputerbugs.mixin;

import com.beta_foprhoton.creativecomputerbugs.CCBMain;
import dan200.computercraft.shared.computer.core.ServerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerContext.class)
public class ServerContextMixin {
    @Inject(at = @At("HEAD"), method = "tick", remap = false)
    public void tick(CallbackInfo ci) {
        CCBMain.BUG_COMPUTER_HOLDER_REGISTER.tick();
    }
}
