package com.mod_author.mod_id

import com.google.common.collect.Multimap
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting


abstract class Socket {
    abstract fun descriptor(): Text
    abstract fun applyModifiers(
        multiMap: Multimap<EntityAttribute, EntityAttributeModifier>,
        quality: Int
    )
}

class EmptySocket : Socket() {
    override fun descriptor(): Text = LiteralText("<> Empty").formatted(Formatting.GRAY)

    override fun applyModifiers(
        multiMap: Multimap<EntityAttribute, EntityAttributeModifier>,
        quality: Int
    ) {
    }
}

class StrengthGem : Socket() {
    override fun descriptor(): Text = LiteralText("💪 Strength Gem").formatted(Formatting.RED)

    override fun applyModifiers(
        multiMap: Multimap<EntityAttribute, EntityAttributeModifier>,
        quality: Int
    ) {
        multiMap[AttributeType.STRENGTH.attribute].add(
            additionMultiplier(15.0, "Reforge")
        )
    }
}