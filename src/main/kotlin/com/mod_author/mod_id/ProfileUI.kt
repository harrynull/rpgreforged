package com.mod_author.mod_id

import io.github.cottonmc.cotton.gui.GuiDescription
import io.github.cottonmc.cotton.gui.client.CottonClientScreen
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.WButton
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.data.Insets
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.text.LiteralText


class ProfileUIScreen(description: GuiDescription) : CottonClientScreen(description)

class ProfileGUI : LightweightGuiDescription() {
    init {
        val root = WGridPanel()
        setRootPanel(root)
        root.setSize(256, 240)

        val attributes = MinecraftClient.getInstance().player?.attributes!!
        root.insets = Insets.ROOT_PANEL
        root.add(WLabel(LiteralText("Profile")), 0, 0, 10, 1)

        root.add(
            WLabel(
                LiteralText(
                    "Constitution: ${attributes.getBaseValue(EntityAttributes.GENERIC_MAX_HEALTH)}"
                )
            ), 0, 3, 10, 1
        )
        root.add(WButton(LiteralText("+")), 11, 3, 1, 1)

        root.add(
            WLabel(
                LiteralText(
                    "Strength: ${attributes.getBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)}"
                )
            ), 0, 5, 10, 1
        )
        root.add(WButton(LiteralText("+")), 11, 5, 1, 1)

        root.add(
            WLabel(
                LiteralText(
                    "Dex: ${attributes.getBaseValue(ATTRIBUTE_STRENGTH.attribute)}"
                )
            ), 0, 7, 10, 1
        )
        root.add(WButton(LiteralText("+")), 11, 7, 1, 1)

        root.validate(this)
    }
}