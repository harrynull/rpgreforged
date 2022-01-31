package tech.harrynull.rpgreforged

import com.google.common.collect.HashMultimap
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import kotlin.math.roundToInt

interface IArmorAttributes : IAttributes {
    val protection: Double
    val toughness: Double
    val knockbackResistance: Double
}

class ArmorRPGAttributeComponent(private val itemStack: ItemStack) :
    IArmorAttributes,
    RPGItemAttributes<ArmorRPGAttributeComponent>(
        itemStack, ItemType.ARMOR, MyComponents.ARMOR_ATTRIBUTES
    ) {

    override var protection: Double = (itemStack.item as ArmorItem).protection.toDouble()
    override var toughness: Double = (itemStack.item as ArmorItem).toughness.toDouble()
    override var knockbackResistance: Double = (itemStack.item as ArmorItem).material.knockbackResistance.toDouble()

    override val score: Int
        get() {
            return (aggregatedAttributes.toList().sumOf { it.second * it.second } / 5 +
                protection * protection +
                toughness * toughness * 2 +
                knockbackResistance * knockbackResistance * 200
                ).roundToInt()
        }

    init {
        loadFromNbt()
    }

    override fun toolTip(): List<Text> = generateToolTip {
        listOfNotNull(
            LiteralText("Armor: ${protection.toString(1)}")
                .takeIf { protection != 0.0 },
            LiteralText("Toughness: ${toughness.toString(1)}")
                .takeIf { toughness != 0.0 },
            LiteralText("Knockback Resistance: ${knockbackResistance.toString(1)}")
                .takeIf { knockbackResistance != 0.0 }
        )
    }

    override fun getAttributeMultimap(slot: EquipmentSlot): HashMultimap<EntityAttribute, EntityAttributeModifier> {
        if (slot != (itemStack.item as ArmorItem).slotType) return HashMultimap.create()
        return super.getAttributeMultimap(slot)
    }
}
