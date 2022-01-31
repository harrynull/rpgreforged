package tech.harrynull.rpgreforged

import com.google.common.collect.HashMultimap
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import tech.harrynull.rpgreforged.mixins.SwordItemMixin
import java.util.*
import kotlin.math.roundToInt

private val ATTACK_SPEED_MODIFIER_ID: UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3")

interface IWeaponAttributes : IAttributes {
    var baseDamage: Double
    var damageSpread: Double
    var attackSpeed: Double
    val minDamage: Double
    val maxDamage: Double
    val dps: Double

    fun getRandomDamageOffset(random: Random): Double
}

class WeaponRPGAttributeComponent(private val itemStack: ItemStack) :
    IWeaponAttributes,
    RPGItemAttributes<WeaponRPGAttributeComponent>(
        itemStack,
        ItemType.WEAPON,
        MyComponents.WEAPON_ATTRIBUTES
    ) {

    override var baseDamage: Double = 0.0
    override var damageSpread: Double = 0.0
    override var attackSpeed: Double = 0.0

    override val score: Int
        get() {
            return (aggregatedAttributes.toList().sumOf { it.second * it.second } / 5 +
                dps * dps / 12).roundToInt()
        }

    override val minDamage get() = baseDamage * (1 - damageSpread)
    override val maxDamage get() = baseDamage * (1 + damageSpread)
    override val dps get() = baseDamage * (4 - attackSpeed)
    override fun getRandomDamageOffset(random: Random): Double =
        (random.nextDouble() * 2 - 1) * damageSpread * baseDamage

    override fun loadFromNbt() {
        super.loadFromNbt()
        if (hasTag("damage_spread")) damageSpread = getDouble("damage_spread")
        if (hasTag("base_damage")) baseDamage = getDouble("base_damage")
        if (hasTag("attack_speed")) attackSpeed = getDouble("attack_speed")
    }

    init {
        baseDamage = (itemStack.item as SwordItem).attackDamage.toDouble()
        attackSpeed = (itemStack.item as SwordItemMixin)
            .attributeModifiers!![EntityAttributes.GENERIC_ATTACK_SPEED]
            .find { it!!.id == ATTACK_SPEED_MODIFIER_ID }
            ?.value ?: 0.0
        damageSpread = 0.2
        loadFromNbt()
    }

    override fun toolTip(): List<Text> {
        return generateToolTip {
            listOf(
                LiteralText(
                    "Base Damage: ${minDamage.toString(1)} ~ " +
                        "${maxDamage.toString(1)} " +
                        "(DPS ${dps.toString(1)})"
                )
            )
        }
    }

    override fun getAttributeMultimap(slot: EquipmentSlot): HashMultimap<EntityAttribute, EntityAttributeModifier> {
        if (slot != EquipmentSlot.MAINHAND) return HashMultimap.create()
        return super.getAttributeMultimap(slot)
    }
}
