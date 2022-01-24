package com.mod_author.mod_id.mixins;

import com.mod_author.mod_id.MyComponents;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public class AttackMixin {
    @Redirect(method = "attack",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "net/minecraft/entity/player/PlayerEntity.getAttributeValue (Lnet/minecraft/entity/attribute/EntityAttribute;)D"
            ))
    private static double getBaseAttackDamage(PlayerEntity instance, EntityAttribute entityAttribute) {
        var attributes = MyComponents.Companion.getWEAPON_ATTRIBUTES().maybeGet(instance.getMainHandStack());
        var baseVal = instance.getAttributeValue(entityAttribute);
        if (attributes.isEmpty()) return baseVal;
        return baseVal + attributes.get().getRandomDamage(instance.getRandom());
    }
}
