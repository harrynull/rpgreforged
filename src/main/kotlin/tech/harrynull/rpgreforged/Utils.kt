package tech.harrynull.rpgreforged

import com.google.common.collect.ImmutableMultimap
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt


fun Double.toString(numOfDec: Int): String {
    val integerDigits = this.toInt()
    val floatDigits = ((this - integerDigits) * 10f.pow(numOfDec)).roundToInt()
    return "${integerDigits}.${floatDigits}"
}

fun additionMultiplier(id: UUID, value: Double, name: String) =
    EntityAttributeModifier(id, name, value, EntityAttributeModifier.Operation.ADDITION)

fun genModifiers(
    multiMap: ImmutableMultimap.Builder<EntityAttribute?, EntityAttributeModifier?>,
    quality: Int,
    uuids: MutableList<UUID>,
    attributes: Map<AttributeType, Int>,
) {
    attributes.onEachIndexed { index, entry ->
        if (uuids.size <= index) uuids.add(UUID.randomUUID())
        multiMap.put(
            entry.key.attribute.get(),
            additionMultiplier(
                uuids[index],
                entry.value.toDouble() * quality,
                "Reforge"
            )
        )
    }
}