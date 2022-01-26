package tech.harrynull.rpgreforged.mixins;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import tech.harrynull.rpgreforged.WeaponRPGAttributesKt;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @ModifyVariable(method = "getAttributeModifiers", at = @At(value = "STORE", ordinal = 1), ordinal = 0)
    private Multimap<EntityAttribute, EntityAttributeModifier> modifyItemStackModifiers(
            Multimap<EntityAttribute, EntityAttributeModifier> original, EquipmentSlot slot
    ) {
        ItemStack stack = (ItemStack) (Object) this;
        Multimap<EntityAttribute, EntityAttributeModifier> modifiers = HashMultimap.create();

        if (original != null) {
            modifiers.putAll(original);
        }

        WeaponRPGAttributesKt.addItemAttributes(stack, slot, modifiers);

        return modifiers;
    }
}
