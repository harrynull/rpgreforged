package com.mod_author.mod_id.mixins;

import com.mod_author.mod_id.AttributesKt;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(PlayerEntity.class)
public class PlayerAttributesMixin {
    @Inject(method = "createPlayerAttributes", at = @At("RETURN"))
    private static void injected(CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
        cir.getReturnValue()
                .add(AttributesKt.getATTRIBUTE_CONSTITUTION().getAttribute())
                .add(AttributesKt.getATTRIBUTE_STRENGTH().getAttribute())
                .add(AttributesKt.getATTRIBUTE_DEFENSE().getAttribute())
                .add(AttributesKt.getATTRIBUTE_DEXTERITY().getAttribute());
    }
}
