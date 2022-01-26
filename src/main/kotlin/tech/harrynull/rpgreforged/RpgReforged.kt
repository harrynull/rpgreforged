package tech.harrynull.rpgreforged

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback


@Suppress("UNUSED")
object RpgReforged : ModInitializer {
    private const val MOD_ID = "rpgreforged"

    override fun onInitialize() {
        ItemTooltipCallback.EVENT.register { itemStack, context, texts ->
            val attributes = MyComponents.WEAPON_ATTRIBUTES.maybeGet(itemStack)
                .takeIf { it.isPresent }?.get()
                ?: return@register

            texts!!.removeAt(0) // remove name
            texts.addAll(0, attributes.toolTip())
        }
        //registerServerListeners()
        println("RpgReforged has been initialized.")
    }
}

class ClientEntrypoint : ClientModInitializer {
    override fun onInitializeClient() {
    }
}
