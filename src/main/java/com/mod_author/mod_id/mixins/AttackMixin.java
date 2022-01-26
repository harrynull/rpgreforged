package com.mod_author.mod_id.mixins;

import com.mod_author.mod_id.AttributeType;
import com.mod_author.mod_id.MyComponents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public class AttackMixin {
    @Redirect(method = "attack",
            at = @At(value = "INVOKE",
                    target = "net/minecraft/entity/player/PlayerEntity.getAttributeValue (Lnet/minecraft/entity/attribute/EntityAttribute;)D"
            ))
    private double getBaseAttackDamage(PlayerEntity instance, EntityAttribute entityAttribute) {
        var mainHand = instance.getMainHandStack();
        var attributes = MyComponents.Companion.getWEAPON_ATTRIBUTES().maybeGet(mainHand);
        var baseVal = instance.getAttributeValue(entityAttribute);
        var mainHandFirstAttribute = mainHand.getAttributeModifiers(EquipmentSlot.MAINHAND)
                .get(entityAttribute).stream().findFirst();
        var offset = mainHandFirstAttribute.map(EntityAttributeModifier::getValue).orElse(0.0);
        var strength = instance.getAttributeValue(AttributeType.STRENGTH.getAttribute());
        if (attributes.isEmpty()) return baseVal;
        return baseVal
                + attributes.get().getRandomDamage(instance.getRandom()) * (1 + strength / 100)
                - offset;
    }
}
