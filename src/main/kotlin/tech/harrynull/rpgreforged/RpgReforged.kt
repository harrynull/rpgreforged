package tech.harrynull.rpgreforged

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW


@Suppress("UNUSED")
object RpgReforged : ModInitializer {
    private const val MOD_ID = "rpgreforged"

    override fun onInitialize() {
        ServerPlayerEvents.AFTER_RESPAWN.register { oldPlayer, newPlayer, alive ->
            val attributeComponent = MyComponents.PLAYER_ATTRIBUTES.get(oldPlayer)
            AttributeType.fill(newPlayer.attributes, attributeComponent)
        }

        ItemTooltipCallback.EVENT.register { itemStack, context, texts ->
            val attributes = MyComponents.WEAPON_ATTRIBUTES.maybeGet(itemStack)
                .takeIf { it.isPresent }?.get()
                ?: return@register

            texts!!.removeAt(0) // remove name
            texts.addAll(0, attributes.toolTip())
        }
        registerServerListeners()
        println("Example mod has been initialized.")
    }
}

class ClientEntrypoint : ClientModInitializer {
    override fun onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: MinecraftClient ->
            if (keyBinding.wasPressed()) {
                MinecraftClient.getInstance().setScreen(ProfileUIScreen(ProfileGUI()))
            }
        })
    }

    companion object {
        val keyBinding = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.examplemod.profile",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_P, // The keycode of the key
                "category.examplemod.test" // The translation key of the keybinding's category.
            )
        )
    }
}
