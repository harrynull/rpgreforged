package tech.harrynull.rpgreforged

private fun addStatsMap(
    map: MutableMap<AttributeType, Double>,
    quality: Int,
    attrs: Map<AttributeType, Double>
) {
    attrs.forEach { (t, u) -> addStats(map, t, quality * u) }
}

enum class Reforge(
    val translationKey: String,
    val modifier: (
        map: MutableMap<AttributeType, Double>,
        quality: Int
    ) -> Unit
) {
    Raw("Raw", modifier = { _, _ -> }),
    Sharp("Sharp", modifier = { map, quality ->
        addStatsMap(map, quality, mapOf(AttributeType.STRENGTH to 3.0))
    }),
    Wise("Wise", modifier = { map, quality ->
        addStatsMap(map, quality, mapOf(AttributeType.INTELLIGENCE to 3.0))
    }),
    Lucky("Lucky", modifier = { map, quality ->
        addStatsMap(map, quality, mapOf(AttributeType.LUCKINESS to 3.0))
    }),
    Light("Light", modifier = { map, quality ->
        addStatsMap(map, quality, mapOf(AttributeType.DEXTERITY to 3.0))
    }),
    Legendary("Legendary", modifier = { map, quality ->
        addStatsMap(
            map, quality, mapOf(
                AttributeType.STRENGTH to 3.0,
                AttributeType.DEXTERITY to 2.0,
                AttributeType.INTELLIGENCE to 2.0,
                AttributeType.LUCKINESS to 1.0,
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
