package com.beta_foprhoton.creativecomputerbugs.mixin;

import com.simibubi.create.content.redstone.link.LinkBehaviour;
import com.simibubi.create.content.redstone.link.RedstoneLinkBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RedstoneLinkBlockEntity.class)
public class RedstoneLinkBlockEntityMixin {
    @Shadow private LinkBehaviour link;

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {

    }
}
