package com.mod_author.mod_id

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.minecraft.nbt.NbtCompound
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText

class PlayerRPGAttributes(val compound: NbtCompound) {
    var constitution: Double by NBT(compound, CONSTITUTION)
    var strength: Double by NBT(compound, STRENGTH)
    var defense: Double by NBT(compound, DEFENSE)
    var dexterity: Double by NBT(compound, DEXTERITY)

    companion object {
        const val CONSTITUTION = "constitution"
        const val STRENGTH = "strength"
        const val DEFENSE = "defense"
        const val DEXTERITY = "dexterity"
    }
}


@Suppress("UNUSED")
object ModName : ModInitializer {
    private const val MOD_ID = "mod_id"

    override fun onInitialize() {
        ItemTooltipCallback.EVENT.register { itemStack, context, texts ->
            if (itemStack.nbt?.contains(RPG_ATTRIBUTES) == true) {
                val attributes = WeaponRPGAttributes(itemStack)
                // hide attack damage since it's dynamic
                texts.removeAll {
                    ((it as? TranslatableText?)
                        ?.args?.lastOrNull() as? TranslatableText?)
                        ?.key in listOf(
                        "attribute.name.generic.attack_damage",
                        "attribute.name.generic.attack_speed"
                    )
                }
                val customLines = listOf(
                    LiteralText("${itemStack!!.rarity} Item").formatted(itemStack.rarity.formatting),

                    LiteralText(
                        "Damage: ${attributes.minDamage.toString(1)} ~ " +
                            "${attributes.maxDamage.toString(1)} " +
                            "(DPS ${attributes.dps.toString(1)})"
                    )
                )
                texts!!.addAll(1, customLines)
            }
        }
        println("Example mod has been initialized.")
    }
}