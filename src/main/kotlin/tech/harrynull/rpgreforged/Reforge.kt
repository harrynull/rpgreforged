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
            AttributeType.CONSTITUTION.attribute.get(),
            additionMultiplier(10.0 * quality, "Reforge")
        )
        /*multiMap.put(
            AttributeType.STRENGTH.attribute.get(),
            additionMultiplier(10.0 * quality, "Reforge")
        )
        multiMap.put(
            AttributeType.DEXTERITY.attribute.get(),
            additionMultiplier(10.0 * quality, "Reforge")
        )
        multiMap.put(
            AttributeType.INTELLIGENCE.attribute.get(),
            additionMultiplier(10.0 * quality, "Reforge")
        )*/
    }

}