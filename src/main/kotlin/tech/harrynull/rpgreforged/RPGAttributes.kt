package tech.harrynull.rpgreforged

import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import dev.onyxstudios.cca.api.v3.component.Component
import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.item.ItemComponent
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.util.Rarity
import java.util.*
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sqrt

enum class ItemType { ARMOR, WEAPON }

interface IAttributes {
    val reforge: Reforge
    val quality: Int
    val sockets: MutableList<Socket>
    val score: Int
    val rarity: Rarity
    val levelRequirement: Int
    val itemType: ItemType

    fun toolTip(): List<Text>
    fun anvilCombine(item1: IAttributes, item2: IAttributes)
    fun canAddSocket(socket: Socket): Boolean
    fun addSocket(socket: Socket)
    fun forge()
    fun reforge()
    fun getAttributeMultimap(slot: EquipmentSlot): HashMultimap<EntityAttribute, EntityAttributeModifier>
}

abstract class RPGItemAttributes<T : Component>(
    private val itemStack: ItemStack,
    override val itemType: ItemType,
    componentKey: ComponentKey<T>
) : IAttributes, ItemComponent(itemStack, componentKey) {

    override var reforge: Reforge = Reforge.Raw // effect strength dependent on quality
    override var quality: Int = 0 // stars from 1 to 5
    override var sockets: MutableList<Socket> = mutableListOf() // number dependent on quality

    override val rarity: Rarity
        get() {
            val modRarity = if (score > 240) Rarity.EPIC
            else if (score > 120) Rarity.RARE
            else if (score > 60) Rarity.UNCOMMON
            else Rarity.COMMON
            return maxOf(modRarity, itemStack.rarity)
        }

    override val levelRequirement: Int
        get() = maxOf(floor(sqrt(score.toDouble()) - 8).roundToInt() * 3, 0)

    protected val aggregatedAttributes: Map<AttributeType, Double>
        get() = mutableMapOf<AttributeType, Double>().apply {
            reforge.modifier(this, quality, itemType)
            sockets.forEach { it.modifier(this, quality) }
        }

    protected val attributeUUIDs: MutableMap<AttributeType, UUID> = mutableMapOf()

    fun numberOfSocketsByQuality(quality: Int): Int = quality.div(2.0).roundToInt()
    override fun canAddSocket(socket: Socket): Boolean = sockets.any { it == Socket.EmptySocket }
    override fun addSocket(socket: Socket) {
        sockets[sockets.indexOfFirst { it == Socket.EmptySocket }] = socket
        saveToNbt()
    }

    fun randomQuality(): Int {
        val rand = kotlin.random.Random.nextDouble()
        if (rand < 0.5) return 1 // 50%
        if (rand < 0.7) return 2 // 20%
        if (rand < 0.85) return 3 // 15%
        if (rand < 0.95) return 4 // 10%
        return 5 // 5%
    }

    override fun forge() {
        quality = randomQuality()
        sockets = (0 until numberOfSocketsByQuality(quality)).map { Socket.EmptySocket }.toMutableList()
        reforge = Reforge.randomReforge(itemType)
        saveToNbt()
    }

    override fun reforge() {
        if (quality == 0) {
            forge()
            return
        }
        reforge = Reforge.randomReforge(itemType)
        saveToNbt()
    }

    override open fun getAttributeMultimap(slot: EquipmentSlot): HashMultimap<EntityAttribute, EntityAttributeModifier> {
        val map = aggregatedAttributes.map {
            it.key.attribute.get() to additionMultiplier(
                attributeUUIDs.getOrDefault(it.key, UUID.randomUUID()),
                it.value,
                "RPG Item"
            )
        }.toMap()
        return HashMultimap.create<EntityAttribute, EntityAttributeModifier>().apply {
            map.forEach { (k, v) -> this.put(k, v) }
        }
    }

    protected fun generateToolTip(extraDesc: () -> List<Text>): List<Text> {
        val basicInfo = listOf(
            LiteralText(reforge.name + " " + itemStack.name.string + " ").formatted(rarity.formatting).append(
                LiteralText("★".repeat(quality)).formatted(Formatting.YELLOW)
            ),
            LiteralText("$rarity ITEM (RATING $score)").formatted(rarity.formatting),
            LiteralText(""),
        ) + extraDesc() + LiteralText("")

        val attributes = aggregatedAttributes.map { (attribute, value) ->
            LiteralText(attribute.icon + " ")
                .append(TranslatableText(attribute.attribute.get().translationKey))
                .append(LiteralText(": $value"))
                .formatted(attribute.formatting)
        } + listOf(LiteralText(""))

        val sockets: List<Text> = if (sockets.isNotEmpty())
            sockets.map { LiteralText(" ").append(it.description) }.toMutableList().apply {
                add(0, LiteralText("Sockets:"))
                add(LiteralText(""))
            }
        else listOf(LiteralText("Use a reforge station to unlock sockets.").formatted(Formatting.GRAY))

        val otherInfo = listOf(
            LiteralText("Requires Lv $levelRequirement to use").formatted(Formatting.GRAY)
        )

        return basicInfo + attributes + sockets + otherInfo
    }

    open fun saveToNbt() {
        putInt("quality", quality)
        sockets.forEachIndexed { index, socket -> putString("socket_$index", socket.name) }
        putString("reforge", reforge.name)
    }

    open fun loadFromNbt() {
        if (hasTag("quality")) quality = getInt("quality")
        sockets = (0 until quality).map {
            if (hasTag("socket_$it"))
                Socket.valueOf(getString("socket_$it"))
            else Socket.EmptySocket
        }.toMutableList()
        if (hasTag("reforge")) reforge = Reforge.valueOf(getString("reforge"))
    }

    override fun anvilCombine(item1: IAttributes, item2: IAttributes) {
        quality =
            if (item1.quality != item2.quality) {
                maxOf(item1.quality + item2.quality)
            } else minOf(item1.quality + 1, 5)
        sockets = item1.sockets
        reforge = item1.reforge
        saveToNbt()
    }
}

fun addItemAttributes(
    itemStack: ItemStack,
    slot: EquipmentSlot,
    modifiers: Multimap<EntityAttribute, EntityAttributeModifier>
) {
    itemStack.getRpgComponent()?.getAttributeMultimap(slot)
        ?.let { modifiers.putAll(it) }
}

fun onCraftCallback(itemStack: ItemStack): ItemStack? {
    if (itemStack.getRpgComponent()?.forge() != null) return itemStack
    return null
}
