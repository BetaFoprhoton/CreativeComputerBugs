package com.betafoprhoton.creativecomputerbugs.mixin;

import com.simibubi.create.content.kinetics.motor.CreativeMotorBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeMotorBlockEntity.class)
public class CreativeMotorBlockEntityMixin {

    @Shadow
    protected ScrollValueBehaviour generatedSpeed;

    @Inject(at = @At("HEAD"), method = "getGeneratedSpeed", remap = false)
    public void getGeneratedSpeed(CallbackInfoReturnable<Float> cir) {
        int reSpeed = ((CreativeMotorBlockEntity) (Object) this).getPersistentData().getInt("ReSpeed");
        if (reSpeed != 0)
            generatedSpeed.setValue(reSpeed);
        ((CreativeMotorBlockEntity) (Object) this).getPersistentData().remove("ReSpeed");
    }
}
