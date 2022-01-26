package tech.harrynull.rpgreforged.mixins;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import tech.harrynull.rpgreforged.AttributeType;
import tech.harrynull.rpgreforged.MyComponents;

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
        if (attributes.isEmpty()) return baseVal;
        return baseVal
                + attributes.get().getRandomDamageOffset(instance.getRandom());
    }
}
