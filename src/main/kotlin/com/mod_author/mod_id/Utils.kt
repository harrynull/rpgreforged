package com.mod_author.mod_id

import net.minecraft.entity.attribute.EntityAttributeModifier
import kotlin.math.pow
import kotlin.math.roundToInt


fun Double.toString(numOfDec: Int): String {
    val integerDigits = this.toInt()
    val floatDigits = ((this - integerDigits) * 10f.pow(numOfDec)).roundToInt()
    return "${integerDigits}.${floatDigits}"
}

fun additionMultiplier(value: Double, name: String) =
    EntityAttributeModifier(name, value, EntityAttributeModifier.Operation.ADDITION)