package tech.harrynull.rpgreforged.mixins;

import tech.harrynull.rpgreforged.AttributeType;
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
                .add(AttributeType.CONSTITUTION.getAttribute())
                .add(AttributeType.STRENGTH.getAttribute())
                .add(AttributeType.DEFENSE.getAttribute())
                .add(AttributeType.DEXTERITY.getAttribute());
    }
}
