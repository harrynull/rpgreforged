package tech.harrynull.rpgreforged

import com.google.common.collect.ImmutableMultimap
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier


abstract class Reforge {
    abstract val name: String
    abstract fun applyModifiers(
        multiMap: ImmutableMultimap.Builder<EntityAttribute?, EntityAttributeModifier?>,
        quality: Int
    )
}

class LegendaryReforge : Reforge() {
    override val name = "Legendary"
    override fun applyModifiers(
        multiMap: ImmutableMultimap.Builder<EntityAttribute?, EntityAttributeModifier?>,
        quality: Int
    ) {
        multiMap.put(
            AttributeType.STRENGTH.attribute,
            additionMultiplier(10.0 * quality, "Reforge")
        )
        multiMap.put(
            AttributeType.CONSTITUTION.attribute,
            additionMultiplier(10.0 * quality, "Reforge")
        )
        multiMap.put(
            AttributeType.DEXTERITY.attribute,
            additionMultiplier(10.0 * quality, "Reforge")
        )
        multiMap.put(
            AttributeType.DEFENSE.attribute,
            additionMultiplier(10.0 * quality, "Reforge")
        )
    }

}