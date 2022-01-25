package com.mod_author.mod_id

import io.github.cottonmc.cotton.gui.GuiDescription
import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.client.CottonClientScreen
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription
import io.github.cottonmc.cotton.gui.widget.WButton
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.data.Insets
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.attribute.AttributeContainer
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText


class ProfileUIScreen(description: GuiDescription) :
    CottonClientScreen(LiteralText("Title"), description)

class ProfileGUI : LightweightGuiDescription() {

    private fun textForAttribute(attributes: AttributeContainer, attribute: AttributeType): Text {
        val baseVal = attributes.getBaseValue(attribute.attribute)
        val totalVal = attributes.getValue(attribute.attribute)
        return TranslatableText(attribute.attribute.translationKey).append(
            LiteralText(": $baseVal ($totalVal)")
        )
    }

    private fun textForAbilityPoints(attributeComponent: PlayerAttributes): Text {
        return LiteralText("Ability Points: ${attributeComponent.abilityPoints}")
    }

    private fun makeUI(): WGridPanel {
        val root = WGridPanel()
        root.setSize(256, 240)
        root.insets = Insets.ROOT_PANEL

        val player = MinecraftClient.getInstance().player!!
        val attributes = player.attributes!!
        val attributeComponent =
            MyComponents.PLAYER_ATTRIBUTES.get(MinecraftClient.getInstance().player!!)

        AttributeType.values().forEachIndexed { row, attribute ->
            val label = WLabel(textForAttribute(attributes, attribute))
            root.add(
                label, 0, row + 1, 5, 1
            )
            root.add(
                WButton(LiteralText("+")).apply {
                    isEnabled = attributeComponent.abilityPoints != 0
                    onClick = Runnable {
                        attributeComponent.useAbilityPoints(1, attribute)
                        attributes.getCustomInstance(attribute.attribute)!!.baseValue += 1
                        c2sUseAbilityPoints(attribute)
                        refreshUI()
                    }
                }, 8, row + 1, 1, 1
            )
        }
        root.add(WLabel(textForAbilityPoints(attributeComponent)), 0, 6, 5, 1)
        root.add(
            WLabel(
                "LV: ${attributeComponent.level} " +
                    "EXP: ${attributeComponent.exp} / ${attributeComponent.expForNextLevel}"
            ),
            0, 7, 5, 1
        )
        root.validate(this)
        root.backgroundPainter = BackgroundPainter.VANILLA
        return root
    }

    private fun refreshUI() {
        setRootPanel(makeUI())
    }

    init {
        refreshUI()
    }
}