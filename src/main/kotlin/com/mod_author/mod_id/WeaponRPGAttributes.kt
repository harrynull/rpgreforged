package com.mod_author.mod_id

import com.mod_author.mod_id.mixins.SwordItemMixin
import dev.onyxstudios.cca.api.v3.item.ItemComponent
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Rarity
import java.util.*

interface RPGItemAttributes {
    val rarity: Rarity
}

interface WeaponAttributes : RPGItemAttributes {
    var baseDamage: Double
    var damageSpread: Double
    var attackSpeed: Double
    val minDamage: Double
    val maxDamage: Double
    val dps: Double
    fun getRandomDamage(random: Random): Double
    fun toolTipName(): Text
    fun toolTipBody(): List<Text>
}

class WeaponRPGAttributeComponent(private val itemStack: ItemStack) :
    WeaponAttributes, ItemComponent(itemStack, MyComponents.WEAPON_ATTRIBUTES) {

    override var baseDamage: Double = 0.0
    override var damageSpread: Double = 0.0
    override var attackSpeed: Double = 0.0

    override val minDamage get() = baseDamage * (1 - damageSpread)
    override val maxDamage get() = baseDamage * (1 + damageSpread)
    override val dps get() = baseDamage * (4 - attackSpeed)
    override fun getRandomDamage(random: Random): Double =
        baseDamage + (random.nextDouble() * 2 - 1) * damageSpread * baseDamage

    private fun modRarity(): Rarity {
        return if (dps > 50) Rarity.EPIC
        else if (dps > 30) Rarity.RARE
        else if (dps > 15) Rarity.UNCOMMON
        else Rarity.COMMON
    }

    override val rarity: Rarity
        get() = listOf(modRarity(), itemStack.rarity).maxByOrNull { it.ordinal }!!

    init {
        baseDamage = (itemStack.item as SwordItem).attackDamage.toDouble()
        attackSpeed = (itemStack.item as SwordItemMixin)
            .attributeModifiers[EntityAttributes.GENERIC_ATTACK_SPEED]
            .find { it.id == ATTACK_SPEED_MODIFIER_ID }
            ?.value ?: 0.0
        damageSpread = 1.0
    }

    override fun toolTipName(): Text = LiteralText(itemStack.name.string)
        .formatted(rarity.formatting)

    override fun toolTipBody(): List<Text> {
        return listOf(
            LiteralText("$rarity ITEM").formatted(rarity.formatting),
            LiteralText(
                "Damage: ${minDamage.toString(1)} ~ " +
                    "${maxDamage.toString(1)} " +
                    "(DPS ${dps.toString(1)})"
            )
        )
    }
}