package com.mod_author.mod_id.mixins;

import com.mod_author.mod_id.AttributeType;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MaxHealthMixin {
    @Inject(method = "getMaxHealth", at = @At("RETURN"), cancellable = true)
    private void getMaxHealth(CallbackInfoReturnable<Float> cir) {
        var entity = ((LivingEntity) (Object) this);
        if (!entity.getAttributes().hasAttribute(AttributeType.CONSTITUTION.getAttribute()))
            return;
        var constitution = entity.getAttributeValue(AttributeType.CONSTITUTION.getAttribute());
        cir.setReturnValue((float) (cir.getReturnValue() + (constitution / 10.0F)));
    }
}
