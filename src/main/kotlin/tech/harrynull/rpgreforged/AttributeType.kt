package tech.harrynull.rpgreforged

import com.github.clevernucleus.dataattributes.api.DataAttributesAPI
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import java.util.function.Supplier

enum class AttributeType(
    id: Identifier,
    val icon: String,
    val formatting: Formatting
) {
    CONSTITUTION(Identifier("playerex", "constitution"), "\uD83E\uDE93", Formatting.YELLOW),
    STRENGTH(Identifier("playerex", "strength"), "\uD83D\uDDE1", Formatting.RED),
    INTELLIGENCE(Identifier("playerex", "intelligence"), "❄", Formatting.BLUE),
    DEXTERITY(Identifier("playerex", "dexterity"), "\uD83C\uDFF9", Formatting.GREEN),
    LUCKINESS(Identifier("playerex", "luckiness"), "\uD83E\uDDEA", Formatting.AQUA);

    val attribute: Supplier<EntityAttribute>

    init {
        attribute = DataAttributesAPI.getAttribute(id)
    }
}
