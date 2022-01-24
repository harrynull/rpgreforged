package com.mod_author.mod_id

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback

@Suppress("UNUSED")
object ModName : ModInitializer {
    private const val MOD_ID = "mod_id"

    override fun onInitialize() {
        ItemTooltipCallback.EVENT.register { itemStack, context, texts ->
            val attributes = MyComponents.WEAPON_ATTRIBUTES.maybeGet(itemStack)
                .takeIf { it.isPresent }?.get()
                ?: return@register

            // hide attack damage since it's dynamic
            /*
            texts.removeAll {
                ((it as? TranslatableText?)
                    ?.args?.lastOrNull() as? TranslatableText?)
                    ?.key in listOf(
                    "attribute.name.generic.attack_damage",
                    "attribute.name.generic.attack_speed"
                )
            }*/
            texts!!.removeAt(0)
            texts!!.add(0, attributes.toolTipName())
            texts!!.addAll(1, attributes.toolTipBody())
        }
        println("Example mod has been initialized.")
    }
}