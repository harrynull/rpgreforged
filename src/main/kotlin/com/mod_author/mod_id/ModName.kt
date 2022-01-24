package com.mod_author.mod_id

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW


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