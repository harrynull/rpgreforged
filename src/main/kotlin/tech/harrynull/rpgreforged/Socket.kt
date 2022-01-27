package tech.harrynull.rpgreforged

import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class SocketItem(
    val socket: Socket
) : Item(FabricItemSettings().group(ItemGroup.MATERIALS).maxCount(1)) {
    override fun getName(stack: ItemStack?): Text {
        return LiteralText(socket.name)
    }
}

enum class Socket(
    val description: Text,
    val modifier: (
        map: MutableMap<AttributeType, Double>,
        quality: Int
    ) -> Unit
) {
    EmptySocket(LiteralText("<> Empty").formatted(Formatting.GRAY), modifier = { _, _ -> }),

    StrengthGem(LiteralText("${AttributeType.STRENGTH.icon} Strength Gem").formatted(Formatting.RED),
        modifier = { map, _ -> addStats(map, AttributeType.STRENGTH, 5.0) });

    companion object {
        val items = values().filter { it != EmptySocket }.map { SocketItem(it) }
    }
}
