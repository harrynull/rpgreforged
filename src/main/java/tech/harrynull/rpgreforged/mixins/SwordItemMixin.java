package tech.harrynull.rpgreforged.mixins;

import com.google.common.collect.Multimap;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SwordItem.class)
public interface SwordItemMixin {
    @Accessor
    Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers();
}
