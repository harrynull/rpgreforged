package com.mod_author.mod_id

import io.github.cottonmc.cotton.gui.GuiDescription
import io.github.cottonmc.cotton.gui.client.CottonClientScreen
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.WButton
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.data.Insets
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.attribute.AttributeContainer
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText


class ProfileUIScreen(description: GuiDescription) : CottonClientScreen(description)

class ProfileGUI : LightweightGuiDescription() {

    private fun addAttributeRow(
        root: WGridPanel,
        attributes: AttributeContainer,
        attribute: EntityAttribute,
        row: Int
    ) {

    }

    init {
        val root = WGridPanel()
        setRootPanel(root)
        root.setSize(256, 240)
        root.insets = Insets.ROOT_PANEL

        val attributes = MinecraftClient.getInstance().player?.attributes!!
        root.add(WLabel(LiteralText("Profile")), 0, 0, 10, 1)

        val playerAttributes =
            MyComponents.PLAYER_ATTRIBUTES.get(MinecraftClient.getInstance().player!!)//ClientEntrypoint.attributesClient
        listOf(
            ATTRIBUTE_CONSTITUTION,
            ATTRIBUTE_STRENGTH,
            ATTRIBUTE_DEFENSE,
            ATTRIBUTE_DEXTERITY
        ).forEachIndexed { row, attribute ->
            val baseVal = attributes.getBaseValue(attribute.attribute)
            val totalVal = attributes.getValue(attribute.attribute)
            root.add(
                WLabel(
                    TranslatableText(attribute.attribute.translationKey).append(
                        LiteralText(": $baseVal ($totalVal)")
                    )
                ), 0, row + 1, 5, 1
            )
            root.add(
                WButton(LiteralText("+")).apply {
                    isEnabled = playerAttributes.abilityPoints != 0
                }, 6, row + 1, 1, 1
            )
        }
        root.add(
            WLabel("Ability Points: ${playerAttributes.abilityPoints}"), 0, 6, 5, 1
        )
        root.add(
            WLabel("LV: ${playerAttributes.level} EXP: ${playerAttributes.exp}"), 0, 7, 5, 1
        )
        root.validate(this)
    }
}