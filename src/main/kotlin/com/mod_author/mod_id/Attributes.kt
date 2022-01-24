package com.mod_author.mod_id

import net.minecraft.entity.attribute.ClampedEntityAttribute
import net.minecraft.util.registry.Registry

class RPGAttribute(id: String, default: Double, translationKey: String) {
    val attribute = Registry.register(
        Registry.ATTRIBUTE, id,
        ClampedEntityAttribute(translationKey, default, 0.0, 2000.0).setTracked(true)
    )
}

val ATTRIBUTE_CONSTITUTION = RPGAttribute("rpg.constitution", 10.0, "rpg.attribute.constitution")
val ATTRIBUTE_STRENGTH = RPGAttribute("rpg.strength", 0.0, "rpg.attribute.strength")
val ATTRIBUTE_DEFENSE = RPGAttribute("rpg.defense", 0.0, "rpg.attribute.defense")
val ATTRIBUTE_DEXTERITY = RPGAttribute("rpg.dexterity", 0.0, "rpg.attribute.dexterity")
