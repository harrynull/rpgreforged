package com.mod_author.mod_id

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import kotlin.random.Random


const val RPG_ATTRIBUTES = "rpg_attributes"

class WeaponRPGAttributes(val compound: NbtCompound) {
    constructor(stack: ItemStack) : this(stack.nbt!!.getCompound(RPG_ATTRIBUTES))

    var baseDamage: Double by NBT(compound, BASE_DAMAGE)
    var damageSpread: Double by NBT(compound, SPREAD)
    var attackSpeed: Double by NBT(compound, ATTACK_SPEED)

    val minDamage = baseDamage * (1 - damageSpread)
    val maxDamage = baseDamage * (1 + damageSpread)
    val dps = baseDamage * (4 - attackSpeed)
    val randomDamage: Double
        get() = Random.nextDouble(minDamage, maxDamage)

    companion object {
        const val BASE_DAMAGE = "base_damage"
        const val ATTACK_SPEED = "attack_speed"
        const val SPREAD = "spread"
    }
}
