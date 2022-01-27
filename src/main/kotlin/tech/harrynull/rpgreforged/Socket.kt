package tech.harrynull.rpgreforged

import com.google.common.collect.ImmutableMultimap
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.util.*

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
        multiMap: ImmutableMultimap.Builder<EntityAttribute?, EntityAttributeModifier?>,
        quality: Int
    ) -> Unit
) {
    EmptySocket(LiteralText("<> Empty").formatted(Formatting.GRAY), modifier = { _, _ -> }),
    StrengthGem(LiteralText("${AttributeType.STRENGTH.icon} Strength Gem").formatted(Formatting.RED),
        modifier = { multiMap, quality ->
            multiMap.put(
                AttributeType.STRENGTH.attribute.get(),
                additionMultiplier(StrengthGem.uuid, 5.0, "Gem")
            )
        });

    val uuid: UUID = UUID.randomUUID()

    companion object {
        val items = values().filter { it != EmptySocket }.map { SocketItem(it) }
    }
}
