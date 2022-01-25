package com.mod_author.mod_id

import com.google.common.collect.Multimap
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier


abstract class Reforge {
    abstract val name: String
    abstract fun applyModifiers(
        multiMap: Multimap<EntityAttribute, EntityAttributeModifier>,
        quality: Int
    )
}

class LegendaryReforge : Reforge() {
    override val name = "Legendary"
    override fun applyModifiers(
        multiMap: Multimap<EntityAttribute, EntityAttributeModifier>,
        quality: Int
    ) {
        multiMap[AttributeType.STRENGTH.attribute].add(
            additionMultiplier(10.0 * quality, "Reforge")
        )
        multiMap[AttributeType.CONSTITUTION.attribute].add(
            additionMultiplier(10.0 * quality, "Reforge")
        )
        multiMap[AttributeType.DEXTERITY.attribute].add(
            additionMultiplier(10.0 * quality, "Reforge")
        )
        multiMap[AttributeType.DEFENSE.attribute].add(
            additionMultiplier(10.0 * quality, "Reforge")
        )
    }

}