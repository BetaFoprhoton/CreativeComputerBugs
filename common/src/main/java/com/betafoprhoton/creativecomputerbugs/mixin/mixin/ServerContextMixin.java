package com.betafoprhoton.creativecomputerbugs.mixin;

import com.betafoprhoton.creativecomputerbugs.registy.BugComputerHolderRegister;
import dan200.computercraft.shared.computer.core.ServerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerContext.class)
public class ServerContextMixin {
    @Inject(at = @At("HEAD"), method = "tick", remap = false)
    public void tick(CallbackInfo ci) {
        BugComputerHolderRegister.Companion.tick();
    }
}
