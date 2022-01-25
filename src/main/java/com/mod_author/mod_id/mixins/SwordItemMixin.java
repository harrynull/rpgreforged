package com.mod_author.mod_id.mixins;

import com.google.common.collect.Multimap;
import com.mod_author.mod_id.SwordAttributesAccess;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.SwordItem;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SwordItem.class)
public abstract class SwordItemMixin implements SwordAttributesAccess {
    @Shadow
    @Final
    @Mutable
    private Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers;


    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers() {
        return attributeModifiers;
    }

    public void setAttributeModifiers(@NotNull Multimap<EntityAttribute, EntityAttributeModifier> modifiers) {
        attributeModifiers = modifiers;
    }
    /*@Accessor
    Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers();

    @Accessor
    void setAttributeModifiers(Multimap<EntityAttribute, EntityAttributeModifier> modifiers);*/
}
