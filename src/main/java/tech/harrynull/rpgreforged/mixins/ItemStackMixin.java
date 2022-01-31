package tech.harrynull.rpgreforged.mixins;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tech.harrynull.rpgreforged.RPGAttributesKt;

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

        RPGAttributesKt.addItemAttributes(stack, slot, modifiers);

        return modifiers;
    }

    @Inject(method = "onCraft", at = @At(value = "RETURN"))
    public void onCraft(World world, PlayerEntity player, int amount, CallbackInfo ci) {
        //ItemStack stack = (ItemStack) (Object) this;
        //MyComponents.Companion.getWEAPON_ATTRIBUTES().maybeGet(stack)
        //        .ifPresent(WeaponRPGAttributeComponent::forge);
    }
}
