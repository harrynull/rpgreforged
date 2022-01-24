package com.mod_author.mod_id

import com.google.common.collect.LinkedListMultimap
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.ItemStack
import java.util.*


val ATTACK_DAMAGE_MODIFIER_ID: UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF")
val ATTACK_SPEED_MODIFIER_ID: UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3")

object AttributeMapProcessor {
    fun mixin(
        multiMap: LinkedListMultimap<EntityAttribute, EntityAttributeModifier>,
        slot: EquipmentSlot,
        stack: ItemStack,
        context: LivingEntity?
    ) {
        if (slot != EquipmentSlot.MAINHAND || context == null) return
        multiMap.removeAll(EntityAttributes.GENERIC_ATTACK_DAMAGE)
    }
}
