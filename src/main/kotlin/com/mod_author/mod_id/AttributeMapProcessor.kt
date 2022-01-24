package com.mod_author.mod_id

import com.google.common.collect.LinkedListMultimap
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import java.util.*


val ATTACK_DAMAGE_MODIFIER_ID: UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF")
val ATTACK_SPEED_MODIFIER_ID: UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3")

fun createNewAttributes(
    stack: ItemStack,
    attributes: LinkedListMultimap<EntityAttribute, EntityAttributeModifier>
): NbtCompound {
    val damage = attributes.get(EntityAttributes.GENERIC_ATTACK_DAMAGE).firstOrNull()?.value
    val atkSpeed = attributes.get(EntityAttributes.GENERIC_ATTACK_SPEED).firstOrNull()?.value
    return WeaponRPGAttributes(NbtCompound()).apply {
        baseDamage = damage ?: 1.0
        attackSpeed = atkSpeed ?: 1.0
        damageSpread = 0.2
    }.compound
}

object AttributeMapProcessor {
    fun mixin(
        multiMap: LinkedListMultimap<EntityAttribute, EntityAttributeModifier>,
        slot: EquipmentSlot,
        stack: ItemStack,
        context: LivingEntity?
    ) {
        if (slot != EquipmentSlot.MAINHAND || context == null) return
        if (!stack.getOrCreateNbt().contains(RPG_ATTRIBUTES)) {
            stack.nbt!!.put(RPG_ATTRIBUTES, createNewAttributes(stack, multiMap))
        }
        val attributes = WeaponRPGAttributes(stack)
        multiMap.removeAll(EntityAttributes.GENERIC_ATTACK_DAMAGE)
        multiMap.removeAll(EntityAttributes.GENERIC_ATTACK_SPEED)
        multiMap.put(
            EntityAttributes.GENERIC_ATTACK_DAMAGE,
            EntityAttributeModifier(
                ATTACK_DAMAGE_MODIFIER_ID,
                "Weapon modifier",
                attributes.randomDamage,
                EntityAttributeModifier.Operation.ADDITION
            )
        )
        multiMap.put(
            EntityAttributes.GENERIC_ATTACK_SPEED,
            EntityAttributeModifier(
                ATTACK_SPEED_MODIFIER_ID,
                "Weapon modifier",
                attributes.attackSpeed,
                EntityAttributeModifier.Operation.ADDITION
            )
        )
    }
}
