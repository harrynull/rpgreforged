package tech.harrynull.rpgreforged

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry


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
        Socket.items.forEach { socketItem ->
            Registry.register(
                Registry.ITEM,
                Identifier("rpgreforge", socketItem.socket.name.lowercase()),
                socketItem
            )
        }

        //registerServerListeners()
        println("RpgReforged has been initialized.")
    }
}

class ClientEntrypoint : ClientModInitializer {
    override fun onInitializeClient() {
    }
}
