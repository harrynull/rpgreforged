package com.mod_author.mod_id.mixins;

import com.mod_author.mod_id.PlayerAttributeComponentKt;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class ExperienceMixin {
    @Inject(method = "addExperience", at = @At("RETURN"))
    private void injected(int experience, CallbackInfo ci) {
        PlayerAttributeComponentKt.onExperienceGain(
                (PlayerEntity) (Object) this, experience
        );
    }
}