package com.mod_author.mod_id

import com.mod_author.mod_id.mixins.SwordItemMixin
import dev.onyxstudios.cca.api.v3.item.ItemComponent
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Rarity
import java.util.*
import kotlin.math.roundToInt

val ATTACK_SPEED_MODIFIER_ID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3")

data class Socket(val dummy: Int)
data class Reforge(val name: String)

interface RPGItemAttributes {
    val rarity: Rarity
    val levelRequirement: Int

    var reforge: Reforge // effect strength dependent on quality
    val quality: Int // stars from 1 to 5
    val sockets: List<Socket> // dependent on quality

    fun numberOfSocketsByQuality(quality: Int): Int = quality.div(2.0).roundToInt()
}

interface WeaponAttributes : RPGItemAttributes {
    var baseDamage: Double
    var damageSpread: Double
    var attackSpeed: Double
    val minDamage: Double
    val maxDamage: Double
    val dps: Double

    fun getRandomDamage(random: Random): Double
    fun toolTip(): List<Text>
}


class WeaponRPGAttributeComponent(private val itemStack: ItemStack) :
    WeaponAttributes, ItemComponent(itemStack, MyComponents.WEAPON_ATTRIBUTES) {

    override var baseDamage: Double = 0.0
    override var damageSpread: Double = 0.0
    override var attackSpeed: Double = 0.0

    override val levelRequirement: Int = 0

    override var reforge: Reforge = Reforge(name = "Normal") // effect strength dependent on quality
    override val quality: Int = 5 // stars from 1 to 5
    override val sockets: List<Socket> =
        (1..numberOfSocketsByQuality(quality)).map { Socket(1) } // dependent on quality

    override val minDamage get() = baseDamage * (1 - damageSpread)
    override val maxDamage get() = baseDamage * (1 + damageSpread)
    override val dps get() = baseDamage * (4 - attackSpeed)
    override fun getRandomDamage(random: Random): Double =
        baseDamage + (random.nextDouble() * 2 - 1) * damageSpread * baseDamage

    private fun modRarity(): Rarity {
        return if (dps > 40) Rarity.EPIC
        else if (dps > 30) Rarity.RARE
        else if (dps > 26) Rarity.UNCOMMON
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

    override fun toolTip(): List<Text> {
        val basicInfo = listOf(
            LiteralText(reforge.name + " ")
                .append(LiteralText(itemStack.name.string).formatted(rarity.formatting)),
            LiteralText(" " + "★".repeat(quality)).formatted(Formatting.YELLOW),
            LiteralText("$rarity ITEM").formatted(rarity.formatting),
            LiteralText(
                "Damage: ${minDamage.toString(1)} ~ " +
                    "${maxDamage.toString(1)} " +
                    "(DPS ${dps.toString(1)})"
            ),
            LiteralText("")
        )

        val sockets: List<Text> = if (sockets.isNotEmpty())
            sockets.map { LiteralText("<> Empty") }.toMutableList().apply {
                add(0, LiteralText("Sockets:"))
                add(LiteralText(""))
            }
        else listOf()

        val otherInfo = listOf(
            LiteralText("Requires $levelRequirement lv to use").formatted(Formatting.GRAY)
        )

        return basicInfo + sockets + otherInfo
    }
}