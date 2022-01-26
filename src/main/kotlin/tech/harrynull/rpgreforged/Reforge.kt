package tech.harrynull.rpgreforged

import com.google.common.collect.ImmutableMultimap
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import java.util.*


enum class Reforge(
    val translationKey: String,
    val modifier: (
        multiMap: ImmutableMultimap.Builder<EntityAttribute?, EntityAttributeModifier?>,
        quality: Int
    ) -> Unit,
    val uuids: MutableList<UUID> = mutableListOf()
) {
    Raw("Raw", modifier = { _, _ -> }),
    Sharp("Sharp", modifier = { multiMap, quality ->
        genModifiers(
            multiMap, quality, Sharp.uuids, mapOf(
                AttributeType.STRENGTH to 3,
            )
        )
    }),
    Wise("Wise", modifier = { multiMap, quality ->
        genModifiers(
            multiMap, quality, Wise.uuids, mapOf(
                AttributeType.INTELLIGENCE to 3,
            )
        )
    }),
    Lucky("Lucky", modifier = { multiMap, quality ->
        genModifiers(
            multiMap, quality, Wise.uuids, mapOf(
                AttributeType.LUCKINESS to 3,
            )
        )
    }),
    Light("Light", modifier = { multiMap, quality ->
        genModifiers(
            multiMap, quality, Wise.uuids, mapOf(
                AttributeType.DEXTERITY to 3,
            )
        )
    }),
    Legendary("Legendary", modifier = { multiMap, quality ->
        genModifiers(
            multiMap, quality, Legendary.uuids, mapOf(
                AttributeType.STRENGTH to 3,
                AttributeType.DEXTERITY to 2,
                AttributeType.INTELLIGENCE to 2,
                AttributeType.LUCKINESS to 1,
            )
        )
    });

    companion object {
        fun randomReforge(): Reforge {
            val forgeToWeight = mapOf(
                Legendary to 1,
                Sharp to 5,
                Wise to 5,
                Lucky to 5,
                Light to 5,
            )
            val sumWeight = forgeToWeight.values.sum()
            val rand = kotlin.random.Random.nextInt(0, sumWeight)
            var weightSoFar = 0
            for (item in forgeToWeight) {
                weightSoFar += item.value
                if (rand < weightSoFar) return item.key
            }
            return forgeToWeight.keys.last()
        }
    }
}
